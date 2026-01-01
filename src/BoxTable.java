import javax.swing.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.util.FPSAnimator;
import java.awt.*;
import java.awt.event.*;

public class BoxTable extends GLJPanel implements GLEventListener, MouseMotionListener {
    private FPSAnimator animator;
    private int lastX, lastY;
    private float rotateX = 0.0f;
    private float rotateY = 0.0f;
    private JButton tableColorButton;
    private JButton scaleUpButton;
    private JButton scaleDownButton;
    private JTextField dimensionField;
    private Color tableColor = Color.blue;
    private float[] normalizedTableColor;
    private float tableDimension = 6.0f;
    private float tableHeight = 1.0f;
    private float scaleX = 1.0f;
    private float scaleY = 1.0f;
    private float scaleZ = 1.0f;
    private JPanel buttonPanel;

    public BoxTable() {
        super(new GLCapabilities(GLProfile.getDefault()));
        setBackground(Color.white);
        setLayout(new BorderLayout());
        buttonPanel = new JPanel(new GridLayout(0, 1));
        add(buttonPanel, BorderLayout.WEST);

        addGLEventListener(this);
        addMouseMotionListener(this);
        animator = new FPSAnimator(this, 60);
        animator.start();
        initColorPicker(buttonPanel);
        normalizeColors();
        initScalingButtons();
        initTableDimensionsForm(buttonPanel);
    }

    // Initialize the button to change table color
    private void initColorPicker(JPanel buttonPanel) {
        tableColorButton = new JButton("Change Table Color");
        tableColorButton.addActionListener(this::changeTableColor);
        tableColorButton.setBackground(Color.lightGray); // Set background color
        tableColorButton.setBorder(BorderFactory.createLineBorder(Color.darkGray)); // Add border
        buttonPanel.add(tableColorButton);
    }

    // Method to handle changing the table color
    private void changeTableColor(ActionEvent e) {
        Color defaultColor = tableColor != null ? tableColor : Color.BLUE;
        Color newTableColor = JColorChooser.showDialog(null, "Choose Table Color", defaultColor);
        if (newTableColor != null) {
            tableColor = newTableColor;
            normalizeColors();
            repaint();
        }
    }

    // Normalize the table color
    private void normalizeColors() {
        normalizedTableColor = normalizeColor(tableColor);
    }

    // Helper method to normalize color values
    private float[] normalizeColor(Color color) {
        return new float[]{color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f};
    }

    // Initialize buttons for scaling the table
    private void initScalingButtons() {
        scaleUpButton = new JButton("Scale Up");
        scaleUpButton.addActionListener(e -> {
            scaleX += 0.1f;
            scaleY += 0.1f;
            scaleZ += 0.1f;
            repaint();
        });
        scaleUpButton.setBackground(Color.lightGray); // Set background color
        scaleUpButton.setBorder(BorderFactory.createLineBorder(Color.darkGray)); // Add border

        scaleDownButton = new JButton("Scale Down");
        scaleDownButton.addActionListener(e -> {
            scaleX -= 0.1f;
            scaleY -= 0.1f;
            scaleZ -= 0.1f;
            repaint();
        });
        scaleDownButton.setBackground(Color.lightGray); // Set background color
        scaleDownButton.setBorder(BorderFactory.createLineBorder(Color.darkGray)); // Add border

        JPanel scalingPanel = new JPanel();
        scalingPanel.setLayout(new GridLayout(2, 1));
        scalingPanel.add(scaleUpButton);
        scalingPanel.add(scaleDownButton);

        buttonPanel.add(scalingPanel);
    }

    // Initialize form to adjust table dimensions
    private void initTableDimensionsForm(JPanel buttonPanel) {
        JLabel dimensionLabel = new JLabel("Dimension:");
        dimensionField = new JTextField(5);

        JButton adjustTableButton = new JButton("Adjust Table");
        adjustTableButton.setBackground(Color.lightGray); // Set background color
        adjustTableButton.setBorder(BorderFactory.createLineBorder(Color.darkGray)); // Add border

        JPanel formPanel = new JPanel(new GridLayout(0, 2));
        formPanel.add(dimensionLabel);
        formPanel.add(dimensionField);
        formPanel.add(adjustTableButton);

        buttonPanel.add(formPanel);

        adjustTableButton.addActionListener(this::submitForm);
    }

    // Method to handle submitting the table dimension form
    private void submitForm(ActionEvent e) {
        try {
            float newDimension = Float.parseFloat(dimensionField.getText());
            tableDimension = newDimension;
            repaint();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid numerical value for the table dimension.");
        }
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glEnable(GL2.GL_DEPTH_TEST);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();

        gl.glColor3f(normalizedTableColor[0], normalizedTableColor[1], normalizedTableColor[2]);

        gl.glTranslatef(0.0f, 0.0f, -20.0f);
        gl.glRotatef(rotateX, 1.0f, 0.0f, 0.0f);
        gl.glRotatef(rotateY, 0.0f, 1.0f, 0.0f);
        gl.glScalef(scaleX, scaleY, scaleZ);

        drawTable(gl);

        gl.glFlush();
    }

