package br.com.fiap.gs.domain.valueobject;

import br.com.fiap.gs.domain.exception.InsufficientEnergyException;

/**
 * Value Object imutável que representa energia em TeraJoules (TJ).
 */
public final class Energy {

    private final double valueInTJ;

    private Energy(double valueInTJ) {
        if (valueInTJ < 0) {
            throw new IllegalArgumentException("Energia não pode ser negativa.");
        }
        this.valueInTJ = valueInTJ;
    }

    public static Energy of(double valueInTJ) {
        return new Energy(valueInTJ);
    }

    public static Energy zero() {
        return new Energy(0);
    }

    public Energy add(Energy other) {
        return new Energy(this.valueInTJ + other.valueInTJ);
    }

    public Energy subtract(Energy other) {
        if (this.valueInTJ < other.valueInTJ) {
            throw new InsufficientEnergyException(other.valueInTJ, this.valueInTJ);
        }
        return new Energy(this.valueInTJ - other.valueInTJ);
    }

    public boolean isGreaterThanOrEqual(Energy other) {
        return this.valueInTJ >= other.valueInTJ;
    }

    public double getValue() { return valueInTJ; }

    @Override
    public String toString() {
        return String.format("%.2f TJ", valueInTJ);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Energy)) return false;
        return Double.compare(valueInTJ, ((Energy) o).valueInTJ) == 0;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(valueInTJ);
    }
}
