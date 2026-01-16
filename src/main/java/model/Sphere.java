package model;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.awt.GLCanvas;


/*
 * TESTING ONYLYYYY
 */
public class Sphere implements GLEventListener{
    private GLU glu;
    private float rotation = 0.0f;

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        glu = new GLU();
        gl.glEnable(GL.GL_DEPTH_TEST); // Enable depth testing for 3D
        //https://www.tutorialspoint.com/jogl/jogl_lighting.htm
        gl.glEnable(GL2.GL_LIGHTING);   
        gl.glEnable(GL2.GL_LIGHT0);  
        // Black background
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f); 
    }
    //https://www.tutorialspoint.com/jogl/jogl_scaling.htm
    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        //make sphere start at origin (reset position)
        //https://stackoverflow.com/questions/628796/what-does-glloadidentity-do-in-opengl
        gl.glLoadIdentity();
        gl.glTranslatef(0.0f, 0.0f, -5.0f); // Move back from the screen
        gl.glRotatef(rotation, 1.0f, 1.0f, 0.0f); // Rotate the sphere

        // Draw the sphere using GLU
        glu.gluSphere(glu.gluNewQuadric(), 1.0, 32, 32); // Radius 1.0, 32 slices/stacks

        gl.glFlush();
        rotation += 0.5f; // Update rotation for animation
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        // Perspective projection
        glu.gluPerspective(45.0f, (double)width / (double)height, 0.1f, 100.0f);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        if (glu != null) {
            glu.gluDeleteQuadric(glu.gluNewQuadric());
        }
    }
    // Main method to set up the Swing window
    public static void main(String[] args) {
        JFrame frame = new JFrame("sphere yay");
        GLCanvas canvas = new GLCanvas();
        canvas.addGLEventListener(new Sphere());
        
        frame.add(canvas);
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}