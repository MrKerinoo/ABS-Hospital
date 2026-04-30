package agents.agentresources;

import OSPABA.*;
import comparators.ResourceComparator;
import entities.Ambulance;
import entities.Doctor;
import entities.Nurse;
import simulation.*;

//meta! id="16"
public class ManagerResources extends OSPABA.Manager
{
	public ManagerResources(int id, Simulation mySim, Agent myAgent)
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

	//meta! sender="AgentHospital", id="116", type="Request"
	public void processRequestMedicalResources(MessageForm message)
	{

	}

	//meta! sender="AgentHospital", id="114", type="Request"
	public void processRequestEntranceResources(MessageForm message)
	{
        MyMessage msg = (MyMessage) message;
        Ambulance selectedAmb = null;

        for (Ambulance amb : myAgent().getFreeAmbulancesB()) {
            if (amb.getNurse() != null) {
                selectedAmb = amb;
                myAgent().getFreeAmbulancesB().remove(selectedAmb);
                break;
            }
        }

        // found B ambulance with nurse
        if (selectedAmb != null) {
            msg.setAmbulance(selectedAmb);

            System.out.println(mySim().currentTime() + " | Odpoveď : Nájdenie ambulancie so sestrou | " + msg.getPatient());

            response(msg);
        } else {
            if (!myAgent().getFreeNurses().isEmpty() && !myAgent().getFreeAmbulancesB().isEmpty()) {
                selectedAmb = myAgent().getFreeAmbulancesB().removeFirst();
                Nurse nurse = myAgent().getFreeNurses().removeFirst();

                msg.setNurse(nurse);
                msg.setAmbulance(selectedAmb);

                msg.setAddressee(myAgent().findAssistant(Id.processMovePersonnel));

                System.out.println(mySim().currentTime() + " | Presuň sestru | " + msg.getPatient());

                startContinualAssistant(msg);
            } else {
                myAgent().getWaitingAmbulanceARequests().add(msg);
            }
        }
	}


    //meta! sender="ProcessMovePersonnel", id="47", type="Finish"
    public void processFinish(MessageForm message)
    {
        MyMessage msg = (MyMessage) message;
        Ambulance targetAmb = msg.getAmbulance();

        System.out.println(mySim().currentTime() + " | Presun ukončený | " + msg.getPatient() );

        if (msg.getNurse() != null) {
            Nurse nurse = msg.getNurse();
            if (nurse.getAmbulance() != null) {
                nurse.getAmbulance().setNurse(null);
            }
            nurse.setAmbulance(targetAmb);
            targetAmb.setNurse(nurse);

            myAgent().getFreeNurses().remove(nurse);
        }

        if (msg.getDoctor() != null) {
            Doctor doctor = msg.getDoctor();
            if (doctor.getAmbulance() != null) {
                doctor.getAmbulance().setDoctor(null);
            }
            doctor.setAmbulance(targetAmb);
            targetAmb.setDoctor(doctor);

            myAgent().getFreeDoctors().remove(doctor);

            msg.setCode(Mc.requestMedicalResources);
        } else {
            msg.setCode(Mc.requestEntranceResources);
        }

        if (targetAmb.getType() == 'A') {
            myAgent().getFreeAmbulancesA().remove(targetAmb);
        } else {
            myAgent().getFreeAmbulancesB().remove(targetAmb);
        }

        response(msg);
    }

	//meta! sender="AgentHospital", id="115", type="Notice"
	public void processReleaseEntranceResources(MessageForm message)
	{
        MyMessage msg = (MyMessage) message;

        Ambulance ambulance = msg.getAmbulance();
        ambulance.setPatient(null);
        Nurse nurse = msg.getNurse();

        myAgent().getFreeAmbulancesB().add(ambulance);
        myAgent().getFreeNurses().add(nurse);

        this.tryWaitingMessage();
	}

    //meta! sender="AgentHospital", id="118", type="Notice"
    public void processReleaseMedicalResources(MessageForm message)
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
		case Mc.releaseEntranceResources:
			processReleaseEntranceResources(message);
		break;

		case Mc.requestEntranceResources:
			processRequestEntranceResources(message);
		break;

		case Mc.requestMedicalResources:
			processRequestMedicalResources(message);
		break;

		case Mc.releaseMedicalResources:
			processReleaseMedicalResources(message);
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
	public AgentResources myAgent()
	{
		return (AgentResources)super.myAgent();
	}

    public void tryWaitingMessage() {
        MyMessage msgA = (MyMessage) myAgent().getWaitingAmbulanceARequests().peek();
        MyMessage msgB = (MyMessage) myAgent().getWaitingAmbulanceBRequests().peek();

        if (msgA == null && msgB == null) {
            return;
        }

        ResourceComparator comp = new ResourceComparator();
        MyMessage msg;

        if (msgA != null && msgB != null) {
            msg = (comp.compare(msgA, msgB) <= 0) ? msgA : msgB;
        } else {
            msg = (msgA != null) ? msgA : msgB;
        }

        myAgent().getWaitingAmbulanceARequests().remove(msg);
        myAgent().getWaitingAmbulanceBRequests().remove(msg);

        if (msg.code() == Mc.requestMedicalResources) {
            this.processRequestMedicalResources(msg);
        } else if (msg.code() == Mc.requestEntranceResources) {
            this.processRequestEntranceResources(msg);
        }
    }
}