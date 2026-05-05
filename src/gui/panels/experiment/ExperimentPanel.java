package gui.panels.experiment;

import simulation.MySimulation;
import statistics.DiscreteStatistics;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import static utils.Utils.formatTime;

public class ExperimentPanel extends JPanel {
    private final MySimulation core;
    private JProgressBar progressBar;
    private DefaultTableModel tableModel;
    private volatile boolean isRunning = false;

    private static final double AMBULANCE_LIMIT = 15 * 60;
    private static final double WALK_LIMIT = 30 * 60;

    public ExperimentPanel(MySimulation core) {
        this.core = core;
        this.setLayout(new BorderLayout(10, 10));

        this.progressBar = new JProgressBar(0, 144);
        this.progressBar.setStringPainted(true);
        this.add(this.progressBar, BorderLayout.NORTH);

        String[] columns = {"Sestry", "Lekári", "Čakanie Sanitka [mm:ss]", "Čakanie Pešo [mm:ss]", "STATUS"};
        this.tableModel = new DefaultTableModel(columns, 0);
        JTable table = new JTable(this.tableModel);
        this.add(new JScrollPane(table), BorderLayout.CENTER);
    }

    public void startAnalysis(int replications) {
        new Thread(() -> {
            this.isRunning = true;
            List<String[]> csvData = new ArrayList<>();

            SwingUtilities.invokeLater(() -> {
                this.tableModel.setRowCount(0);
                this.progressBar.setValue(0);
            });

            try {
                int step = 0;
                for (int nurses = 1; nurses <= 12; nurses++) {
                    for (int doctors = 1; doctors <= 12; doctors++) {
                        if (!this.isRunning) break;

                        core.setNursesCount(nurses);
                        core.setDoctorsCount(doctors);
                        core.setWarmupFind(false);
                        core.setTotalReplications(replications);
                        core.setMaxSimSpeed();
                        core.simulate(replications, 2_419_200 + core.getWarmupTime());

                        if (!this.isRunning) break;

                        double ambulanceWait = core.getArrivalToMedicalAmb().getMean();
                        double walkWait = core.getArrivalToMedicalWalk().getMean();
                        boolean ok = ambulanceWait <= AMBULANCE_LIMIT && walkWait < WALK_LIMIT;
                        String status = ok ? "✓ VYHOVUJE" : "✗ NEVYHOVUJE";

                        int fn = nurses, fd = doctors;
                        String fa = formatTime(ambulanceWait), fw = formatTime(walkWait);
                        step++;
                        int fs = step;
                        SwingUtilities.invokeLater(() -> {
                            this.tableModel.addRow(new Object[]{fn, fd, fa, fw, status});
                            this.progressBar.setValue(fs);
                        });

                        csvData.add(new String[]{
                                String.valueOf(nurses), String.valueOf(doctors),
                                String.format("%.2f", ambulanceWait).replace('.', ','),
                                String.format("%.2f", walkWait).replace('.', ','),
                                ok ? "VYHOVUJE" : "NEVYHOVUJE"
                        });
                    }
                    if (!this.isRunning) break;
                }
                this.exportToCSV(csvData);
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                this.isRunning = false;
            }
        }).start();
    }

    public void stopAnalysis() {
        this.isRunning = false;
    }

    private void exportToCSV(List<String[]> data) {
        File folder = new File("./data");
        if (!folder.exists()) folder.mkdirs();

        try (FileWriter writer = new FileWriter("./data/experiment_results.csv")) {
            writer.write("Sestry;Lekari;Cakanie_Sanitka_s;Cakanie_Peso_s;Status\n");
            for (String[] row : data) {
                writer.write(String.join(";", row) + "\n");
            }
            System.out.println("Export hotový: ./data/experiment_results.csv");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}