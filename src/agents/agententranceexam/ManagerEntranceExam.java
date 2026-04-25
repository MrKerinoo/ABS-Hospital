package agents.agententranceexam;

import OSPABA.*;
import simulation.*;

//meta! id="30"
public class ManagerEntranceExam extends OSPABA.Manager
{
	public ManagerEntranceExam(int id, Simulation mySim, Agent myAgent)
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

	//meta! sender="AgentHospital", id="69", type="Request"
	public void processEntranceExamination(MessageForm message)
	{
	}

	//meta! sender="AgentHospital", id="79", type="Response"
	public void processRequestAmbulance(MessageForm message)
	{
	}

	//meta! sender="AgentHospital", id="72", type="Response"
	public void processRequestPersonell(MessageForm message)
	{
	}

	//meta! sender="ProcessEntranceExam", id="50", type="Finish"
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
		case Mc.requestAmbulance:
			processRequestAmbulance(message);
		break;

		case Mc.finish:
			processFinish(message);
		break;

		case Mc.entranceExamination:
			processEntranceExamination(message);
		break;

		case Mc.requestPersonell:
			processRequestPersonell(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public AgentEntranceExam myAgent()
	{
		return (AgentEntranceExam)super.myAgent();
	}

}
