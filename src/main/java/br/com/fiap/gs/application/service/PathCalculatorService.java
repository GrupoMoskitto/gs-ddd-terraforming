package br.com.fiap.gs.application.service;

import br.com.fiap.gs.application.dto.PathResultDTO;
import br.com.fiap.gs.domain.valueobject.StageType;

import java.util.*;

/**
 * Implementa Dijkstra sobre o DAG de terraformação marciana.
 * Cada aresta representa a energia (TJ) necessária para ir de uma etapa para a próxima.
 */
public class PathCalculatorService {

    private static final int NUM_NODES = StageType.values().length;

    private final Map<Integer, List<int[]>> adjacency = new HashMap<>();

    public PathCalculatorService() {
        buildGraph();
    }

    private void buildGraph() {
        // Índices dos StageType na ordem do enum
        // INITIAL_MARS(0) -> MAGNETIC_FIELD(1)
        // MAGNETIC_FIELD(1) -> ATMOSPHERE(2)
        // ATMOSPHERE(2) -> WATER(3), OXYGEN(4)
        // WATER(3) -> SOIL(5)
        // OXYGEN(4) -> SOIL(5), FAUNA(6)
        // SOIL(5) -> FAUNA(6)
        // FAUNA(6) -> HABITABLE_MARS(7)

        addEdge(0, 1, 120);   // Marte Inicial -> Campo Magnético
        addEdge(1, 2, 350);   // Campo Magnético -> Atmosfera
        addEdge(2, 3, 280);   // Atmosfera -> Água
        addEdge(2, 4, 410);   // Atmosfera -> Oxigênio
        addEdge(3, 5, 190);   // Água -> Solo Fértil
        addEdge(4, 5, 150);   // Oxigênio -> Solo Fértil (rota alternativa)
        addEdge(4, 6, 500);   // Oxigênio -> Fauna
        addEdge(5, 6, 500);   // Solo Fértil -> Fauna
        addEdge(6, 7, 0);     // Fauna -> Marte Habitável
    }

    private void addEdge(int from, int to, int cost) {
        adjacency.computeIfAbsent(from, k -> new ArrayList<>()).add(new int[]{to, cost});
    }

    /**
     * Calcula o caminho de menor custo energético entre duas etapas usando Dijkstra.
     */
    public PathResultDTO findOptimalPath(StageType origin, StageType destination) {
        int src  = origin.ordinal();
        int dest = destination.ordinal();

        double[] dist = new double[NUM_NODES];
        int[] prev    = new int[NUM_NODES];
        Arrays.fill(dist, Double.MAX_VALUE);
        Arrays.fill(prev, -1);
        dist[src] = 0;

        // PriorityQueue: [custo, nó]
        PriorityQueue<double[]> pq = new PriorityQueue<>(Comparator.comparingDouble(a -> a[0]));
        pq.offer(new double[]{0, src});

        while (!pq.isEmpty()) {
            double[] curr = pq.poll();
            double currCost = curr[0];
            int currNode    = (int) curr[1];

            if (currCost > dist[currNode]) continue;

            List<int[]> neighbors = adjacency.getOrDefault(currNode, Collections.emptyList());
            for (int[] edge : neighbors) {
                int neighbor  = edge[0];
                double newCost = dist[currNode] + edge[1];
                if (newCost < dist[neighbor]) {
                    dist[neighbor] = newCost;
                    prev[neighbor] = currNode;
                    pq.offer(new double[]{newCost, neighbor});
                }
            }
        }

        List<String> path = reconstructPath(prev, src, dest);
        return new PathResultDTO(path, dist[dest]);
    }

    private List<String> reconstructPath(int[] prev, int src, int dest) {
        List<String> path = new ArrayList<>();
        int current = dest;

        while (current != -1) {
            path.add(0, StageType.values()[current].getDisplayName());
            if (current == src) break;
            current = prev[current];
        }

        return path;
    }
}
