package gui.panels;

import OSPABA.ISimDelegate;
import OSPABA.SimState;
import OSPABA.Simulation;
import gui.panels.animation.AnimationPanel;
import simulation.MySimulation;

import javax.swing.*;
import java.awt.*;

public class ControlPanel extends JPanel implements ISimDelegate {

    private final MySimulation core;
    private Thread simThread;
    private final AnimationPanel animationPanel;

    private final JButton btnStart;
    private final JButton btnPause;
    private final JButton btnStop;
    private final JTextField txtReplications;
    private final JTextField txtNurses;
    private final JTextField txtDoctors;
    private final JCheckBox chkVisualization;
    private final JCheckBox chkAnimation;
    private final JSlider sldSpeed;
    private final JLabel lblSimTime;
    private final JLabel lblReplications;

    public ControlPanel(MySimulation core, AnimationPanel animPanel) {
        this.core = core;
        this.animationPanel = animPanel;

        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEtchedBorder(),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));

        // Control components
        btnStart = new JButton("Spustiť");
        txtReplications = new JTextField("1", 4);
        txtReplications.setMaximumSize(txtReplications.getPreferredSize());

        // Resource configuration
        txtNurses = new JTextField("5", 3);
        txtNurses.setMaximumSize(txtNurses.getPreferredSize());
        txtDoctors = new JTextField("3", 3);
        txtDoctors.setMaximumSize(txtDoctors.getPreferredSize());

        // Visualization and speed settings
        chkVisualization = new JCheckBox("Vizualizácia");
        chkAnimation = new JCheckBox("Animácia");
        sldSpeed = new JSlider(1, 100, 10);
        sldSpeed.setEnabled(false);
        sldSpeed.setMaximumSize(new Dimension(120, 30));

        // Time and replication display
        lblSimTime = new JLabel("Deň 1  00:00:00");
        lblSimTime.setFont(new Font("Monospaced", Font.BOLD, 18));
        lblSimTime.setOpaque(true);
        lblSimTime.setBackground(Color.WHITE);
        lblSimTime.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(3, 10, 3, 10)
        ));

        lblReplications = new JLabel("Replikácie: 0/0");
        btnPause = new JButton("Pozastaviť");
        btnStop = new JButton("Skončiť");
        btnStop.setForeground(Color.RED);

        // Layout assembly
        this.add(btnStart);
        this.add(Box.createHorizontalStrut(10));
        this.add(new JLabel("Replikácie:"));
        this.add(txtReplications);
        this.add(Box.createHorizontalStrut(10));
        this.add(new JLabel("Sestry:"));
        this.add(txtNurses);
        this.add(Box.createHorizontalStrut(10));
        this.add(new JLabel("Lekári:"));
        this.add(txtDoctors);
        this.add(Box.createHorizontalStrut(12));
        this.add(chkVisualization);
        this.add(Box.createHorizontalStrut(5));
        this.add(chkAnimation);
        this.add(Box.createHorizontalStrut(10));
        this.add(sldSpeed);
        this.add(Box.createHorizontalStrut(15));
        this.add(lblSimTime);
        this.add(Box.createHorizontalStrut(15));
        this.add(lblReplications);
        this.add(Box.createHorizontalGlue());
        this.add(btnPause);
        this.add(Box.createHorizontalStrut(10));
        this.add(btnStop);

        setupListeners();
        core.registerDelegate(this);
    }

    private void setupListeners() {
        // Main simulation controls
        btnStart.addActionListener(e -> {
            if (core.isRunning()) core.stopSimulation();
            else runSim();
        });

        btnPause.addActionListener(e -> {
            if (core.isPaused()) core.resumeSimulation();
            else core.pauseSimulation();
        });

        btnStop.addActionListener(e -> core.stopSimulation());

        // Animation logic using handleAnimation from AnimationPanel
        chkAnimation.addActionListener(e -> {
            if (chkAnimation.isSelected()) {
                core.createAnimator();
                if (animationPanel != null) {
                    animationPanel.handleAnimation(true);
                }

                if (chkVisualization.isSelected()) {
                    core.setSimSpeed(1, computeDuration());
                } else {
                    core.setMaxSimSpeed();
                }
            } else {
                if (core.animatorExists()) {
                    core.removeAnimator();
                }
                if (animationPanel != null) {
                    animationPanel.handleAnimation(false);
                }
            }
        });

        // Visualization logic
        chkVisualization.addActionListener(e -> {
            sldSpeed.setEnabled(chkVisualization.isSelected());
            if (chkVisualization.isSelected()) core.setSimSpeed(1, computeDuration());
            else core.setMaxSimSpeed();
        });

        sldSpeed.addChangeListener(e -> {
            if (chkVisualization.isSelected()) core.setSimSpeed(1, computeDuration());
        });
    }

    private double computeDuration() {
        double diff = sldSpeed.getValue() - 10;
        if (diff == 0) return 1.0;
        if (diff > 0) return 1.0 / (diff + 1.0);
        return Math.abs(diff);
    }

    private void runSim() {
        try {
            int replCount = Integer.parseInt(txtReplications.getText());
            core.setNursesCount(Integer.parseInt(txtNurses.getText()));
            core.setDoctorsCount(Integer.parseInt(txtDoctors.getText()));

            if (chkVisualization.isSelected()) core.setSimSpeed(1, computeDuration());
            else core.setMaxSimSpeed();

            simThread = new Thread(() -> core.simulate(replCount, 2_419_200));
            simThread.start();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input values.");
        }
    }

    private String formatSimTime(double seconds) {
        int day = (int) (seconds / 86400) + 1;
        int remaining = (int) (seconds % 86400);
        int hours = remaining / 3600;
        int minutes = (remaining % 3600) / 60;
        int secs = remaining % 60;
        return String.format("Deň %d  %02d:%02d:%02d", day, hours, minutes, secs);
    }

    @Override
    public void refresh(Simulation sim) {
        if (chkVisualization.isSelected() || core.currentReplication() % 50 == 0) {
            SwingUtilities.invokeLater(() -> {
                lblSimTime.setText(formatSimTime(core.currentTime()));
                lblReplications.setText("Replikácie: " + core.currentReplication() + "/" + txtReplications.getText());
            });
        }
    }

    @Override
    public void simStateChanged(Simulation sim, SimState simState) {
        SwingUtilities.invokeLater(() -> {
            switch (simState) {
                case stopped:
                    btnStart.setText("Spustiť");
                    btnPause.setText("Pozastaviť");
                    break;
                case paused:
                    btnPause.setText("Obnoviť");
                    break;
                case running:
                    btnPause.setText("Pozastaviť");
                    break;
            }
        });
    }
}