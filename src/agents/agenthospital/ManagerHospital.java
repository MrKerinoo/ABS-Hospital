package agents.agenthospital;

import OSPABA.*;
import entities.Ambulance;
import entities.Patient;
import simulation.*;
import utils.Utils;

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
        MyMessage msg = (MyMessage) message;
        MyMessage copyMessage = (MyMessage) msg.createCopy();

        Patient patient = msg.getPatient();
        Ambulance ambulance = msg.getAmbulance();
        System.out.println(mySim().currentTime() + " | Pacient sa presúva preč z čakárne | " + msg.getPatient());

        if (mySim().animatorExists()) {
            Utils.moveAlongPath(patient, 2, mySim().currentTime(),
                    Utils.p2d(ambulance.getXInside(), ambulance.getYInside()),
                    Utils.p2d(ambulance.getXAfterExam() - 33, ambulance.getYAfterExam()),
                    Utils.p2d(ambulance.getXAfterExam(), ambulance.getYAfterExam())
            );
        }


        switch (patient.getPriority()) {
            case 1, 2:
                myAgent().getMedicalTypeAQueue().add(msg);
                break;
            case 3, 4:
                myAgent().getMedicalTypeAQueue().add(msg);
                myAgent().getMedicalTypeBQueue().add(msg);
                break;
            case 5:
                myAgent().getMedicalTypeBQueue().add(msg);
                break;
        }

        msg.setAmbulance(null);
        msg.setNurse(null);
        msg.setDoctor(null);

        msg.setCode(Mc.requestMedicalResources);
        msg.setAddressee(mySim().findAgent(Id.agentResources));

        System.out.println(mySim().currentTime() + " | Požiadanie o zdroje lekárskeho vyšetrenia | " + msg.getPatient());

        request(msg);

        copyMessage.setCode(Mc.releaseEntranceResources);
        copyMessage.setAddressee(mySim().findAgent(Id.agentResources));

        System.out.println(mySim().currentTime() + " | Uvoľnenie zdrojov po vstupnom vyšetrení | " + msg.getPatient());

        notice(copyMessage);
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

        Patient patient = msg.getPatient();
        Ambulance ambulance = msg.getAmbulance();

        msg.getAmbulance().setPatient(patient);

        patient.setVisitedAmbulance(ambulance);

        if (mySim().animatorExists()) {
            Utils.moveAlongPath(patient, 2, mySim().currentTime(),
                    Utils.p2d(ambulance.getXDoor(), ambulance.getYDoor()),
                    Utils.p2d(ambulance.getXInside(), ambulance.getYInside()),
                    Utils.p2d(ambulance.getXPatient(), ambulance.getYPatient())
            );
        }

        msg.setCode(Mc.entranceExamination);
        msg.setAddressee(mySim().findAgent(Id.agentEntranceExam));

        request(msg);
    }

    //meta! sender="AgentResources", id="116", type="Response"
    public void processRequestMedicalResources(MessageForm message)
    {
        MyMessage msg = (MyMessage) message;

        Patient patient = msg.getPatient();
        Ambulance ambulance = msg.getAmbulance();

        if (ambulance.getType() == 'A') {
            myAgent().getMedicalTypeAQueue().poll();
            myAgent().getMedicalTypeBQueue().remove(msg);
        } else {
            myAgent().getMedicalTypeBQueue().poll();
            myAgent().getMedicalTypeAQueue().remove(msg);
        }

        System.out.println(mySim().currentTime() + " | Pacient vstupuje do ambulancie |" + msg.getPatient());

        msg.getAmbulance().setPatient(patient);

    }

	//meta! sender="ProcessMovePatient", id="93", type="Finish"
	public void processFinish(MessageForm message)
	{
        MyMessage msg = (MyMessage) message;

        myAgent().getEntranceQueue().add(msg);

        msg.setCode(Mc.requestEntranceResources);
        msg.setAddressee(mySim().findAgent(Id.agentResources));

        System.out.println(mySim().currentTime() + " | Požiadanie o zdroje vstupného vyšetrenia| " + msg.getPatient());

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