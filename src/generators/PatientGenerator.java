package generators;

import entities.Patient;

public class PatientGenerator {

    private int lastPatientId;

    public PatientGenerator() {
        this.lastPatientId = 0;
    }

    public Patient generatePatient() {
        Patient patient = new Patient(lastPatientId);

        this.lastPatientId++;

        return patient;
    }
}
