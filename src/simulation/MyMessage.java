package simulation;

import OSPABA.*;
import entities.Ambulance;
import entities.Doctor;
import entities.Nurse;
import entities.Patient;

public class MyMessage extends OSPABA.MessageForm
{
    private Patient patient;
    private Nurse nurse;
    private Doctor doctor;
    private Ambulance ambulance;

	public MyMessage(Simulation mySim)
	{
		super(mySim);
	}

	public MyMessage(MyMessage original)
	{
		super(original);
        patient = original.getPatient();
        nurse = original.getNurse();
        doctor = original.getDoctor();
        ambulance = original.getAmbulance();
		// copy() is called in superclass
	}

	@Override
	public MessageForm createCopy()
	{
		return new MyMessage(this);
	}

	@Override
	protected void copy(MessageForm message)
	{
		super.copy(message);
		MyMessage original = (MyMessage)message;
		// Copy attributes
	}

    public Patient getPatient() {
        return patient;
    }

    public Nurse getNurse() {
        return nurse;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public Ambulance getAmbulance() {
        return ambulance;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public void setNurse(Nurse nurse) {
        this.nurse = nurse;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public void setAmbulance(Ambulance ambulance) {
        this.ambulance = ambulance;
    }
}