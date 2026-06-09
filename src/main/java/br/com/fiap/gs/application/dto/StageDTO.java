package br.com.fiap.gs.application.dto;

import br.com.fiap.gs.domain.entity.TerraformingStage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * DTO para transferência de dados de etapas (evita expor a entidade diretamente).
 */
public class StageDTO {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final Long id;
    private final String name;
    private final String type;
    private final String description;
    private final double energyCostTJ;
    private final double riskPercent;
    private final boolean completed;
    private final String createdAt;
    private final String completedAt;

    private StageDTO(TerraformingStage stage) {
        this.id          = stage.getId();
        this.name        = stage.getName();
        this.type        = stage.getType().name();
        this.description = stage.getDescription();
        this.energyCostTJ = stage.getRequiredEnergy().getValue();
        this.riskPercent  = stage.getRiskFactor() * 100;
        this.completed   = stage.isCompleted();
        this.createdAt   = stage.getCreatedAt().format(FMT);
        this.completedAt = stage.getCompletedAt() != null ? stage.getCompletedAt().format(FMT) : "-";
    }

    public static StageDTO from(TerraformingStage stage) {
        return new StageDTO(stage);
    }

    public Long getId()            { return id; }
    public String getName()        { return name; }
    public String getType()        { return type; }
    public String getDescription() { return description; }
    public double getEnergyCostTJ(){ return energyCostTJ; }
    public double getRiskPercent() { return riskPercent; }
    public boolean isCompleted()   { return completed; }
    public String getCreatedAt()   { return createdAt; }
    public String getCompletedAt() { return completedAt; }

    @Override
    public String toString() {
        return String.format(
            "StageDTO{id=%d, nome='%s', tipo=%s, custo=%.2fTJ, risco=%.0f%%, concluida=%b, criadoEm=%s}",
            id, name, type, energyCostTJ, riskPercent, completed, createdAt);
    }
}
