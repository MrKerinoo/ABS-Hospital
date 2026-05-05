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
    // GENERATORS

    private TriangularGenerator walkEntranceMoveGenerator;
    private ContinuousGenerator ambulanceCarEntranceMoveGenerator;
    private ContinuousGenerator randomXWaitingRoomGenerator;
    private ContinuousGenerator randomYWaitingRoomGenerator;
    private TriangularGenerator ambulanceMoveGenerator;
    private ContinuousGenerator exitMoveGenerator;

    // STATISTICS

    private DiscreteStatistics waitEntrance;
    private DiscreteStatistics waitMedical;

    private DiscreteStatistics waitEntranceWalk;
    private DiscreteStatistics waitEntranceAmbulance;
    private DiscreteStatistics waitMedicalWalk;
    private DiscreteStatistics waitMedicalAmbulance;

    private DiscreteStatistics timeFromArrivalToMedicalExam;
    private DiscreteStatistics timeFromArrivalToMedicalExamWalk;
    private DiscreteStatistics timeFromArrivalToMedicalExamAmbulance;

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

        Random seedRandom = ((MySimulation) mySim()).getSeedRandom();

        walkEntranceMoveGenerator = new TriangularGenerator(seedRandom, 120, 300, 150);
        ambulanceCarEntranceMoveGenerator = new ContinuousGenerator(seedRandom, 90, 200);
        ambulanceMoveGenerator = new TriangularGenerator(seedRandom, 15, 45, 20);
        exitMoveGenerator = new ContinuousGenerator(seedRandom, 150, 240);

        randomXWaitingRoomGenerator = new ContinuousGenerator(seedRandom, 491, 1200);
        randomYWaitingRoomGenerator = new ContinuousGenerator(seedRandom,445, 620);

        waitEntrance = new DiscreteStatistics();
        waitMedical = new DiscreteStatistics();
        waitEntranceWalk = new DiscreteStatistics();
        waitEntranceAmbulance = new DiscreteStatistics();
        waitMedicalWalk = new DiscreteStatistics();
        waitMedicalAmbulance = new DiscreteStatistics();

        timeFromArrivalToMedicalExam = new DiscreteStatistics();
        timeFromArrivalToMedicalExamWalk = new DiscreteStatistics();
        timeFromArrivalToMedicalExamAmbulance = new DiscreteStatistics();
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

    public void resetLocalStats() {
        this.waitEntrance.reset();
        this.waitMedical.reset();
        this.waitEntranceWalk.reset();
        this.waitEntranceAmbulance.reset();
        this.waitMedicalWalk.reset();
        this.waitMedicalAmbulance.reset();

        this.timeFromArrivalToMedicalExam.reset();
        this.timeFromArrivalToMedicalExamWalk.reset();
        this.timeFromArrivalToMedicalExamAmbulance.reset();
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

    public DiscreteStatistics getTimeFromArrivalToMedicalExam() {
        return timeFromArrivalToMedicalExam;
    }

    public DiscreteStatistics getTimeFromArrivalToMedicalExamWalk() {
        return timeFromArrivalToMedicalExamWalk;
    }

    public DiscreteStatistics getTimeFromArrivalToMedicalExamAmbulance() {
        return timeFromArrivalToMedicalExamAmbulance;
    }
}
