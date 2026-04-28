package agents.agenthospital;

import OSPABA.*;
import entities.Patient;
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
        MyMessage msg = (MyMessage) message;

        System.out.println(mySim().currentTime() + " | Pacient sa presúva do čakárne | " + msg.getPatient());

        msg.setAddressee(myAgent().findAssistant(Id.processMovePatient));
        startContinualAssistant(message);
	}

	//meta! sender="AgentEntranceExam", id="69", type="Response"
	public void processEntranceExamination(MessageForm message)
	{
	}

    //meta! sender="AgentMedicalExam", id="70", type="Response"
    public void processMedicalExamination(MessageForm message)
    {
    }

    //meta! sender="AgentResources", id="114", type="Response"
    public void processRequestEntranceResources(MessageForm message)
    {
        MyMessage msg = (MyMessage) message;

        myAgent().getEntranceQueue().remove(msg);
        System.out.println(mySim().currentTime() + " | Pacient vstupuje do ambulancie |" + msg.getPatient());

        msg.getAmbulance().setPatient(msg.getPatient());

        msg.setCode(Mc.entranceExamination);
        msg.setAddressee(mySim().findAgent(Id.agentEntranceExam));

        request(msg);
    }

    //meta! sender="AgentResources", id="116", type="Response"
    public void processRequestMedicalResources(MessageForm message)
    {
    }

	//meta! sender="ProcessMovePatient", id="93", type="Finish"
	public void processFinish(MessageForm message)
	{
        MyMessage msg = (MyMessage) message;

        myAgent().getEntranceQueue().add(msg);

        msg.setCode(Mc.requestEntranceResources);
        msg.setAddressee(mySim().findAgent(Id.agentResources));

        Patient patient = msg.getPatient();

        if (mySim().animatorExists()) {
            double start = Math.max(patient.getEndTimeOfAllAnims(), mySim().currentTime());

            double randX = myAgent().getRandomXGenerator().randDouble();
            double randY = myAgent().getRandomYGenerator().randDouble();

            if (patient.isWithAmbulance()) {
                patient.moveTo(start, 3.0, 1350, 300);
                patient.moveTo(start, 3, randX, randY);
            } else {
                patient.moveTo(start, 3.0, 380, 300);
                patient.moveTo(start, 3, randX, randY);
            }

        }

        System.out.println(mySim().currentTime() + " | Požiadanie o zdroje | " + msg.getPatient());

        request(msg);
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
		case Mc.requestMedicalResources:
			processRequestMedicalResources(message);
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

		case Mc.requestEntranceResources:
			processRequestEntranceResources(message);
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
	public AgentHospital myAgent()
	{
		return (AgentHospital)super.myAgent();
	}

}