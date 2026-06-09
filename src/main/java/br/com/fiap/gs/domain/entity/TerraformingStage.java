package br.com.fiap.gs.domain.entity;

import br.com.fiap.gs.domain.exception.InsufficientEnergyException;
import br.com.fiap.gs.domain.valueobject.Energy;
import br.com.fiap.gs.domain.valueobject.StageType;

import java.time.LocalDateTime;

/**
 * Etapa de terraformação concreta. Herda de Stage e implementa polimorfismo.
 */
public class TerraformingStage extends Stage {

    private StageType type;
    private boolean completed;
    private LocalDateTime completedAt;
    private double riskFactor;

    public TerraformingStage(Long id, StageType type, String description, double riskFactor) {
        super(id, type.getDisplayName(), description);
        this.type       = type;
        this.riskFactor = riskFactor;
        this.completed  = false;
    }

    @Override
    public Energy execute(Energy availableEnergy) {
        Energy required = getRequiredEnergy();

        if (!availableEnergy.isGreaterThanOrEqual(required)) {
            throw new InsufficientEnergyException(required.getValue(), availableEnergy.getValue());
        }

        this.completed   = true;
        this.completedAt = LocalDateTime.now();
        markUpdated();

        System.out.printf("  [OK] Etapa '%s' executada. Custo: %s%n", getName(), required);
        return availableEnergy.subtract(required);
    }

    @Override
    public Energy getRequiredEnergy() {
        double adjusted = type.getBaseCostTJ() * (1 + riskFactor);
        return Energy.of(adjusted);
    }

    @Override
    public String getSummary() {
        return String.format("%-20s | Custo: %-10s | Risco: %.0f%% | %s",
                getName(),
                getRequiredEnergy(),
                riskFactor * 100,
                completed ? "CONCLUÍDA (" + completedAt.toLocalDate() + ")" : "PENDENTE");
    }

    /** Restaura o estado de conclusão ao carregar do banco. */
    public void forceComplete(LocalDateTime completedAt) {
        this.completed   = true;
        this.completedAt = completedAt;
    }

    public StageType getType()           { return type; }
    public boolean isCompleted()         { return completed; }
    public LocalDateTime getCompletedAt(){ return completedAt; }
    public double getRiskFactor()        { return riskFactor; }

    public void setType(StageType type)        { this.type = type; markUpdated(); }
    public void setRiskFactor(double risk)     { this.riskFactor = risk; markUpdated(); }
}
