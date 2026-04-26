import gui.MainFrame;
import simulation.MySimulation;

public class Main {

    public static void main(String[] args) {
        MySimulation core = new MySimulation();

        javax.swing.SwingUtilities.invokeLater(() -> new MainFrame(core));
    }
}