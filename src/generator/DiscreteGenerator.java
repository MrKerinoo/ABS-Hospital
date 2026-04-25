package generator;

import java.util.Random;

public class DiscreteGenerator {
    private final Random generator;
    private final int min;
    private final int max;

    public DiscreteGenerator(Random rand, int min, int max) {
        this.generator = new Random(rand.nextInt());
        this.min = min;
        this.max = max;
    }

    public int randInt() {
        return this.generator.nextInt(this.min, this.max + 1);
    }
}