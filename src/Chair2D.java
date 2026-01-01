import javax.swing.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLJPanel;
import java.awt.*;
//import java.awt.event.*;

public class Chair2D extends GLJPanel implements GLEventListener {
    private float seatWidth = 100;
    private float seatHeight = 20;
    private float backrestWidth = 20;
    private float backrestHeight = 80;

    public Chair2D() {
        super(new GLCapabilities(GLProfile.getDefault()));
        setBackground(Color.white);
        setPreferredSize(new Dimension(400, 400));
        addGLEventListener(this);
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        // Initialization code
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        // Cleanup code
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
        gl.glLoadIdentity();

        // Draw chair seat
        gl.glColor3f(0.0f, 0.0f, 1.0f); // Blue color
        gl.glRectf(-seatWidth / 2, -seatHeight / 2, seatWidth / 2, seatHeight / 2);

        // Draw backrest
        gl.glColor3f(1.0f, 0.0f, 0.0f); // Red color
        gl.glRectf(-seatWidth / 2 - backrestWidth, -seatHeight / 2, -seatWidth / 2, -seatHeight / 2 + backrestHeight);

        // Draw legs
        float legWidth = 15;
        float legHeight = 20;
        gl.glColor3f(0.0f, 0.0f, 0.0f); // Black color
        // Front legs
        gl.glRectf(-seatWidth / 2, -seatHeight / 2 - legHeight, -seatWidth / 2 + legWidth, -seatHeight / 2);
        gl.glRectf(seatWidth / 2 - legWidth, -seatHeight / 2 - legHeight, seatWidth / 2, -seatHeight / 2);
        // Back legs
        gl.glRectf(-seatWidth / 2 - backrestWidth, -seatHeight / 2 - legHeight, -seatWidth / 2 - backrestWidth + legWidth, -seatHeight / 2 + backrestHeight);
        gl.glRectf(-seatWidth / 2, -seatHeight / 2 - legHeight, -seatWidth / 2 + legWidth, -seatHeight / 2 + backrestHeight);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(-width / 2, width / 2, -height / 2, height / 2, -1, 1);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("2D Chair Renderer");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            Chair2D chair2D = new Chair2D();

            JButton scaleUpButton = new JButton("Scale Up");
            scaleUpButton.addActionListener(e -> {
                chair2D.seatWidth *= 1.1;
                chair2D.seatHeight *= 1.1;
                chair2D.repaint();
            });

            JButton scaleDownButton = new JButton("Scale Down");
            scaleDownButton.addActionListener(e -> {
                chair2D.seatWidth *= 0.9;
                chair2D.seatHeight *= 0.9;
                chair2D.repaint();
            });

            JPanel buttonPanel = new JPanel();
            buttonPanel.add(scaleUpButton);
            buttonPanel.add(scaleDownButton);

            frame.add(buttonPanel, BorderLayout.SOUTH);
            frame.add(chair2D, BorderLayout.CENTER);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
