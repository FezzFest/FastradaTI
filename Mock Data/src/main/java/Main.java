import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Created by FezzFest on 17/03/14.
 */
public class Main extends JFrame {
    private JPanel panel;
    private JButton startBtn;
    private JButton stopBtn;
    private JLabel textLabel;
    private JLabel packetsLabel;
    private Thread thread;

    public Main() {
        // Set title
        super("Mock Data");

        // Panel
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Buttons
        addElements();
        addListeners();
        showUserInterface();
    }

    private void addElements() {
        // Buttons
        startBtn = new JButton();
        stopBtn = new JButton();
        stopBtn.setEnabled(false);

        // Labels
        textLabel = new JLabel("Sent packets:");
        packetsLabel = new JLabel();

        // Button images
        try {
            Image startImg = ImageIO.read(getClass().getResource("start.png"));
            startBtn.setIcon(new ImageIcon(startImg));

            Image stopImg = ImageIO.read(getClass().getResource("stop.png"));
            stopBtn.setIcon(new ImageIcon(stopImg));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Positioning
        GridBagConstraints startConstr = new GridBagConstraints();
        startConstr.gridx = 0;
        startConstr.gridy = 0;
        panel.add(startBtn, startConstr);

        GridBagConstraints stopConstr = new GridBagConstraints();
        stopConstr.gridx = 1;
        stopConstr.gridy = 0;
        panel.add(stopBtn, stopConstr);

        GridBagConstraints textConstr = new GridBagConstraints();
        textConstr.anchor = GridBagConstraints.LINE_START;
        textConstr.gridx = 0;
        textConstr.gridy = 1;
        textConstr.insets = new Insets(10, 0, 0, 0);
        panel.add(textLabel, textConstr);

        GridBagConstraints packetsConstr = new GridBagConstraints();
        packetsConstr.anchor = GridBagConstraints.LINE_END;
        packetsConstr.gridx = 1;
        packetsConstr.gridy = 1;
        packetsConstr.insets = new Insets(10, 0, 0, 0);
        panel.add(packetsLabel, packetsConstr);
    }

    private void addListeners() {
        startBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Start thread
                thread = new Thread() {
                    public void run() {
                        RunFromMemory runFromMemory = new RunFromMemory(packetsLabel);
                        runFromMemory.start();
                    }
                };
                thread.start();

                // Disable button
                startBtn.setEnabled(false);
                stopBtn.setEnabled(true);
            }
        });

        stopBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Stop thread
                if (thread != null) {
                    thread.interrupt();
                }

                // Enable button
                stopBtn.setEnabled(false);
                startBtn.setEnabled(true);
            }
        });
    }

    private void showUserInterface() {
        add(panel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
