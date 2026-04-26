package agents.agentboss;

import OSPABA.*;
import simulation.*;

//meta! id="1"
public class ManagerBoss extends OSPABA.Manager
{
	public ManagerBoss(int id, Simulation mySim, Agent myAgent)
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

	//meta! sender="AgentHospital", id="62", type="Response"
	public void processPatientCare(MessageForm message)
	{
	}

	//meta! sender="AgentEnvironment", id="56", type="Notice"
	public void processPatientArrival(MessageForm message)
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
		case Mc.patientCare:
			processPatientCare(message);
		break;

		case Mc.patientArrival:
			processPatientArrival(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public AgentBoss myAgent()
	{
		return (AgentBoss)super.myAgent();
	}

}