package intervals;

public class Interval {

    private final int min;
    private final int max;
    private final double probability;

    public Interval(int min, int max, double probability) {
        this.min = min;
        this.max = max;
        this.probability = probability;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public double getProbability() {
        return probability;
    }
}