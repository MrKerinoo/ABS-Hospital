package gui.panels.animation;

import javax.swing.*;
import java.awt.*;
import simulation.Config;
import simulation.MySimulation;

public class AnimationPanel extends JPanel {
    private final MySimulation sim;

    public AnimationPanel(MySimulation sim) {
        this.sim = sim;
        this.setLayout(new BorderLayout());
    }

    public void handleAnimation(boolean enabled) {
        this.removeAll();
        if (enabled && sim.animatorExists()) {

            sim.animator().setBackgroundImage(Config.IMG_BACKGROUND);
            this.add(sim.animator().canvas(), BorderLayout.CENTER);
            sim.animator().canvas().setBackground(Color.WHITE);
        }

        // Refresh GUI hierarchy
        this.revalidate();
        this.repaint();
    }
}