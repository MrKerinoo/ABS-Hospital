package utils;

import OSPAnimator.Anim;
import OSPAnimator.AnimImageItem;
import java.awt.geom.Point2D;
import java.util.List;

public class Utils {
    /**
     * Kód vytvorený s pomocou AI, zdokumentované v kapitole 1.1
     */
    public static void moveAlongPath(AnimImageItem entity, double totalDuration, double currentTime, Point2D... points) {
        if (entity == null || points.length == 0 || totalDuration <= 0) return;

        double lastX;
        double lastY;

        Anim lastAnim = entity.getPreviousAnim(Double.MAX_VALUE, false);
        if (lastAnim != null && lastAnim.getPath() != null) {
            lastX = lastAnim.getPath().lastPoint().getX();
            lastY = lastAnim.getPath().lastPoint().getY();
        } else {
            lastX = entity.getPosX();
            lastY = entity.getPosY();
            if (lastX < -1000000) {
                lastX = points[0].getX();
                lastY = points[0].getY();
                entity.setPosition(lastX, lastY);
            }
        }

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

        double currentAnimStartTime = Math.max(entity.getEndTimeOfAllAnims(), currentTime);

        for (int i = 0; i < points.length; i++) {
            if (segmentDists[i] <= 0) continue;

            double segmentDuration = (segmentDists[i] / totalDist) * totalDuration;

            entity.moveTo(
                    currentAnimStartTime,
                    segmentDuration,
                    points[i].getX(),
                    points[i].getY()
            );

            currentAnimStartTime += segmentDuration;
        }
    }

    public static Point2D p2d(double x, double y) {
        return new Point2D.Double(x, y);
    }


    /**
     * Kód vytvorený s pomocou AI, zdokumentované v kapitole 1.2
     */
    public static String formatSimTime(double seconds) {
        int days = (int) (seconds / 86400) + 1;

        int h = (int) ((seconds % 86400) / 3600);

        int m = (int) ((seconds % 3600) / 60);
        int s = (int) (seconds % 60);

        return String.format("Deň %d  %02d:%02d:%02d", days, h, m, s);
    }

    /**
     * Kód vytvorený s pomocou AI, zdokumentované v kapitole 1.2
     */
    public static String formatTime(double seconds) {
        if (Double.isNaN(seconds) || Double.isInfinite(seconds) || seconds <= 0) return "00:00:00";
        int h = (int)(seconds / 3600);
        int m = (int)((seconds % 3600) / 60);
        int s = (int)(seconds % 60);
        return String.format("%02d:%02d:%02d", h, m, s);
    }

    /**
     * Kód vytvorený s pomocou AI, zdokumentované v kapitole 1.2
     */
    public static String formatNum(double value) {
        if (Double.isNaN(value) || Double.isInfinite(value)) return "0.00";
        return String.format("%.2f", value);
    }

    /**
     * Kód vytvorený s pomocou AI, zdokumentované v kapitole 1.2
     */
    public static String formatIS(List<Double> ci) {
        if (ci.get(0) == 0.0 && ci.get(1) == 0.0) return "<N/A>";
        return String.format("<%.2f, %.2f>", ci.get(0), ci.get(1));
    }

    /**
     * Kód vytvorený s pomocou AI, zdokumentované v kapitole 1.2
     */
    public static String formatISTime(List<Double> ci) {
        if (ci.get(0) == 0.0 && ci.get(1) == 0.0) return "<N/A>";
        return String.format("<%s, %s>", formatTime(ci.get(0)), formatTime(ci.get(1)));
    }

    /**
     * Kód vytvorený s pomocou AI, zdokumentované v kapitole 1.2
     */
    public static String formatPercent(double value) {
        if (Double.isNaN(value) || Double.isInfinite(value)) return "0.00 %";
        return String.format("%.2f %%", value * 100);
    }

    /**
     * Kód vytvorený s pomocou AI, zdokumentované v kapitole 1.2
     */
    public static String formatISPercent(List<Double> ci) {
        if (ci.get(0) == 0.0 && ci.get(1) == 0.0) return "<N/A>";
        return String.format("<%.2f %%, %.2f %%>", ci.get(0) * 100, ci.get(1) * 100);
    }
}