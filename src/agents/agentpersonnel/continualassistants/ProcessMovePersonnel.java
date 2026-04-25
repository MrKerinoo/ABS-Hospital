package agents.agentpersonnel.continualassistants;

import OSPABA.*;
import simulation.*;
import agents.agentpersonnel.*;
import OSPABA.Process;

//meta! id="46"
public class ProcessMovePersonnel extends OSPABA.Process
{
	public ProcessMovePersonnel(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="AgentPersonnel", id="47", type="Start"
	public void processStart(MessageForm message)
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
	@Override
	public void processMessage(MessageForm message)
	{
		switch (message.code())
		{
		case Mc.start:
			processStart(message);
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
