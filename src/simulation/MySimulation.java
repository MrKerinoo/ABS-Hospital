package simulation;

import OSPABA.*;
import agents.agentmedicalexam.*;
import agents.agentresources.*;
import agents.agentenvironment.*;
import agents.agentboss.*;
import agents.agententranceexam.*;
import agents.agenthospital.*;
import gui.panels.simulation.SimulationPanel;
import statistics.DiscreteStatistics;
import java.util.Random;

public class MySimulation extends OSPABA.Simulation
{
    private Random random;

    private int nursesCount;
    private int doctorsCount;

    // GLOBAL STATISTICS

    private DiscreteStatistics patientsIn;
    private DiscreteStatistics patientsOut;
    private DiscreteStatistics patientsInWalk;
    private DiscreteStatistics patientsInAmbulance;
    private DiscreteStatistics patientsOutWalk;
    private DiscreteStatistics patientsOutAmbulance;

    private DiscreteStatistics timeInSystem;
    private DiscreteStatistics timeInSystemWalk;
    private DiscreteStatistics timeInSystemAmbulance;

    private DiscreteStatistics entranceQueueLength;
    private DiscreteStatistics medicalQueueLength;

    private DiscreteStatistics waitEntrance;
    private DiscreteStatistics waitMedical;
    private DiscreteStatistics waitEntranceWalk;
    private DiscreteStatistics waitEntranceAmbulance;
    private DiscreteStatistics waitMedicalWalk;
    private DiscreteStatistics waitMedicalAmbulance;

    private DiscreteStatistics doctorUsage;
    private DiscreteStatistics nurseUsage;
    private DiscreteStatistics ambulanceAUsage;
    private DiscreteStatistics ambulanceBUsage;


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
        patientsOut = new DiscreteStatistics();
        patientsInWalk = new DiscreteStatistics();
        patientsInAmbulance = new DiscreteStatistics();
        patientsOutWalk = new DiscreteStatistics();
        patientsOutAmbulance = new DiscreteStatistics();

        timeInSystem = new DiscreteStatistics();
        timeInSystemWalk = new DiscreteStatistics();
        timeInSystemAmbulance = new DiscreteStatistics();

        entranceQueueLength = new DiscreteStatistics();
        medicalQueueLength = new DiscreteStatistics();

        waitEntrance = new DiscreteStatistics();
        waitMedical = new DiscreteStatistics();
        waitEntranceWalk = new DiscreteStatistics();
        waitEntranceAmbulance = new DiscreteStatistics();
        waitMedicalWalk = new DiscreteStatistics();
        waitMedicalAmbulance = new DiscreteStatistics();

        doctorUsage = new DiscreteStatistics();
        nurseUsage = new DiscreteStatistics();
        ambulanceAUsage = new DiscreteStatistics();
        ambulanceBUsage = new DiscreteStatistics();

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
        patientsOut.add(agentEnvironment().getPatientsOut());
        patientsInWalk.add(agentEnvironment().getPatientsInWalk());
        patientsInAmbulance.add(agentEnvironment().getPatientsInAmbulance());
        patientsOutWalk.add(agentEnvironment().getPatientsOutWalk());
        patientsOutAmbulance.add(agentEnvironment().getPatientsOutAmbulance());

        timeInSystem.add(agentEnvironment().getTimeInSystem().getMean());
        timeInSystemWalk.add(agentEnvironment().getTimeInSystemWalk().getMean());
        timeInSystemAmbulance.add(agentEnvironment().getTimeInSystemAmbulance().getMean());

        entranceQueueLength.add(agentHospital().getEntranceQueueLength().getMean());
        medicalQueueLength.add(agentHospital().getMedicalQueueLength().getMean());
        waitEntrance.add(agentHospital().getWaitEntrance().getMean());
        waitMedical.add(agentHospital().getWaitMedical().getMean());
        waitEntranceWalk.add(agentHospital().getWaitEntranceWalk().getMean());
        waitEntranceAmbulance.add(agentHospital().getWaitEntranceAmbulance().getMean());
        waitMedicalWalk.add(agentHospital().getWaitMedicalWalk().getMean());
        waitMedicalAmbulance.add(agentHospital().getWaitMedicalAmbulance().getMean());

        doctorUsage.add(agentResources().getDoctorUsage().getMean());
        nurseUsage.add(agentResources().getNurseUsage().getMean());
        ambulanceAUsage.add(agentResources().getAmbulanceAUsage().getMean());
        ambulanceBUsage.add(agentResources().getAmbulanceBUsage().getMean());

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
        if(animator()!=null)animator().clearAll();

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

    public void logEvent(String message) {
        if (!this.isMaxSpeed()) {
            for (ISimDelegate delegate : delegates()) {
                if (delegate instanceof SimulationPanel) {
                    ((SimulationPanel) delegate).log(message);
                }
            }
        }
    }

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

    public DiscreteStatistics getPatientsIn() {
        return patientsIn;
    }

    public DiscreteStatistics getPatientsOut() {
        return patientsOut;
    }

    public DiscreteStatistics getTimeInSystem() {
        return timeInSystem;
    }

    public DiscreteStatistics getEntranceQueueLength() {
        return entranceQueueLength;
    }

    public DiscreteStatistics getMedicalQueueLength() {
        return medicalQueueLength;
    }

    public DiscreteStatistics getWaitEntrance() {
        return waitEntrance;
    }

    public DiscreteStatistics getWaitMedical() {
        return waitMedical;
    }

    public DiscreteStatistics getDoctorUsage() {
        return doctorUsage;
    }

    public DiscreteStatistics getNurseUsage() {
        return nurseUsage;
    }

    public DiscreteStatistics getAmbulanceAUsage() {
        return ambulanceAUsage;
    }

    public DiscreteStatistics getAmbulanceBUsage() {
        return ambulanceBUsage;
    }

    public DiscreteStatistics getTimeInSystemWalk() {
        return timeInSystemWalk;
    }

    public DiscreteStatistics getTimeInSystemAmbulance() {
        return timeInSystemAmbulance;
    }

    public DiscreteStatistics getWaitEntranceWalk() {
        return waitEntranceWalk;
    }

    public DiscreteStatistics getWaitEntranceAmbulance() {
        return waitEntranceAmbulance;
    }

    public DiscreteStatistics getWaitMedicalWalk() {
        return waitMedicalWalk;
    }

    public DiscreteStatistics getWaitMedicalAmbulance() {
        return waitMedicalAmbulance;
    }

    public DiscreteStatistics getPatientsInWalk() {
        return patientsInWalk;
    }

    public DiscreteStatistics getPatientsInAmbulance() {
        return patientsInAmbulance;
    }

    public DiscreteStatistics getPatientsOutWalk() {
        return patientsOutWalk;
    }

    public DiscreteStatistics getPatientsOutAmbulance() {
        return patientsOutAmbulance;
    }
}