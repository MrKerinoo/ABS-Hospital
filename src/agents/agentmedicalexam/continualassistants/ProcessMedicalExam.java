package agents.agentmedicalexam.continualassistants;

import OSPABA.*;
import agents.agentmedicalexam.*;
import entities.Patient;
import simulation.*;
import OSPABA.Process;

//meta! id="52"
public class ProcessMedicalExam extends OSPABA.Process
{
	public ProcessMedicalExam(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="AgentMedicalExam", id="53", type="Start"
	public void processStart(MessageForm message)
	{
        MyMessage msg = (MyMessage) message;

        double duration;

        Patient patient = msg.getPatient();

        if (patient.isWithAmbulance()) {
            duration = myAgent().getMedicalExamAmbulanceCarGenerator().randDouble();
        } else {
            duration = myAgent().getMedicalExamWalkGenerator().randDouble();
        }

        System.out.println(mySim().currentTime() + " | Začiatok lekárskeho ošetrenia | " + msg.getPatient());

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
	public AgentMedicalExam myAgent()
	{
		return (AgentMedicalExam)super.myAgent();
	}

}