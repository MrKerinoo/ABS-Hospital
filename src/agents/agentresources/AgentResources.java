package agents.agentresources;

import OSPABA.*;
import simulation.*;
import agents.agentresources.continualassistants.*;
import comparators.ResourceComparator;
import entities.Ambulance;
import entities.Doctor;
import entities.Nurse;
import generators.ContinuousGenerator;
import generators.TriangularGenerator;
import statistics.TimeStatistics;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

//meta! id="16"
public class AgentResources extends OSPABA.Agent
{
    List<Nurse> freeNurses;
    List<Doctor> freeDoctors;
    List<Ambulance> freeAmbulancesA;
    List<Ambulance> freeAmbulancesB;

    List<Nurse> allNurses;
    List<Doctor> allDoctors;
    List<Ambulance> allAmbulancesA;
    List<Ambulance> allAmbulancesB;

    PriorityQueue<MessageForm> waitingAmbulanceARequests;
    PriorityQueue<MessageForm> waitingAmbulanceBRequests;

    // GENERATORS

    private ContinuousGenerator entranceAmbulanceMoveGenerator;
    private TriangularGenerator ambulanceMoveGenerator;

    // STATISTICS

    // Priemerné vyťaženie lekárov
    private TimeStatistics doctorUsage;
    // Priemerné vyťaženie sestričiek
    private TimeStatistics nurseUsage;
    // Priemerné vyťaženie ambulancií typu A
    private TimeStatistics ambulanceAUsage;
    // Priemerné vyťaženie ambulancií typu B
    private TimeStatistics ambulanceBUsage;

	public AgentResources(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);
		init();
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication

        freeNurses = new ArrayList<>();
        freeDoctors = new ArrayList<>();
        freeAmbulancesA = new ArrayList<>();
        freeAmbulancesB = new ArrayList<>();

        allNurses = new ArrayList<>();
        allDoctors = new ArrayList<>();
        allAmbulancesA = new ArrayList<>();
        allAmbulancesB = new ArrayList<>();

        waitingAmbulanceARequests = new PriorityQueue<>(new ResourceComparator());
        waitingAmbulanceBRequests = new PriorityQueue<>(new ResourceComparator());

        Random seedRandom = ((MySimulation) mySim()).getSeedRandom();

        entranceAmbulanceMoveGenerator = new ContinuousGenerator(seedRandom, 90, 200);
        ambulanceMoveGenerator = new TriangularGenerator(seedRandom, 15, 45, 20);

        doctorUsage = new TimeStatistics((MySimulation) mySim());
        nurseUsage = new TimeStatistics((MySimulation) mySim());
        ambulanceAUsage = new TimeStatistics((MySimulation) mySim());
        ambulanceBUsage = new TimeStatistics((MySimulation) mySim());

        for (int i = 0; i < ((MySimulation)mySim()).getNursesCount(); i++) {
            Nurse n = new Nurse(i);
            freeNurses.add(n);
            allNurses.add(n);

            if(mySim().animatorExists()) {
                mySim().animator().register(n);

                if (i < 6) {
                    n.setPosition(1422 + (i * 45), 568);
                } else {
                    n.setPosition(1422 + ((i - 6) * 45), 626);
                }
            }
        }

        for (int i = 0; i < ((MySimulation)mySim()).getDoctorsCount(); i++) {
            Doctor d = new Doctor(i);
            freeDoctors.add(d);
            allDoctors.add(d);

            if(mySim().animatorExists()) {
                mySim().animator().register(d);

                if (i < 6) {
                    d.setPosition(1422 + (i * 45), 450);
                } else {
                    d.setPosition(1422 + ((i - 6) * 45), 509);
                }

            }
        }

        int ambAstartX = 191;
        int ambBStartX = 878;
        int startY = 117;
        int gap = 117;

        for (int i = 1; i <= 5; i++) {
            Ambulance a = new Ambulance(i, 'A', ambAstartX + ((i - 1) * gap), startY);
            freeAmbulancesA.add(a);
            allAmbulancesA.add(a);
        }

        for (int i = 1; i <= 7; i++) {
            Ambulance b = new Ambulance(i + 5, 'B', ambBStartX + ((i - 1) * gap), startY);
            freeAmbulancesB.add(b);
            allAmbulancesB.add(b);
        }
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		new ManagerResources(Id.managerResources, mySim(), this);
		new ProcessMovePersonnel(Id.processMovePersonnel, mySim(), this);
		addOwnMessage(Mc.releaseEntranceResources);
		addOwnMessage(Mc.requestMedicalResources);
		addOwnMessage(Mc.releaseMedicalResources);
		addOwnMessage(Mc.requestEntranceResources);
	}
	//meta! tag="end"


    public List<Nurse> getFreeNurses() {
        return freeNurses;
    }

    public List<Doctor> getFreeDoctors() {
        return freeDoctors;
    }

    public List<Ambulance> getFreeAmbulancesA() {
        return freeAmbulancesA;
    }

    public List<Ambulance> getFreeAmbulancesB() {
        return freeAmbulancesB;
    }

    public List<Nurse> getAllNurses() {
        return allNurses;
    }

    public List<Doctor> getAllDoctors() {
        return allDoctors;
    }

    public List<Ambulance> getAllAmbulancesA() {
        return allAmbulancesA;
    }

    public List<Ambulance> getAllAmbulancesB() {
        return allAmbulancesB;
    }

    public PriorityQueue<MessageForm> getWaitingAmbulanceARequests() {
        return waitingAmbulanceARequests;
    }

    public PriorityQueue<MessageForm> getWaitingAmbulanceBRequests() {
        return waitingAmbulanceBRequests;
    }

    public ContinuousGenerator getEntranceAmbulanceMoveGenerator() {
        return entranceAmbulanceMoveGenerator;
    }

    public TriangularGenerator getAmbulanceMoveGenerator() {
        return ambulanceMoveGenerator;
    }

    public TimeStatistics getDoctorUsage() {
        return doctorUsage;
    }

    public TimeStatistics getNurseUsage() {
        return nurseUsage;
    }

    public TimeStatistics getAmbulanceAUsage() {
        return ambulanceAUsage;
    }

    public TimeStatistics getAmbulanceBUsage() {
        return ambulanceBUsage;
    }

    public void recordDoctorUsage() {
        double val = (double)(allDoctors.size() - freeDoctors.size()) / allDoctors.size();
//        if (val < 0) {
//            System.out.println("ZÁPORNÉ VYŤAŽENIE! Voľných: " + freeDoctors.size() + " z " + allDoctors.size());
//        }
        doctorUsage.add(val);
    }

    public void recordNurseUsage() {
        nurseUsage.add((double)(allNurses.size() - freeNurses.size()) / allNurses.size());
    }

    public void recordAmbulanceAUsage() {
        ambulanceAUsage.add((double)(allAmbulancesA.size() - freeAmbulancesA.size()) / allAmbulancesA.size());
    }

    public void recordAmbulanceBUsage() {
        ambulanceBUsage.add((double)(allAmbulancesB.size() - freeAmbulancesB.size()) / allAmbulancesB.size());
    }
}