package agents.agenthospital;

import OSPABA.*;
import agents.agenthospital.continualassistants.*;
import entities.Patient;
import simulation.*;

import java.util.PriorityQueue;

//meta! id="13"
public class AgentHospital extends OSPABA.Agent
{
    private PriorityQueue<Patient> entranceQueue;
    private PriorityQueue<Patient> medicalTypeAQueue;
    private PriorityQueue<Patient> medicalTypeBQueue;

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
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		new ManagerHospital(Id.managerHospital, mySim(), this);
		new ProcessMovePatient(Id.processMovePatient, mySim(), this);
		addOwnMessage(Mc.patientCare);
		addOwnMessage(Mc.entranceExamination);
		addOwnMessage(Mc.requestAmbulance);
		addOwnMessage(Mc.requestPersonnel);
		addOwnMessage(Mc.medicalExamination);
		addOwnMessage(Mc.releaseResources);
	}
	//meta! tag="end"
}