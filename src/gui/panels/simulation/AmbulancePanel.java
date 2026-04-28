package gui.panels.simulation;

import entities.Ambulance;
import javax.swing.*;
import java.awt.*;

public class AmbulancePanel extends JPanel {
    private final String label;
    private final JLabel doctorLabel;
    private final JLabel nurseLabel;
    private final JLabel patientLabel;

    public AmbulancePanel(String label) {
        this.label = label;
        this.setLayout(new GridLayout(4, 1, 2, 2));
        this.setBorder(BorderFactory.createTitledBorder(label));
        this.setPreferredSize(new Dimension(150, 100));

        this.doctorLabel = new JLabel("Doktor: -");
        this.nurseLabel = new JLabel("Sestra: -");
        this.patientLabel = new JLabel("Pacient: -");

        this.add(doctorLabel);
        this.add(nurseLabel);
        this.add(patientLabel);
    }

    public void refresh(Ambulance ambulance) {
        if (ambulance == null) return;

        doctorLabel.setText(ambulance.getDoctor() != null
                ? "Doktor: D" + ambulance.getDoctor().getId()
                : "Doktor: -");

        nurseLabel.setText(ambulance.getNurse() != null
                ? "Sestra: S" + ambulance.getNurse().getId()
                : "Sestra: -");

        if (ambulance.getPatient() != null) {
            this.setBackground(new Color(144, 238, 144));
            patientLabel.setText("Pacient: P" + ambulance.getPatient().getId());
        } else {
            this.setBackground(new Color(220, 220, 220));
            patientLabel.setText("Pacient: -");
        }
    }
}