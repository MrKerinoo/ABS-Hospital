package generators;

import java.util.Random;

public class ErlangGenerator {
    private final Random generator;
    private final int k;
    private final double lambda;
    private final double offset; // 1. Pridaná premenná pre posun

    public ErlangGenerator(Random rand, int k, double lambda, double offset) {
        // Používame priamo rand z konštruktora pre kontinuitu náhodnosti
        this.generator = rand;
        this.k = k;
        this.lambda = lambda;
        this.offset = offset; // 2. Priradenie offsetu
    }

    public double randDouble() {
        double product = 1.0;
        for (int i = 0; i < k; i++) {
            product *= generator.nextDouble();
        }

        // 3. Výpočet so zahrnutím offsetu
        // Vzorec: (-(1/lambda) * ln(súčin)) + posun
        double result = (-(1.0 / lambda) * Math.log(product)) + offset;

        // 4. Bezpečnostná poistka
        // Ak by (Erlang + offset) vyšlo menej ako 0, vrátime 0 (čas nemôže byť záporný)
        return Math.max(0, result);
    }
}