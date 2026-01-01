import javax.swing.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.util.FPSAnimator;
import java.awt.*;
import java.awt.event.*;

public class StoolRenderer extends GLJPanel implements GLEventListener, MouseMotionListener {
    private FPSAnimator animator;
    private int lastX, lastY;
    private float rotateX = 0.0f;
    private float rotateY = 0.0f;
    private JButton stoolColorButton;
    private JButton scaleUpButton;
    private JButton scaleDownButton;
    private JTextField radiusField;
    private JTextField heightField;
    private Color stoolColor = Color.green;
    private float[] normalizedStoolColor;
    private float radius = 3.0f;
    private float height = 15.0f;
    private float scaleX = 1.0f;
    private float scaleY = 1.0f;
    private float scaleZ = 1.0f;
    private JPanel buttonPanel;
    float legHeight = height / 2;
    float legOffset = 0.0f; // Adjust the offset to bring the legs closer to each other
    float seatOffset = 0f;
    public StoolRenderer() {
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
        initStoolDimensionsForm(buttonPanel);
    }

    private void initColorPicker(JPanel buttonPanel) {
        stoolColorButton = new JButton("Change Stool Color");
        stoolColorButton.addActionListener(this::changeStoolColor);
        buttonPanel.add(stoolColorButton);
    }

    private void changeStoolColor(ActionEvent e) {
        Color defaultColor = stoolColor != null ? stoolColor : Color.GREEN;
        Color newStoolColor = JColorChooser.showDialog(null, "Choose Stool Color", defaultColor);
        if (newStoolColor != null) {
            stoolColor = newStoolColor;
            normalizeColors();
            repaint();
        }
    }

    private void normalizeColors() {
        normalizedStoolColor = normalizeColor(stoolColor);
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

    private void initStoolDimensionsForm(JPanel buttonPanel) {
        JLabel radiusLabel = new JLabel("Radius:");
        JLabel heightLabel = new JLabel("Height:");

        radiusField = new JTextField(5);
        radiusField.setText(String.valueOf(radius));

        heightField = new JTextField(5);
        heightField.setText(String.valueOf(height));

        JButton adjustStoolButton = new JButton("Adjust Stool");

        JPanel formPanel = new JPanel(new GridLayout(0, 2));
        formPanel.add(radiusLabel);
        formPanel.add(radiusField);
        formPanel.add(heightLabel);
        formPanel.add(heightField);
        formPanel.add(adjustStoolButton);

        buttonPanel.add(formPanel);

        adjustStoolButton.addActionListener(this::submitForm);
    }

    private void submitForm(ActionEvent e) {
        try {
            float newRadius = Float.parseFloat(radiusField.getText());
            float newHeight = Float.parseFloat(heightField.getText());

            radius = newRadius;
            height = newHeight;

            repaint();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numerical values for the stool dimensions.");
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

        gl.glColor3f(normalizedStoolColor[0], normalizedStoolColor[1], normalizedStoolColor[2]);

        gl.glTranslatef(0.0f, 0.0f, -20.0f);
        gl.glRotatef(rotateX, 1.0f, 0.0f, 0.0f);
        gl.glRotatef(rotateY, 0.0f, 1.0f, 0.0f);
        gl.glScalef(scaleX, scaleY, scaleZ);

        drawStool(gl);

        gl.glFlush();
    }

    private void drawStool(GL2 gl) {
        int segments = 36; // Number of segments to approximate the circle
        float legWidth = 0.2f;
        float seatDepth = 1.0f;
         // Adjust the offset to reduce the gap between seat and legs


        // Draw stool top (circle)
        gl.glPushMatrix();
        gl.glTranslatef(0, height / 2, 0);
        gl.glBegin(GL2.GL_POLYGON);
        for (int i = 0; i < segments; i++) {
            float theta = (float) (i * 2 * Math.PI / segments);
            float x = (float) (radius * Math.cos(theta));
            float z = (float) (radius * Math.sin(theta));
            gl.glVertex3f(x, seatDepth / 2, z);
        }
        gl.glEnd();
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-radius + legWidth / 2 - legOffset, seatOffset - legHeight, -radius + legWidth / 2 - legOffset);
        drawLeg(gl, legWidth, height - seatOffset, legWidth);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(-radius + legWidth / 2 - legOffset, seatOffset - legHeight, radius - legWidth / 2 + legOffset);
        drawLeg(gl, legWidth, height - seatOffset, legWidth);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(radius - legWidth / 2 + legOffset, seatOffset - legHeight, -radius + legWidth / 2 - legOffset);
        drawLeg(gl, legWidth, height - seatOffset, legWidth);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(radius - legWidth / 2 + legOffset, seatOffset - legHeight, radius - legWidth / 2 + legOffset);
        drawLeg(gl, legWidth, height - seatOffset, legWidth);
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

    private void gluCylinder(GL2 gl, float base, float top, float height, int slices) {
        float[] x = new float[slices];
        float[] z = new float[slices];
        float angle;
        float step = (float) (2.0 * Math.PI / slices);

        // Define the circle in the XZ plane
        for (int i = 0; i < slices; i++) {
            angle = i * step;
            x[i] = (float) Math.cos(angle);
            z[i] = (float) Math.sin(angle);
        }

        // Draw the cylinder body
        gl.glBegin(GL2.GL_QUAD_STRIP);
        for (int i = 0; i < slices; i++) {
            gl.glVertex3f(x[i] * base, 0, z[i] * base);
            gl.glVertex3f(x[i] * top, height, z[i] * top);
        }
        gl.glEnd();

        // Draw the bottom circle
        gl.glBegin(GL2.GL_POLYGON);
        for (int i = 0; i < slices; i++) {
            gl.glVertex3f(x[i] * base, 0, z[i] * base);
        }
        gl.glEnd();

        // Draw the top circle
        gl.glBegin(GL2.GL_POLYGON);
        for (int i = 0; i < slices; i++) {
            gl.glVertex3f(x[i] * top, height, z[i] * top);
        }
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
            JFrame frame = new JFrame("3D Stool Renderer");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);

            StoolRenderer renderer = new StoolRenderer();
            frame.getContentPane().add(renderer);

            frame.setVisible(true);
        });
    }
}
