package agents.agentmedicalexam;

import OSPABA.*;
import agents.agentmedicalexam.continualassistants.*;
import simulation.*;
import generators.ContinuousGenerator;
import generators.DiscreteGenerator;
import generators.EmpiricalGenerator;
import intervals.Interval;
import java.util.List;
import java.util.Random;

//meta! id="31"
public class AgentMedicalExam extends OSPABA.Agent
{
    private EmpiricalGenerator medicalExamWalkGenerator;
    private ContinuousGenerator medicalExamAmbulanceCarGenerator;

	public AgentMedicalExam(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);
		init();
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication

        Random seedRandom = ((MySimulation) mySim()).getSeedRandom();

        Interval i1 = new Interval(10, 12, 0.1);
        Interval i2 = new Interval(12, 14, 0.6);
        Interval i3 = new Interval(14, 18, 0.3);

        medicalExamWalkGenerator = new EmpiricalGenerator(seedRandom, List.of(i1, i2, i3), List.of());
        medicalExamAmbulanceCarGenerator = new ContinuousGenerator(seedRandom, 15, 30);
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		new ManagerMedicalExam(Id.managerMedicalExam, mySim(), this);
		new ProcessMedicalExam(Id.processMedicalExam, mySim(), this);
		addOwnMessage(Mc.medicalExamination);
	}
	//meta! tag="end"


    public EmpiricalGenerator getMedicalExamWalkGenerator() {
        return medicalExamWalkGenerator;
    }

    public ContinuousGenerator getMedicalExamAmbulanceCarGenerator() {
        return medicalExamAmbulanceCarGenerator;
    }
}