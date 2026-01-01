import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class FurnitureWorld extends JFrame {
    // Constants for button appearance
    private static final Color BUTTON_TEXT_COLOR = Color.BLACK;
    private static final Color BUTTON_BACKGROUND_COLOR = Color.WHITE;
    private static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 14);
    private static final int BUTTON_WIDTH = 250;
    private static final int BUTTON_HEIGHT = 60;

    // Constructor
    public FurnitureWorld() {
        // Setup UI components and listeners
        setupUI();
        setupListeners();
        pack();
        setLocationRelativeTo(null); // Center the window
        setVisible(true); // Make the window visible
    }

    // Setup UI components
    private void setupUI() {
        setTitle("Welcome to Furniture World");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close operation
        setPreferredSize(new Dimension(1000, 600)); // Preferred window size
        getContentPane().setBackground(Color.WHITE); // Set background color
        setLayout(new BorderLayout()); // Use BorderLayout for main layout

        // Create and add top panel with welcome message
        add(createTopPanel(), BorderLayout.NORTH);

        // Create and add button panel with main functionality buttons
        add(createButtonPanel(), BorderLayout.CENTER);
    }

    // Create and return the top panel with welcome message
    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(); // Create new JPanel
        JLabel welcomeLabel = new JLabel("Welcome to Furniture World"); // Create welcome label
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24)); // Set font for welcome label
        topPanel.add(welcomeLabel); // Add welcome label to top panel
        topPanel.setBackground(Color.WHITE); // Set background color of top panel
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add empty border for spacing
        return topPanel; // Return top panel
    }

    // Create and return the button panel with main functionality buttons
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new GridLayout(0, 2, 10, 10)); // Use GridLayout with 2 columns for two vertical rows
        buttonPanel.setBackground(Color.WHITE); // Set background color of button panel
        GridBagConstraints gbc = new GridBagConstraints(); // Create GridBagConstraints for button layout
        gbc.gridx = 0; // Set initial gridx value
        gbc.gridy = 0; // Set initial gridy value
        gbc.insets = new Insets(20, 30, 20, 30); // Set insets for spacing between buttons

            // Add main functionality buttons to button panel
        addButton(buttonPanel, "Login", "Click to login to your account", LoginFrame.class, Color.RED);
        addButton(buttonPanel, "New Customer Registration", "Register as a new customer", CustomerRegistrationForm.class, Color.BLUE);
        addButton(buttonPanel, "New Chair", "Explore new chair designs", ChairRenderer.class, Color.GREEN);
        addButton(buttonPanel, "New Table", "Discover new table designs", BoxTable.class, Color.ORANGE);
        addButton(buttonPanel, "Round Table", "View round table designs", RoundTable.class, Color.MAGENTA);
        addButton(buttonPanel, "Stool", "Browse stool designs", StoolRenderer.class, Color.CYAN);
        addButton(buttonPanel, "Dinner Table", "Browse dinner table designs", TableRenderer.class, Color.PINK);
        addButton(buttonPanel, "Designer Sign Up Form", "Sign up as a designer", SignUpForm.class, Color.YELLOW);

        return buttonPanel; // Return button panel
    }

    // Helper method to create and add a button to the button panel
    private void addButton(JPanel panel, String text, String tooltip, Class<?> clazz, Color borderColor) {
        JButton button = new JButton(text); // Create new JButton with specified text
        button.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT)); // Set preferred size for button
        button.setForeground(BUTTON_TEXT_COLOR); // Set foreground color (text color) for button
        button.setFont(BUTTON_FONT); // Set font for button text
        button.setFocusPainted(false); // Remove focus border
        button.setBackground(BUTTON_BACKGROUND_COLOR); // Set background color
        button.setBorder(new CompoundBorder(new LineBorder(borderColor, 3), new EmptyBorder(5, 15, 5, 15))); // Set border with color
        button.setToolTipText(tooltip); // Set tooltip for button
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ((JButton)evt.getSource()).setBorder(new CompoundBorder(new LineBorder(Color.BLACK, 3), new EmptyBorder(5, 15, 5, 15))); // Change border color on mouse hover
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                ((JButton)evt.getSource()).setBorder(new CompoundBorder(new LineBorder(borderColor, 3), new EmptyBorder(5, 15, 5, 15))); // Restore border color on mouse exit
            }
        });
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    JFrame frame = new JFrame(clazz.getSimpleName());
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    frame.setSize(800, 600);
                    frame.setLocationRelativeTo(null);

                    JComponent component = (JComponent) clazz.getDeclaredConstructor().newInstance();
                    frame.getContentPane().add(component);

                    frame.setVisible(true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        panel.add(button); // Add button to button panel
    }

    // Setup listeners for window events
    private void setupListeners() {
        addWindowListener(new WindowAdapter() { // Add window listener
            @Override
            public void windowClosing(WindowEvent e) { // Handle window closing event
                performCleanup(); // Call method to perform cleanup tasks
            }
        });
    }

    // Method to perform cleanup tasks before closing the application
    private void performCleanup() {
        // Perform cleanup tasks here
        System.out.println("Performing cleanup tasks..."); // Example: Print message to console
    }

    // Main method
    public static void main(String[] args) {
        SwingUtilities.invokeLater(FurnitureWorld::new); // Create and show GUI on EDT
    }
}
