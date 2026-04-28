package gui.panels.simulation;

import OSPABA.MessageForm;
import simulation.MyMessage;
import entities.Patient;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

public class QueuePanel extends JPanel {

    private final DefaultTableModel tableModel;
    private final JLabel countLabel;
    private final boolean showPriority;

    public QueuePanel(String title, String[] columns, boolean showPriority) {
        this.showPriority = showPriority;
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        this.setPreferredSize(new Dimension(450, 400));

        // Model
        this.tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(242, 242, 242));
        header.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        JLabel titleLabel = new JLabel(title.toUpperCase());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 12));

        this.countLabel = new JLabel("V rade: 0");
        this.countLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        this.countLabel.setForeground(Color.DARK_GRAY);

        textPanel.add(titleLabel);
        textPanel.add(Box.createVerticalStrut(2));
        textPanel.add(this.countLabel);

        header.add(textPanel, BorderLayout.WEST);

        // Table
        JTable table = new JTable(tableModel);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(440, 320));
        int rowHeight = table.getRowHeight();
        int headerHeight = table.getTableHeader().getPreferredSize().height;
        scroll.setPreferredSize(new Dimension(0, rowHeight * 15 + headerHeight + 2));

        this.add(header, BorderLayout.NORTH);
        this.add(scroll, BorderLayout.CENTER);
    }

    public void refresh(Collection<MyMessage> queueData, Comparator<MessageForm> comparator) {
        tableModel.setRowCount(0);
        List<MyMessage> sortedList = new ArrayList<>(queueData);
        sortedList.sort(comparator);

        int i = 1;
        for (MyMessage msg : sortedList) {
            Patient p = msg.getPatient();
            if (showPriority) {
                tableModel.addRow(new Object[]{i++, p.getId(), p.getPriority(), p.isWithAmbulance() ? "Áno" : "Nie"});
            } else {
                tableModel.addRow(new Object[]{i++, p.getId(), p.isWithAmbulance() ? "Áno" : "Nie"});
            }
        }
        countLabel.setText("V rade: " + sortedList.size());
    }
}