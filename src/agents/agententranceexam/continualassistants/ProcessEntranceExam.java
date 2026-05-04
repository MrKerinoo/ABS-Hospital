package agents.agententranceexam.continualassistants;

import OSPABA.*;
import entities.Patient;
import simulation.*;
import agents.agententranceexam.*;
import OSPABA.Process;

//meta! id="49"
public class ProcessEntranceExam extends OSPABA.Process
{
	public ProcessEntranceExam(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="AgentEntranceExam", id="50", type="Start"
	public void processStart(MessageForm message)
	{
        MyMessage msg = (MyMessage) message;

        double duration;
        int priority;

        Patient patient = msg.getPatient();

        if (patient.isWithAmbulance()) {
            duration = myAgent().getEntranceExamAmbulanceCarGenerator().randInt();
            priority = myAgent().getPriorityAmbulanceCarGenerator().randInt();
        } else {
            duration = myAgent().getEntranceExamWalkGenerator().randDouble();
            priority = myAgent().getPriorityWalkGenerator().randInt();
        }

        patient.setPriority(priority);


        if (!mySim().isMaxSpeed()) {
            ((MySimulation) mySim()).logEvent(" | Začiatok vstupného vyšetrenia | " + msg.getPatient());
            System.out.println(mySim().currentTime() + " | Začiatok vstupného vyšetrenia | " + msg.getPatient());
        }

        msg.setCode(Mc.finish);
        hold(duration, msg);
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
	public AgentEntranceExam myAgent()
	{
		return (AgentEntranceExam)super.myAgent();
	}

}