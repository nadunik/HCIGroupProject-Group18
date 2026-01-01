import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class CustomerRegistrationForm extends JFrame {
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField addressField;
    private JTextField cityField;
    private JTextField postalCodeField;
    private JTextField phoneNumberField;
    private JTextField emailField;
    private JTextArea noteArea;
    private JButton registerButton;

    public CustomerRegistrationForm() {
        setTitle("Customer Registration");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create a panel for the content
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Create a panel for the form
        JPanel formPanel = new JPanel(new GridLayout(8, 2, 10, 10));
        formPanel.setBackground(Color.WHITE);

        // Define font for the labels and fields
        Font font = new Font("Helvetica", Font.PLAIN, 14);

        // Create components for bio details
        firstNameField = createTextField("Enter your first name", font);
        lastNameField = createTextField("Enter your last name", font);
        addressField = createTextField("Enter your address", font);
        cityField = createTextField("Enter your city", font);
        postalCodeField = createTextField("Enter your postal code", font);
        phoneNumberField = createTextField("Enter your phone number (e.g., +123456789)", font);
        emailField = createTextField("Enter your email address", font);
        noteArea = new JTextArea();
        noteArea.setFont(font);
        noteArea.setRows(3);
        noteArea.setLineWrap(true);
        JScrollPane noteScrollPane = new JScrollPane(noteArea);
        noteArea.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (noteArea.getText().equals("Enter any additional notes")) {
                    noteArea.setText("");
                    noteArea.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (noteArea.getText().isEmpty()) {
                    noteArea.setText("Enter any additional notes");
                    noteArea.setForeground(Color.GRAY);
                }
            }
        });

        // Add components to form panel
        formPanel.add(new JLabel("First Name:"));
        formPanel.add(firstNameField);
        formPanel.add(new JLabel("Last Name:"));
        formPanel.add(lastNameField);
        formPanel.add(new JLabel("Address:"));
        formPanel.add(addressField);
        formPanel.add(new JLabel("City:"));
        formPanel.add(cityField);
        formPanel.add(new JLabel("Postal Code:"));
        formPanel.add(postalCodeField);
        formPanel.add(new JLabel("Phone Number:"));
        formPanel.add(phoneNumberField);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);
        formPanel.add(new JLabel("Note:"));
        formPanel.add(noteScrollPane);

        // Create a panel for the button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        registerButton = new JButton("Register");
        registerButton.setBackground(new Color(52, 152, 219));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFont(font);
        buttonPanel.add(registerButton);

        // Add form panel and button panel to content panel
        contentPanel.add(formPanel, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add content panel to the frame
        add(contentPanel);

        // Add action listener to register button
        registerButton.addActionListener(e -> registerCustomer());

        pack();
        setSize(700, 500);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JTextField createTextField(String placeholder, Font font) {
        JTextField textField = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty() && !isFocusOwner()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setColor(Color.GRAY);
                    g2.setFont(font);
                    g2.drawString(placeholder, getInsets().left, g.getFontMetrics().getMaxAscent() + getInsets().top);
                    g2.dispose();
                }
            }
        };
        textField.setFont(font);
        textField.setForeground(Color.BLACK);
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setText(placeholder);
                    textField.setForeground(Color.GRAY);
                }
            }
        });
        textField.setText(placeholder);
        textField.setForeground(Color.GRAY);
        return textField;
    }

    private void registerCustomer() {
        // Retrieve field values
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String address = addressField.getText();
        String city = cityField.getText();
        String postalCode = postalCodeField.getText();
        String phoneNumber = phoneNumberField.getText();
        String email = emailField.getText();
        String note = noteArea.getText();

        // Perform customer registration process
        // For demonstration, we'll simply print the collected information
        System.out.println("First Name: " + firstName);
        System.out.println("Last Name: " + lastName);
        System.out.println("Address: " + address);
        System.out.println("City: " + city);
        System.out.println("Postal Code: " + postalCode);
        System.out.println("Phone Number: " + phoneNumber);
        System.out.println("Email: " + email);
        System.out.println("Note: " + note);

        // Show success message
        JOptionPane.showMessageDialog(this, "Customer registered successfully!");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CustomerRegistrationForm());
    }
}
