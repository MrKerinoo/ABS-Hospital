package agents.agentenvironment;

import OSPABA.*;
import simulation.*;
import agents.agentenvironment.continualassistants.*;
import generators.PatientGenerator;
import generators.ErlangGenerator;
import generators.ExponentialGenerator;
import statistics.DiscreteStatistics;
import java.util.Random;

//meta! id="2"
public class AgentEnvironment extends OSPABA.Agent
{
    private int patientsIn;
    private int patientsOut;
    private int patientsInWalk;
    private int patientsInAmbulance;
    private int patientsOutWalk;
    private int patientsOutAmbulance;

    private DiscreteStatistics timeInSystem;
    private DiscreteStatistics timeInSystemWalk;
    private DiscreteStatistics timeInSystemAmbulance;

    private ExponentialGenerator patientWalkGenerator;
    private ErlangGenerator patientAmbulanceCarGenerator;
    private PatientGenerator patientGenerator;

    // PATIENT GENERATOR

	public AgentEnvironment(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);
		init();
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication

        this.patientsIn = 0;
        this.patientsOut = 0;
        this.patientsInWalk = 0;
        this.patientsInAmbulance = 0;
        this.patientsOutWalk = 0;
        this.patientsOutAmbulance = 0;

        this.timeInSystem = new DiscreteStatistics();
        this.timeInSystemWalk = new DiscreteStatistics();
        this.timeInSystemAmbulance = new DiscreteStatistics();

        Random seedRandom = ((MySimulation) mySim()).getSeedRandom();

        this.patientWalkGenerator = new ExponentialGenerator(seedRandom, 572.6253, 2.0);
        this.patientAmbulanceCarGenerator = new ErlangGenerator(seedRandom, 8, 0.0210, -13.5462);
        this.patientGenerator = new PatientGenerator();
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		new ManagerEnvironment(Id.managerEnvironment, mySim(), this);
		new SchedulerWalk(Id.schedulerWalk, mySim(), this);
		new SchedulerAmbulanceCar(Id.schedulerAmbulanceCar, mySim(), this);
		new SchedulerWarmup(Id.schedulerWarmup, mySim(), this);
		new SchedulerWarmupFind(Id.schedulerWarmupFind, mySim(), this);
		addOwnMessage(Mc.patientExit);
		addOwnMessage(Mc.noticeInit);
	}
	//meta! tag="end"

    public void resetLocalStats() {
        this.patientsIn = 0;
        this.patientsOut = 0;
        this.patientsInWalk = 0;
        this.patientsInAmbulance = 0;
        this.patientsOutWalk = 0;
        this.patientsOutAmbulance = 0;

        this.timeInSystem.reset();
        this.timeInSystemWalk.reset();
        this.timeInSystemAmbulance.reset();
    }

    public ExponentialGenerator getPatientWalkGenerator() {
        return patientWalkGenerator;
    }

    public ErlangGenerator getPatientAmbulanceCarGenerator() {
        return patientAmbulanceCarGenerator;
    }

    public PatientGenerator getPatientGenerator() {
        return patientGenerator;
    }

    public int getPatientsIn() {
        return patientsIn;
    }

    public int getPatientsOut() {
        return patientsOut;
    }

    public int getPatientsInWalk() {
        return patientsInWalk;
    }

    public int getPatientsInAmbulance() {
        return patientsInAmbulance;
    }

    public int getPatientsOutWalk() {
        return patientsOutWalk;
    }

    public int getPatientsOutAmbulance() {
        return patientsOutAmbulance;
    }

    public DiscreteStatistics getTimeInSystem() {
        return timeInSystem;
    }

    public DiscreteStatistics getTimeInSystemWalk() {
        return timeInSystemWalk;
    }

    public DiscreteStatistics getTimeInSystemAmbulance() {
        return timeInSystemAmbulance;
    }

    public void setPatientsIn(int patientsIn) {
        this.patientsIn = patientsIn;
    }

    public void setPatientsOut(int patientsOut) {
        this.patientsOut = patientsOut;
    }

    public void setPatientsInWalk(int patientsInWalk) {
        this.patientsInWalk = patientsInWalk;
    }

    public void setPatientsInAmbulance(int patientsInAmbulance) {
        this.patientsInAmbulance = patientsInAmbulance;
    }

    public void setPatientsOutWalk(int patientsOutWalk) {
        this.patientsOutWalk = patientsOutWalk;
    }

    public void setPatientsOutAmbulance(int patientsOutAmbulance) {
        this.patientsOutAmbulance = patientsOutAmbulance;
    }
}