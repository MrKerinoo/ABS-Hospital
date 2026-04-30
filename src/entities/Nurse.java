package entities;

import OSPAnimator.AnimImageItem;
import simulation.Config;

public class Nurse extends AnimImageItem {

    private int id;
    private Ambulance ambulance;

    public Nurse(int id) {
        super(Config.IMG_NURSE, 55, 55);
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
