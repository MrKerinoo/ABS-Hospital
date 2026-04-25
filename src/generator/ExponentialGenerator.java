package generator;

import java.util.Random;

public class ExponentialGenerator {
    private final Random generator;
    private final double lambda;

    public ExponentialGenerator(Random rand, double mean) {
        this.generator = new Random(rand.nextInt());
        this.lambda = 1.0 / mean;
    }

    /**
     * Kód vytvorený s pomocou AI, zdokumentované v kapitole 1.2
     */
    public double randDouble() {
        double u = this.generator.nextDouble();

        return -Math.log(1.0 - u) / this.lambda;
    }
}
