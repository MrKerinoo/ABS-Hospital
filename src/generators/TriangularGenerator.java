package generators;

import java.util.Random;

public class TriangularGenerator {
    private final Random generator;
    private final int min;
    private final int max;
    private final int modus;

    public TriangularGenerator(Random rand, int min, int max, int modus) {
        this.generator = new Random(rand.nextInt());
        this.min = min;
        this.max = max;
        this.modus = modus;
    }

    /**
     * Kód vytvorený s pomocou AI, zdokumentované v kapitole 1.3
     */
    public double randDouble() {
        double u = this.generator.nextDouble();
        double splitPoint = (double) (this.modus - this.min) / (this.max - this.min);

        if (u < splitPoint) {
            return (double) this.min + Math.sqrt(u * (this.max - this.min) * (this.modus - this.min));
        } else {
            return (double) this.max - Math.sqrt((1.0 - u) * (this.max - this.min) * (this.max - this.modus));
        }
    }
}