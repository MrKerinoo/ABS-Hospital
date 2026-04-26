package entities;

public class Patient {

    private int id;
    private int priority;
    private double arrivalTime;
    private boolean withAmbulance;

    public Patient (int id) {
        this.id = id;
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

    @Override
    public String toString() {
        return String.format("Pacient [ID: %d | Priorita: %d | Príchod: %.2f | Sanitka: %b]",
                id, priority, arrivalTime, withAmbulance);
    }
}
