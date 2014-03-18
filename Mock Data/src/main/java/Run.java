import javax.swing.*;

/**
 * Created by FezzFest on 18/03/14.
 */
public class Run {
    public static void main(String[] args) {
        // Set Nimbus Look & Feel
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Show main UI
        new Main();
    }
}
