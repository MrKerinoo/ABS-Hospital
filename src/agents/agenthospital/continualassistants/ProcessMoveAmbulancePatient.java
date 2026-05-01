package agents.agenthospital.continualassistants;

import OSPABA.*;
import entities.Ambulance;
import entities.Patient;
import simulation.*;
import agents.agenthospital.*;
import OSPABA.Process;
import utils.Utils;

//meta! id="135"
public class ProcessMoveAmbulancePatient extends OSPABA.Process
{
	public ProcessMoveAmbulancePatient(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="AgentHospital", id="136", type="Start"
	public void processStart(MessageForm message)
	{
        MyMessage msg = (MyMessage) message;

        double duration = myAgent().getAmbulanceMoveGenerator().randDouble();

        Patient patient = msg.getPatient();
        Ambulance ambulance = msg.getAmbulance();

        if (mySim().animatorExists()) {
            Utils.moveAlongPath(patient, duration, mySim().currentTime(),
                    Utils.p2d(ambulance.getXDoor(), ambulance.getYDoor()),
                    Utils.p2d(ambulance.getXInside(), ambulance.getYInside()),
                    Utils.p2d(ambulance.getXPatient(), ambulance.getYPatient())
            );
        }

        message.setCode(Mc.finish);
        hold(duration, message);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
            case Mc.finish:
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
	public AgentHospital myAgent()
	{
		return (AgentHospital)super.myAgent();
	}

}
