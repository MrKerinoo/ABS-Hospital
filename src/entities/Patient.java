package entities;

import OSPAnimator.AnimImageItem;
import simulation.Config;

public class Patient extends AnimImageItem {

    private int id;
    private int priority;
    private double arrivalTime;
    private boolean withAmbulance;

    private double entranceQueueArrivalTime;
    private double medicalQueueArrivalTime;

    private Ambulance visitedAmbulance;

    public Patient (int id) {
        super(Config.IMG_PATIENT, 45, 45);
        setZIndex(1);
        this.id = id;
        this.priority = 0;
    }

    public int getId() {
        return id;
    }

    public int getPriority() {
        return priority;
    }

    public double getArrivalTime() {
        return arrivalTime;
    }

    public boolean isWithAmbulance() {
        return withAmbulance;
    }

    public Ambulance getVisitedAmbulance() {
        return visitedAmbulance;
    }

    public double getEntranceQueueArrivalTime() {
        return entranceQueueArrivalTime;
    }

    public double getMedicalQueueArrivalTime() {
        return medicalQueueArrivalTime;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setArrivalTime(double arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public void setWithAmbulance(boolean withAmbulance) {
        this.withAmbulance = withAmbulance;
    }

    public void setVisitedAmbulance(Ambulance visitedAmbulance) {
        this.visitedAmbulance = visitedAmbulance;
    }

    public void setEntranceQueueArrivalTime(double entranceQueueArrivalTime) {
        this.entranceQueueArrivalTime = entranceQueueArrivalTime;
    }

    public void setMedicalQueueArrivalTime(double medicalQueueArrivalTime) {
        this.medicalQueueArrivalTime = medicalQueueArrivalTime;
    }

    @Override
    public String toString() {
        return String.format("Pacient [ID: %d | Priorita: %d | Príchod: %.2f | Sanitka: %b]",
                id, priority, arrivalTime, withAmbulance);
    }
}
