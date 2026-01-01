import javax.swing.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.util.FPSAnimator;
import java.awt.*;
import java.awt.event.*;

public class RoundTable extends GLJPanel implements GLEventListener, MouseMotionListener {
    private FPSAnimator animator;
    private int lastX, lastY;
    private float rotateX = 0.0f;
    private float rotateY = 0.0f;
    private JButton tableColorButton;
    private JButton scaleUpButton;
    private JButton scaleDownButton;
    private JTextField radiusField;
    private Color tableColor = Color.blue;
    private float[] normalizedTableColor;
    private float radius = 4.0f; // Default radius
    private float tableHeight = 0.2f; // Thickness of the table top
    private float scaleX = 1.0f;
    private float scaleY = 1.0f;
    private float scaleZ = 1.0f;
    private JPanel buttonPanel;

    public RoundTable() {
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
    private void initButtonPanel(JPanel buttonPanel) {
        // Color Picker Button
        tableColorButton = new JButton("Change Table Color");
        tableColorButton.addActionListener(this::changeTableColor);
        tableColorButton.setBackground(Color.orange); // Change button color
        tableColorButton.setForeground(Color.white); // Change text color

        // Scaling Buttons
        JPanel scalingPanel = new JPanel(new GridLayout(2, 1));
        scaleUpButton = new JButton("Scale Up");
        scaleUpButton.addActionListener(e -> {
            scaleX += 0.1f;
            scaleY += 0.1f;
            scaleZ += 0.1f;
            repaint();
        });
        scaleUpButton.setBackground(Color.green); // Change button color
        scaleUpButton.setForeground(Color.white); // Change text color
        scaleDownButton = new JButton("Scale Down");
        scaleDownButton.addActionListener(e -> {
            scaleX -= 0.1f;
            scaleY -= 0.1f;
            scaleZ -= 0.1f;
            repaint();
        });
        scaleDownButton.setBackground(Color.red); // Change button color
        scaleDownButton.setForeground(Color.white); // Change text color
        scalingPanel.add(scaleUpButton);
        scalingPanel.add(scaleDownButton);

        // Adjust Table Button
        JPanel adjustTablePanel = new JPanel();
        JLabel radiusLabel = new JLabel("Radius:");
        radiusField = new JTextField(5);
        JButton adjustTableButton = new JButton("Adjust Table");
        adjustTableButton.setBackground(Color.blue); // Change button color
        adjustTableButton.setForeground(Color.white); // Change text color
        adjustTableButton.addActionListener(this::submitForm);
        adjustTablePanel.add(radiusLabel);
        adjustTablePanel.add(radiusField);
        adjustTablePanel.add(adjustTableButton);

        // Add components to button panel
        buttonPanel.add(tableColorButton);
        buttonPanel.add(scalingPanel);
        buttonPanel.add(adjustTablePanel);
    }


    private void initColorPicker(JPanel buttonPanel) {
        tableColorButton = new JButton("Change Table Color");
        tableColorButton.addActionListener(this::changeTableColor);
        buttonPanel.add(tableColorButton);
    }

    private void changeTableColor(ActionEvent e) {
        Color defaultColor = tableColor != null ? tableColor : Color.BLUE;
        Color newTableColor = JColorChooser.showDialog(null, "Choose Table Color", defaultColor);
        if (newTableColor != null) {
            tableColor = newTableColor;
            normalizeColors();
            repaint();
        }
    }

    private void normalizeColors() {
        normalizedTableColor = normalizeColor(tableColor);
    }

    private float[] normalizeColor(Color color) {
        return new float[]{color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f};
    }

    private void initScalingButtons() {
        scaleUpButton = new JButton("Scale Up");
        scaleUpButton.addActionListener(e -> {
            scaleX += 0.1f;
            scaleY += 0.1f;
            scaleZ += 0.1f;
            repaint();
        });

        scaleDownButton = new JButton("Scale Down");
        scaleDownButton.addActionListener(e -> {
            scaleX -= 0.1f;
            scaleY -= 0.1f;
            scaleZ -= 0.1f;
            repaint();
        });

        JPanel scalingPanel = new JPanel();
        scalingPanel.setLayout(new GridLayout(2, 1));
        scalingPanel.add(scaleUpButton);
        scalingPanel.add(scaleDownButton);

        buttonPanel.add(scalingPanel);
    }

    private void initTableDimensionsForm(JPanel buttonPanel) {
        JLabel radiusLabel = new JLabel("Radius:");
        radiusField = new JTextField(5);

        JButton adjustTableButton = new JButton("Adjust Table");

        JPanel formPanel = new JPanel(new GridLayout(0, 2));
        formPanel.add(radiusLabel);
        formPanel.add(radiusField);
        formPanel.add(adjustTableButton);

        buttonPanel.add(formPanel);

        adjustTableButton.addActionListener(this::submitForm);
    }

    private void submitForm(ActionEvent e) {
        try {
            float newRadius = Float.parseFloat(radiusField.getText());
            radius = newRadius;
            repaint();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid numerical value for the radius.");
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

    private void drawTable(GL2 gl) {
        int segments = 36; // Number of segments to approximate the circle

        // Draw table-top (cylinder)
        gl.glBegin(GL2.GL_QUAD_STRIP);
        for (int i = 0; i <= segments; i++) {
            float theta = (float) (i * 2 * Math.PI / segments);
            float x = (float) (radius * Math.cos(theta));
            float z = (float) (radius * Math.sin(theta));
            gl.glVertex3f(x, tableHeight / 2, z);
            gl.glVertex3f(x, -tableHeight / 2, z);
        }
        gl.glEnd();

        // Fill up the sides of the table top
        gl.glBegin(GL2.GL_QUADS);
        for (int i = 0; i < segments; i++) {
            float theta1 = (float) (i * 2 * Math.PI / segments);
            float theta2 = (float) ((i + 1) * 2 * Math.PI / segments);
            float x1 = (float) (radius * Math.cos(theta1));
            float z1 = (float) (radius * Math.sin(theta1));
            float x2 = (float) (radius * Math.cos(theta2));
            float z2 = (float) (radius * Math.sin(theta2));

            // Side 1
            gl.glVertex3f(x1, tableHeight / 2, z1);
            gl.glVertex3f(x2, tableHeight / 2, z2);
            gl.glVertex3f(x2, -tableHeight / 2, z2);
            gl.glVertex3f(x1, -tableHeight / 2, z1);

            // Side 2
            gl.glVertex3f(x2, tableHeight / 2, z2);
            gl.glVertex3f(x1, tableHeight / 2, z1);
            gl.glVertex3f(x1, -tableHeight / 2, z1);
            gl.glVertex3f(x2, -tableHeight / 2, z2);
        }
        gl.glEnd();

        // Draw top face
        gl.glBegin(GL2.GL_POLYGON);
        for (int i = 0; i <= segments; i++) {
            float theta = (float) (i * 2 * Math.PI / segments);
            float x = (float) (radius * Math.cos(theta));
            float z = (float) (radius * Math.sin(theta));
            gl.glVertex3f(x, tableHeight / 2, z);
        }
        gl.glEnd();

        // Draw bottom face
        gl.glBegin(GL2.GL_POLYGON);
        for (int i = 0; i <= segments; i++) {
            float theta = (float) (i * 2 * Math.PI / segments);
            float x = (float) (radius * Math.cos(theta));
            float z = (float) (radius * Math.sin(theta));
            gl.glVertex3f(x, -tableHeight / 2, z);
        }
        gl.glEnd();

        // Draw table legs
        float legWidth = 0.2f;
        float legHeight = -4.0f;
        float legOffset = 2f; // Offset to ensure legs are slightly inside the table top

        float legPosition = radius - legOffset;

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
            JFrame frame = new JFrame("3D Round Table Renderer");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);

            RoundTable tableRenderer = new RoundTable();
            frame.getContentPane().add(tableRenderer);

            frame.setVisible(true);
        });
    }
}


