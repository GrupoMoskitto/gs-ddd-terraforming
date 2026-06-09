package br.com.fiap.gs.domain.exception;

public class InsufficientEnergyException extends TerraformingException {

    private final double required;
    private final double available;

    public InsufficientEnergyException(double required, double available) {
        super("ENERGY_001",
              String.format("Energia insuficiente. Necessário: %.2f TJ, Disponível: %.2f TJ",
                            required, available));
        this.required = required;
        this.available = available;
    }

    public double getRequired()  { return required; }
    public double getAvailable() { return available; }
}
