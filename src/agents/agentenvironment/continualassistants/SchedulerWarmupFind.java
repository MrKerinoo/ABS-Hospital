package agents.agentenvironment.continualassistants;

import OSPABA.*;
import simulation.*;
import agents.agentenvironment.*;

import java.util.ArrayList;
import java.util.List;

//meta! id="148"
public class SchedulerWarmupFind extends OSPABA.Scheduler
{
	public SchedulerWarmupFind(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="AgentEnvironment", id="149", type="Start"
	public void processStart(MessageForm message)
	{
        message.setCode(Mc.finish);
        hold(3600, message);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
            case Mc.finish:
                assistantFinished(message);
                break;
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
	public AgentEnvironment myAgent()
	{
		return (AgentEnvironment)super.myAgent();
	}

}
