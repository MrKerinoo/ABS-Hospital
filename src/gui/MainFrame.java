package gui;

import OSPABA.ISimDelegate;
import OSPABA.SimState;
import OSPABA.Simulation;
import simulation.MySimulation;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private final MySimulation core;
    private final JTabbedPane tabbedPane;

    public MainFrame (MySimulation core) {
        super("Simulácia nemocnice");
        this.core = core;

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1920, 1030);
        this.setLayout(new BorderLayout());

        this.tabbedPane = new JTabbedPane();

        this.tabbedPane.addTab("Simulácia", this.createSimulationTab());

        this.tabbedPane.addTab("Animácia", null);

        this.add(this.tabbedPane, BorderLayout.CENTER);
        this.setVisible(true);
    }

    private JPanel createSimulationTab() {
        JPanel mainPanel = new JPanel(new BorderLayout(5, 5));

        core.simulate(1, 2_419_200);

        return mainPanel;
    }
}
