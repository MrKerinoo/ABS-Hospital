package gui.panels.simulation;

import entities.Doctor;
import entities.Nurse;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Collection;

public class PersonnelPanel extends JPanel {

    private final DefaultTableModel tableModel;
    private final JLabel countLabel;
    private final String personnelType; // "D" pre doktorov, "S" pre sestry

    public PersonnelPanel(String title, String[] columns, String personnelType) {
        this.personnelType = personnelType;
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        this.setPreferredSize(new Dimension(450, 400));

        this.tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(235, 235, 235));
        header.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        JLabel titleLabel = new JLabel(title.toUpperCase());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 12));

        this.countLabel = new JLabel("Celkom: 0");
        this.countLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        this.countLabel.setForeground(Color.DARK_GRAY);

        textPanel.add(titleLabel);
        textPanel.add(Box.createVerticalStrut(2));
        textPanel.add(this.countLabel);

        header.add(textPanel, BorderLayout.WEST);

        JTable table = new JTable(tableModel);
        JScrollPane scroll = new JScrollPane(table);
        // Nastavenie výšky podľa tvojho QueuePanelu
        int rowHeight = table.getRowHeight();
        int headerHeight = table.getTableHeader().getPreferredSize().height;
        scroll.setPreferredSize(new Dimension(0, rowHeight * 15 + headerHeight + 2));

        this.add(header, BorderLayout.NORTH);
        this.add(scroll, BorderLayout.CENTER);
    }

    public void refresh(Collection<?> data) {
        tableModel.setRowCount(0);
        int i = 1;
        for (Object o : data) {
            if (personnelType.equals("D")) {
                Doctor d = (Doctor) o;
                String amb = (d.getAmbulance() != null) ? d.getAmbulance().getType() +  "" + d.getAmbulance().getId() : "Vchod";
                String working = (d.getAmbulance() != null && d.getAmbulance().getPatient() != null) ? "Áno" : "Nie";
                tableModel.addRow(new Object[]{i++, d.getId(), amb, working});
            } else {
                Nurse n = (Nurse) o;
                String amb = (n.getAmbulance() != null) ? n.getAmbulance().getType()+  "" + n.getAmbulance().getId() : "Vchod";
                String working = (n.getAmbulance() != null && n.getAmbulance().getPatient() != null) ? "Áno" : "Nie";
                tableModel.addRow(new Object[]{i++, n.getId(), amb, working});
            }
        }
        countLabel.setText("Celkom: " + data.size());
    }
}