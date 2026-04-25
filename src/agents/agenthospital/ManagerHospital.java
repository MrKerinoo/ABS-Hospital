package agents.agenthospital;

import OSPABA.*;
import simulation.*;

//meta! id="13"
public class ManagerHospital extends OSPABA.Manager
{
	public ManagerHospital(int id, Simulation mySim, Agent myAgent)
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

	//meta! sender="AgentBoss", id="62", type="Request"
	public void processPatientCare(MessageForm message)
	{
	}

	//meta! sender="AgentEntranceExam", id="69", type="Response"
	public void processEntranceExamination(MessageForm message)
	{
	}

	//meta! sender="AgentEntranceExam", id="79", type="Request"
	public void processRequestAmbulanceAgentEntranceExam(MessageForm message)
	{
	}

	//meta! sender="AgentAmbulance", id="84", type="Response"
	public void processRequestAmbulanceAgentAmbulance(MessageForm message)
	{
	}

	//meta! sender="AgentMedicalExam", id="83", type="Request"
	public void processRequestAmbulanceAgentMedicalExam(MessageForm message)
	{
	}

	//meta! sender="AgentMedicalExam", id="78", type="Request"
	public void processRequestPersonellAgentMedicalExam(MessageForm message)
	{
	}

	//meta! sender="AgentPersonnel", id="77", type="Response"
	public void processRequestPersonellAgentPersonnel(MessageForm message)
	{
	}

	//meta! sender="AgentEntranceExam", id="72", type="Request"
	public void processRequestPersonellAgentEntranceExam(MessageForm message)
	{
	}

	//meta! sender="ProcessMovePatient", id="93", type="Finish"
	public void processFinish(MessageForm message)
	{
	}

	//meta! sender="AgentMedicalExam", id="70", type="Response"
	public void processMedicalExamination(MessageForm message)
	{
	}

	//meta! sender="AgentMedicalExam", id="86", type="Notice"
	public void processReleaseResourcesAgentMedicalExam(MessageForm message)
	{
	}

	//meta! sender="AgentEntranceExam", id="85", type="Notice"
	public void processReleaseResourcesAgentEntranceExam(MessageForm message)
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
			switch (message.sender().id())
			{
			case Id.agentEntranceExam:
				processRequestAmbulanceAgentEntranceExam(message);
			break;

			case Id.agentAmbulance:
				processRequestAmbulanceAgentAmbulance(message);
			break;

			case Id.agentMedicalExam:
				processRequestAmbulanceAgentMedicalExam(message);
			break;
			}
		break;

		case Mc.releaseResources:
			switch (message.sender().id())
			{
			case Id.agentMedicalExam:
				processReleaseResourcesAgentMedicalExam(message);
			break;

			case Id.agentEntranceExam:
				processReleaseResourcesAgentEntranceExam(message);
			break;
			}
		break;

		case Mc.requestPersonell:
			switch (message.sender().id())
			{
			case Id.agentMedicalExam:
				processRequestPersonellAgentMedicalExam(message);
			break;

			case Id.agentPersonnel:
				processRequestPersonellAgentPersonnel(message);
			break;

			case Id.agentEntranceExam:
				processRequestPersonellAgentEntranceExam(message);
			break;
			}
		break;

		case Mc.medicalExamination:
			processMedicalExamination(message);
		break;

		case Mc.finish:
			processFinish(message);
		break;

		case Mc.entranceExamination:
			processEntranceExamination(message);
		break;

		case Mc.patientCare:
			processPatientCare(message);
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
