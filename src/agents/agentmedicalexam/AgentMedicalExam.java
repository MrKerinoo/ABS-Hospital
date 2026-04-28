package agents.agentmedicalexam;

import OSPABA.*;
import agents.agentmedicalexam.continualassistants.*;
import simulation.*;

//meta! id="31"
public class AgentMedicalExam extends OSPABA.Agent
{
	public AgentMedicalExam(int id, Simulation mySim, Agent parent)
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
		new ManagerMedicalExam(Id.managerMedicalExam, mySim(), this);
		new ProcessMedicalExam(Id.processMedicalExam, mySim(), this);
		addOwnMessage(Mc.medicalExamination);
	}
	//meta! tag="end"
}