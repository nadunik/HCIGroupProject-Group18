import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RoundTable2D extends JPanel implements MouseMotionListener {
    private int lastX, lastY;
    private float rotateX = 0.0f;
    private float rotateY = 0.0f;
    private JButton scaleUpButton;
    private JButton scaleDownButton;
    private JButton switchViewButton;
    private JTextField widthField;
    private JTextField heightField;
    private float tableWidth = 200.0f;
    private float tableHeight = 100.0f;
    private float scaleX = 1.0f;
    private float scaleY = 1.0f;
    private String currentView = "Top"; // Default view

    public RoundTable2D() {
        setBackground(Color.white);
        setLayout(new BorderLayout());

        initScalingButtons();
        initSwitchViewButton();
        initTableDimensionsForm();
        addMouseMotionListener(this);
    }

    private void initScalingButtons() {
        JPanel scalingPanel = new JPanel();
        scalingPanel.setLayout(new GridLayout(2, 1));

        scaleUpButton = new JButton("Scale Up");
        scaleUpButton.addActionListener(e -> {
            scaleX += 0.1f;
            scaleY += 0.1f;
            repaint();
        });
        scalingPanel.add(scaleUpButton);

        scaleDownButton = new JButton("Scale Down");
        scaleDownButton.addActionListener(e -> {
            scaleX -= 0.1f;
            scaleY -= 0.1f;
            repaint();
        });
        scalingPanel.add(scaleDownButton);

        add(scalingPanel, BorderLayout.WEST);
    }

    private void initSwitchViewButton() {
        switchViewButton = new JButton("Switch View");
        switchViewButton.addActionListener(e -> {
            switch (currentView) {
                case "Top":
                    rotateX = 0;
                    rotateY = 0;
                    currentView = "Right";
                    break;
                case "Right":
                    rotateX = 90;
                    rotateY = 0;
                    currentView = "Front";
                    break;
                case "Front":
                    rotateX = 0;
                    rotateY = 90;
                    currentView = "Top";
                    break;
            }
            repaint();
        });
        add(switchViewButton, BorderLayout.NORTH);
    }

    private void initTableDimensionsForm() {
        JLabel widthLabel = new JLabel("Width:");
        widthField = new JTextField(5);
        widthField.setText(String.valueOf(tableWidth));

        JLabel heightLabel = new JLabel("Height:");
        heightField = new JTextField(5);
        heightField.setText(String.valueOf(tableHeight));

        JButton adjustTableButton = new JButton("Adjust Table");
        adjustTableButton.addActionListener(this::submitForm);

        JPanel formPanel = new JPanel(new GridLayout(0, 2));
        formPanel.add(widthLabel);
        formPanel.add(widthField);
        formPanel.add(heightLabel);
        formPanel.add(heightField);
        formPanel.add(adjustTableButton);

        add(formPanel, BorderLayout.SOUTH);
    }

    private void submitForm(ActionEvent e) {
        try {
            float newWidth = Float.parseFloat(widthField.getText());
            float newHeight = Float.parseFloat(heightField.getText());
            tableWidth = newWidth;
            tableHeight = newHeight;
            repaint();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numerical values for width and height.");
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g.create();

        g2d.setColor(Color.blue);
        g2d.translate(getWidth() / 2, getHeight() / 2);
        g2d.scale(scaleX, scaleY);
        g2d.rotate(Math.toRadians(rotateX));
        g2d.rotate(Math.toRadians(rotateY));
        drawTable(g2d);

        g2d.dispose();
    }

    private void drawTable(Graphics2D g2d) {
        int tableX = (int) (-tableWidth / 2);
        int tableY = (int) (-tableHeight / 2);
        g2d.fillRect(tableX, tableY, (int) tableWidth, (int) tableHeight);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        int dx = x - lastX;
        int dy = y - lastY;
        rotateX += dy;
        rotateY += dx;
        lastX = x;
        lastY = y;
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        lastX = e.getX();
        lastY = e.getY();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("2D Round Table Renderer");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);

            RoundTable2D tableRenderer = new RoundTable2D();
            frame.getContentPane().add(tableRenderer);

            frame.setVisible(true);
        });
    }
}
