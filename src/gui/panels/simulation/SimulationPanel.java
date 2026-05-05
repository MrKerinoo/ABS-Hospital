package gui.panels.simulation;

import OSPABA.ISimDelegate;
import OSPABA.SimState;
import OSPABA.Simulation;
import comparators.ResourceComparator;
import entities.Ambulance;
import simulation.MySimulation;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SimulationPanel extends JPanel implements ISimDelegate {

    private final MySimulation core;
    private final List<AmbulancePanel> panelsA = new ArrayList<>();
    private final List<AmbulancePanel> panelsB = new ArrayList<>();

    private QueuePanel entranceQueuePanel;
    private QueuePanel medicalQueuePanel;
    private PersonnelPanel doctorsPanel;
    private PersonnelPanel nursesPanel;
    private StatisticsPanel statisticsPanel;
    private LoggerPanel loggerPanel; // Added LoggerPanel

    public SimulationPanel(MySimulation core) {
        this.core = core;
        this.setLayout(new BorderLayout());

        // Initial GUI setup
        this.statisticsPanel = new StatisticsPanel(core);
        this.loggerPanel = new LoggerPanel(core); // Initialize logger

        setupLayout();
        core.registerDelegate(this);
    }

    // --- GUI Building ---

    private void setupLayout() {
        this.removeAll();

        // Main vertical wrapper
        JPanel topWrapper = new JPanel();
        topWrapper.setLayout(new BoxLayout(topWrapper, BoxLayout.Y_AXIS));

        // Section 1: Ambulances
        topWrapper.add(createAmbulanceSection());

        // Section 2: Queues and Personnel
        topWrapper.add(createQueuesSection());

        // Section 3: Statistics
        topWrapper.add(Box.createVerticalStrut(10));
        topWrapper.add(statisticsPanel);

        // Section 4: Logger
        topWrapper.add(Box.createVerticalStrut(10));
        topWrapper.add(loggerPanel);

        JPanel scrollContent = new JPanel(new BorderLayout());
        scrollContent.add(topWrapper, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(scrollContent);
        scrollPane.setBorder(null);

        this.add(scrollPane, BorderLayout.CENTER);

        this.revalidate();
        this.repaint();
    }

    private JPanel createAmbulanceSection() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Ambulancie"));

        panelsA.clear();
        for (int i = 1; i <= 5; i++) {
            AmbulancePanel ap = new AmbulancePanel("A" + i);
            panelsA.add(ap);
            panel.add(ap);
        }

        panel.add(new JSeparator(SwingConstants.VERTICAL) {{ setPreferredSize(new Dimension(2, 110)); }});

        panelsB.clear();
        for (int i = 1; i <= 7; i++) {
            AmbulancePanel bp = new AmbulancePanel("B" + i);
            panelsB.add(bp);
            panel.add(bp);
        }

        return panel;
    }

    private JPanel createQueuesSection() {
        JPanel container = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        // PACIENTI
        entranceQueuePanel = new QueuePanel(
                "Rad pred vstupným vyšetrením",
                new String[]{"#", "ID", "Sanitka"},
                false
        );

        medicalQueuePanel = new QueuePanel(
                "Rad pred lekárskym vyšetrením",
                new String[]{"#", "ID", "Priorita", "Sanitka"},
                true
        );

        // PERSONÁL
        doctorsPanel = new PersonnelPanel(
                core,
                "Zoznam lekárov",
                new String[]{"#", "ID", "Miesto", "Pracuje"},
                "D"
        );

        nursesPanel = new PersonnelPanel(
                core,
                "Zoznam sestričiek",
                new String[]{"#", "ID", "Miesto", "Pracuje"},
                "S"
        );

        container.add(entranceQueuePanel);
        container.add(medicalQueuePanel);
        container.add(doctorsPanel);
        container.add(nursesPanel);

        return container;
    }

    // --- Logic & Updates ---

    @Override
    public void refresh(Simulation sim) {
        if (!core.isVisualizationEnabled()) return;

        SwingUtilities.invokeLater(() -> {
            if (entranceQueuePanel == null || medicalQueuePanel == null) return;

            // --- Update Ambulances ---
            List<Ambulance> listA = core.agentResources().getAllAmbulancesA();
            List<Ambulance> listB = core.agentResources().getAllAmbulancesB();

            for (int i = 0; i < panelsA.size(); i++) {
                if (i < listA.size()) panelsA.get(i).refresh(listA.get(i));
            }
            for (int i = 0; i < panelsB.size(); i++) {
                if (i < listB.size()) panelsB.get(i).refresh(listB.get(i));
            }

            // --- Update Queues ---
            entranceQueuePanel.refresh(
                    core.agentResources().getEntranceRequests(),
                    new ResourceComparator()
            );

            java.util.Set<simulation.MyMessage> allMedicalRequests = new java.util.HashSet<>();
            allMedicalRequests.addAll(core.agentResources().getMedicalARequests());
            allMedicalRequests.addAll(core.agentResources().getMedicalBRequests());

            medicalQueuePanel.refresh(
                    allMedicalRequests,
                    new ResourceComparator()
            );

            // --- Update Personnel ---
            if (doctorsPanel != null) {
                doctorsPanel.refresh(core.agentResources().getAllDoctors());
            }
            if (nursesPanel != null) {
                nursesPanel.refresh(core.agentResources().getAllNurses());
            }

            // --- Update Statistics ---
            if (statisticsPanel != null) {
                statisticsPanel.refreshLocalStatistics();
            }
        });
    }

    @Override
    public void simStateChanged(Simulation sim, SimState simState) {
        switch (simState) {
            case running:
                break;

            case replicationRunning:
                if (loggerPanel != null) {
                    loggerPanel.clearLog();
                }
                SwingUtilities.invokeLater(this::setupLayout);
                break;

            case replicationStopped:
                this.statisticsPanel.refreshGlobalStatistics();
                break;

            case stopped:
                break;
        }
    }

    public void log(String message) {
        if (loggerPanel != null) {
            loggerPanel.addMessage(message);
        }
    }
}