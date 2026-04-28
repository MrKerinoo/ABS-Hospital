package simulation;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Config {

    public static final BufferedImage IMG_PATIENT;
    public static final BufferedImage IMG_NURSE;
    public static final BufferedImage IMG_DOCTOR;
    public static final BufferedImage IMG_BACKGROUND;

    public static final String IMG_PATIENT_PATH = "img/IMG_PATIENT.png";
    public static final String IMG_NURSE_PATH = "img/IMG_NURSE.png";
    public static final String IMG_DOCTOR_PATH = "img/IMG_DOCTOR.png";
    public static final String IMG_BACKGROUND_PATH = "img/IMG_BACKGROUND.png";

    static {
        try {
            IMG_PATIENT = ImageIO.read(new File(Config.IMG_PATIENT_PATH));
            IMG_NURSE = ImageIO.read(new File(Config.IMG_NURSE_PATH));
            IMG_DOCTOR = ImageIO.read(new File(Config.IMG_DOCTOR_PATH));
            IMG_BACKGROUND = ImageIO.read(new File(Config.IMG_BACKGROUND_PATH));
        } catch (IOException e) {
            throw new RuntimeException("Nenacitalo obrazky pre animator. Skontroluj priecinok 'img/'.", e);
        }
    }
}