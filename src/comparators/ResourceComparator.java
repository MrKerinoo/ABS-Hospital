package comparators;

import OSPABA.MessageForm;
import simulation.Mc;
import simulation.MyMessage;
import entities.Patient;
import simulation.MySimulation;

import java.util.Comparator;

/**
 * Kód vytvorený s pomocou AI, zdokumentované v kapitole 1.4
 */
public class ResourceComparator implements Comparator<MessageForm> {

    @Override
    public int compare(MessageForm m1, MessageForm m2) {
        MyMessage msg1 = (MyMessage) m1;
        MyMessage msg2 = (MyMessage) m2;
        Patient p1 = msg1.getPatient();
        Patient p2 = msg2.getPatient();

        if (msg1.code() != msg2.code()) {
            if (msg1.code() == Mc.requestMedicalResources) return -1;
            if (msg2.code() == Mc.requestMedicalResources) return 1;
        }

        if (msg1.code() == Mc.requestMedicalResources) {
            if (p1.getPriority() != p2.getPriority()) {
                return Integer.compare(p1.getPriority(), p2.getPriority());
            }

            MySimulation sim = (MySimulation) msg1.mySim();
            if (sim.getStrategyType() == MySimulation.STRATEGY_AMBULANCE_PREFERENCE) {
                if (p1.isWithAmbulance() != p2.isWithAmbulance()) {
                    return p1.isWithAmbulance() ? -1 : 1;
                }
            }
        } else {
            if (p1.isWithAmbulance() != p2.isWithAmbulance()) {
                return p1.isWithAmbulance() ? -1 : 1;
            }
        }

        double time1 = (msg1.code() == Mc.requestMedicalResources) ? p1.getMedicalQueueArrivalTime() : p1.getEntranceQueueArrivalTime();
        double time2 = (msg2.code() == Mc.requestMedicalResources) ? p2.getMedicalQueueArrivalTime() : p2.getEntranceQueueArrivalTime();

        return Double.compare(time1, time2);
    }
}