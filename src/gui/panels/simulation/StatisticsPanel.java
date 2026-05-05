package gui.panels.simulation;

import agents.agentenvironment.AgentEnvironment;
import agents.agenthospital.AgentHospital;
import agents.agentresources.AgentResources;
import simulation.MySimulation;

import javax.swing.*;
import java.awt.*;

import static utils.Utils.*;

public class StatisticsPanel extends JPanel {

    private final MySimulation sim;

    // --- LOCAL STATISTICS ---
    // --- Queues ---
    private final JLabel lAvgQueueEntrance = new JLabel("0.00");
    private final JLabel lAvgQueueMedical  = new JLabel("0.00");

    // --- Waiting Times (Generic) ---
    private final JLabel lAvgWaitEntrance  = new JLabel("00:00:00");
    private final JLabel lAvgWaitMedical   = new JLabel("00:00:00");
    private final JLabel lAvgTimeInSystem  = new JLabel("00:00:00");

    // --- Waiting Times (Partitioned) ---
    private final JLabel lWaitEntWalk      = new JLabel("00:00:00");
    private final JLabel lWaitEntAmb       = new JLabel("00:00:00");
    private final JLabel lWaitMedWalk      = new JLabel("00:00:00");
    private final JLabel lWaitMedAmb       = new JLabel("00:00:00");
    private final JLabel lTimeSysWalk      = new JLabel("00:00:00");
    private final JLabel lTimeSysAmb       = new JLabel("00:00:00");

    private final JLabel lArrivalMedical      = new JLabel("00:00:00");
    private final JLabel lArrivalMedicalWalk  = new JLabel("00:00:00");
    private final JLabel lArrivalMedicalAmb   = new JLabel("00:00:00");

    // --- Resource Usage ---
    private final JLabel lUsageDoctors     = new JLabel("0.00 %");
    private final JLabel lUsageNurses      = new JLabel("0.00 %");
    private final JLabel lUsageAmbulancesA = new JLabel("0.00 %");
    private final JLabel lUsageAmbulancesB = new JLabel("0.00 %");

    // --- Patient Counts ---
    private final JLabel lPatientsIn       = new JLabel("0");
    private final JLabel lPatientsOut      = new JLabel("0");
    private final JLabel lPatientsInSys    = new JLabel("0");
    private final JLabel lPatientsInWalk    = new JLabel("0");
    private final JLabel lPatientsInAmb     = new JLabel("0");
    private final JLabel lPatientsOutWalk   = new JLabel("0");
    private final JLabel lPatientsOutAmb    = new JLabel("0");



    // --- GLOBAL STATISTICS + CI ---
    private final JLabel gAvgQueueEntrance = new JLabel("0.00");
    private final JLabel gIsQueueEntrance  = new JLabel("<0.00, 0.00>");
    private final JLabel gAvgQueueMedical  = new JLabel("0.00");
    private final JLabel gIsQueueMedical   = new JLabel("<0.00, 0.00>");

    private final JLabel gAvgWaitEntrance  = new JLabel("00:00:00");
    private final JLabel gIsWaitEntrance   = new JLabel("<00:00:00, 00:00:00>");
    private final JLabel gAvgWaitMedical   = new JLabel("00:00:00");
    private final JLabel gIsWaitMedical    = new JLabel("<00:00:00, 00:00:00>");
    private final JLabel gAvgTimeInSystem  = new JLabel("00:00:00");
    private final JLabel gIsTimeInSystem   = new JLabel("<00:00:00, 00:00:00>");

    // Entrance
    private final JLabel gWaitEntWalk      = new JLabel("00:00:00");
    private final JLabel gIsWaitEntWalk    = new JLabel("<00:00:00, 00:00:00>");
    private final JLabel gWaitEntAmb       = new JLabel("00:00:00");
    private final JLabel gIsWaitEntAmb     = new JLabel("<00:00:00, 00:00:00>");

    private final JLabel gArrivalMedical      = new JLabel("00:00:00");
    private final JLabel gIsArrivalMedical    = new JLabel("<00:00:00, 00:00:00>");
    private final JLabel gArrivalMedicalWalk  = new JLabel("00:00:00");
    private final JLabel gIsArrivalMedicalWalk = new JLabel("<00:00:00, 00:00:00>");
    private final JLabel gArrivalMedicalAmb   = new JLabel("00:00:00");
    private final JLabel gIsArrivalMedicalAmb = new JLabel("<00:00:00, 00:00:00>");

