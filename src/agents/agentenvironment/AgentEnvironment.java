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
    private DiscreteStatistics timeInSystem;

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
        this.timeInSystem = new DiscreteStatistics();

        Random seedRandom = ((MySimulation) mySim()).getSeedRandom();

        this.patientWalkGenerator = new ExponentialGenerator(seedRandom, 572.6253);
        this.patientAmbulanceCarGenerator = new ErlangGenerator(seedRandom, 8, 0.0210);
        this.patientGenerator = new PatientGenerator();
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		new ManagerEnvironment(Id.managerEnvironment, mySim(), this);
		new SchedulerWalk(Id.schedulerWalk, mySim(), this);
		new SchedulerAmbulanceCar(Id.schedulerAmbulanceCar, mySim(), this);
		addOwnMessage(Mc.patientExit);
		addOwnMessage(Mc.noticeInit);
	}
	//meta! tag="end"

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

    public DiscreteStatistics getTimeInSystem() {
        return timeInSystem;
    }

    public void setPatientsIn(int patientsIn) {
        this.patientsIn = patientsIn;
    }

    public void setPatientsOut(int patientsOut) {
        this.patientsOut = patientsOut;
    }

    public void setTimeInSystem(DiscreteStatistics timeInSystem) {
        this.timeInSystem = timeInSystem;
    }
}