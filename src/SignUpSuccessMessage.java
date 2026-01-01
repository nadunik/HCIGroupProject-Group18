import javax.swing.*;
import java.awt.*;

public class SignUpSuccessMessage extends JFrame {
    public SignUpSuccessMessage() {
        setTitle("Sign Up Success");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(400, 200));

        JLabel messageLabel = new JLabel("<html><div style='text-align: center;'>Sign up process is successfully done.<br>You can now use the login form to login using your ID and password.</div></html>");
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        add(messageLabel, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new SignUpSuccessMessage();
            }
        });
    }
}
