package statistics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Statistics {

    protected double sumValues;
    protected double sumPowValues;
    protected double count;

    public Statistics() {
        this.sumValues = 0;
        this.sumPowValues = 0;
        this.count = 0;
    }

    public double getCount() {
        return this.count;
    }

    private final List<Double> tTable = new ArrayList<>(Arrays.asList(
            12.706, 4.303, 3.182, 2.776, 2.571, 2.447, 2.365, 2.306, 2.262, 2.228,
            2.201, 2.179, 2.160, 2.145, 2.131, 2.120, 2.110, 2.101, 2.093, 2.086,
            2.080, 2.074, 2.069, 2.064, 2.060, 2.056, 2.052, 2.048, 2.045, 2.042
    ));

    protected abstract void add(double value);

    protected void registerValue(double value, double weight) {
        this.sumValues += value * weight;
        this.sumPowValues += Math.pow(value, 2) * weight;
        this.count += weight;
    }

    protected double getStandardDeviation() {
        if (this.count == 0) return 0;

        double mean = getMean();
        double variance = (this.sumPowValues / this.count) - Math.pow(mean, 2);

        return Math.sqrt(Math.max(0, variance));
    }

    public double getMean() {
        return this.sumValues / this.count;
    }

    public List<Double> getConfidenceInterval95() {
        if (this.count < 2) return List.of(0.0, 0.0);

        double criticalValue;
        int df = (int) this.count - 1;

        if (df >= 1 && df <= 30) {
            criticalValue = this.tTable.get(df - 1);
        } else {
            criticalValue = 1.96;
        }

        double halfWidth = (criticalValue * this.getStandardDeviation()) / Math.sqrt(this.count);
        double mean = this.getMean();

        return List.of(mean - halfWidth, mean + halfWidth);
    }

    public void reset() {
        this.sumValues = 0;
        this.sumPowValues = 0;
        this.count = 0;
    }
}
