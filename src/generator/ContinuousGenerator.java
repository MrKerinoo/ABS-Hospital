package generator;

import java.util.Random;

public class ContinuousGenerator {
    private final Random generator;
    private final int min;
    private final int max;

    public ContinuousGenerator(Random rand, int min, int max) {
        this.generator = new Random(rand.nextInt());
        this.min = min;
        this.max = max;
    }

    public double randDouble() {
        return this.generator.nextDouble(this.min, this.max);
    }
}