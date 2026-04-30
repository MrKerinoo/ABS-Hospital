package entities;

public class Ambulance {

    private int id;
    private char type;

    private double x;
    private double y;

    private Patient patient;
    private Nurse nurse;
    private Doctor doctor;

    public Ambulance(int id, char type, double x, double y) {
        this.id = id;
        this.type = type;
        this.x = x;
        this.y = y;
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

        int ambAstartX = 191;
        int ambBStartX = 879;
        int startY = 117;
        int gap = 120;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getXDoor() {
        return x + 40;
    }

    public double getYDoor() {
        return y + 202;
    }

    public double getXInside() {
        return x + 35;
    }

    public double getYInside() {
        return y + 90;
    }

    public double getXAfterExam() {
        return x + 75;
    }

    public double getYAfterExam() {
        return y + 150;
    }

    public double getXDoctor() {
        return x + 5;
    }

    public double getYDoctor() {
        return y + 7;
    }

    public double getXNurse() {
        return x + 5;
    }

    public double getYNurse() {
        return y + 90;
    }

    public double getXPatient() {
        return x + 66;
    }

    public double getYPatient() {
        return y + 52;
    }
}
