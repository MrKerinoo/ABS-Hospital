package statistics;

public class DiscreteStatistics extends Statistics {

    @Override
    public void add(double value) {
        super.registerValue(value, 1.0);
    }
}
