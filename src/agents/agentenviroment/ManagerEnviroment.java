package agents.agentenviroment;

import OSPABA.*;
import simulation.*;

//meta! id="2"
public class ManagerEnviroment extends OSPABA.Manager
{
	public ManagerEnviroment(int id, Simulation mySim, Agent myAgent)
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

	//meta! sender="AgentBoss", id="57", type="Notice"
	public void processPatientExit(MessageForm message)
	{
	}

	//meta! sender="SchedulerPatient", id="9", type="Finish"
	public void processFinishSchedulerPatient(MessageForm message)
	{
	}

	//meta! sender="SchedulerCar", id="11", type="Finish"
	public void processFinishSchedulerCar(MessageForm message)
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
		case Mc.patientExit:
			processPatientExit(message);
		break;

		case Mc.finish:
			switch (message.sender().id())
			{
			case Id.schedulerPatient:
				processFinishSchedulerPatient(message);
			break;

			case Id.schedulerCar:
				processFinishSchedulerCar(message);
			break;
			}
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public AgentEnviroment myAgent()
	{
		return (AgentEnviroment)super.myAgent();
	}

}
