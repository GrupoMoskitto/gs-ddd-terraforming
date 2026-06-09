package br.com.fiap.gs.domain.valueobject;

public enum StageType {

    INITIAL_MARS    ("Marte Inicial",         0),
    MAGNETIC_FIELD  ("Campo Magnético",       120),
    ATMOSPHERE      ("Atmosfera",             350),
    WATER           ("Água Líquida",          280),
    OXYGEN          ("Oxigênio",              410),
    SOIL            ("Solo Fértil",           190),
    FAUNA           ("Fauna",                 500),
    HABITABLE_MARS  ("Marte Habitável",       0);

    private final String displayName;
    private final double baseCostTJ;

    StageType(String displayName, double baseCostTJ) {
        this.displayName = displayName;
        this.baseCostTJ  = baseCostTJ;
    }

    public String getDisplayName() { return displayName; }
    public double getBaseCostTJ()  { return baseCostTJ; }

    @Override
    public String toString() { return displayName; }
}
