package entities;

public class Nurse {

    private int id;
    private Ambulance ambulance;

    public Nurse(int id) {
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
