package agents.agenthospital;

import OSPABA.*;
import agents.agenthospital.continualassistants.*;
import simulation.*;
import generators.ContinuousGenerator;
import generators.TriangularGenerator;
import comparators.EntranceExamComparator;
import comparators.MedicalExamComparator;
import java.util.PriorityQueue;
import java.util.Random;

//meta! id="13"
public class AgentHospital extends OSPABA.Agent
{
    private PriorityQueue<MyMessage> entranceQueue;
    private PriorityQueue<MyMessage> medicalTypeAQueue;
    private PriorityQueue<MyMessage> medicalTypeBQueue;

    private TriangularGenerator walkEntranceMoveGenerator;
    private ContinuousGenerator ambulanceCarEntranceMoveGenerator;
    private ContinuousGenerator randomXWaitingRoomGenerator;
    private ContinuousGenerator randomYWaitingRoomGenerator;
    private TriangularGenerator ambulanceMoveGenerator;

	public AgentHospital(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);
		init();
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication

        entranceQueue = new PriorityQueue<>(new EntranceExamComparator());
        medicalTypeAQueue = new PriorityQueue<>(new MedicalExamComparator());
        medicalTypeBQueue = new PriorityQueue<>(new MedicalExamComparator());

        Random seedRandom = ((MySimulation) mySim()).getSeedRandom();

        walkEntranceMoveGenerator = new TriangularGenerator(seedRandom, 120, 300, 150);
        ambulanceCarEntranceMoveGenerator = new ContinuousGenerator(seedRandom, 90, 200);
        ambulanceMoveGenerator = new TriangularGenerator(seedRandom, 15, 45, 20);

        randomXWaitingRoomGenerator = new ContinuousGenerator(seedRandom, 491, 1200);
        randomYWaitingRoomGenerator = new ContinuousGenerator(seedRandom,445, 620);
    }

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		new ManagerHospital(Id.managerHospital, mySim(), this);
		new ProcessMoveAmbulancePatient(Id.processMoveAmbulancePatient, mySim(), this);
		new ProcessMoveExitPatient(Id.processMoveExitPatient, mySim(), this);
		new ProcessMoveEntrancePatient(Id.processMoveEntrancePatient, mySim(), this);
		addOwnMessage(Mc.patientCare);
		addOwnMessage(Mc.entranceExamination);
		addOwnMessage(Mc.requestMedicalResources);
		addOwnMessage(Mc.requestEntranceResources);
		addOwnMessage(Mc.medicalExamination);
	}
	//meta! tag="end"


    public PriorityQueue<MyMessage> getEntranceQueue() {
        return entranceQueue;
    }

    public PriorityQueue<MyMessage> getMedicalTypeAQueue() {
        return medicalTypeAQueue;
    }

    public PriorityQueue<MyMessage> getMedicalTypeBQueue() {
        return medicalTypeBQueue;
    }

    public ContinuousGenerator getRandomXWaitingRoomGenerator() {
        return randomXWaitingRoomGenerator;
    }

    public ContinuousGenerator getRandomYWaitingRoomGenerator() {
        return randomYWaitingRoomGenerator;
    }

    public ContinuousGenerator getAmbulanceCarEntranceMoveGenerator() {
        return ambulanceCarEntranceMoveGenerator;
    }

    public TriangularGenerator getWalkEntranceMoveGenerator() {
        return walkEntranceMoveGenerator;
    }

    public TriangularGenerator getAmbulanceMoveGenerator() {
        return ambulanceMoveGenerator;
    }
}