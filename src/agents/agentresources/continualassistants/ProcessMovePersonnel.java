package agents.agentresources.continualassistants;

import OSPABA.*;
import entities.Nurse;
import simulation.*;
import agents.agentresources.*;
import OSPABA.Process;

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

        if (msg.getNurse() != null) {
            if (msg.getNurse().getAmbulance() == null) {
                durationNurse = myAgent().getEntranceAmbulanceMoveGenerator().randDouble();
            } else {
                durationNurse = myAgent().getAmbulanceMoveGenerator().randDouble();
            }
        } else if (msg.getDoctor() != null) {
            if (msg.getDoctor().getAmbulance() == null) {
                durationDoctor = myAgent().getEntranceAmbulanceMoveGenerator().randDouble();
            } else {
                durationDoctor = myAgent().getAmbulanceMoveGenerator().randDouble();
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