    // Medical
    private final JLabel gWaitMedWalk      = new JLabel("00:00:00");
    private final JLabel gIsWaitMedWalk    = new JLabel("<00:00:00, 00:00:00>");
    private final JLabel gWaitMedAmb       = new JLabel("00:00:00");
    private final JLabel gIsWaitMedAmb     = new JLabel("<00:00:00, 00:00:00>");

    private final JLabel gTimeSysWalk      = new JLabel("00:00:00");
    private final JLabel gIsTimeSysWalk    = new JLabel("<00:00:00, 00:00:00>");
    private final JLabel gTimeSysAmb       = new JLabel("00:00:00");
    private final JLabel gIsTimeSysAmb     = new JLabel("<00:00:00, 00:00:00>");

    // Resource Usage
    private final JLabel gUsageDoctors      = new JLabel("0.00 %");
    private final JLabel gIsUsageDoctors    = new JLabel("<0.00, 0.00>");
    private final JLabel gUsageNurses       = new JLabel("0.00 %");
    private final JLabel gIsUsageNurses     = new JLabel("<0.00, 0.00>");
    private final JLabel gUsageAmbulancesA  = new JLabel("0.00 %");
    private final JLabel gIsUsageAmbulancesA = new JLabel("<0.00, 0.00>");
    private final JLabel gUsageAmbulancesB  = new JLabel("0.00 %");
    private final JLabel gIsUsageAmbulancesB = new JLabel("<0.00, 0.00>");

    // Patient Counts ---
    private final JLabel gAvgPatientsIn     = new JLabel("0.00");
    private final JLabel gIsPatientsIn      = new JLabel("<0.00, 0.00>");
    private final JLabel gAvgPatientsOut    = new JLabel("0.00");
    private final JLabel gIsPatientsOut     = new JLabel("<0.00, 0.00>");
    private final JLabel gAvgPatientsInWalk = new JLabel("0.00");
    private final JLabel gIsPatientsInWalk  = new JLabel("<0.00, 0.00>");
    private final JLabel gAvgPatientsInAmb  = new JLabel("0.00");
    private final JLabel gIsPatientsInAmb   = new JLabel("<0.00, 0.00>");
    private final JLabel gAvgPatientsOutWalk = new JLabel("0.00");
    private final JLabel gIsPatientsOutWalk  = new JLabel("<0.00, 0.00>");
    private final JLabel gAvgPatientsOutAmb  = new JLabel("0.00");
    private final JLabel gIsPatientsOutAmb   = new JLabel("<0.00, 0.00>");

