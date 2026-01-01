import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class SignUpForm extends JFrame {
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField designerIdField;
    private JTextField phoneNumberField;
    private JTextField nicField;
    private JTextField emailField;
    private JButton signUpButton;

    public SignUpForm() {
        setTitle("Sign Up");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(400, 800)); // Increased height to accommodate the big "Sign UP" label

        // Define futuristic font
        Font futuristicFont = new Font("Segoe UI", Font.PLAIN, 14);

        JPanel contentPanel = new JPanel(new BorderLayout());

        // Create the "Sign UP" label
        JLabel signUpLabel = new JLabel("Sign UP");
        signUpLabel.setFont(new Font("Arial", Font.BOLD, 36)); // Custom font and size
        signUpLabel.setForeground(new Color(52, 152, 219)); // Custom color
        signUpLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center align the text
        contentPanel.add(signUpLabel, BorderLayout.NORTH); // Add to the top of the content panel

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(Color.WHITE); // Set background color

        // Define text field dimensions
        Dimension textFieldDimension = new Dimension(200, 25);

        // Create GridBagConstraints for flexible layout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel firstNameLabel = new JLabel("First Name:");
        firstNameLabel.setFont(futuristicFont); // Set font
        firstNameField = new JTextField();
        firstNameField.setPreferredSize(textFieldDimension);
        firstNameField.setFont(futuristicFont); // Set font
        firstNameField.setBorder(textFieldBorder()); // Apply border
        formPanel.add(firstNameLabel, gbc);
        formPanel.add(firstNameField, gbc);

        JLabel lastNameLabel = new JLabel("Last Name:");
        lastNameLabel.setFont(futuristicFont); // Set font
        lastNameField = new JTextField();
        lastNameField.setPreferredSize(textFieldDimension);
        lastNameField.setFont(futuristicFont); // Set font
        lastNameField.setBorder(textFieldBorder()); // Apply border
        formPanel.add(lastNameLabel, gbc);
        formPanel.add(lastNameField, gbc);

        JLabel designerIdLabel = new JLabel("Designer ID:");
        designerIdLabel.setFont(futuristicFont); // Set font
        designerIdField = new JTextField();
        designerIdField.setPreferredSize(textFieldDimension);
        designerIdField.setFont(futuristicFont); // Set font
        designerIdField.setBorder(textFieldBorder()); // Apply border
        formPanel.add(designerIdLabel, gbc);
        formPanel.add(designerIdField, gbc);

        JLabel phoneNumberLabel = new JLabel("Phone Number:");
        phoneNumberLabel.setFont(futuristicFont); // Set font
        phoneNumberField = new JTextField();
        phoneNumberField.setPreferredSize(textFieldDimension);
        phoneNumberField.setFont(futuristicFont); // Set font
        phoneNumberField.setBorder(textFieldBorder()); // Apply border
        formPanel.add(phoneNumberLabel, gbc);
        formPanel.add(phoneNumberField, gbc);

        JLabel nicLabel = new JLabel("NIC:");
        nicLabel.setFont(futuristicFont); // Set font
        nicField = new JTextField();
        nicField.setPreferredSize(textFieldDimension);
        nicField.setFont(futuristicFont); // Set font
        nicField.setBorder(textFieldBorder()); // Apply border
        formPanel.add(nicLabel, gbc);
        formPanel.add(nicField, gbc);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(futuristicFont); // Set font
        emailField = new JTextField();
        emailField.setPreferredSize(textFieldDimension);
        emailField.setFont(futuristicFont); // Set font
        emailField.setBorder(textFieldBorder()); // Apply border
        formPanel.add(emailLabel, gbc);
        formPanel.add(emailField, gbc);

        // Customize SignUp button
        signUpButton = new JButton("Sign Up");
        signUpButton.setBackground(new Color(52, 152, 219)); // Set button color
        signUpButton.setForeground(Color.WHITE); // Set text color
        signUpButton.setFont(futuristicFont); // Set font
        signUpButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                signUp();
            }
        });

        // Add SignUp button to buttonPanel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(signUpButton);

        // Add formPanel and buttonPanel to contentPanel
        contentPanel.add(formPanel, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add contentPanel to the frame
        add(contentPanel);

        pack();
        setLocationRelativeTo(null); // Center the frame on the screen
        setVisible(true);
    }

    private Border textFieldBorder() {
        // Set border for text fields
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        );
    }

    private void signUp() {
        // Retrieve field values
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String designerId = designerIdField.getText();
        String phoneNumber = phoneNumberField.getText();
        String nic = nicField.getText();
        String email = emailField.getText();

        // Perform sign-up process (e.g., send data to server, store in database, etc.)
        // For demonstration, we'll simply print the collected information
        System.out.println("First Name: " + firstName);
        System.out.println("Last Name: " + lastName);
        System.out.println("Designer ID: " + designerId);
        System.out.println("Phone Number: " + phoneNumber);
        System.out.println("NIC: " + nic);
        System.out.println("Email: " + email);

        // Show success message (you can replace this with your own logic)
        SignUpSuccessMessage successMessage = new SignUpSuccessMessage();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new SignUpForm();
            }
        });
    }
}
