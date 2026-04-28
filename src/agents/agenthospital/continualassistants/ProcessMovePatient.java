package agents.agenthospital.continualassistants;

import OSPABA.*;
import simulation.*;
import agents.agenthospital.*;
import OSPABA.Process;

//meta! id="92"
public class ProcessMovePatient extends OSPABA.Process
{
	public ProcessMovePatient(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="AgentHospital", id="93", type="Start"
	public void processStart(MessageForm message)
	{
        double duration = myAgent().getEntranceAmbulanceMoveGenerator().randDouble();

        message.setCode(Mc.finish);
        hold(duration , message);
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
	public AgentHospital myAgent()
	{
		return (AgentHospital)super.myAgent();
	}

}