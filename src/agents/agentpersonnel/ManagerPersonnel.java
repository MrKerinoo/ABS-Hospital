package agents.agentpersonnel;

import OSPABA.*;
import simulation.*;

//meta! id="16"
public class ManagerPersonnel extends OSPABA.Manager
{
	public ManagerPersonnel(int id, Simulation mySim, Agent myAgent)
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

	//meta! sender="AgentHospital", id="90", type="Notice"
	public void processReleasePersonell(MessageForm message)
	{
	}

	//meta! sender="AgentHospital", id="77", type="Request"
	public void processRequestPersonell(MessageForm message)
	{
	}

	//meta! sender="ProcessMovePersonnel", id="47", type="Finish"
	public void processFinish(MessageForm message)
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
		case Mc.releasePersonell:
			processReleasePersonell(message);
		break;

		case Mc.requestPersonell:
			processRequestPersonell(message);
		break;

		case Mc.finish:
			processFinish(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public AgentPersonnel myAgent()
	{
		return (AgentPersonnel)super.myAgent();
	}

}
