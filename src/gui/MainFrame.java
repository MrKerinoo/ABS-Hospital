package gui;

import gui.panels.ControlPanel;
import gui.panels.animation.AnimationPanel;
import gui.panels.simulation.SimulationPanel;
import simulation.MySimulation;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private final MySimulation core;
    private final JTabbedPane tabbedPane;

    public MainFrame(MySimulation core) {
        super("Simulácia nemocnice");
        this.core = core;

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1920, 1030);
        this.setLayout(new BorderLayout());

        AnimationPanel animationPanel = new AnimationPanel(core);

        this.add(new ControlPanel(this.core, animationPanel), BorderLayout.NORTH);
        this.tabbedPane = new JTabbedPane();

        SimulationPanel simulationPanel = new SimulationPanel(core);

        this.tabbedPane.addTab("Simulácia", simulationPanel);
        this.tabbedPane.addTab("Animácia", animationPanel);

        this.add(this.tabbedPane, BorderLayout.CENTER);
        this.setVisible(true);
    }
}