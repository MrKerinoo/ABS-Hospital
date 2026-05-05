package agents.agentresources;

import OSPABA.*;
import entities.Ambulance;
import entities.Doctor;
import entities.Nurse;
import simulation.*;

import java.util.List;
import java.util.PriorityQueue;

//meta! id="16"
public class ManagerResources extends OSPABA.Manager
{
	public ManagerResources(int id, Simulation mySim, Agent myAgent)
	{
		super(id, mySim, myAgent);
		this.init();
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication

		if (this.petriNet() != null)
		{
			this.petriNet().clear();
		}
	}

	//meta! sender="AgentHospital", id="114", type="Request"
	public void processRequestEntranceResources(MessageForm message)
	{
        MyMessage msg = (MyMessage) message;
        myAgent().getEntranceRequests().add(msg);
        
        this.updateQueueStats();
        this.allocateResources();
	}

	//meta! sender="AgentHospital", id="116", type="Request"
	public void processRequestMedicalResources(MessageForm message) {
        MyMessage msg = (MyMessage) message;
        int priority = msg.getPatient().getPriority();

        if (priority <= 2) {
            myAgent().getMedicalARequests().add(msg);
        } else if (priority == 5) {
            myAgent().getMedicalBRequests().add(msg);
        } else {
            myAgent().getMedicalARequests().add(msg);
            myAgent().getMedicalBRequests().add(msg);
        }

        myAgent().setMedicalWaitingCount(myAgent().getMedicalWaitingCount() + 1);
        this.updateQueueStats();
        this.allocateResources();
    }

	//meta! sender="ProcessMovePersonnel", id="47", type="Finish"
	public void processFinish(MessageForm message)
    {
        MyMessage msg = (MyMessage) message;
        Ambulance targetAmb = msg.getAmbulance();

        if (msg.getNurse() != null) {
            Nurse nurse = msg.getNurse();
            if (nurse.getAmbulance() != null) {
                nurse.getAmbulance().setNurse(null);
            }
            nurse.setAmbulance(targetAmb);
            targetAmb.setNurse(nurse);
        }

        if (msg.getDoctor() != null) {
            Doctor doctor = msg.getDoctor();
            if (doctor.getAmbulance() != null) {
                doctor.getAmbulance().setDoctor(null);
            }
            doctor.setAmbulance(targetAmb);
            targetAmb.setDoctor(doctor);
            
            // Restore code for medical exam
            msg.setCode(Mc.requestMedicalResources);
        } else {
            // Restore code for entrance exam
            msg.setCode(Mc.requestEntranceResources);
        }

        this.response(msg);
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

        myAgent().recordNurseUsage();
        myAgent().recordAmbulanceBUsage();

        this.allocateResources();
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
            myAgent().recordAmbulanceAUsage();
        } else {
            myAgent().getFreeAmbulancesB().add(ambulance);
            myAgent().recordAmbulanceBUsage();
        }

        myAgent().getFreeDoctors().add(doc);
        myAgent().getFreeNurses().add(nurse);

        myAgent().recordDoctorUsage();
        myAgent().recordNurseUsage();

        this.allocateResources();
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

		case Mc.finish:
			processFinish(message);
		break;

		case Mc.requestEntranceResources:
			processRequestEntranceResources(message);
		break;

		case Mc.releaseEntranceResources:
			processReleaseEntranceResources(message);
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
	public AgentResources myAgent()
	{
		return (AgentResources)super.myAgent();
	}

    private void updateQueueStats() {
        myAgent().getEntranceQueueLength().add(myAgent().getEntranceRequests().size());
        myAgent().getMedicalQueueLength().add(myAgent().getMedicalWaitingCount());
    }

    private void allocateResources() {
        int strategy = ((MySimulation) mySim()).getStrategyType();
        switch (strategy) {
            case MySimulation.STRATEGY_SAVING_A:
                this.allocateResourcesSavingA();
                break;
            case MySimulation.STRATEGY_AMBULANCE_PREFERENCE:
                this.allocateResourcesAmbulancePreference();
                break;
            case MySimulation.STRATEGY_EXAM_PREFERENCE:
                this.allocateResourcesExamPreference();
                break;
            case MySimulation.STRATEGY_BASIC:
            default:
                this.allocateResourcesBasic();
                break;
        }
    }

    private void allocateResourcesBasic() {
        boolean changed = true;

        while (changed) {
            changed = false;

            // Medical Exam A
            if (this.tryAllocateMedical(myAgent().getMedicalARequests(), myAgent().getMedicalBRequests(),
                    myAgent().getFreeAmbulancesA(), 'A')) {
                changed = true;
                continue;
            }

            // Medical Exam B
            if (this.tryAllocateMedical(myAgent().getMedicalBRequests(), myAgent().getMedicalARequests(),
                    myAgent().getFreeAmbulancesB(), 'B')) {
                changed = true;
                continue;
            }

            // Entrance Exam
            if (this.tryAllocateEntrance()) {
                changed = true;
            }
        }
    }

