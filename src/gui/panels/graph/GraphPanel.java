package gui.panels.graph;

import OSPABA.ISimDelegate;
import OSPABA.SimState;
import OSPABA.Simulation;
import simulation.MySimulation;

import javax.swing.*;
import java.awt.*;

public class GraphPanel extends JPanel implements ISimDelegate {
    private final MySimulation core;
    private final Graph graphWalk;
    private final Graph graphAmbulance;

    public GraphPanel(MySimulation core) {
        this.core = core;
        this.setLayout(new GridLayout(2, 1));

        this.graphWalk = new Graph("Čas od príchodu po ošetrenie - Pešo", "Počet replikácií", "Čas [hh:mm:ss]");
        this.graphAmbulance = new Graph("Čas od príchodu po ošetrenie - Sanitka", "Počet replikácií", "Čas [hh:mm:ss]");

        this.add(this.graphWalk.getChartPanel());
        this.add(this.graphAmbulance.getChartPanel());

        core.registerDelegate(this);
    }

    @Override
    public void refresh(Simulation sim) {

    }

    @Override
    public void simStateChanged(Simulation sim, SimState simState) {
        switch (simState) {
            case running:
                SwingUtilities.invokeLater(() -> {
                    graphWalk.reset();
                    graphAmbulance.reset();
                });
                break;

            case replicationStopped:
                this.addGraphPoint(sim);
                break;

            case stopped:
                break;
        }
    }

    private void addGraphPoint(Simulation sim) {
        int currentRepl = sim.currentReplication();
        int totalRepl = ((MySimulation) sim).getTotalReplications();

        int step = Math.max(totalRepl / 1000, 1);
        if (currentRepl % step != 0) return;

        double walk = core.getArrivalToMedicalWalk().getMean();
        double ambulance = core.getArrivalToMedicalAmb().getMean();

        if (!Double.isNaN(walk) && !Double.isNaN(ambulance) && walk > 0) {
            SwingUtilities.invokeLater(() -> {
                graphWalk.addPoint(currentRepl + 1, walk, "Pešo");
                graphAmbulance.addPoint(currentRepl + 1, ambulance, "Sanitka");
            });
        }
    }
}