    // Draw the table
    private void drawTable(GL2 gl) {
        float halfDimension = tableDimension / 2;
        float legWidth = 1.0f;
        float legHeight = -4.0f;
        float legOffset = 0.9f; // Offset to ensure legs are slightly inside the table top

        // Draw table top
        gl.glBegin(GL2.GL_QUADS);
        // Front face
        gl.glVertex3f(-halfDimension, 0, halfDimension);
        gl.glVertex3f(halfDimension, 0, halfDimension);
        gl.glVertex3f(halfDimension, tableHeight, halfDimension);
        gl.glVertex3f(-halfDimension, tableHeight, halfDimension);
        // Back face
        gl.glVertex3f(-halfDimension, 0, -halfDimension);
        gl.glVertex3f(-halfDimension, tableHeight, -halfDimension);
        gl.glVertex3f(halfDimension, tableHeight, -halfDimension);
        gl.glVertex3f(halfDimension, 0, -halfDimension);
        // Left face
        gl.glVertex3f(-halfDimension, 0, halfDimension);
        gl.glVertex3f(-halfDimension, tableHeight, halfDimension);
        gl.glVertex3f(-halfDimension, tableHeight, -halfDimension);
        gl.glVertex3f(-halfDimension, 0, -halfDimension);
        // Right face
        gl.glVertex3f(halfDimension, 0, halfDimension);
        gl.glVertex3f(halfDimension, 0, -halfDimension);
        gl.glVertex3f(halfDimension, tableHeight, -halfDimension);
        gl.glVertex3f(halfDimension, tableHeight, halfDimension);
        // Top face
        gl.glVertex3f(-halfDimension, tableHeight, halfDimension);
        gl.glVertex3f(halfDimension, tableHeight, halfDimension);
        gl.glVertex3f(halfDimension, tableHeight, -halfDimension);
        gl.glVertex3f(-halfDimension, tableHeight, -halfDimension);
        // Bottom face
        gl.glVertex3f(-halfDimension, 0, halfDimension);
        gl.glVertex3f(-halfDimension, 0, -halfDimension);
        gl.glVertex3f(halfDimension, 0, -halfDimension);
        gl.glVertex3f(halfDimension, 0, halfDimension);
        gl.glEnd();

        // Draw table legs
        float legPosition = halfDimension - legOffset;

        gl.glPushMatrix();
        gl.glTranslatef(-legPosition, 0, legPosition);
        drawLeg(gl, legWidth, legHeight, legWidth);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-legPosition, 0, -legPosition);
        drawLeg(gl, legWidth, legHeight, legWidth);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(legPosition, 0, legPosition);
        drawLeg(gl, legWidth, legHeight, legWidth);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(legPosition, 0, -legPosition);
        drawLeg(gl, legWidth, legHeight, legWidth);
        gl.glPopMatrix();
    }

    // Draw a table leg
    private void drawLeg(GL2 gl, float width, float height, float length) {
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex3f(-width / 2, 0, length / 2);
        gl.glVertex3f(width / 2, 0, length / 2);
        gl.glVertex3f(width / 2, height, length / 2);
        gl.glVertex3f(-width / 2, height, length / 2);

        gl.glVertex3f(-width / 2, 0, -length / 2);
        gl.glVertex3f(-width / 2, height, -length / 2);
        gl.glVertex3f(width / 2, height, -length / 2);
        gl.glVertex3f(width / 2, 0, -length / 2);

        gl.glVertex3f(-width / 2, 0, length / 2);
        gl.glVertex3f(-width / 2, height, length / 2);
        gl.glVertex3f(-width / 2, height, -length / 2);
        gl.glVertex3f(-width / 2, 0, -length / 2);

        gl.glVertex3f(width / 2, 0, length / 2);
        gl.glVertex3f(width / 2, 0, -length / 2);
        gl.glVertex3f(width / 2, height, -length / 2);
        gl.glVertex3f(width / 2, height, length / 2);

        gl.glVertex3f(-width / 2, height, length / 2);
        gl.glVertex3f(width / 2, height, length / 2);
        gl.glVertex3f(width / 2, height, -length / 2);
        gl.glVertex3f(-width / 2, height, -length / 2);
        gl.glEnd();
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        double aspect = (double) width / (double) height;
        gl.glFrustum(-1.0, 1.0, -aspect, aspect, 1.0, 100.0);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
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
            JFrame frame = new JFrame("3D Table Renderer");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);

            BoxTable tableRenderer = new BoxTable();
            frame.getContentPane().add(tableRenderer);

            frame.setVisible(true);
        });
    }
}
