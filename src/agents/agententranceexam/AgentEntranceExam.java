package agents.agententranceexam;

import OSPABA.*;
import agents.agententranceexam.continualassistants.*;
import simulation.*;
import generators.DiscreteGenerator;
import generators.EmpiricalGenerator;
import intervals.Interval;
import java.util.List;
import java.util.Random;

//meta! id="30"
public class AgentEntranceExam extends OSPABA.Agent
{
    private EmpiricalGenerator entranceExamWalkGenerator;
    private DiscreteGenerator entranceExamAmbulanceCarGenerator;
    private EmpiricalGenerator priorityWalkGenerator;
    private EmpiricalGenerator priorityAmbulanceCarGenerator;

	public AgentEntranceExam(int id, Simulation mySim, Agent parent)
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

        Interval i1 = new Interval(3, 5, 0.6);
        Interval i2 = new Interval(5, 9, 0.4);

        entranceExamWalkGenerator = new EmpiricalGenerator(seedRandom, List.of(i1, i2), List.of());
        entranceExamAmbulanceCarGenerator = new DiscreteGenerator(seedRandom, 4, 8);

        Interval w1 = new Interval(1, 2, 0.1);
        Interval w2 = new Interval(2, 3, 0.2);
        Interval w3 = new Interval(3, 4, 0.15);
        Interval w4 = new Interval(4, 5, 0.25);
        Interval w5 = new Interval(5, 6, 0.3);

        priorityWalkGenerator = new EmpiricalGenerator(seedRandom, List.of(), List.of(w1,w2,w3,w4,w5));

        Interval a1 = new Interval(1, 2, 0.1);
        Interval a2 = new Interval(2, 3, 0.2);
        Interval a3 = new Interval(3, 4, 0.15);
        Interval a4 = new Interval(4, 5, 0.25);
        Interval a5 = new Interval(5, 6, 0.3);

        priorityAmbulanceCarGenerator = new EmpiricalGenerator(seedRandom, List.of(), List.of(a1,a2,a3,a4,a5));
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		new ManagerEntranceExam(Id.managerEntranceExam, mySim(), this);
		new ProcessEntranceExam(Id.processEntranceExam, mySim(), this);
		addOwnMessage(Mc.entranceExamination);
	}
	//meta! tag="end"


    public EmpiricalGenerator getEntranceExamWalkGenerator() {
        return entranceExamWalkGenerator;
    }

    public DiscreteGenerator getEntranceExamAmbulanceCarGenerator() {
        return entranceExamAmbulanceCarGenerator;
    }

    public EmpiricalGenerator getPriorityWalkGenerator() {
        return priorityWalkGenerator;
    }

    public EmpiricalGenerator getPriorityAmbulanceCarGenerator() {
        return priorityAmbulanceCarGenerator;
    }
}