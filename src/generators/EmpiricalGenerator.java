package generators;

import intervals.Interval;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EmpiricalGenerator {

    private List<Interval> continuousIntervals;
    private List<Interval> discreteIntervals;

    private List<Random> continuousGenerators;
    private List<Random> discreteGenerators;

    public EmpiricalGenerator(Random rand, List<Interval> continuousIntervals, List<Interval> discreteIntervals) {
        this.continuousIntervals = continuousIntervals;
        this.discreteIntervals = discreteIntervals;

        this.continuousGenerators = new ArrayList<>();
        this.discreteGenerators = new ArrayList<>();

        for (int i = 0; i < this.continuousIntervals.size() + 1; i++) {
            this.continuousGenerators.add(new Random(rand.nextInt()));
        }

        for (int i = 0; i < this.discreteIntervals.size() + 1; i++) {
            this.discreteGenerators.add(new Random(rand.nextInt()));
        }
    }

    public int randInt() {
        Random randProbability = this.discreteGenerators.getFirst();
        Random generator;
        Interval interval;

        double probability = randProbability.nextDouble();

        double cumulative = 0.0;
        for (int i = 0; i < this.discreteIntervals.size(); i++) {
            interval = this.discreteIntervals.get(i);
            cumulative += interval.getProbability();

            if (probability < cumulative) {
                generator = this.discreteGenerators.get(i + 1);
                return generator.nextInt(interval.getMin(), interval.getMax());
            }
        }

        generator = this.discreteGenerators.getLast();
        interval = this.discreteIntervals.getLast();
        return generator.nextInt(interval.getMin(), interval.getMax());
    }

    public double randDouble() {
        Random randProbability = this.continuousGenerators.getFirst();
        Random generator;
        Interval interval;

        double probability = randProbability.nextDouble();

        double cumulative = 0.0;
        for (int i = 0; i < this.continuousIntervals.size(); i++) {
            interval = this.continuousIntervals.get(i);
            cumulative += interval.getProbability();

            if (probability < cumulative) {
                generator = this.continuousGenerators.get(i + 1);
                return generator.nextDouble(interval.getMin(), interval.getMax());
            }
        }

        generator = this.continuousGenerators.getLast();
        interval = this.continuousIntervals.getLast();
        return generator.nextDouble(interval.getMin(), interval.getMax());
    }
}
