package agents.agentenviroment;

import OSPABA.*;
import simulation.*;
import agents.agentenviroment.continualassistants.*;

//meta! id="2"
public class AgentEnviroment extends OSPABA.Agent
{
	public AgentEnviroment(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);
		init();
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		new ManagerEnviroment(Id.managerEnviroment, mySim(), this);
		new SchedulerCar(Id.schedulerCar, mySim(), this);
		new SchedulerPatient(Id.schedulerPatient, mySim(), this);
		addOwnMessage(Mc.patientExit);
	}
	//meta! tag="end"
}
