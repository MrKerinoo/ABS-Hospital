package agents.agentpersonnel;

import OSPABA.*;
import simulation.*;
import agents.agentpersonnel.continualassistants.*;

//meta! id="16"
public class AgentPersonnel extends OSPABA.Agent
{
	public AgentPersonnel(int id, Simulation mySim, Agent parent)
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
		new ManagerPersonnel(Id.managerPersonnel, mySim(), this);
		new ProcessMovePersonnel(Id.processMovePersonnel, mySim(), this);
		addOwnMessage(Mc.releasePersonnel);
		addOwnMessage(Mc.requestPersonnel);
	}
	//meta! tag="end"
}