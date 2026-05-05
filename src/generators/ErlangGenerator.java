package generators;

import java.util.Random;

public class ErlangGenerator {
    private final Random generator;
    private final int k;
    private final double lambda;
    private final double offset;

    public ErlangGenerator(Random rand, int k, double lambda, double offset) {
        this.generator = rand;
        this.k = k;
        this.lambda = lambda;
        this.offset = offset;
    }

    public double randDouble() {
        double product = 1.0;
        for (int i = 0; i < k; i++) {
            product *= generator.nextDouble();
        }

        double result = (-(1.0 / lambda) * Math.log(product)) + offset;

        return Math.max(0, result);
    }
}