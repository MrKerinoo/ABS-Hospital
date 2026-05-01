package agents.agentresources.continualassistants;

import OSPABA.*;
import entities.Ambulance;
import entities.Doctor;
import entities.Nurse;
import simulation.*;
import agents.agentresources.*;
import OSPABA.Process;
import utils.Utils;

//meta! id="46"
public class ProcessMovePersonnel extends OSPABA.Process
{
	public ProcessMovePersonnel(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="AgentResources", id="47", type="Start"
	public void processStart(MessageForm message)
    {
        MyMessage msg = (MyMessage) message;
        double durationNurse = 0;
        double durationDoctor = 0;

        Nurse nurse = msg.getNurse();
        Doctor doctor = msg.getDoctor();
        Ambulance ambulance = msg.getAmbulance();

        if (nurse != null) {
            Ambulance currentAmbulance = nurse.getAmbulance();

            if (ambulance != currentAmbulance) {
                if (currentAmbulance == null) {
                    durationNurse = myAgent().getEntranceAmbulanceMoveGenerator().randDouble();

                    if (mySim().animatorExists()) {
                        Utils.moveAlongPath(nurse, durationNurse, mySim().currentTime(),
                                Utils.p2d(1424, 548),
                                Utils.p2d(1312, 548),
                                Utils.p2d(1312, 374),
                                Utils.p2d(ambulance.getXDoor(), ambulance.getYDoor()),
                                Utils.p2d(ambulance.getXInside(), ambulance.getYInside()),
                                Utils.p2d(ambulance.getXNurse(), ambulance.getYNurse())
                        );
                    }
                } else {
                    durationNurse = myAgent().getAmbulanceMoveGenerator().randDouble();

                    if (mySim().animatorExists()) {
                        Utils.moveAlongPath(nurse, durationNurse, mySim().currentTime(),
                                Utils.p2d(currentAmbulance.getXInside(), currentAmbulance.getYInside()),
                                Utils.p2d(currentAmbulance.getXDoor(), currentAmbulance.getYDoor()),
                                Utils.p2d(ambulance.getXDoor(), ambulance.getYDoor()),
                                Utils.p2d(ambulance.getXInside(), ambulance.getYInside()),
                                Utils.p2d(ambulance.getXNurse(), ambulance.getYNurse())
                        );
                    }
                }
            }
        }

        if (doctor != null) {
            Ambulance currentAmbulance = doctor.getAmbulance();

            if (currentAmbulance != ambulance) {
                if (doctor.getAmbulance() == null) {
                    durationDoctor = myAgent().getEntranceAmbulanceMoveGenerator().randDouble();

                    if (mySim().animatorExists()) {
                        Utils.moveAlongPath(doctor, durationDoctor, mySim().currentTime(),
                                Utils.p2d(1424, 548),
                                Utils.p2d(1312, 548),
                                Utils.p2d(1312, 374),
                                Utils.p2d(ambulance.getXDoor(), ambulance.getYDoor()),
                                Utils.p2d(ambulance.getXInside(), ambulance.getYInside()),
                                Utils.p2d(ambulance.getXDoctor(), ambulance.getYDoctor())
                        );
                    }
                } else {
                    durationDoctor = myAgent().getAmbulanceMoveGenerator().randDouble();

                    if (mySim().animatorExists()) {
                        Utils.moveAlongPath(doctor, durationDoctor, mySim().currentTime(),
                                Utils.p2d(currentAmbulance.getXInside(), currentAmbulance.getYInside()),
                                Utils.p2d(currentAmbulance.getXDoor(), currentAmbulance.getYDoor()),
                                Utils.p2d(ambulance.getXDoor(), ambulance.getYDoor()),
                                Utils.p2d(ambulance.getXInside(), ambulance.getYInside()),
                                Utils.p2d(ambulance.getXDoctor(), ambulance.getYDoctor())
                        );
                    }
                }
            }
        }

        System.out.println(mySim().currentTime() + " | Začiatok presunu zamestnancov | " + durationNurse +  " | " + msg.getPatient() );

        message.setCode(Mc.finish);
        hold(Math.max(durationNurse, durationDoctor), msg);
    }

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{

		switch (message.code())
		{
            case Mc.finish :
                assistantFinished(message);
                break;
		}
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	@Override
	public void processMessage(MessageForm message)
	{
		switch (message.code())
		{
		case Mc.start:
			processStart(message);
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