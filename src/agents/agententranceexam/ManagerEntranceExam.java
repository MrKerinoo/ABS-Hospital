package agents.agententranceexam;

import OSPABA.*;
import entities.Ambulance;
import entities.Patient;
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
        MyMessage msg = (MyMessage) message;

        msg.setAddressee(myAgent().findAssistant(Id.processEntranceExam));

        startContinualAssistant(msg);
	}

	//meta! sender="ProcessEntranceExam", id="50", type="Finish"
	public void processFinish(MessageForm message)
	{
        MyMessage msg = (MyMessage) message;

        System.out.println(mySim().currentTime() + " | Koniec vstupného vyšetrenia | " + msg.getPatient());

        message.setCode(Mc.entranceExamination);
        response(message);
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
		case Mc.entranceExamination:
			processEntranceExamination(message);
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
	public AgentEntranceExam myAgent()
	{
		return (AgentEntranceExam)super.myAgent();
	}

}