package agents.agentenvironment.continualassistants;

import OSPABA.*;
import entities.Patient;
import simulation.*;
import agents.agentenvironment.*;

//meta! id="8"
public class SchedulerWalk extends OSPABA.Scheduler
{
	public SchedulerWalk(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="AgentEnvironment", id="9", type="Start"
	public void processStart(MessageForm message)
	{
        MyMessage msg = (MyMessage) message;

        Patient patient = myAgent().getPatientGenerator().generatePatient();

        msg.setPatient(patient);

        double nextArrival = myAgent().getPatientWalkGenerator().randDouble();

        if (mySim().currentTime() + nextArrival <= 2_419_200) {
            msg.setCode(Mc.finish);
            hold(nextArrival, msg);
        }
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