    public StatisticsPanel(MySimulation sim) {
        this.sim = sim;
        this.setLayout(new GridLayout(2, 1, 0, 10));

        // --- LOCAL ---
        JPanel localPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        localPanel.setBorder(BorderFactory.createTitledBorder("Lokálne štatistiky"));

        // 1. Zdroje a Rady (Spojené)
        localPanel.add(createSection("Zdroje a Rady", new String[]{
                "Dĺžka radu (Vstup):", "Dĺžka radu (Lekárske):",
                "Lekári:", "Sestričky:", "Ambulancie A:", "Ambulancie B:"
        }, new JLabel[]{
                lAvgQueueEntrance, lAvgQueueMedical,
                lUsageDoctors, lUsageNurses, lUsageAmbulancesA, lUsageAmbulancesB
        }));

        // 2. Časy čakania (Vstup/Lekár)
        localPanel.add(createSection("Doba čakania", new String[]{
                "Vstupné (CELKOVO):", "Vstupné (PEŠÍ):", "Vstupné (SANITKA):",
                "Lekárske (CELKOVO):", "Lekárske (PEŠÍ):", "Lekárske (SANITKA):"
        }, new JLabel[]{
                lAvgWaitEntrance, lWaitEntWalk, lWaitEntAmb,
                lAvgWaitMedical, lWaitMedWalk, lWaitMedAmb
        }));

        // 3. Systémové časy
        localPanel.add(createSection("Čas v systéme", new String[]{
                "Celkovo:", "Peší:", "Sanitka:",
                "Príchod -> Ambulancia (CELKOM):", "Príchod -> Ambulancia (PEŠÍ):", "Príchod -> Ambulancia (AMB):"
        }, new JLabel[]{
                lAvgTimeInSystem, lTimeSysWalk, lTimeSysAmb,
                lArrivalMedical, lArrivalMedicalWalk, lArrivalMedicalAmb
        }));

        // 4. Pacienti
        localPanel.add(createSection("Pacienti", new String[]{
                "Príchody (CELKOVO):", "Príchody (PEŠO):", "Príchody (SANITKA):",
                "Odchody (CELKOVO):", "Odchody (PEŠO):", "Odchody (SANITKA):",
                "Počet v systéme:"
        }, new JLabel[]{
                lPatientsIn, lPatientsInWalk, lPatientsInAmb,
                lPatientsOut, lPatientsOutWalk, lPatientsOutAmb,
                lPatientsInSys
        }));

        // --- GLOBAL ---
        JPanel globalPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        globalPanel.setBorder(BorderFactory.createTitledBorder("Globálne štatistiky"));

        // 1. Zdroje a Rady (Spojené)
        globalPanel.add(createSection("Zdroje a Rady",
                new String[]{
                        "Dĺžka radu (Vstup):", "Dĺžka radu (Lekárske):",
                        "Lekári:", "Sestričky:", "Ambulancie A:", "Ambulancie B:"
                },
                new JLabel[]{
                        gAvgQueueEntrance, gIsQueueEntrance,
                        gAvgQueueMedical, gIsQueueMedical,
                        gUsageDoctors, gIsUsageDoctors,
                        gUsageNurses, gIsUsageNurses,
                        gUsageAmbulancesA, gIsUsageAmbulancesA,
                        gUsageAmbulancesB, gIsUsageAmbulancesB
                }));

        // 2. Doba čakania (Vstup/Lekár)
        globalPanel.add(createSection("Doba čakania", new String[]{
                "Vstupné (CELKOVO):", "Vstupné (PEŠÍ):", "Vstupné (SANITKA):",
                "Lekárske (CELKOVO):", "Lekárske (PEŠÍ):", "Lekárske (SANITKA):"
        }, new JLabel[]{
                gAvgWaitEntrance, gIsWaitEntrance,
                gWaitEntWalk, gIsWaitEntWalk,
                gWaitEntAmb, gIsWaitEntAmb,
                gAvgWaitMedical, gIsWaitMedical,
                gWaitMedWalk, gIsWaitMedWalk,
                gWaitMedAmb, gIsWaitMedAmb
        }));

        // 3. Systémové časy
        globalPanel.add(createSection("Čas v systéme", new String[]{
                "Celkovo:", "Peší:", "Sanitka:",
                "Príchod -> Ambulancia (CELKOM):", "Príchod -> Ambulancia (PEŠÍ):", "Príchod -> Ambulancia (AMB):"
        }, new JLabel[]{
                gAvgTimeInSystem, gIsTimeInSystem,
                gTimeSysWalk, gIsTimeSysWalk,
                gTimeSysAmb, gIsTimeSysAmb,
                gArrivalMedical, gIsArrivalMedical,
                gArrivalMedicalWalk, gIsArrivalMedicalWalk,
                gArrivalMedicalAmb, gIsArrivalMedicalAmb
        }));

        // 4. Pacienti
        globalPanel.add(createSection("Pacienti", new String[]{
                "Príchody (CELKOVO):", "Príchody (PEŠO):", "Príchody (SANITKA):",
                "Odchody (CELKOVO):", "Odchody (PEŠO):", "Odchody (SANITKA):"
        }, new JLabel[]{
                gAvgPatientsIn, gIsPatientsIn,
                gAvgPatientsInWalk, gIsPatientsInWalk,
                gAvgPatientsInAmb, gIsPatientsInAmb,
                gAvgPatientsOut, gIsPatientsOut,
                gAvgPatientsOutWalk, gIsPatientsOutWalk,
                gAvgPatientsOutAmb, gIsPatientsOutAmb
        }));

        this.add(localPanel);
        this.add(globalPanel);
    }

