package app;

import auth.ProVehicleLogin;
import data.SampleDataLoader;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class MainClass {

    public static void main(String[] args) {
        SampleDataLoader.loadSampleData();
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ignored) {
        }

        SwingUtilities.invokeLater(() -> new ProVehicleLogin().setVisible(true));
    }
}