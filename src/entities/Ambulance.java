package entities;

public class Ambulance {

    private int id;
    private char type;

    private Patient patient;
    private Nurse nurse;
    private Doctor doctor;

    public Ambulance(int id, char type) {
        this.id = id;
        this.type = type;
    }

    public int getId() { return id; }

    public Patient getPatient() {
        return patient;
    }

    public Nurse getNurse() {
        return nurse;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public char getType() {
        return type;
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

    public void setType(char type) {
        this.type = type;
    }
}
