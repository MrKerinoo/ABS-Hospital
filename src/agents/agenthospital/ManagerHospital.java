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

        if (!mySim().isMaxSpeed()) {
            ((MySimulation) mySim()).logEvent(" | Pacient sa presúva do čakárne | " + msg.getPatient());
            System.out.println(mySim().currentTime() + " | Pacient sa presúva do čakárne | " + msg.getPatient());
        }

        msg.setAddressee(myAgent().findAssistant(Id.processMoveEntrancePatient));
        startContinualAssistant(message);
	}

	//meta! sender="AgentEntranceExam", id="69", type="Response"
	public void processEntranceExamination(MessageForm message)
	{
        MyMessage msg = (MyMessage) message;

        Patient patient = msg.getPatient();
        Ambulance ambulance = msg.getAmbulance();

        if (!mySim().isMaxSpeed()) {
            ((MySimulation) mySim()).logEvent(" | Pacient sa presúva preč z ambulancie | " + msg.getPatient());
            System.out.println(mySim().currentTime() + " | Pacient sa presúva preč z ambulancie | " + msg.getPatient());
        }

        if (mySim().animatorExists()) {
            Utils.moveAlongPath(patient, 2, mySim().currentTime(),
                    Utils.p2d(ambulance.getXInside(), ambulance.getYInside()),
                    Utils.p2d(ambulance.getXAfterExam() - 33, ambulance.getYAfterExam()),
                    Utils.p2d(ambulance.getXAfterExam(), ambulance.getYAfterExam())
            );
        }

        msg.getPatient().setMedicalQueueArrivalTime(mySim().currentTime());

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

        myAgent().incrementMedicalQueue();

        MyMessage copyMessage = (MyMessage) msg.createCopy();
        copyMessage.setCode(Mc.releaseEntranceResources);
        copyMessage.setAddressee(mySim().findAgent(Id.agentResources));

        if (!mySim().isMaxSpeed()) {
            ((MySimulation) mySim()).logEvent(" | Uvoľnenie zdrojov po vstupnom vyšetrení | " + msg.getPatient());
            System.out.println(mySim().currentTime() + " | Uvoľnenie zdrojov po vstupnom vyšetrení | " + msg.getPatient());
        }

        notice(copyMessage);

        msg.setAmbulance(null);
        msg.setNurse(null);
        msg.setDoctor(null);

        msg.setCode(Mc.requestMedicalResources);
        msg.setAddressee(mySim().findAgent(Id.agentResources));

        if (!mySim().isMaxSpeed()) {
            ((MySimulation) mySim()).logEvent(" | Požiadanie o zdroje lekárskeho vyšetrenia | " + msg.getPatient());
            System.out.println(mySim().currentTime() + " | Požiadanie o zdroje lekárskeho vyšetrenia | " + msg.getPatient());
        }

        request(msg);
    }

	//meta! sender="AgentMedicalExam", id="70", type="Response"
	public void processMedicalExamination(MessageForm message)
    {
        MyMessage msg = (MyMessage) message;

        if (!mySim().isMaxSpeed()) {
            ((MySimulation) mySim()).logEvent(" | Pacient sa presúva preč z čakárne | " + msg.getPatient());
            System.out.println(mySim().currentTime() + " | Pacient sa presúva preč z čakárne | " + msg.getPatient());
        }

        msg.setAddressee(myAgent().findAssistant(Id.processMoveExitPatient));
        startContinualAssistant(msg);

        MyMessage copyMessage = (MyMessage) message.createCopy();
        copyMessage.setCode(Mc.releaseMedicalResources);
        copyMessage.setAddressee(mySim().findAgent(Id.agentResources));

        notice(copyMessage);
    }

	//meta! sender="AgentResources", id="114", type="Response"
	public void processRequestEntranceResources(MessageForm message)
    {
        MyMessage msg = (MyMessage) message;

        myAgent().getEntranceQueue().remove(msg);
        double waitTime = mySim().currentTime() - msg.getPatient().getEntranceQueueArrivalTime();

        myAgent().getEntranceQueueLength().add(myAgent().getEntranceQueue().size());
        myAgent().getWaitEntrance().add(waitTime);

        if (msg.getPatient().isWithAmbulance()) {
            myAgent().getWaitEntranceAmbulance().add(waitTime);
        } else {
            myAgent().getWaitEntranceWalk().add(waitTime);
        }

        if (!mySim().isMaxSpeed()) {
            ((MySimulation) mySim()).logEvent(" | Pacient vstupuje do ambulancie |" + msg.getPatient());
            System.out.println(mySim().currentTime() + " | Pacient vstupuje do ambulancie |" + msg.getPatient());
        }

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

        myAgent().getMedicalTypeAQueue().remove(msg);
        myAgent().getMedicalTypeBQueue().remove(msg);

        double waitTime = mySim().currentTime() - patient.getMedicalQueueArrivalTime();

        myAgent().decrementMedicalQueue();
        myAgent().getWaitMedical().add(waitTime);

        double entranceTime = mySim().currentTime() - msg.getPatient().getArrivalTime();

        myAgent().getTimeFromArrivalToMedicalExam().add(entranceTime);

        if (patient.isWithAmbulance()) {
            myAgent().getWaitMedicalAmbulance().add(waitTime);
            myAgent().getTimeFromArrivalToMedicalExamAmbulance().add(entranceTime);

        } else {
            myAgent().getWaitMedicalWalk().add(waitTime);
            myAgent().getTimeFromArrivalToMedicalExamWalk().add(entranceTime);

        }

        msg.getAmbulance().setPatient(patient);

        if (ambulance != patient.getVisitedAmbulance()) {
            msg.setAddressee(myAgent().findAssistant(Id.processMoveAmbulancePatient));

            startContinualAssistant(msg);
        } else {
            if (!mySim().isMaxSpeed()) {
                ((MySimulation) mySim()).logEvent(" | Pacient vstupuje do ambulancie |" + msg.getPatient());
                System.out.println(mySim().currentTime() + " | Pacient vstupuje do ambulancie |" + msg.getPatient());
            }

            if (mySim().animatorExists()) {
                Utils.moveAlongPath(patient, 2, mySim().currentTime(),
                        Utils.p2d(ambulance.getXDoor(), ambulance.getYDoor()),
                        Utils.p2d(ambulance.getXInside(), ambulance.getYInside()),
                        Utils.p2d(ambulance.getXPatient(), ambulance.getYPatient())
                );
            }

            msg.setCode(Mc.medicalExamination);
            msg.setAddressee(mySim().findAgent(Id.agentMedicalExam));

            request(msg);
        }
    }

	//meta! sender="ProcessMoveEntrancePatient", id="93", type="Finish"
	public void processFinishProcessMoveEntrancePatient(MessageForm message)
	{
        MyMessage msg = (MyMessage) message;

        msg.getPatient().setEntranceQueueArrivalTime(mySim().currentTime());

        myAgent().getEntranceQueue().add(msg);

        myAgent().getEntranceQueueLength().add(myAgent().getEntranceQueue().size());

        msg.setCode(Mc.requestEntranceResources);
        msg.setAddressee(mySim().findAgent(Id.agentResources));

        if (!mySim().isMaxSpeed()) {
            ((MySimulation) mySim()).logEvent(" | Požiadanie o zdroje vstupného vyšetrenia| " + msg.getPatient());
            System.out.println(mySim().currentTime() + " | Požiadanie o zdroje vstupného vyšetrenia| " + msg.getPatient());
        }

        request(msg);
	}

	//meta! sender="ProcessMoveAmbulancePatient", id="136", type="Finish"
	public void processFinishProcessMoveAmbulancePatient(MessageForm message)
    {
        MyMessage msg = (MyMessage) message;

        if (!mySim().isMaxSpeed()) {
            ((MySimulation) mySim()).logEvent(" | Pacient vstupuje do ambulancie |" + msg.getPatient());
            System.out.println(mySim().currentTime() + " | Pacient vstupuje do ambulancie |" + msg.getPatient());
        }

        msg.setCode(Mc.medicalExamination);
        msg.setAddressee(mySim().findAgent(Id.agentMedicalExam));

        request(msg);
    }

	//meta! sender="ProcessMoveExitPatient", id="142", type="Finish"
	public void processFinishProcessMoveExitPatient(MessageForm message)
    {
        MyMessage msg = (MyMessage) message;

        msg.setCode(Mc.patientCare);
        response(msg);
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
			switch (message.sender().id())
			{
			case Id.processMoveExitPatient:
				processFinishProcessMoveExitPatient(message);
			break;

			case Id.processMoveAmbulancePatient:
				processFinishProcessMoveAmbulancePatient(message);
			break;

			case Id.processMoveEntrancePatient:
				processFinishProcessMoveEntrancePatient(message);
			break;
			}
		break;

		case Mc.requestEntranceResources:
			processRequestEntranceResources(message);
		break;

		case Mc.medicalExamination:
			processMedicalExamination(message);
		break;

		case Mc.patientCare:
			processPatientCare(message);
		break;

		case Mc.requestMedicalResources:
			processRequestMedicalResources(message);
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