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

        boolean m1Medical = msg1.code() == Mc.requestMedicalResources;
        boolean m2Medical = msg2.code() == Mc.requestMedicalResources;

        if (m1Medical && !m2Medical) return -1;
        if (!m1Medical && m2Medical) return 1;

        return Integer.compare(msg1.getPatient().getPriority(), msg2.getPatient().getPriority());
    }
}