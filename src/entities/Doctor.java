package entities;

public class Doctor {

    private int id;
    private Ambulance ambulance;

    public Doctor(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Ambulance getAmbulance() {
        return ambulance;
    }

    public void setAmbulance(Ambulance ambulance) {
        this.ambulance = ambulance;
    }
}
