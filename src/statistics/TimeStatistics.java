package statistics;

import simulation.MySimulation;

public class TimeStatistics extends Statistics {
    private MySimulation mySim;
    private double lastChangeTime = 0;
    private double lastValue = 0;
    private double warmupTime = 0;

    public TimeStatistics(MySimulation sim) {
        this.mySim = sim;
    }

    @Override
    public void add(double value) {
        double currentTime = this.mySim.currentTime();
        double duration = currentTime - this.lastChangeTime;

        if (duration > 0 && currentTime > warmupTime) {
            double effectiveDuration = currentTime - Math.max(this.lastChangeTime, warmupTime);
            if (effectiveDuration > 0) {
                super.registerValue(this.lastValue, effectiveDuration);
            }
        }

        this.lastValue = value;
        this.lastChangeTime = currentTime;
    }

    @Override
    public double getMean() {
        double currentTime = this.mySim.currentTime();
        double measuredDuration = currentTime - warmupTime;

        if (measuredDuration <= 0) return 0;

        double lastSegmentDuration = currentTime - Math.max(this.lastChangeTime, warmupTime);

        double totalSum = this.sumValues + (this.lastValue * Math.max(0, lastSegmentDuration));
        return totalSum / measuredDuration;
    }

    @Override
    public double getStandardDeviation() {
        double currentTime = this.mySim.currentTime();
        double measuredDuration = currentTime - warmupTime;

        if (measuredDuration <= 0) return 0;

        double lastSegmentDuration = currentTime - Math.max(this.lastChangeTime, warmupTime);

        double totalSumPow = this.sumPowValues + (Math.pow(this.lastValue, 2) * Math.max(0, lastSegmentDuration));
        double mean = getMean();

        double variance = (totalSumPow / measuredDuration) - Math.pow(mean, 2);
        return Math.sqrt(Math.max(0, variance));
    }

    @Override
    public void reset() {
        super.reset();
        this.lastChangeTime = this.mySim.currentTime();
    }

    public void setWarmupTime(double warmupTime) {
        this.warmupTime = warmupTime;
    }
}