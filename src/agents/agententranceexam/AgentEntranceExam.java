package agents.agententranceexam;

import OSPABA.*;
import agents.agententranceexam.continualassistants.*;
import simulation.*;

//meta! id="30"
public class AgentEntranceExam extends OSPABA.Agent
{
	public AgentEntranceExam(int id, Simulation mySim, Agent parent)
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
		new ManagerEntranceExam(Id.managerEntranceExam, mySim(), this);
		new ProcessEntranceExam(Id.processEntranceExam, mySim(), this);
		addOwnMessage(Mc.entranceExamination);
		addOwnMessage(Mc.requestAmbulance);
		addOwnMessage(Mc.requestPersonell);
	}
	//meta! tag="end"
}
