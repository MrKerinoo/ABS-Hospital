package comparators;

import OSPABA.MessageForm;
import simulation.Mc;
import simulation.MyMessage;
import entities.Patient;
import java.util.Comparator;

public class ResourceComparator implements Comparator<MessageForm> {

    @Override
    public int compare(MessageForm m1, MessageForm m2) {
        MyMessage msg1 = (MyMessage) m1;
        MyMessage msg2 = (MyMessage) m2;
        Patient p1 = msg1.getPatient();
        Patient p2 = msg2.getPatient();

        // 1. KROK: Priorita podľa typu požiadavky (Lekárske > Vstupné)
        // Ak majú rôzne kódy, ten s MedicalResources musí byť prvý (-1)
        if (msg1.code() != msg2.code()) {
            if (msg1.code() == Mc.requestMedicalResources) return -1;
            if (msg2.code() == Mc.requestMedicalResources) return 1;
        }

        // 2. KROK: Ak sú rovnakého typu, riešime ich špecifickú prioritu
        if (msg1.code() == Mc.requestMedicalResources) {
            // Obaja idú na lekárske -> porovnaj priority 1, 2, 3, 4, 5
            if (p1.getPriority() != p2.getPriority()) {
                return Integer.compare(p1.getPriority(), p2.getPriority());
            }
        } else {
            // Obaja idú na vstupné -> porovnaj či je to sanitka
            if (p1.isWithAmbulance() != p2.isWithAmbulance()) {
                return p1.isWithAmbulance() ? -1 : 1;
            }
        }

        // 3. KROK: Ak je všetko rovnaké (napr. obaja P1 lekárske), rozhoduje čas príchodu do RADU
        double time1 = (msg1.code() == Mc.requestMedicalResources) ? p1.getMedicalQueueArrivalTime() : p1.getEntranceQueueArrivalTime();
        double time2 = (msg2.code() == Mc.requestMedicalResources) ? p2.getMedicalQueueArrivalTime() : p2.getEntranceQueueArrivalTime();

        return Double.compare(time1, time2);
    }
}