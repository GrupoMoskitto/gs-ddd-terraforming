package br.com.fiap.gs.domain.entity;

import br.com.fiap.gs.domain.valueobject.Energy;

import java.time.LocalDateTime;

/**
 * Classe abstrata base para todas as etapas da missão.
 * Aplica herança e polimorfismo via método execute().
 */
public abstract class Stage {

    private Long id;
    private String name;
    private String description;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    protected Stage(Long id, String name, String description) {
        this.id          = id;
        this.name        = name;
        this.description = description;
        this.createdAt   = LocalDateTime.now();
        this.updatedAt   = LocalDateTime.now();
    }

    /**
     * Executa a lógica específica de cada tipo de etapa (polimorfismo).
     * @param availableEnergy energia disponível para execução
     * @return energia restante após execução
     */
    public abstract Energy execute(Energy availableEnergy);

    /**
     * Retorna a energia necessária para esta etapa.
     */
    public abstract Energy getRequiredEnergy();

    /**
     * Descrição resumida legível da etapa.
     */
    public abstract String getSummary();

    protected void markUpdated() {
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId()              { return id; }
    public void setId(Long id)       { this.id = id; }
    public String getName()          { return name; }
    public void setName(String name) { this.name = name; markUpdated(); }
    public String getDescription()   { return description; }
    public void setDescription(String d) { this.description = d; markUpdated(); }
    public LocalDateTime getCreatedAt()  { return createdAt; }
    public LocalDateTime getUpdatedAt()  { return updatedAt; }

    @Override
    public String toString() {
        return String.format("Stage{id=%d, name='%s', criado=%s}", id, name, createdAt);
    }
}
