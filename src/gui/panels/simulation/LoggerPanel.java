package gui.panels.simulation;

import simulation.MySimulation;
import javax.swing.*;
import java.awt.*;

import static utils.Utils.formatTime;

public class LoggerPanel extends JPanel {
    private final DefaultListModel<String> listModel;
    private final JList<String> logList;
    private final int MAX_LINES = 200;
    private final MySimulation core;

    public LoggerPanel(MySimulation core) {
        this.core = core;
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(400, 250));
        this.setBorder(BorderFactory.createTitledBorder("Log udalostí (Posledných " + MAX_LINES + ")"));

        this.listModel = new DefaultListModel<>();
        this.logList = new JList<>(listModel);
        this.logList.setFont(new Font("Monospaced", Font.PLAIN, 12));

        this.add(new JScrollPane(logList), BorderLayout.CENTER);
    }

    public void addMessage(String message) {
        String timeStr = formatTime(core.currentTime());
        String fullEntry = String.format("[%s] %s", timeStr, message);

        SwingUtilities.invokeLater(() -> {
            listModel.add(0, fullEntry); // Pridá na začiatok (vrch)
            if (listModel.size() > MAX_LINES) {
                listModel.removeElementAt(listModel.size() - 1);
            }
        });
    }

    public void clearLog() {
        SwingUtilities.invokeLater(listModel::clear);
    }
}