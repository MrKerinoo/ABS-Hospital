package comparators;

import OSPABA.MessageForm;
import simulation.Mc;
import simulation.MyMessage;

import java.util.Comparator;

public class ResourceComparator implements Comparator<MessageForm> {
    @Override
    public int compare(MessageForm m1, MessageForm m2) {
        MyMessage msg1 = (MyMessage) m1;
        MyMessage msg2 = (MyMessage) m2;

        int codeCompare = Integer.compare(msg2.code(), msg1.code());
        if (codeCompare != 0) return codeCompare;

        int priorityCompare = Integer.compare(
                msg1.getPatient().getPriority(),
                msg2.getPatient().getPriority()
        );

        if (priorityCompare != 0) {
            return priorityCompare;
        }

        return Double.compare(msg1.getPatient().getArrivalTime(), msg2.getPatient().getArrivalTime());
    }
}