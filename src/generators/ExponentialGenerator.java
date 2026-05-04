package generators;

import java.util.Random;

public class ExponentialGenerator {
    private final Random generator;
    private final double lambda;
    private final double shift;

    public ExponentialGenerator(Random rand, double mean, double shift) {
        this.generator = new Random(rand.nextInt());
        this.lambda = 1.0 / mean;
        this.shift = shift;
    }

    /**
     * Kód vytvorený s pomocou AI, zdokumentované v kapitole 1.2
     */
    public double randDouble() {
        return (-Math.log(1.0 - this.generator.nextDouble()) / this.lambda) + this.shift;
    }
}
