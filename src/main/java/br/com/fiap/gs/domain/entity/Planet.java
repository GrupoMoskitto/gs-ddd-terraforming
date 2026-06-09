package br.com.fiap.gs.domain.entity;

import br.com.fiap.gs.domain.valueobject.Energy;

/**
 * Entidade que representa um planeta alvo de terraformação.
 */
public class Planet {

    private Long id;
    private final String name;
    private double distanceFromEarthAU;
    private Energy totalEnergyBudget;
    private boolean habitable;

    public Planet(Long id, String name, double distanceFromEarthAU, Energy totalEnergyBudget) {
        this.id                  = id;
        this.name                = name;
        this.distanceFromEarthAU = distanceFromEarthAU;
        this.totalEnergyBudget   = totalEnergyBudget;
        this.habitable           = false;
    }

    public static Planet mars() {
        return new Planet(1L, "Marte", 1.52, Energy.of(5000));
    }

    public void markHabitable() {
        this.habitable = true;
        System.out.println("  [MISSAO CONCLUIDA] " + name + " agora e habitavel!");
    }

    public Long getId()                     { return id; }
    public String getName()                 { return name; }
    public double getDistanceFromEarthAU()  { return distanceFromEarthAU; }
    public Energy getTotalEnergyBudget()    { return totalEnergyBudget; }
    public boolean isHabitable()            { return habitable; }

    public void setId(Long id)                         { this.id = id; }
    public void setTotalEnergyBudget(Energy budget)    { this.totalEnergyBudget = budget; }

    @Override
    public String toString() {
        return String.format("Planet{name='%s', distancia=%.2f UA, orcamento=%s, habitavel=%b}",
                name, distanceFromEarthAU, totalEnergyBudget, habitable);
    }
}
