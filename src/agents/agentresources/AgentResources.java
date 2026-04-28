package agents.agentresources;

import OSPABA.*;
import comparators.ResourceComparator;
import entities.Ambulance;
import entities.Doctor;
import entities.Nurse;
import generators.ContinuousGenerator;
import generators.TriangularGenerator;
import simulation.*;
import agents.agentresources.continualassistants.*;

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

    PriorityQueue<MessageForm> waitingRequests;

    private ContinuousGenerator entranceAmbulanceMoveGenerator;
    private TriangularGenerator ambulanceMoveGenerator;

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

        waitingRequests = new PriorityQueue<>(new ResourceComparator());

        Random seedRandom = ((MySimulation) mySim()).getSeedRandom();

        entranceAmbulanceMoveGenerator = new ContinuousGenerator(seedRandom, 150, 240);
        ambulanceMoveGenerator = new TriangularGenerator(seedRandom, 15, 45, 20);

        for (int i = 0; i < ((MySimulation)mySim()).getNursesCount(); i++) {
            Nurse n = new Nurse(i);
            freeNurses.add(n);
            allNurses.add(n);
        }

        for (int i = 0; i < ((MySimulation)mySim()).getDoctorsCount(); i++) {
            Doctor d = new Doctor(i);
            freeDoctors.add(d);
            allDoctors.add(d);
        }

        for (int i = 0; i < 5; i++) {
            Ambulance a = new Ambulance(i, 'A');
            freeAmbulancesA.add(a);
            allAmbulancesA.add(a);
        }

        for (int i = 0; i < 7; i++) {
            Ambulance b = new Ambulance(i + 5, 'B');
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

    public PriorityQueue<MessageForm> getWaitingRequests() {
        return waitingRequests;
    }

    public ContinuousGenerator getEntranceAmbulanceMoveGenerator() {
        return entranceAmbulanceMoveGenerator;
    }

    public TriangularGenerator getAmbulanceMoveGenerator() {
        return ambulanceMoveGenerator;
    }
}