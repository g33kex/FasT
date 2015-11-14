package render;

import game.FasT;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import physics.maths.Point;



public class Render {

	 private String windowTitle;
	 private int width,height;
	
	
	 private JFrame frame = new JFrame();
	 private final JPanel panel = new JPanel();
	 private final Canvas glCanvas = new Canvas();
	 private final JPanel panelOptions = new JPanel();
	 private final JPanel panelHelp = new JPanel();
	

	public Render(){
		
	}
	
	public void init(int width,int height, String windowTitle) throws LWJGLException
	{
		this.width = width;
		this.height = height;
		this.windowTitle = windowTitle;
		this.initGUI();
		this.initGL();
	
	
	}
	
	private void initGL() throws LWJGLException
	{
	   Display.setParent(glCanvas);
	   Display.create();
	   //Display.setTitle(this.getWindowTitle());
		//Display.setDisplayMode(new DisplayMode(this.getWidth(),this.getHeight()));
		//Display.setDisplayMode(new DisplayMode(Display.getParent().getWidth(),Display.getParent().getHeight()));
        Display.setVSyncEnabled(true);
        //Display.setResizable(true);
        this.resetGL();
        glCanvas.requestFocus();
  
	}

	public void initGUI()
	{
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle(this.getWindowTitle());

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setResizable(false);
	     
	    
	    frame.getContentPane().setLayout(new BorderLayout(0,0));
	    

	    
	     panelOptions.setLayout(new BorderLayout(0,0));
	  
	     panelOptions.setPreferredSize(new Dimension(200,this.getHeight()));
	  
	     panelOptions.setBackground(Color.BLUE);
	     
	     panelHelp.setLayout(new BorderLayout(0,0));
	     
	     panelHelp.setPreferredSize(new Dimension(200,this.getHeight()));
	     
	     panelHelp.setBackground(Color.RED);
	    // panelText.add(textPane);
	
	    
	     panel.setLayout(new BorderLayout(0, 0));
	     panel.setPreferredSize(new Dimension(this.getWidth(),this.getHeight()));
	     panel.setBackground(Color.DARK_GRAY);

	     panel.setRequestFocusEnabled(true);
	     glCanvas.setFocusable(true);
	     glCanvas.setBackground(Color.DARK_GRAY);
	     glCanvas.setIgnoreRepaint(true);

	     panel.add(glCanvas);

	     
	     
	     frame.getContentPane().add(panelOptions,BorderLayout.EAST);
	     frame.getContentPane().add(panel,BorderLayout.CENTER);
	     frame.getContentPane().add(panelHelp,BorderLayout.WEST);
	     
	     frame.pack();
	     frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
	     
	     frame.setVisible(true);

	}
	
	private void resetGL()
	{
		GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0, Display.getWidth(), 0.0F,Display.getHeight(), -1.0, 1.0);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
        
        GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc( GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA );	
	}

	public String getWindowTitle() {
		return windowTitle;
	}

	public void setWindowTitle(String windowTitle) {
		this.windowTitle = windowTitle;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	
	public void StartRender() {

		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_STENCIL_BUFFER_BIT);
		 GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		 GL11.glClearColor(1.0F, 1.0F, 1.0F, 1.0F);
		 
		 GL11.glColor3d(0.0F, 0.0F, 0.0F);
		 
		 
		 GL11.glPopMatrix();
	}
	
	
	
	public void drawSquare(Point min, Point max)
	{
		 GL11.glColor3d(0,0,0);
		GL11.glBegin(GL11.GL_POLYGON);
		GL11.glVertex2d(min.getX(), min.getY());
		GL11.glVertex2d(min.getX(), max.getY());
		GL11.glVertex2d(max.getX(),max.getY());
		GL11.glVertex2d(max.getX(), min.getY());
		GL11.glEnd();
	}
	
	public void drawCircle(Point center, double radius, float[] color)
	{
		this.drawCircle(center, radius, 100, color);
	}
	
	public void drawLines(ArrayList<Point> positions) {
		GL11.glColor3d(0,245,51);
		GL11.glBegin(GL11.GL_LINE_STRIP);
		for(Point position : positions)
		{
			//FasT.getFasT().getLogger().error(position.getX());
			GL11.glVertex2d(position.getX(), position.getY());
		}
		GL11.glEnd();
		
	}
	
	public void drawLine(Point position, Point posMax) {
		ArrayList<Point> p = new ArrayList<Point>();
		p.add(position);
		p.add(posMax);
		this.drawLines(p);
	}
	
	private void drawCircle(Point center, double radius, int num_segments,float[] color) {
		 GL11.glColor3d(color[0],color[1],color[2]);
		GL11.glBegin(GL11.GL_POLYGON); 
		for(int ii = 0; ii < num_segments; ii++) 
		{ 
			float theta = 2.0f * 3.1415926f * ii / num_segments;//get the current angle 

			float x = (float) (radius * Math.cos(theta));//calculate the x component 
			float y = (float) (radius * Math.sin(theta));//calculate the y component 

			GL11.glVertex2d(x + center.getX(), y + center.getY());//output vertex 

		} 
		GL11.glEnd(); 	
	}

	

	public void EndRender() {
		GL11.glPushMatrix();

		Display.update();
		//glCanvas.paint(glCanvas.getGraphics());
		//panel.paint(frame.getGraphics());


	}
}
	
	