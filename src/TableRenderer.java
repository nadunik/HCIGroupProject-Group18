import javax.swing.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.util.FPSAnimator;
import java.awt.*;
import java.awt.event.*;

public class TableRenderer extends GLJPanel implements GLEventListener, MouseMotionListener {
    private FPSAnimator animator;
    private int lastX, lastY;
    private float rotateX = 0.0f;
    private float rotateY = 0.0f;
    private JButton tableColorButton;
    private JButton scaleUpButton;
    private JButton scaleDownButton;
    private JTextField widthField;
    private JTextField heightField;
    private JTextField lengthField;
    private Color tableColor = Color.blue;
    private float[] normalizedTableColor;
    private float tableWidth = 6.0f;
    private float tableHeight = 1.0f;
    private float tableLength = 4.0f;
    private float scaleX = 1.0f;
    private float scaleY = 1.0f;
    private float scaleZ = 1.0f;
    private JPanel buttonPanel;

    public TableRenderer() {
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
        JLabel widthLabel = new JLabel("Width:");
        JLabel heightLabel = new JLabel("Height:");
        JLabel lengthLabel = new JLabel("Length:");

        widthField = new JTextField(5);
        heightField = new JTextField(5);
        lengthField = new JTextField(5);

        JButton adjustTableButton = new JButton("Adjust Table");

        JPanel formPanel = new JPanel(new GridLayout(0, 2));
        formPanel.add(widthLabel);
        formPanel.add(widthField);
        formPanel.add(heightLabel);
        formPanel.add(heightField);
        formPanel.add(lengthLabel);
        formPanel.add(lengthField);
        formPanel.add(adjustTableButton);

        buttonPanel.add(formPanel);

        adjustTableButton.addActionListener(this::submitForm);
    }

    private void submitForm(ActionEvent e) {
        try {
            float newWidth = Float.parseFloat(widthField.getText());
            float newHeight = Float.parseFloat(heightField.getText());
            float newLength = Float.parseFloat(lengthField.getText());

            tableWidth = newWidth;
            tableHeight = newHeight;
            tableLength = newLength;

            repaint();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numerical values for the table dimensions.");
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


        float halfWidth = tableWidth / 2;
        float halfLength = tableLength / 2;

        // Draw table top
        gl.glBegin(GL2.GL_QUADS);

        // Front face
        gl.glVertex3f(-halfWidth, 0, halfLength);
        gl.glVertex3f(halfWidth, 0, halfLength);
        gl.glVertex3f(halfWidth, tableHeight, halfLength);
        gl.glVertex3f(-halfWidth, tableHeight, halfLength);

        // Back face
        gl.glVertex3f(-halfWidth, 0, -halfLength);
        gl.glVertex3f(-halfWidth, tableHeight, -halfLength);
        gl.glVertex3f(halfWidth, tableHeight, -halfLength);
        gl.glVertex3f(halfWidth, 0, -halfLength);

        // Left face
        gl.glVertex3f(-halfWidth, 0, halfLength);
        gl.glVertex3f(-halfWidth, tableHeight, halfLength);
        gl.glVertex3f(-halfWidth, tableHeight, -halfLength);
        gl.glVertex3f(-halfWidth, 0, -halfLength);

        // Right face
        gl.glVertex3f(halfWidth, 0, halfLength);
        gl.glVertex3f(halfWidth, 0, -halfLength);
        gl.glVertex3f(halfWidth, tableHeight, -halfLength);
        gl.glVertex3f(halfWidth, tableHeight, halfLength);

        // Top face
        gl.glVertex3f(-halfWidth, tableHeight, halfLength);
        gl.glVertex3f(halfWidth, tableHeight, halfLength);
        gl.glVertex3f(halfWidth, tableHeight, -halfLength);
        gl.glVertex3f(-halfWidth, tableHeight, -halfLength);

        // Bottom face
        gl.glVertex3f(-halfWidth, 0, halfLength);
        gl.glVertex3f(-halfWidth, 0, -halfLength);
        gl.glVertex3f(halfWidth, 0, -halfLength);
        gl.glVertex3f(halfWidth, 0, halfLength);

        gl.glEnd();

        // Draw table legs
        float legWidth = 1.0f;
        float legLength = 1.0f;
        float legHeight = -4.0f;

        gl.glPushMatrix();
        gl.glTranslatef(-halfWidth + legWidth / 2, 0, halfLength - legLength / 2);
        drawLeg(gl, legWidth, legHeight, legLength);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(halfWidth - legWidth / 2, 0, halfLength - legLength / 2);
        drawLeg(gl, legWidth, legHeight, legLength);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-halfWidth + legWidth / 2, 0, -halfLength + legLength / 2);
        drawLeg(gl, legWidth, legHeight, legLength);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(halfWidth - legWidth / 2, 0, -halfLength + legLength / 2);
        drawLeg(gl, legWidth, legHeight, legLength);
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
            JFrame frame = new JFrame("3D Table Renderer");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);

            TableRenderer renderer = new TableRenderer();
            frame.getContentPane().add(renderer);

            frame.setVisible(true);
        });
    }
}
