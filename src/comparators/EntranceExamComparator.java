package comparators;

import java.util.Comparator;
import OSPABA.MessageForm;
import entities.Patient;
import simulation.MyMessage;

public class EntranceExamComparator implements Comparator<MessageForm> {
    @Override
    public int compare(MessageForm m1, MessageForm m2) {
        MyMessage msg1 = (MyMessage) m1;
        MyMessage msg2 = (MyMessage) m2;

        Patient p1 = msg1.getPatient();
        Patient p2 = msg2.getPatient();

        if (p1.isWithAmbulance() != p2.isWithAmbulance()) {
            return p1.isWithAmbulance() ? -1 : 1;
        }

        return Double.compare(p1.getArrivalTime(), p2.getArrivalTime());
    }
}