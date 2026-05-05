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

	//meta! sender="AgentHospital", id="114", type="Request"
	public void processRequestEntranceResources(MessageForm message)
	{
        MyMessage msg = (MyMessage) message;
        myAgent().getEntranceRequests().add(msg);
        
        updateQueueStats();
        allocateResources();
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

        updateQueueStats();
        allocateResources();
    }

	//meta! sender="ProcessMovePersonnel", id="47", type="Finish"
	public void processFinish(MessageForm message)
    {
        // Assistant no longer used in simplified logic
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

        allocateResources();
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

        allocateResources();
    }

    private void updateQueueStats() {
        myAgent().getEntranceQueueLength().add(myAgent().getEntranceRequests().size());
        
        java.util.Set<Integer> uniquePatients = new java.util.HashSet<>();
        for (MyMessage m : myAgent().getMedicalARequests()) {
            uniquePatients.add(m.getPatient().getId());
        }
        for (MyMessage m : myAgent().getMedicalBRequests()) {
            uniquePatients.add(m.getPatient().getId());
        }
        myAgent().getMedicalQueueLength().add(uniquePatients.size());
    }

    private void allocateResources() {
        boolean changed = true;

        while (changed) {
            changed = false;

            // 1. Try allocate Medical Exam A
            if (!myAgent().getMedicalARequests().isEmpty() && 
                !myAgent().getFreeDoctors().isEmpty() && 
                !myAgent().getFreeNurses().isEmpty() && 
                !myAgent().getFreeAmbulancesA().isEmpty()) {
                
                MyMessage msg = myAgent().getMedicalARequests().poll();
                myAgent().getMedicalBRequests().remove(msg);
                
                updateQueueStats();

                Nurse nurse = myAgent().getFreeNurses().remove(0);
                Doctor doctor = myAgent().getFreeDoctors().remove(0);
                Ambulance ambulance = myAgent().getFreeAmbulancesA().remove(0);
                
                msg.setNurse(nurse);
                msg.setDoctor(doctor);
                msg.setAmbulance(ambulance);
                
                myAgent().recordNurseUsage();
                myAgent().recordDoctorUsage();
                myAgent().recordAmbulanceAUsage();
                
                response(msg);
                changed = true;
                continue;
            }

            // 2. Try allocate Medical Exam B
            if (!myAgent().getMedicalBRequests().isEmpty() && 
                !myAgent().getFreeDoctors().isEmpty() && 
                !myAgent().getFreeNurses().isEmpty() && 
                !myAgent().getFreeAmbulancesB().isEmpty()) {
                
                MyMessage msg = myAgent().getMedicalBRequests().poll();
                myAgent().getMedicalARequests().remove(msg);
                
                updateQueueStats();

                Nurse nurse = myAgent().getFreeNurses().remove(0);
                Doctor doctor = myAgent().getFreeDoctors().remove(0);
                Ambulance ambulance = myAgent().getFreeAmbulancesB().remove(0);
                
                msg.setNurse(nurse);
                msg.setDoctor(doctor);
                msg.setAmbulance(ambulance);
                
                myAgent().recordNurseUsage();
                myAgent().recordDoctorUsage();
                myAgent().recordAmbulanceBUsage();
                
                response(msg);
                changed = true;
                continue;
            }

            // 3. Try allocate Entrance Exam (only needs Nurse and Amb B)
            if (!myAgent().getEntranceRequests().isEmpty() && 
                !myAgent().getFreeNurses().isEmpty() && 
                !myAgent().getFreeAmbulancesB().isEmpty()) {
                
                MyMessage msg = myAgent().getEntranceRequests().poll();
                
                updateQueueStats();

                Nurse nurse = myAgent().getFreeNurses().remove(0);
                Ambulance ambulance = myAgent().getFreeAmbulancesB().remove(0);
                
                msg.setNurse(nurse);
                msg.setAmbulance(ambulance);
                
                myAgent().recordNurseUsage();
                myAgent().recordAmbulanceBUsage();
                
                response(msg);
                changed = true;
                continue;
            }
        }
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
}
