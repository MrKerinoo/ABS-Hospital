package agents.agentenvironment;

import OSPABA.*;
import entities.Patient;
import simulation.*;

//meta! id="2"
public class ManagerEnvironment extends OSPABA.Manager
{
	public ManagerEnvironment(int id, Simulation mySim, Agent myAgent)
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

	//meta! sender="AgentBoss", id="57", type="Notice"
	public void processPatientExit(MessageForm message)
	{
        // UPDATE STATISTICS

	}

	//meta! sender="SchedulerWalk", id="9", type="Finish"
	public void processFinishSchedulerWalk(MessageForm message)
	{
        MyMessage msg = (MyMessage) message;
        Patient patient = myAgent().getPatientGenerator().generatePatient();

        patient.setArrivalTime(mySim().currentTime());
        patient.setWithAmbulance(false);

        if (mySim().animatorExists()) {
            mySim().animator().register(patient);

            patient.setPosition(437, 693);
        }

        message.setCode(Mc.patientArrival);
        message.setAddressee(Id.agentBoss);

        myAgent().setPatientsIn(myAgent().getPatientsIn() + 1);
        System.out.println(mySim().currentTime() + " | Príchod pacienta | " + patient);

        msg.setPatient(patient);
        notice(message);

        MyMessage nextMsg = new MyMessage(mySim());
        nextMsg.setAddressee(myAgent().findAssistant(Id.schedulerWalk));
        startContinualAssistant(nextMsg);
	}

	//meta! sender="SchedulerAmbulanceCar", id="11", type="Finish"
	public void processFinishSchedulerAmbulanceCar(MessageForm message)
	{
        MyMessage msg = (MyMessage) message;
        Patient patient = myAgent().getPatientGenerator().generatePatient();

        if (mySim().animatorExists()) {
            mySim().animator().register(patient);

            patient.setPosition(1370, 693);
        }

        message.setCode(Mc.patientArrival);
        message.setAddressee(Id.agentBoss);

        myAgent().setPatientsIn(myAgent().getPatientsIn() + 1);
        System.out.println(mySim().currentTime() + " | Príchod pacienta | " + patient);


        patient.setArrivalTime(mySim().currentTime());
        patient.setWithAmbulance(true);

        msg.setPatient(patient);

        notice(message);

        MyMessage nextMsg = new MyMessage(mySim());
        nextMsg.setAddressee(myAgent().findAssistant(Id.schedulerAmbulanceCar));
        startContinualAssistant(nextMsg);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
		}
	}

	//meta! sender="AgentBoss", id="105", type="Notice"
	public void processNoticeInit(MessageForm message)
	{
        MessageForm copyMessage = message.createCopy();

        message.setAddressee(myAgent().findAssistant(Id.schedulerWalk));
        startContinualAssistant(message);

        copyMessage.setAddressee(myAgent().findAssistant(Id.schedulerAmbulanceCar));
        startContinualAssistant(copyMessage);
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
		case Mc.patientExit:
			processPatientExit(message);
		break;

		case Mc.finish:
			switch (message.sender().id())
			{
			case Id.schedulerWalk:
				processFinishSchedulerWalk(message);
			break;

			case Id.schedulerAmbulanceCar:
				processFinishSchedulerAmbulanceCar(message);
			break;
			}
		break;

		case Mc.noticeInit:
			processNoticeInit(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public AgentEnvironment myAgent()
	{
		return (AgentEnvironment)super.myAgent();
	}

}