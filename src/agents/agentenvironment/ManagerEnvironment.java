package agents.agentenvironment;

import OSPABA.*;
import entities.Patient;
import simulation.*;

import java.util.ArrayList;
import java.util.List;

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
        MyMessage msg = (MyMessage) message;
        // UPDATE STATISTICS

        Patient patient = msg.getPatient();

        double duration = mySim().currentTime() - patient.getArrivalTime();

        myAgent().setPatientsOut(myAgent().getPatientsOut() + 1);
        myAgent().getTimeInSystem().add(duration);

        if (patient.isWithAmbulance()) {
            myAgent().getTimeInSystemAmbulance().add(duration);
            myAgent().setPatientsOutAmbulance(myAgent().getPatientsOutAmbulance() + 1);
        } else {
            myAgent().getTimeInSystemWalk().add(duration);
            myAgent().setPatientsOutWalk(myAgent().getPatientsOutWalk() + 1);
        }

        if (mySim().animatorExists()) {
            msg.getPatient().remove();
        }
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
        myAgent().setPatientsInWalk(myAgent().getPatientsInWalk() + 1);

        if (!mySim().isMaxSpeed()) {
            ((MySimulation) mySim()).logEvent(" | Príchod pacienta | " + patient);
            System.out.println(mySim().currentTime() + " | Príchod pacienta | " + patient);
        }

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
        myAgent().setPatientsInAmbulance(myAgent().getPatientsInAmbulance() + 1);

        if (!mySim().isMaxSpeed()) {
            ((MySimulation) mySim()).logEvent(" | Príchod pacienta | " + patient);
            System.out.println(mySim().currentTime() + " | Príchod pacienta | " + patient);
        }

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

        if (((MySimulation) mySim()).isWarmupFind()) {
            MyMessage wipMsg = new MyMessage(mySim());

            wipMsg.setAddressee(myAgent().findAssistant(Id.schedulerWarmupFind));

            startContinualAssistant(wipMsg);
        }
	}

	//meta! sender="SchedulerWarmupFind", id="149", type="Finish"
	public void processFinishSchedulerWarmupFind(MessageForm message)
	{
        int hourIndex = (int)(mySim().currentTime() / 3600);
        List<List<Double>> wipData = ((MySimulation) mySim()).getWipData();

        while (wipData.size() <= hourIndex) {
            wipData.add(new ArrayList<>());
        }

        int currentCount = myAgent().getPatientsIn() - myAgent().getPatientsOut();

        wipData.get(hourIndex).add((double) currentCount);

        message.setAddressee(myAgent().findAssistant(Id.schedulerWarmupFind));
        startContinualAssistant(message);
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
		case Mc.finish:
			switch (message.sender().id())
			{
			case Id.schedulerWarmupFind:
				processFinishSchedulerWarmupFind(message);
			break;

			case Id.schedulerAmbulanceCar:
				processFinishSchedulerAmbulanceCar(message);
			break;

			case Id.schedulerWalk:
				processFinishSchedulerWalk(message);
			break;
			}
		break;

		case Mc.noticeInit:
			processNoticeInit(message);
		break;

		case Mc.patientExit:
			processPatientExit(message);
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