package br.com.fiap.gs.application.dto;

import java.util.List;

/**
 * DTO com o resultado do cálculo de caminho ótimo (Dijkstra).
 */
public class PathResultDTO {

    private final List<String> stages;
    private final double totalEnergyCostTJ;
    private final int totalSteps;

    public PathResultDTO(List<String> stages, double totalEnergyCostTJ) {
        this.stages           = stages;
        this.totalEnergyCostTJ = totalEnergyCostTJ;
        this.totalSteps       = stages.size();
    }

    public List<String> getStages()         { return stages; }
    public double getTotalEnergyCostTJ()    { return totalEnergyCostTJ; }
    public int getTotalSteps()              { return totalSteps; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Caminho Otimo (%d etapas, %.2f TJ):%n", totalSteps, totalEnergyCostTJ));
        for (int i = 0; i < stages.size(); i++) {
            sb.append(String.format("  %d. %s%n", i + 1, stages.get(i)));
        }
        return sb.toString();
    }
}
