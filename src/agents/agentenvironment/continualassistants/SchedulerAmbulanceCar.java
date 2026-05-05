package agents.agentenvironment.continualassistants;

import OSPABA.*;
import entities.Patient;
import simulation.*;
import agents.agentenvironment.*;

//meta! id="10"
public class SchedulerAmbulanceCar extends OSPABA.Scheduler
{
	public SchedulerAmbulanceCar(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="AgentEnvironment", id="11", type="Start"
	public void processStart(MessageForm message)
	{
        double nextArrival = myAgent().getPatientAmbulanceCarGenerator().randDouble();

        message.setCode(Mc.finish);
        hold(nextArrival, message);
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