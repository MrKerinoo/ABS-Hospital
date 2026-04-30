package entities;

import OSPAnimator.AnimImageItem;
import simulation.Config;

public class Doctor extends AnimImageItem {

    private int id;
    private Ambulance ambulance;

    public Doctor(int id) {
        super(Config.IMG_DOCTOR, 55, 55);
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
