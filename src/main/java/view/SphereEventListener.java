package view;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;

/*
 * This program is responsible for actually rendering the 3D
 * sphere in the bloch frame. Using cartesian coordinates, tiny
 * spheres can be added to the semi transparent bigger sphere.
 * A quadric object is used to create the sphere so that calculations
 * for geometric properties such as vertices can be handled by OpenGL.
 * 
 */
public class SphereEventListener implements GLEventListener{
	//objects needed for creating the sphere
    private final GLU glu = new GLU();
    private GLUquadric quadric;

    //initial rotation
    private float rotX = 20f;
    private float rotY = 30f;
    
    //float array containing the radius (indicates whether
    //openGL should create a qubit representation (tiny sphere)
    //and the cartesian coordinates
    //- format: {radius,x,y,z}
    private float[] qubitInfo;
    private float[] qubitInfo2;
    
    //getters and setters
    public float[] getQubitInfo() {
		return qubitInfo;
	}

	public void setQubitInfo(float[] qubitInfo) {
		this.qubitInfo = qubitInfo;
	}

    public float[] getQubitInfo2() {
		return qubitInfo2;
	}

	public void setQubitInfo2(float[] qubitInfo2) {
		this.qubitInfo2 = qubitInfo2;
	}

	public void addRotation(float dRotX, float dRotY) {
    	//update the rotation angles
        rotX += dRotX;
        rotY += dRotY;
    }
    
	//method that initializes lighting and the sphere
    @Override
    public void init(GLAutoDrawable drawable) {
    	//initialize the GL2 object that allows access to OpenGL methods
    	//- its utility methods are used to customize the object
    	//source: https://eng.libretexts.org/Bookshelves/Computer_Science/
    	//Applied_Programming/Introduction_to_Computer_Graphics_(Eck)/
    	//03%3A_OpenGL_1.1-_Geometry/3.06%3A_Using_GLUT_and_JOGL
        GL2 gl = drawable.getGL().getGL2();
        
        //initialize qubit info arrays with negative radius
        //so that no spheres are drawn
        float[] initial = {-1,0,0,0};
        setQubitInfo(initial);
        setQubitInfo2(initial);
        
        //enable lighting on the sphere
        //Source: https://www.tutorialspoint.com/jogl/jogl_lighting.htm
        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_LIGHT0);

