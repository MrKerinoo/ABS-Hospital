package agents.agenthospital;

import OSPABA.*;
import agents.agenthospital.continualassistants.*;
import simulation.*;
import generators.ContinuousGenerator;
import generators.TriangularGenerator;
import comparators.ResourceComparator;
import statistics.DiscreteStatistics;
import statistics.TimeStatistics;

import java.util.PriorityQueue;
import java.util.Random;

//meta! id="13"
public class AgentHospital extends OSPABA.Agent
{
    private PriorityQueue<MyMessage> entranceQueue;
    private PriorityQueue<MyMessage> medicalTypeAQueue;
    private PriorityQueue<MyMessage> medicalTypeBQueue;

    // GENERATORS

    private TriangularGenerator walkEntranceMoveGenerator;
    private ContinuousGenerator ambulanceCarEntranceMoveGenerator;
    private ContinuousGenerator randomXWaitingRoomGenerator;
    private ContinuousGenerator randomYWaitingRoomGenerator;
    private TriangularGenerator ambulanceMoveGenerator;
    private ContinuousGenerator exitMoveGenerator;

    // STATISTICS

    // Priemerná dĺžka radu na vstupné vyšetrenie
    private TimeStatistics entranceQueueLength;
    // Priemerná dĺžka radu na lekárske vyšetrenie
    private TimeStatistics medicalQueueLength;
    // Priemerný čas čakania v rade na vstupné vyšetrenie
    private DiscreteStatistics waitEntrance;
    // Priemerný čas čakania v rade na lekárske vyšetrenie
    private DiscreteStatistics waitMedical;
    // Aktuálny počet pacientov čakajúcich na lekárske vyšetrenie
    private int medicalQueueSize;

    private DiscreteStatistics waitEntranceWalk;
    private DiscreteStatistics waitEntranceAmbulance;
    private DiscreteStatistics waitMedicalWalk;
    private DiscreteStatistics waitMedicalAmbulance;

    private DiscreteStatistics timeFromArrivalToEntranceExam;
    private DiscreteStatistics timeFromArrivalToEntranceExamWalk;
    private DiscreteStatistics timeFromArrivalToEntranceExamAmbulance;

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

        entranceQueue = new PriorityQueue<>(new ResourceComparator());
        medicalTypeAQueue = new PriorityQueue<>(new ResourceComparator());
        medicalTypeBQueue = new PriorityQueue<>(new ResourceComparator());

        Random seedRandom = ((MySimulation) mySim()).getSeedRandom();

        walkEntranceMoveGenerator = new TriangularGenerator(seedRandom, 120, 300, 150);
        ambulanceCarEntranceMoveGenerator = new ContinuousGenerator(seedRandom, 90, 200);
        ambulanceMoveGenerator = new TriangularGenerator(seedRandom, 15, 45, 20);
        exitMoveGenerator = new ContinuousGenerator(seedRandom, 150, 240);

        randomXWaitingRoomGenerator = new ContinuousGenerator(seedRandom, 491, 1200);
        randomYWaitingRoomGenerator = new ContinuousGenerator(seedRandom,445, 620);

        entranceQueueLength = new TimeStatistics((MySimulation) mySim());
        medicalQueueLength = new TimeStatistics((MySimulation) mySim());

        waitEntrance = new DiscreteStatistics();
        waitMedical = new DiscreteStatistics();
        waitEntranceWalk = new DiscreteStatistics();;
        waitEntranceAmbulance = new DiscreteStatistics();;
        waitMedicalWalk = new DiscreteStatistics();;
        waitMedicalAmbulance = new DiscreteStatistics();;

        timeFromArrivalToEntranceExam = new DiscreteStatistics();
        timeFromArrivalToEntranceExamWalk = new DiscreteStatistics();
        timeFromArrivalToEntranceExamAmbulance = new DiscreteStatistics();

        this.medicalQueueSize = 0;
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

    public TimeStatistics getEntranceQueueLength() {
        return entranceQueueLength;
    }

    public TimeStatistics getMedicalQueueLength() {
        return medicalQueueLength;
    }

    public DiscreteStatistics getWaitEntrance() {
        return waitEntrance;
    }

    public DiscreteStatistics getWaitMedical() {
        return waitMedical;
    }

    public ContinuousGenerator getExitMoveGenerator() {
        return exitMoveGenerator;
    }

    public DiscreteStatistics getWaitEntranceWalk() {
        return waitEntranceWalk;
    }

    public DiscreteStatistics getWaitEntranceAmbulance() {
        return waitEntranceAmbulance;
    }

    public DiscreteStatistics getWaitMedicalWalk() {
        return waitMedicalWalk;
    }

    public DiscreteStatistics getWaitMedicalAmbulance() {
        return waitMedicalAmbulance;
    }

    public DiscreteStatistics getTimeFromArrivalToEntranceExam() {
        return timeFromArrivalToEntranceExam;
    }

    public DiscreteStatistics getTimeFromArrivalToEntranceExamWalk() {
        return timeFromArrivalToEntranceExamWalk;
    }

    public DiscreteStatistics getTimeFromArrivalToEntranceExamAmbulance() {
        return timeFromArrivalToEntranceExamAmbulance;
    }

    public void incrementMedicalQueue() {
        medicalQueueSize++;
        medicalQueueLength.add(medicalQueueSize);
    }

    public void decrementMedicalQueue() {
        medicalQueueSize--;
        medicalQueueLength.add(medicalQueueSize);
    }
}