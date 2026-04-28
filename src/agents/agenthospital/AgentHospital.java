package agents.agenthospital;

import OSPABA.*;
import agents.agenthospital.continualassistants.*;
import generators.ContinuousGenerator;
import simulation.*;
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

    private ContinuousGenerator entranceAmbulanceMoveGenerator;
    private ContinuousGenerator randomXGenerator;
    private ContinuousGenerator randomYGenerator;

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

        entranceAmbulanceMoveGenerator = new ContinuousGenerator(seedRandom, 150, 240);
        randomXGenerator = new ContinuousGenerator(seedRandom, 190, 1650);
        randomYGenerator = new ContinuousGenerator(seedRandom,240, 400);
    }

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		new ManagerHospital(Id.managerHospital, mySim(), this);
		new ProcessMovePatient(Id.processMovePatient, mySim(), this);
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

    public ContinuousGenerator getRandomXGenerator() {
        return randomXGenerator;
    }

    public ContinuousGenerator getRandomYGenerator() {
        return randomYGenerator;
    }

    public ContinuousGenerator getEntranceAmbulanceMoveGenerator() {
        return entranceAmbulanceMoveGenerator;


    }
}