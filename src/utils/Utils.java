package utils;

import OSPAnimator.Anim;
import OSPAnimator.AnimImageItem;
import java.awt.geom.Point2D;

public class Utils {

    /**
     * Kód vytvorený s pomocou AI, zdokumentované v kapitole 1.1
     */
    public static void moveAlongPath(AnimImageItem entity, double totalDuration, double currentTime, Point2D... points) {
        if (entity == null || points.length == 0 || totalDuration <= 0) return;

        double lastX;
        double lastY;

        // 1. Zistenie aktuálnej pozície konca všetkých animácií
        Anim lastAnim = entity.getPreviousAnim(Double.MAX_VALUE, false);
        if (lastAnim != null && lastAnim.getPath() != null) {
            lastX = lastAnim.getPath().lastPoint().getX();
            lastY = lastAnim.getPath().lastPoint().getY();
        } else {
            lastX = entity.getPosX();
            lastY = entity.getPosY();
            // Ak je mimo, skoč na prvý bod
            if (lastX < -1000000) {
                lastX = points[0].getX();
                lastY = points[0].getY();
                entity.setPosition(lastX, lastY);
            }
        }

        // 2. Výpočet celkovej dĺžky trasy (sumár segmentov)
        double totalDist = 0;
        double[] segmentDists = new double[points.length];
        double tempX = lastX;
        double tempY = lastY;

        for (int i = 0; i < points.length; i++) {
            double dx = points[i].getX() - tempX;
            double dy = points[i].getY() - tempY;
            segmentDists[i] = Math.sqrt(dx * dx + dy * dy);
            totalDist += segmentDists[i];
            tempX = points[i].getX();
            tempY = points[i].getY();
        }

        if (totalDist == 0) return;

        // 3. Plánovanie segmentov
        // Dôležité: startTime si uložíme raz a neustále k nemu pripočítavame odpracovaný čas
        double currentAnimStartTime = Math.max(entity.getEndTimeOfAllAnims(), currentTime);

        for (int i = 0; i < points.length; i++) {
            if (segmentDists[i] <= 0) continue; // Preskoč body, kde už stojíme

            // Čas segmentu je pomerná časť z totalDuration podľa dĺžky úseku
            double segmentDuration = (segmentDists[i] / totalDist) * totalDuration;

            entity.moveTo(
                    currentAnimStartTime,
                    segmentDuration,
                    points[i].getX(),
                    points[i].getY()
            );

            // Posunieme štart pre ďalší segment hneď za tento
            currentAnimStartTime += segmentDuration;
        }
    }

    public static Point2D p2d(double x, double y) {
        return new Point2D.Double(x, y);
    }

    public static String formatSimTime(double seconds) {
        int days = (int) (seconds / 86400) + 1;

        int h = (int) ((seconds % 86400) / 3600);

        int m = (int) ((seconds % 3600) / 60);
        int s = (int) (seconds % 60);

        return String.format("Deň %d  %02d:%02d:%02d", days, h, m, s);
    }
}