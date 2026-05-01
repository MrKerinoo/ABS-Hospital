package agents.agentresources;

import OSPABA.*;
import comparators.ResourceComparator;
import entities.Ambulance;
import entities.Doctor;
import entities.Nurse;
import entities.Patient;
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
    public void processRequestMedicalResources(MessageForm message) {
        MyMessage msg = (MyMessage) message;
        int priority = msg.getPatient().getPriority();

        if (this.tryFullReuse(msg)) {
            return;
        }

        Ambulance readyAmbulance = this.findReadyAmbulance(priority);

        if (readyAmbulance != null) {
            msg.setAmbulance(readyAmbulance);
            msg.setDoctor(readyAmbulance.getDoctor());
            msg.setNurse(readyAmbulance.getNurse());

            response(msg);
            return;
        }

        if (this.tryReuseCurrentAmbulance(msg)) {
            return;
        }

        this.tryMedicalResources(msg);
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
        MyMessage msg = (MyMessage) message;

        Ambulance ambulance = msg.getAmbulance();
        Doctor doc = msg.getDoctor();
        Nurse nurse = msg.getNurse();

        ambulance.setPatient(null);

        if (ambulance.getType() == 'A') {
            myAgent().getFreeAmbulancesA().add(ambulance);
        } else {
            myAgent().getFreeAmbulancesB().add(ambulance);
        }

        myAgent().getFreeDoctors().add(doc);
        myAgent().getFreeNurses().add(nurse);

        System.out.println(mySim().currentTime() + " | Uvoľnené lekárske zdroje | " + msg.getPatient());

        this.tryWaitingMessage();
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
		case Mc.releaseMedicalResources:
			processReleaseMedicalResources(message);
		break;

		case Mc.requestEntranceResources:
			processRequestEntranceResources(message);
		break;

		case Mc.requestMedicalResources:
			processRequestMedicalResources(message);
		break;

		case Mc.finish:
			processFinish(message);
		break;

		case Mc.releaseEntranceResources:
			processReleaseEntranceResources(message);
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

    // --------------------- HELP FUNCTIONS ---------------------

    private Ambulance findReadyAmbulance(int priority) {
        //find doctor and nurse ambulance A
        if (priority <= 4) {
            for (Ambulance amb : myAgent().getFreeAmbulancesA()) {
                if (amb.getDoctor() != null && amb.getNurse() != null) {
                    myAgent().getFreeAmbulancesA().remove(amb);
                    myAgent().getFreeDoctors().remove(amb.getDoctor());
                    myAgent().getFreeNurses().remove(amb.getNurse());
                    return amb;
                }
            }
        }

        //find doctor and nurse ambulance B
        if (priority >= 3) {
            for (Ambulance amb : myAgent().getFreeAmbulancesB()) {
                if (amb.getDoctor() != null && amb.getNurse() != null) {
                    myAgent().getFreeAmbulancesB().remove(amb);
                    myAgent().getFreeDoctors().remove(amb.getDoctor());
                    myAgent().getFreeNurses().remove(amb.getNurse());
                    return amb;
                }
            }
        }
        return null;
    }

    private boolean tryFullReuse(MyMessage msg) {
        Patient patient = msg.getPatient();
        Ambulance currentAmbulance = patient.getVisitedAmbulance();

        if (currentAmbulance != null && canStayInAmbulance(currentAmbulance, patient.getPriority())) {
            Nurse nurse = currentAmbulance.getNurse();
            Doctor doctor = currentAmbulance.getDoctor();

            if (nurse != null && doctor != null &&
                    myAgent().getFreeNurses().contains(nurse) &&
                    myAgent().getFreeDoctors().contains(doctor)) {

                if (currentAmbulance.getType() == 'A') {
                    myAgent().getFreeAmbulancesA().remove(currentAmbulance);
                } else {
                    myAgent().getFreeAmbulancesB().remove(currentAmbulance);
                }

                myAgent().getFreeNurses().remove(nurse);
                myAgent().getFreeDoctors().remove(doctor);

                msg.setAmbulance(currentAmbulance);
                msg.setNurse(nurse);
                msg.setDoctor(doctor);

                System.out.println(mySim().currentTime() + " | Úplná recyklácia tímu v " + currentAmbulance.getId() + " | " + patient);

                response(msg);
                return true;
            }
        }
        return false;
    }

    private void tryWaitingMessage() {
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

    private boolean tryReuseCurrentAmbulance(MyMessage msg) {
        Patient p = msg.getPatient();
        Ambulance currentAmb = p.getVisitedAmbulance();

        if (currentAmb == null || !canStayInAmbulance(currentAmb, p.getPriority())) {
            return false;
        }

        boolean removed = (currentAmb.getType() == 'A')
                ? myAgent().getFreeAmbulancesA().remove(currentAmb)
                : myAgent().getFreeAmbulancesB().remove(currentAmb);

        if (removed) {
            Nurse nurse = currentAmb.getNurse();
            Doctor doctorInAmb = currentAmb.getDoctor();

            if (nurse != null && myAgent().getFreeNurses().contains(nurse)) {
                Doctor selectedDoc = null;

                if (doctorInAmb != null && myAgent().getFreeDoctors().contains(doctorInAmb)) {
                    selectedDoc = doctorInAmb;
                    myAgent().getFreeDoctors().remove(selectedDoc);
                } else if (!myAgent().getFreeDoctors().isEmpty()) {
                    selectedDoc = myAgent().getFreeDoctors().removeFirst();
                }

                if (selectedDoc != null) {
                    myAgent().getFreeNurses().remove(nurse);

                    msg.setAmbulance(currentAmb);
                    msg.setNurse(nurse);
                    msg.setDoctor(selectedDoc);

                    msg.setAddressee(myAgent().findAssistant(Id.processMovePersonnel));

                    startContinualAssistant(msg);

                    return true;
                }
            }

            if (currentAmb.getType() == 'A') {
                myAgent().getFreeAmbulancesA().addFirst(currentAmb);
            } else {
                myAgent().getFreeAmbulancesB().addFirst(currentAmb);
            }
        }

        return false;
    }

    private boolean canStayInAmbulance(Ambulance amb, int priority) {
        if (amb.getType() == 'A') return (priority <= 4);
        if (amb.getType() == 'B') return (priority >= 3);
        return false;
    }

    private void tryMedicalResources(MyMessage msg) {
        int priority = msg.getPatient().getPriority();

        // find doctor/nurse in ambulance
        Ambulance freeAmb = this.findPartialAmbulance(priority);

        // find empty ambulance
        if (freeAmb == null) {
            if (priority <= 4 && !myAgent().getFreeAmbulancesA().isEmpty()) {
                freeAmb = myAgent().getFreeAmbulancesA().removeFirst();
            } else if (priority >= 3 && !myAgent().getFreeAmbulancesB().isEmpty()) {
                freeAmb = myAgent().getFreeAmbulancesB().removeFirst();
            }
        }

        if (freeAmb == null) {
            this.addToWaitingQueues(msg, priority);
            return;
        }

        // found personnel
        Doctor freeDoc = (freeAmb.getDoctor() != null) ? freeAmb.getDoctor() :
                (!myAgent().getFreeDoctors().isEmpty() ? myAgent().getFreeDoctors().removeFirst() : null);

        Nurse freeNurse = (freeAmb.getNurse() != null) ? freeAmb.getNurse() :
                (!myAgent().getFreeNurses().isEmpty() ? myAgent().getFreeNurses().removeFirst() : null);

        if (freeDoc != null && freeNurse != null) {
            msg.setDoctor(freeDoc);
            msg.setNurse(freeNurse);
            msg.setAmbulance(freeAmb);

            msg.setAddressee(myAgent().findAssistant(Id.processMovePersonnel));

            System.out.println(mySim().currentTime() + " | Presun k personálu | " + msg.getPatient());
            startContinualAssistant(msg);
        } else {
            // ROLLBACK
            if (freeDoc != null && freeDoc != freeAmb.getDoctor()) myAgent().getFreeDoctors().addFirst(freeDoc);
            if (freeNurse != null && freeNurse != freeAmb.getNurse()) myAgent().getFreeNurses().addFirst(freeNurse);

            if (freeAmb.getType() == 'A') myAgent().getFreeAmbulancesA().addFirst(freeAmb);
            else myAgent().getFreeAmbulancesB().addFirst(freeAmb);

            addToWaitingQueues(msg, priority);
        }
    }

    private Ambulance findPartialAmbulance(int priority) {
        if (priority <= 4) {
            for (Ambulance amb : myAgent().getFreeAmbulancesA()) {
                if (amb.getDoctor() != null || amb.getNurse() != null) {
                    myAgent().getFreeAmbulancesA().remove(amb);
                    return amb;
                }
            }
        }
        if (priority >= 3) {
            for (Ambulance amb : myAgent().getFreeAmbulancesB()) {
                if (amb.getDoctor() != null || amb.getNurse() != null) {
                    myAgent().getFreeAmbulancesB().remove(amb);
                    return amb;
                }
            }
        }

        return null;
    }

    private void addToWaitingQueues(MyMessage msg, int priority) {
        if (priority <= 2) {
            myAgent().getWaitingAmbulanceARequests().add(msg);
        } else if (priority == 5) {
            myAgent().getWaitingAmbulanceBRequests().add(msg);
        } else {
            myAgent().getWaitingAmbulanceARequests().add(msg);
            myAgent().getWaitingAmbulanceBRequests().add(msg);
        }
    }
}