    private void allocateResourcesExamPreference() {
        boolean changed = true;

        while (changed) {
            changed = false;

            // 1. Entrance Exam -> Absolute Priority
            if (this.tryAllocateEntrance()) {
                changed = true;
                continue;
            }

            // 2. Medical Exam (P1 & P2) -> only Ambulance A
            if (this.tryAllocateMedical(myAgent().getMedicalARequests(), myAgent().getMedicalBRequests(),
                    myAgent().getFreeAmbulancesA(), 'A')) {
                changed = true;
                continue;
            }

            // 3. Medical Exam B (P3, P4, P5)
            if (this.tryAllocateMedical(myAgent().getMedicalBRequests(), myAgent().getMedicalARequests(),
                    myAgent().getFreeAmbulancesB(), 'B')) {
                changed = true;
                continue;
            }

            // 4. Medical Exam A (Fallback for P3, P4)
            if (this.tryAllocateMedical(myAgent().getMedicalARequests(), myAgent().getMedicalBRequests(),
                    myAgent().getFreeAmbulancesA(), 'A')) {
                changed = true;
            }
        }
    }

    private void allocateResourcesAmbulancePreference() {
        this.allocateResourcesBasic();
    }

    private void allocateResourcesSavingA() {
        boolean changed = true;

        while (changed) {
            changed = false;

            // 1. Critical Medical Exam (P1 & P2) -> only Ambulance A
            if (this.tryAllocateMedical(myAgent().getMedicalARequests(), myAgent().getMedicalBRequests(),
                    myAgent().getFreeAmbulancesA(), 'A')) {
                changed = true;
                continue;
            }

            // 2. Optimized: P3 & P4 Patients prefer Ambulance B first
            // We check if the head of the medical queue is a P3 or P4 patient
            if (!myAgent().getMedicalBRequests().isEmpty()) {
                MyMessage head = myAgent().getMedicalBRequests().peek();
                int priority = head.getPatient().getPriority();
                
                // If it's P3 or P4, try to give them B first
                if (priority == 3 || priority == 4) {
                    if (this.tryAllocateMedical(myAgent().getMedicalBRequests(), myAgent().getMedicalARequests(),
                            myAgent().getFreeAmbulancesB(), 'B')) {
                        changed = true;
                        continue;
                    }
                }
            }

            // 3. Entrance Exam -> Ambulance B
            if (this.tryAllocateEntrance()) {
                changed = true;
                continue;
            }

            // 4. Low Priority (P5) -> only Ambulance B
            if (!myAgent().getMedicalBRequests().isEmpty()) {
                MyMessage head = myAgent().getMedicalBRequests().peek();
                if (head.getPatient().getPriority() == 5) {
                    if (this.tryAllocateMedical(myAgent().getMedicalBRequests(), myAgent().getMedicalARequests(),
                            myAgent().getFreeAmbulancesB(), 'B')) {
                        changed = true;
                        continue;
                    }
                }
            }

            // 5. Fallback for P3 & P4 -> if B was not available, try A
            if (this.tryAllocateMedical(myAgent().getMedicalARequests(), myAgent().getMedicalBRequests(),
                    myAgent().getFreeAmbulancesA(), 'A')) {
                changed = true;
            }
        }
    }

    private boolean tryAllocateMedical(PriorityQueue<MyMessage> primary, PriorityQueue<MyMessage> secondary,
                                       List<Ambulance> ambulances, char type) {
        if (!primary.isEmpty() && !myAgent().getFreeDoctors().isEmpty() &&
            !myAgent().getFreeNurses().isEmpty() && !ambulances.isEmpty()) {
            
            MyMessage msg = primary.poll();
            secondary.remove(msg);
            
            myAgent().setMedicalWaitingCount(myAgent().getMedicalWaitingCount() - 1);
            this.updateQueueStats();

            Nurse nurse = myAgent().getFreeNurses().removeFirst();
            Doctor doctor = myAgent().getFreeDoctors().removeFirst();
            Ambulance ambulance = ambulances.removeFirst();
            
            msg.setNurse(nurse);
            msg.setDoctor(doctor);
            msg.setAmbulance(ambulance);
            ambulance.setPatient(msg.getPatient());
            
            myAgent().recordNurseUsage();
            myAgent().recordDoctorUsage();

            if (type == 'A') {
                myAgent().recordAmbulanceAUsage();
            } else {
                myAgent().recordAmbulanceBUsage();
            }
            
            msg.setAddressee(myAgent().findAssistant(Id.processMovePersonnel));

            this.startContinualAssistant(msg);

            return true;
        }
        return false;
    }

    private boolean tryAllocateEntrance() {
        if (!myAgent().getEntranceRequests().isEmpty() && 
            !myAgent().getFreeNurses().isEmpty() && 
            !myAgent().getFreeAmbulancesB().isEmpty()) {
            
            MyMessage msg = myAgent().getEntranceRequests().poll();
            this.updateQueueStats();

            Nurse nurse = myAgent().getFreeNurses().removeFirst();
            Ambulance ambulance = myAgent().getFreeAmbulancesB().removeFirst();
            
            msg.setNurse(nurse);
            msg.setAmbulance(ambulance);
            ambulance.setPatient(msg.getPatient());
            
            myAgent().recordNurseUsage();
            myAgent().recordAmbulanceBUsage();
            
            msg.setAddressee(myAgent().findAssistant(Id.processMovePersonnel));

            this.startContinualAssistant(msg);

            return true;
        }
        return false;
    }
}
