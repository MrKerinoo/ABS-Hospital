package agents.agenthospital.continualassistants;

import OSPABA.*;
import entities.Patient;
import simulation.*;
import agents.agenthospital.*;
import OSPABA.Process;
import utils.Utils;

//meta! id="92"
public class ProcessMovePatient extends OSPABA.Process
{
	public ProcessMovePatient(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="AgentHospital", id="93", type="Start"
	public void processStart(MessageForm message)
	{
        MyMessage msg = (MyMessage) message;

        double duration;
        if (msg.getPatient().isWithAmbulance()) {
            duration = myAgent().getAmbulanceCarEntranceMoveGenerator().randDouble();
        } else {
            duration = myAgent().getWalkEntranceMoveGenerator().randDouble();
        }

        Patient patient = msg.getPatient();

        if (mySim().animatorExists()) {
            double randX = myAgent().getRandomXWaitingRoomGenerator().randDouble();
            double randY = myAgent().getRandomYWaitingRoomGenerator().randDouble();

            if (patient.isWithAmbulance()) {
                Utils.moveAlongPath(patient, duration, mySim().currentTime(),
                        Utils.p2d(1310, 693),
                        Utils.p2d(1310, 372),
                        Utils.p2d(833, 372),
                        Utils.p2d(randX, randY)
                );
            } else {
                Utils.moveAlongPath(patient, duration, mySim().currentTime(),
                        Utils.p2d(380, 693),
                        Utils.p2d(380, 372),
                        Utils.p2d(833, 372),
                        Utils.p2d(randX, randY)
                );
            }

        }

        message.setCode(Mc.finish);
        hold(duration , message);
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