package agents.agentmedicalexam;

import OSPABA.*;
import simulation.*;

//meta! id="31"
public class ManagerMedicalExam extends OSPABA.Manager
{
	public ManagerMedicalExam(int id, Simulation mySim, Agent myAgent)
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

	//meta! sender="ProcessMedicalExam", id="53", type="Finish"
	public void processFinish(MessageForm message)
	{
	}

	//meta! sender="AgentHospital", id="70", type="Request"
	public void processMedicalExamination(MessageForm message)
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
		case Mc.finish:
			processFinish(message);
		break;

		case Mc.medicalExamination:
			processMedicalExamination(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public AgentMedicalExam myAgent()
	{
		return (AgentMedicalExam)super.myAgent();
	}

}