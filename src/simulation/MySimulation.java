package simulation;

import OSPABA.*;
import agents.agentmedicalexam.*;
import agents.agentambulance.*;
import agents.agentenvironment.*;
import agents.agentboss.*;
import agents.agententranceexam.*;
import agents.agenthospital.*;
import agents.agentpersonnel.*;
import statistics.DiscreteStatistics;

import java.util.Random;

public class MySimulation extends OSPABA.Simulation
{
    private Random random;

    // GLOBAL STATISTICS

    private DiscreteStatistics patientsIn;

	public MySimulation()
	{
		init();
	}

    public Random getSeedRandom() {
        return this.random;
    }

	@Override
	public void prepareSimulation()
	{
		super.prepareSimulation();
		// Create global statistcis
        patientsIn = new DiscreteStatistics();

        this.random = new Random();
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Reset entities, queues, local statistics, etc...
        agentBoss().firstArrival();
	}

	@Override
	public void replicationFinished()
	{
        patientsIn.add(agentEnvironment().getPatientsIn());
		// Collect local statistics into global, update UI, etc...
		super.replicationFinished();
	}

	@Override
	public void simulationFinished()
	{
		// Display simulation results
		super.simulationFinished();
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		setAgentBoss(new AgentBoss(Id.agentBoss, this, null));
		setAgentEnvironment(new AgentEnvironment(Id.agentEnvironment, this, agentBoss()));
		setAgentHospital(new AgentHospital(Id.agentHospital, this, agentBoss()));
		setAgentPersonnel(new AgentPersonnel(Id.agentPersonnel, this, agentHospital()));
		setAgentAmbulance(new AgentAmbulance(Id.agentAmbulance, this, agentHospital()));
		setAgentEntranceExam(new AgentEntranceExam(Id.agentEntranceExam, this, agentHospital()));
		setAgentMedicalExam(new AgentMedicalExam(Id.agentMedicalExam, this, agentHospital()));
	}

	private AgentBoss _agentBoss;

public AgentBoss agentBoss()
	{ return _agentBoss; }

	public void setAgentBoss(AgentBoss agentBoss)
	{_agentBoss = agentBoss; }

	private AgentEnvironment _agentEnvironment;

public AgentEnvironment agentEnvironment()
	{ return _agentEnvironment; }

	public void setAgentEnvironment(AgentEnvironment agentEnvironment)
	{_agentEnvironment = agentEnvironment; }

	private AgentHospital _agentHospital;

public AgentHospital agentHospital()
	{ return _agentHospital; }

	public void setAgentHospital(AgentHospital agentHospital)
	{_agentHospital = agentHospital; }

	private AgentPersonnel _agentPersonnel;

public AgentPersonnel agentPersonnel()
	{ return _agentPersonnel; }

	public void setAgentPersonnel(AgentPersonnel agentPersonnel)
	{_agentPersonnel = agentPersonnel; }

	private AgentAmbulance _agentAmbulance;

public AgentAmbulance agentAmbulance()
	{ return _agentAmbulance; }

	public void setAgentAmbulance(AgentAmbulance agentAmbulance)
	{_agentAmbulance = agentAmbulance; }

	private AgentEntranceExam _agentEntranceExam;

public AgentEntranceExam agentEntranceExam()
	{ return _agentEntranceExam; }

	public void setAgentEntranceExam(AgentEntranceExam agentEntranceExam)
	{_agentEntranceExam = agentEntranceExam; }

	private AgentMedicalExam _agentMedicalExam;

public AgentMedicalExam agentMedicalExam()
	{ return _agentMedicalExam; }

	public void setAgentMedicalExam(AgentMedicalExam agentMedicalExam)
	{_agentMedicalExam = agentMedicalExam; }
	//meta! tag="end"
}