    private JPanel createSection(String title, String[] labels, JLabel[] values) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createTitledBorder(title));

        int maxWidth = 0;
        for (String label : labels) {
            JLabel tmp = new JLabel(label);
            tmp.setFont(new Font("SansSerif", Font.PLAIN, 11));
            maxWidth = Math.max(maxWidth, tmp.getPreferredSize().width);
        }
        final int labelWidth = maxWidth + 6;

        boolean hasIntervals = values.length == labels.length * 2;

        for (int i = 0; i < labels.length; i++) {
            JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 2));

            JLabel desc = new JLabel(labels[i]);
            desc.setFont(new Font("SansSerif", Font.PLAIN, 11));
            desc.setPreferredSize(new Dimension(labelWidth, desc.getPreferredSize().height));
            row.add(desc);

            JLabel valPrimary = hasIntervals ? values[i * 2] : values[i];
            valPrimary.setFont(new Font("Monospaced", Font.BOLD, 12));
            valPrimary.setForeground(new Color(0, 102, 204));
            row.add(valPrimary);

            if (hasIntervals) {
                JLabel valIs = values[i * 2 + 1];
                valIs.setFont(new Font("Monospaced", Font.PLAIN, 12));
                valIs.setForeground(Color.BLACK);
                row.add(new JLabel(" "));
                row.add(valIs);
            }

            p.add(row);
        }
        return p;
    }

    public void refreshAllStatistics() {
        this.refreshGlobalStatistics();
        this.refreshLocalStatistics();
    }

    public void refreshLocalStatistics() {
        // --- Agent references ---
        AgentEnvironment env = sim.agentEnvironment();
        AgentHospital hosp = sim.agentHospital();
        AgentResources res = sim.agentResources();

        // --- Basic counts & Overall times ---
        lPatientsIn.setText(String.valueOf(env.getPatientsIn()));
        lPatientsOut.setText(String.valueOf(env.getPatientsOut()));
        lPatientsInWalk.setText(String.valueOf(env.getPatientsInWalk()));
        lPatientsInAmb.setText(String.valueOf(env.getPatientsInAmbulance()));
        lPatientsOutWalk.setText(String.valueOf(env.getPatientsOutWalk()));
        lPatientsOutAmb.setText(String.valueOf(env.getPatientsOutAmbulance()));
        lPatientsInSys.setText(String.valueOf(env.getPatientsIn() - env.getPatientsOut()));
        lAvgTimeInSystem.setText(formatTime(env.getTimeInSystem().getMean()));

        // --- Queue lengths ---
        lAvgQueueEntrance.setText(formatNum(res.getEntranceQueueLength().getMean()));
        lAvgQueueMedical.setText(formatNum(res.getMedicalQueueLength().getMean()));

        // --- Generic waiting times ---
        lAvgWaitEntrance.setText(formatTime(hosp.getWaitEntrance().getMean()));
        lAvgWaitMedical.setText(formatTime(hosp.getWaitMedical().getMean()));

        lArrivalMedical.setText(formatTime(hosp.getTimeFromArrivalToMedicalExam().getMean()));
        lArrivalMedicalWalk.setText(formatTime(hosp.getTimeFromArrivalToMedicalExamWalk().getMean()));
        lArrivalMedicalAmb.setText(formatTime(hosp.getTimeFromArrivalToMedicalExamAmbulance().getMean()));

        // --- Partitioned waiting times (WALK / AMBULANCE) ---
        lWaitEntWalk.setText(formatTime(hosp.getWaitEntranceWalk().getMean()));
        lWaitEntAmb.setText(formatTime(hosp.getWaitEntranceAmbulance().getMean()));
        lWaitMedWalk.setText(formatTime(hosp.getWaitMedicalWalk().getMean()));
        lWaitMedAmb.setText(formatTime(hosp.getWaitMedicalAmbulance().getMean()));

        // --- Partitioned system times (WALK / AMBULANCE) ---
        lTimeSysWalk.setText(formatTime(env.getTimeInSystemWalk().getMean()));
        lTimeSysAmb.setText(formatTime(env.getTimeInSystemAmbulance().getMean()));

        // --- Resource utilization ---
        lUsageDoctors.setText(formatPercent(res.getDoctorUsage().getMean()));
        lUsageNurses.setText(formatPercent(res.getNurseUsage().getMean()));
        lUsageAmbulancesA.setText(formatPercent(res.getAmbulanceAUsage().getMean()));
        lUsageAmbulancesB.setText(formatPercent(res.getAmbulanceBUsage().getMean()));
    }

    public void refreshGlobalStatistics() {
        // --- Global patient throughput ---
        gAvgPatientsIn.setText(formatNum(sim.getPatientsIn().getMean()));
        gIsPatientsIn.setText(formatIS(sim.getPatientsIn().getConfidenceInterval95()));
        gAvgPatientsInWalk.setText(formatNum(sim.getPatientsInWalk().getMean()));
        gIsPatientsInWalk.setText(formatIS(sim.getPatientsInWalk().getConfidenceInterval95()));
        gAvgPatientsInAmb.setText(formatNum(sim.getPatientsInAmbulance().getMean()));
        gIsPatientsInAmb.setText(formatIS(sim.getPatientsInAmbulance().getConfidenceInterval95()));

        gAvgPatientsOut.setText(formatNum(sim.getPatientsOut().getMean()));
        gIsPatientsOut.setText(formatIS(sim.getPatientsOut().getConfidenceInterval95()));
        gAvgPatientsOutWalk.setText(formatNum(sim.getPatientsOutWalk().getMean()));
        gIsPatientsOutWalk.setText(formatIS(sim.getPatientsOutWalk().getConfidenceInterval95()));
        gAvgPatientsOutAmb.setText(formatNum(sim.getPatientsOutAmbulance().getMean()));
        gIsPatientsOutAmb.setText(formatIS(sim.getPatientsOutAmbulance().getConfidenceInterval95()));

        // --- Global generic times ---
        gAvgTimeInSystem.setText(formatTime(sim.getTimeInSystem().getMean()));
        gIsTimeInSystem.setText(formatISTime(sim.getTimeInSystem().getConfidenceInterval95()));
        gAvgWaitEntrance.setText(formatTime(sim.getWaitEntrance().getMean()));
        gIsWaitEntrance.setText(formatISTime(sim.getWaitEntrance().getConfidenceInterval95()));
        gAvgWaitMedical.setText(formatTime(sim.getWaitMedical().getMean()));
        gIsWaitMedical.setText(formatISTime(sim.getWaitMedical().getConfidenceInterval95()));

        gArrivalMedical.setText(formatTime(sim.getArrivalToMedical().getMean()));
        gIsArrivalMedical.setText(formatISTime(sim.getArrivalToMedical().getConfidenceInterval95()));
        gArrivalMedicalWalk.setText(formatTime(sim.getArrivalToMedicalWalk().getMean()));
        gIsArrivalMedicalWalk.setText(formatISTime(sim.getArrivalToMedicalWalk().getConfidenceInterval95()));
        gArrivalMedicalAmb.setText(formatTime(sim.getArrivalToMedicalAmb().getMean()));
        gIsArrivalMedicalAmb.setText(formatISTime(sim.getArrivalToMedicalAmb().getConfidenceInterval95()));

        // --- Global partitioned Entrance waiting ---
        gWaitEntWalk.setText(formatTime(sim.getWaitEntranceWalk().getMean()));
        gIsWaitEntWalk.setText(formatISTime(sim.getWaitEntranceWalk().getConfidenceInterval95()));
        gWaitEntAmb.setText(formatTime(sim.getWaitEntranceAmbulance().getMean()));
        gIsWaitEntAmb.setText(formatISTime(sim.getWaitEntranceAmbulance().getConfidenceInterval95()));

        // --- Global partitioned Medical waiting ---
        gWaitMedWalk.setText(formatTime(sim.getWaitMedicalWalk().getMean()));
        gIsWaitMedWalk.setText(formatISTime(sim.getWaitMedicalWalk().getConfidenceInterval95()));
        gWaitMedAmb.setText(formatTime(sim.getWaitMedicalAmbulance().getMean()));
        gIsWaitMedAmb.setText(formatISTime(sim.getWaitMedicalAmbulance().getConfidenceInterval95()));

        // --- Global partitioned System times ---
        gTimeSysWalk.setText(formatTime(sim.getTimeInSystemWalk().getMean()));
        gIsTimeSysWalk.setText(formatISTime(sim.getTimeInSystemWalk().getConfidenceInterval95()));
        gTimeSysAmb.setText(formatTime(sim.getTimeInSystemAmbulance().getMean()));
        gIsTimeSysAmb.setText(formatISTime(sim.getTimeInSystemAmbulance().getConfidenceInterval95()));

        // --- Global queue lengths ---
        gAvgQueueEntrance.setText(formatNum(sim.getEntranceQueueLength().getMean()));
        gIsQueueEntrance.setText(formatIS(sim.getEntranceQueueLength().getConfidenceInterval95()));
        gAvgQueueMedical.setText(formatNum(sim.getMedicalQueueLength().getMean()));
        gIsQueueMedical.setText(formatIS(sim.getMedicalQueueLength().getConfidenceInterval95()));

        // --- Global resource utilization ---
        gUsageDoctors.setText(formatPercent(sim.getDoctorUsage().getMean()));
        gIsUsageDoctors.setText(formatISPercent(sim.getDoctorUsage().getConfidenceInterval95()));
        gUsageNurses.setText(formatPercent(sim.getNurseUsage().getMean()));
        gIsUsageNurses.setText(formatISPercent(sim.getNurseUsage().getConfidenceInterval95()));
        gUsageAmbulancesA.setText(formatPercent(sim.getAmbulanceAUsage().getMean()));
        gIsUsageAmbulancesA.setText(formatISPercent(sim.getAmbulanceAUsage().getConfidenceInterval95()));
        gUsageAmbulancesB.setText(formatPercent(sim.getAmbulanceBUsage().getMean()));
        gIsUsageAmbulancesB.setText(formatISPercent(sim.getAmbulanceBUsage().getConfidenceInterval95()));
    }
}