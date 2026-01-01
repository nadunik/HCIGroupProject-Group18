import org.jdesktop.swingx.*;
import org.jdesktop.swingx.prompt.PromptSupport;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class LoginFrame extends JXFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JXButton loginButton;
    private JXButton signUpButton;

    public LoginFrame() {
        super("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false); // Disable resizing

        // Create a panel for the content
        JXPanel contentPanel = new JXPanel(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        contentPanel.setBackground(Color.WHITE); // Set background color

        // Create a panel for the form
        JXPanel formPanel = new JXPanel(new GridLayout(2, 1, 20, 20));
        formPanel.setBackground(Color.WHITE); // Set background color
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20)); // Add padding

        // Create username components
        JXPanel usernamePanel = new JXPanel(new FlowLayout(FlowLayout.CENTER));
        JXLabel usernameLabel = new JXLabel("Username:");
        usernameField = new JTextField(20);
        usernameField.setBackground(Color.WHITE); // Set background color
        PromptSupport.setPrompt("Enter your username", usernameField);
        setFloatingEffect(usernameField); // Set floating effect
        usernamePanel.add(usernameLabel);
        usernamePanel.add(usernameField);

        // Create password components
        JXPanel passwordPanel = new JXPanel(new FlowLayout(FlowLayout.CENTER));
        JXLabel passwordLabel = new JXLabel("Password:");
        passwordField = new JPasswordField(20);
        passwordField.setBackground(Color.WHITE); // Set background color
        PromptSupport.setPrompt("Enter your password", passwordField);
        setFloatingEffect(passwordField); // Set floating effect
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordField);

        // Add username and password panels to the form panel
        formPanel.add(usernamePanel);
        formPanel.add(passwordPanel);

        // Create a panel for the buttons
        JXPanel buttonPanel = new JXPanel(new FlowLayout(FlowLayout.CENTER));
        loginButton = new JXButton("Login");
        signUpButton = new JXButton("Sign Up");
        loginButton.setBackground(Color.WHITE); // Set background color
        signUpButton.setBackground(Color.WHITE); // Set background color
        buttonPanel.add(loginButton);
        buttonPanel.add(signUpButton);

        // Add form panel and button panel to the content panel
        contentPanel.add(formPanel, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add content panel to the frame
        add(contentPanel);

        // Customize button colors and font
        loginButton.setForeground(new Color(33, 150, 243)); // Blue color
        loginButton.setFont(new Font("Arial", Font.BOLD, 14)); // Set button font
        signUpButton.setForeground(new Color(76, 175, 80)); // Green color
        signUpButton.setFont(new Font("Arial", Font.BOLD, 14)); // Set button font

        // Add action listeners
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Handle login logic here
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                // Dummy check, replace with actual login logic
                if (username.equals("admin") && password.equals("admin")) {
                    showLoginSuccessMessage();
                    // Open FurnitureWorld window on successful login
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            new FurnitureWorld();
                        }
                    });
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(LoginFrame.this, "Invalid username or password!", "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        signUpButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Open the sign-up form
                new SignUpForm();
            }
        });

        // Set frame visibility and size
        pack(); // pack the frame to ensure components are sized properly
        setLocationRelativeTo(null); // Center the frame on the screen
        setVisible(true);
    }

    // Method to set floating effect for text field
    private void setFloatingEffect(JTextField textField) {
        Border border = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(33, 150, 243), 2), // Blue color
                BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Add padding
        textField.setBorder(border);
    }

    // Method to show login success message
    private void showLoginSuccessMessage() {
        JXPanel messagePanel = new JXPanel(new BorderLayout());
        messagePanel.setBackground(new Color(46, 125, 50)); // Green color
        messagePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JXLabel messageLabel = new JXLabel("Login successful!");
        messageLabel.setForeground(Color.WHITE);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 16));

        messagePanel.add(messageLabel, BorderLayout.CENTER);

        JOptionPane.showMessageDialog(this, messagePanel, "Login Success", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new LoginFrame();
            }
        });
    }
}

