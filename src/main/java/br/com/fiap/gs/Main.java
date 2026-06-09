package br.com.fiap.gs;

import br.com.fiap.gs.application.dto.PathResultDTO;
import br.com.fiap.gs.application.dto.StageDTO;
import br.com.fiap.gs.application.service.PathCalculatorService;
import br.com.fiap.gs.application.service.TerraformingService;
import br.com.fiap.gs.domain.entity.Planet;
import br.com.fiap.gs.domain.entity.TerraformingStage;
import br.com.fiap.gs.domain.exception.InsufficientEnergyException;
import br.com.fiap.gs.domain.exception.StageNotFoundException;
import br.com.fiap.gs.domain.exception.TerraformingException;
import br.com.fiap.gs.domain.valueobject.Energy;
import br.com.fiap.gs.domain.valueobject.StageType;
import br.com.fiap.gs.infrastructure.database.DatabaseConnection;
import br.com.fiap.gs.infrastructure.repository.StageRepositoryImpl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {

    public static void main(String[] args) {

        System.out.println("============================================================");
        System.out.println("  TERRAPATH — Sistema de Terraformacao de Marte (DDD)");
        System.out.println("  FIAP | Global Solution 2026");
        System.out.println("  Iniciado em: " + LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        System.out.println("============================================================\n");

        StageRepositoryImpl repository   = new StageRepositoryImpl();
        repository.deleteAll();
        TerraformingService  service      = new TerraformingService(repository);
        PathCalculatorService pathCalc    = new PathCalculatorService();
        Planet mars = Planet.mars();

        // ------------------------------------------------------------------
        // 1. Cadastro de etapas
        // ------------------------------------------------------------------
        System.out.println(">>> [1] CADASTRO DE ETAPAS <<<");
        System.out.println("------------------------------------------------------------");

        StageDTO s1 = service.registerStage(StageType.MAGNETIC_FIELD, "Ativar geradores magneticos polares", 0.10);
        StageDTO s2 = service.registerStage(StageType.ATMOSPHERE,     "Liberar CO2 e criar efeito estufa",   0.15);
        StageDTO s3 = service.registerStage(StageType.WATER,          "Derreter calotas polares",            0.20);
        StageDTO s4 = service.registerStage(StageType.OXYGEN,         "Cultivar cianobacterias produtoras",  0.25);
        StageDTO s5 = service.registerStage(StageType.SOIL,           "Introduzir microorganismos no solo",  0.12);
        StageDTO s6 = service.registerStage(StageType.FAUNA,          "Introduzir fauna resistente",         0.30);

        System.out.printf("  Total de etapas cadastradas: %d%n", service.listAllStages().size());

        // ------------------------------------------------------------------
        // 2. Listar etapas pendentes
        // ------------------------------------------------------------------
        System.out.println("\n>>> [2] ETAPAS PENDENTES <<<");
        System.out.println("------------------------------------------------------------");
        service.listPendingStages().forEach(s -> System.out.println("  " + s));

        // ------------------------------------------------------------------
        // 3. Dijkstra — caminho ótimo
        // ------------------------------------------------------------------
        System.out.println("\n>>> [3] CAMINHO OTIMO — DIJKSTRA <<<");
        System.out.println("------------------------------------------------------------");

        PathResultDTO caminho = pathCalc.findOptimalPath(StageType.INITIAL_MARS, StageType.HABITABLE_MARS);
        System.out.println(caminho);

        // ------------------------------------------------------------------
        // 4. Executar etapas
        // ------------------------------------------------------------------
        System.out.println(">>> [4] EXECUCAO DAS ETAPAS <<<");
        System.out.println("------------------------------------------------------------");
        System.out.println("  Orcamento de Marte: " + mars.getTotalEnergyBudget());

        runSafe(service, mars, s1.getId());
        runSafe(service, mars, s2.getId());
        runSafe(service, mars, s3.getId());
        runSafe(service, mars, s4.getId());
        runSafe(service, mars, s5.getId());
        runSafe(service, mars, s6.getId());

        System.out.println("  Orcamento restante: " + mars.getTotalEnergyBudget());

        // ------------------------------------------------------------------
        // 5. Tratamento de exceções
        // ------------------------------------------------------------------
        System.out.println("\n>>> [5] TRATAMENTO DE EXCECOES <<<");
        System.out.println("------------------------------------------------------------");

        try {
            service.findById(999L);
        } catch (StageNotFoundException e) {
            System.out.println("  [CAPTURADO] " + e);
        }

        try {
            TerraformingStage caro = new TerraformingStage(0L, StageType.FAUNA, "Teste", 0.5);
            caro.execute(Energy.of(10));
        } catch (InsufficientEnergyException e) {
            System.out.printf("  [CAPTURADO] %s — Necessario: %.1f TJ | Disponivel: %.1f TJ%n",
                    e.getCode(), e.getRequired(), e.getAvailable());
        }

        // ------------------------------------------------------------------
        // 6. Status final
        // ------------------------------------------------------------------
        System.out.println("\n>>> [6] STATUS FINAL <<<");
        System.out.println("------------------------------------------------------------");
        service.listAllStages().forEach(s -> System.out.println("  " + s));

        long concluidas = service.listAllStages().stream().filter(StageDTO::isCompleted).count();
        if (concluidas == 6) mars.markHabitable();

        System.out.println("\n============================================================");
        System.out.println("  Finalizado em: " + LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        System.out.println("============================================================");

        DatabaseConnection.getInstance().close();
    }

    private static void runSafe(TerraformingService service, Planet planet, Long stageId) {
        try {
            service.executeStage(stageId, planet);
        } catch (TerraformingException e) {
            System.err.println("  [ERRO] " + e);
        }
    }
}
