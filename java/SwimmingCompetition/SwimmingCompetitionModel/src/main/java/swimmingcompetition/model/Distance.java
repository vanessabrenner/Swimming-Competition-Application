package swimmingcompetition.model;

import java.io.Serializable;

public enum Distance implements Serializable {
    MICA(50),
    MARE(200),
    MEDIE(800),
    EXTREMA(1500);

    private final int valoare;

    Distance(int valoare){
        this.valoare = valoare;
    }
    public int get(){
        return valoare;
    }
    public static Distance fromInt(int valoare) {
        for (Distance d : Distance.values()) {
            if (d.get() == valoare) {
                return d;
            }
        }
        throw new IllegalArgumentException("Nu există o distanță cu valoarea specificată: " + valoare);
    }
}
