package agents.agentambulance;

import OSPABA.*;
import simulation.*;

//meta! id="18"
public class AgentAmbulance extends OSPABA.Agent
{
	public AgentAmbulance(int id, Simulation mySim, Agent parent)
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
		new ManagerAmbulance(Id.managerAmbulance, mySim(), this);
		addOwnMessage(Mc.requestAmbulance);
		addOwnMessage(Mc.releaseAmbulance);
	}
	//meta! tag="end"
}