        //setting the colour with an array of float values
        //https://www.geeksforgeeks.org/java/java-awt-color-class/
        float[] lightPos = { 3f, 3f, 6f, 1f };
        //determine and customize type of light source
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPos, 0);

        //create a quadric object so that I won't have to define normals,
        //vertices, etc. for each longitude and latitude manually
        //Sources:
        //- https://registry.khronos.org/OpenGL-Refpages/gl2.1/xhtml/gluSphere.xml
        //- https://stackoverflow.com/questions/37017238/import-and-use-opengl-gluquadric
        //- https://www.land-of-kain.de/docs/jogl/
        quadric = glu.gluNewQuadric();
        //specify smooth shading to add lighting to the sphere
        //https://webhome.cs.uvic.ca/~blob/courses/305/html/OpenGL2withQuadric.html
        glu.gluQuadricNormals(quadric, GLU.GLU_SMOOTH);
    }

    //dispose the sphere drawing
    @Override
    public void dispose(GLAutoDrawable drawable) {
    	//check if quadric object that is used to create
    	//the sphere is null
        if (quadric != null) {
        	//delete the quadric and set it to null
            glu.gluDeleteQuadric(quadric);
            quadric = null;
        }
    }

    //paints objects on screen
    @Override
    public void display(GLAutoDrawable drawable) {
    	//initialize the GL2 object that allows access to OpenGL methods
    	//source: https://eng.libretexts.org/Bookshelves/Computer_Science/
    	//Applied_Programming/Introduction_to_Computer_Graphics_(Eck)/
    	//03%3A_OpenGL_1.1-_Geometry/3.06%3A_Using_GLUT_and_JOGL
        GL2 gl = drawable.getGL().getGL2();

        //clear buffers for reloading the sphere
        //Source: https://mbien.dev/blog/entry/jogl_2_opengl_profiles_explained
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

        //place object in unprojected space (this means that objects are defined
        //by latitude and longitude instead of being flattened as a 2D representation)
        //Source: https://stackoverflow.com/questions/5367361/opengl-what-is-matrixmode
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        
        //reset object position at origin
        //source: https://stackoverflow.com/questions/628796/
        //what-does-glloadidentity-do-in-opengl
        gl.glLoadIdentity();

        //Set up the view/camera orientation and position
        //- set the camera on the z-axis (directly in front of the sphere) with
        //  a zoom value (larger value makes the sphere smaller)
        //- set the camera at the origin with 0,0,0
        //- set the up vector as the normal y direction
        //Source: https://stackoverflow.com/questions/5717654/glulookat-explanation
        glu.gluLookAt(
                0, 0, 5, 
                0, 0, 0,
                0, 1, 0);

        //rotate sphere with mouse using updated angles 
        //(method parameters are angle,x,y,z)
        //Sources: 
        //- https://www.tutorialspoint.com/jogl/jogl_rotation.htm
        //- https://registry.khronos.org/OpenGL-Refpages/gl2.1/xhtml/glRotate.xml
        gl.glRotatef(rotX, 1f, 0f, 0f);
        gl.glRotatef(rotY, 0f, 1f, 0f);

        //make the sphere transparent by adjusting the alpha(4th)value
        //and enabling blending
        //Source: https://stackoverflow.com/questions/4451399/drawing-a
        //-solid-sphere-with-transparency-in-opengl
        gl.glEnable(GL2.GL_BLEND);
        gl.glBlendFunc (GL2.GL_SRC_ALPHA,GL2.GL_ONE_MINUS_SRC_ALPHA);
        
        //material color
        float[] diffuse = {0.8f, 0.8f, 0.8f, 0.6f};
        float[] spec = { 1f, 1f, 1f, 1f };
        //tell openGL how the material should behave under different light
        //Source: https://registry.khronos.org/OpenGL-Refpages/gl2.1/xhtml/glMaterial.xml
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, diffuse, 0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, spec, 0);
        
        //change the size of the highlight by customizing shininess of the material
        //https://en.wikibooks.org/wiki/OpenGL_Programming/Intermediate/Materials
        gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, 64f);

        //disable lighting to make sure color does not get dimmed
    	//(I want components to show on the panel even when rotated 
    	//to areas with dim light, only the big sphere will be textured 
        //with lighting)
    	//SOurce: https://community.khronos.org/t/glcolor/59599
    	gl.glDisable(GL2.GL_LIGHTING);
        //draw the lines on the sphere
        drawAxes(drawable);
        //draw qubit representation (if any)
        drawTinySphere(drawable);
        //re-enable lighting after drawing the components
    	gl.glEnable(GL2.GL_LIGHTING);
        
        //draw the sphere on the panel
        glu.gluSphere(quadric, 1.2, 48, 48);
    }

    //runs when sphere panel is resized/reloaded
    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
    	//initialize the GL2 object that allows access to OpenGL methods
    	//source: https://eng.libretexts.org/Bookshelves/Computer_Science/
    	//Applied_Programming/Introduction_to_Computer_Graphics_(Eck)/
    	//03%3A_OpenGL_1.1-_Geometry/3.06%3A_Using_GLUT_and_JOGL
        GL2 gl = drawable.getGL().getGL2();
        //switch to projection matrix
        //Source: https://eng.libretexts.org/Bookshelves/Computer_Science/Applied_Programming/
        //Introduction_to_Computer_Graphics_(Eck)/03%3A_OpenGL_1.1-_Geometry/3.03%3A_Projection_and_Viewing
        gl.glMatrixMode(GL2.GL_PROJECTION);
        //reset object position at origin
        //source: https://stackoverflow.com/questions/628796/
        //what-does-glloadidentity-do-in-opengl
        gl.glLoadIdentity();
        //maintain aspect ratio when resizing
        //Sources: 
        //- https://registry.khronos.org/OpenGL-Refpages/gl2.1/xhtml/gluPerspective.xml
        //- https://stackoverflow.com/questions/16571981/gluperspective-parameters-what-do-they-mean
        float aspect = (float) w / (float) Math.max(h, 1);
        glu.gluPerspective(45.0, aspect, 0.1, 100.0);
        //switch back to model view
        gl.glMatrixMode(GL2.GL_MODELVIEW);
    }
    
    //draw the three axes on the sphere
    public void drawAxes(GLAutoDrawable drawable) {
    	GL2 gl = drawable.getGL().getGL2();
    	
    	//increase line thickness
    	//Source: https://registry.khronos.org/OpenGL-Refpages/gl4/html/glLineWidth.xhtml
    	gl.glLineWidth(10);
    	
    	//For each line, use gl begin to start the process of drawing a line
    	//and gl end to end the line.
    	//- use vertex 3f to define 3d float coordinates
    	//Source: https://www.tutorialspoint.com/jogl/jogl_drawing_basics.htm
    	
    	//X-AXIS
    	//set the color
    	//Source: https://community.khronos.org/t/glcolor/59599
    	//red line for x axis
    	//- set first parameter to 1 in floating point since red is the
    	//  first in RGB (red, blue, green)
    	gl.glColor3f(1f, 0f, 0f);
    	//tell openGL that we are going to draw lines
    	gl.glBegin(GL2.GL_LINES);
    	//determine coordinates for starting and end points
    	//- from -1.3 to 1.3 in the x parameter since we are drawing the x axis
    	//  (make values greater than 1 so that the axes stick out of the sphere)
    	gl.glVertex3f(-1.3f, 0f, 0f);
    	gl.glVertex3f(1.3f, 0f, 0f);
    	//end the line
    	gl.glEnd();
    	
    	//Y-AXIS
    	//set the color to green
    	gl.glColor3f(0f, 1f, 0f);
    	//tell openGL that we are going to draw lines
    	gl.glBegin(GL2.GL_LINES);
    	//determine coordinates for starting and end points
    	//- from -1 to 1 in the y parameter since we are drawing the y axis
    	gl.glVertex3f(0f, -1.3f, 0f);
    	gl.glVertex3f(0f, 1.3f, 0f);
    	//end the line
    	gl.glEnd();
    	
    	//Z-AXIS
    	//set the color to blue
    	gl.glColor3f(0f, 0f, 1f);
    	//tell openGL that we are going to draw lines
    	gl.glBegin(GL2.GL_LINES);
    	//determine coordinates for starting and end points
    	//- from -1 to 1 in the y parameter since we are drawing the y axis
    	gl.glVertex3f(0f, 0f, -1.3f);
    	gl.glVertex3f(0f, 0f, 1.3f);
    	//end the line
    	gl.glEnd();
    	
    	//draw the triangles representing arrow heads
    	drawArrowHeads(drawable);
    	
    }
    
    //draw three arrow heads for the axes
    public void drawArrowHeads(GLAutoDrawable drawable) {
    	GL2 gl = drawable.getGL().getGL2();
    	//tell openGL that we are going to draw triangles
    	gl.glBegin(GL2.GL_TRIANGLES);
    	//
    	//SOurce: https://www.tutorialspoint.com/jogl/jogl_3d_triangle.htm
    	//drawing triangle in all dimensions
    	
    	//use 2D array to store vertex coordinates 
    	//of each face of the pyramid
    	//- I got these coordinates from drawing diagrams and 
    	//  repeated trial and error
    	//- default order of coordinates is for the arrowhead of the x-axis
    	float[][] coords = {//x-coordinates
    			{1.3f,1.3f,1.3f,1.3f,1.3f,1.5f,
    			1.5f,1.3f,1.3f,1.3f,1.3f,1.5f},
    			//y-coordinates
    			{0.1f,-0.1f,-0.1f,0.1f,-0.1f,0f,
    			0f,-0.1f,-0.1f,0.1f,-0.1f,0f},
    			//z-coordinates
    			{0,-0.1f,0.1f,0f,0.1f,0f,0f,
    			-0.1f,0.1f,0f,-0.1f,0f}};
    	
    	//draw the three arrows
    	for(int i = 0; i < 3; i++) {
    		//use iteration variable to determine which is the current
    		//axis arrow head
    		//since all axes have the same length, and are all perpendicular,
    		//I can just rearrange "rows" of the 2D array defined above
    		//to get a 90 degree translation from the default coordinates (which
    		//are those of the arrowhead of the x-axis)
    		//determine color and position based on axes
    		if(i == 0)
    			//red
    			gl.glColor3f(1f, 0f, 0f);
    			//keep default coordinate order
    		else if (i == 1) {
    			//green
    			gl.glColor3f(0f, 1f, 0f);
    			//switch x and y coordinates from the default order
    			//temporarily store coordinates
    			float[] temp = coords[0];
    			coords[0] = coords[1];
    			coords[1] = temp;
    		}
    		else {
    			//blue
    			gl.glColor3f(0f, 0f, 1f);
    			//switch x and z coordinates from the default order
    			//- since x and y were already switched in the previous iteration,
    			//  I will switch y and z
    			float[] temp = coords[1];
    			coords[1] = coords[2];
    			coords[2] = temp;
    		}
    		
    		//create pyramid arrow head for the current axis
    		for(int j = 0; j < coords[0].length; j++) {
    			//create vertex of triangle
    			gl.glVertex3f(coords[0][j], coords[1][j], coords[2][j]);
    			//there is no need to loop 3 times since I already have an outer loop
    			//for each axis
    			//openGL automatically starts the next triangle once it has 3 vertices
    		}
    		
    	}
    	//end the process of drawing triangles to form pyramids
        gl.glEnd();
    }
    
    //draw the qubit representation
    public void drawTinySphere(GLAutoDrawable drawable) {
    	GL2 gl = drawable.getGL().getGL2();
    	//check if the radius is less than 0 (initialized at -1,
    	//buttons in the bloch frame will change it to positive to
    	//indicate that the user has chosen a state to display)
    	if(qubitInfo[0] > 0) {
    		//clone the top matrix (used by openGL for transformations)
        	//so that translations do not affect the big sphere,
        	//only the tiny one
        	//Source: https://stackoverflow.com/questions/23971752/
        	//understanding-glpushmatrix-and-glpopmatrix
        	gl.glPushMatrix();
        	
        	//translate to coordinates of tiny sphere
        	//Source: https://www.tutorialspoint.com/jogl/jogl_transformation.htm
        	gl.glTranslatef(qubitInfo[1], qubitInfo[2], qubitInfo[3]);
        	
        	//set color of tiny sphere
        	gl.glColor3f(1f, 1f, 1f);
        	//create the sphere
        	//source: https://www.land-of-kain.de/docs/jogl/
        	glu.gluSphere(quadric, qubitInfo[0], 16, 16);
        	
        	//delete the top matrix (cloned from the one below earlier)
        	//to make sure transformation won't affect the big sphere
        	gl.glPopMatrix();
    	}
    	//check if there is a second sphere to be drawn
    	//- don't draw if radius is negative
    	if(qubitInfo2[0] > 0) {
    		//clone the top matrix (used by openGL for transformations)
        	//so that translations do not affect the big sphere,
        	//only the tiny one
        	//Source: https://stackoverflow.com/questions/23971752/
        	//understanding-glpushmatrix-and-glpopmatrix
        	gl.glPushMatrix();
        	
        	//translate to coordinates of tiny sphere
        	//Source: https://www.tutorialspoint.com/jogl/jogl_transformation.htm
        	gl.glTranslatef(qubitInfo2[1], qubitInfo2[2], qubitInfo2[3]);
        	
        	//set color of tiny sphere
        	gl.glColor3f(1f, 1f, 1f);
        	//create the sphere
        	//source: https://www.land-of-kain.de/docs/jogl/
        	glu.gluSphere(quadric, qubitInfo2[0], 16, 16);
        	
        	//delete the top matrix (cloned from the one below earlier)
        	//to make sure transformation won't affect the big sphere
        	gl.glPopMatrix();
    	}
    }
}