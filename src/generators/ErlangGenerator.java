package generators;

import java.util.Random;

public class ErlangGenerator {
    private final Random generator;
    private final int k;
    private final double lambda;

    public ErlangGenerator(Random rand, int k, double lambda) {
        this.generator = new Random(rand.nextInt());
        this.k = k;
        this.lambda = lambda;
    }

    public double randDouble() {
        double product = 1.0;
        for (int i = 0; i < k; i++) {
            product *= generator.nextDouble();
        }

        return - (1.0 / lambda) * Math.log(product);
    }
}