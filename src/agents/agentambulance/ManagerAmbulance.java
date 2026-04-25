package agents.agentambulance;

import OSPABA.*;
import simulation.*;

//meta! id="18"
public class ManagerAmbulance extends OSPABA.Manager
{
	public ManagerAmbulance(int id, Simulation mySim, Agent myAgent)
	{
		super(id, mySim, myAgent);
		init();
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication

		if (petriNet() != null)
		{
			petriNet().clear();
		}
	}

	//meta! sender="AgentHospital", id="84", type="Request"
	public void processRequestAmbulance(MessageForm message)
	{
	}

	//meta! sender="AgentHospital", id="91", type="Notice"
	public void processReleaseAmbulance(MessageForm message)
	{
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
		}
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	public void init()
	{
	}

	@Override
	public void processMessage(MessageForm message)
	{
		switch (message.code())
		{
		case Mc.releaseAmbulance:
			processReleaseAmbulance(message);
		break;

		case Mc.requestAmbulance:
			processRequestAmbulance(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public AgentAmbulance myAgent()
	{
		return (AgentAmbulance)super.myAgent();
	}

}