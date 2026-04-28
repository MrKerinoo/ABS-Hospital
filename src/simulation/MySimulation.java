package simulation;

import OSPABA.*;
import agents.agentmedicalexam.*;
import agents.agentresources.*;
import agents.agentenvironment.*;
import agents.agentboss.*;
import agents.agententranceexam.*;
import agents.agenthospital.*;
import statistics.DiscreteStatistics;
import java.util.Random;

public class MySimulation extends OSPABA.Simulation
{
    private Random random;

    private int nursesCount;
    private int doctorsCount;

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

        this.random = new Random(1);

        this.nursesCount = 5;
        this.doctorsCount = 3;
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

        if (animatorExists()) {
            animator().clearAll();
        }

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
		setAgentResources(new AgentResources(Id.agentResources, this, agentHospital()));
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

	private AgentResources _agentResources;

public AgentResources agentResources()
	{ return _agentResources; }

	public void setAgentResources(AgentResources agentResources)
	{_agentResources = agentResources; }

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


    public int getNursesCount() {
        return nursesCount;
    }

    public int getDoctorsCount() {
        return doctorsCount;
    }

    public void setNursesCount(int nursesCount) {
        this.nursesCount = nursesCount;
    }

    public void setDoctorsCount(int doctorsCount) {
        this.doctorsCount = doctorsCount;
    }
}