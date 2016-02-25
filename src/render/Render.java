/*
 *  FasT -- A FasT algorithm for simulaTions. 
 * 
 *  Copyright © 2016 Tourdetour
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *  
 *  Contact me by email : <TPEFasT@mail.com>
 */

package render;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.Method;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import com.sun.glass.ui.Application;

import game.FasT;
import game.Liquid;
import game.entities.Ball;
import game.entities.Box;
import game.entities.Entity;
import game.entities.Wall;
import log.Logger.logItem;
import physics.maths.Angle;
import physics.maths.C;
import physics.maths.Maths;
import physics.maths.Normal;
import physics.maths.Point;


public class Render {
/* Render options */
	protected boolean showArrows = false;
	public boolean showTails = true;
	
/*-----------------------*/
	
	
	
	 private String windowTitle;
	 private int width,height;
	
	
	 private JFrame frame = new JFrame();
	 public final JPanel panel = new JPanel();
	 private final Canvas glCanvas = new Canvas();
	 private final JPanel panelLeft = new JPanel();
	 private final JPanel panelOptions = new JPanel();
	 private final JPanel panelHelp = new JPanel();
	 public final JPanel BPanel = new JPanel();
	 
	public final JMenu game = new JMenu("game");
	 
	 public final JMenuItem play = new JMenuItem();
     
     public final JPopupMenu popupMenu = new JPopupMenu();
	
	 public JFrame getFrame()
	 {
		 return this.frame;
	 }
	 
		public  Canvas getCanvas() {
			return this.glCanvas;
		}

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
        frame.pack();
  
	}

	
	//We init the GUI by creating the frames
	JFrame frameAbout;
	public void initGUI()
	{
		//Creating About frame
		frameAbout = new JFrame();

		frameAbout.setTitle("About FasT");
		frameAbout.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	    frameAbout.setResizable(false);
	    
	  frameAbout.setMinimumSize(new Dimension(400,150));
	  
	  JLabel pane = new JLabel(); 
	  pane.setFont(new Font("serif", 1, 20));
	  pane.setText("<html>FasT ©, FasT Algorithm for simulaTions, est un logiciel développé par tourdetour depuis septembre 2015 dans le cadre de ce TPE.</html>");
	  
	  frameAbout.add(pane);
	  
	  frameAbout.setLocation(dim.width/2-frame.getSize().width, dim.height/2-frame.getSize().height);
	  frameAbout.setVisible(false);

	  
	  
	  frame.pack();
	     frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
		
		//Creating JFRAME
		frame = new JFrame();
			
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle(this.getWindowTitle());

		//Setting dimension and putting it on the center of the screen
	    frame.setResizable(true);
	 //  frame.setMaximumSize(new Dimension(1280,773));

	  frame.setMinimumSize(new Dimension(765,465));
	   //dispose() is the reason that this trick doesn't work with videos
     
	    
	    //Set the layout of the JFRAME
	    frame.getContentPane().setLayout(new BorderLayout(0,0));
	  
	    //End with JFRAME -----
	    

	   
	    //Starting Help panel
	     panelHelp.setLayout(new BorderLayout(0,0));
	     
	     panelHelp.setPreferredSize(new Dimension(400,this.getHeight()));
	     
	     panelHelp.setBackground(Color.RED);
	  
         	HelpBrowser browser = new HelpBrowser();
         	browser.setHomePage("/website/index.html");
           
         panelHelp.add(browser);
	     
         //We create the main panel
	     panel.setLayout(new BorderLayout(0,0));
	     panel.setPreferredSize(new Dimension(this.getWidth(),this.getHeight()));
	     panel.setBackground(Color.DARK_GRAY);
	     panel.setRequestFocusEnabled(true);
	     
	     //With the canvas
	     	glCanvas.setFocusable(true);
	     	glCanvas.setBackground(Color.DARK_GRAY);
	     	glCanvas.setIgnoreRepaint(true);
	     	//glCanvas.setBounds(0, 0, this.width, this.height-100);
	     panel.add(glCanvas,BorderLayout.CENTER);
		    BPanel.setPreferredSize(new Dimension(panel.getWidth(),200));
		    BPanel.setBackground(Color.BLACK);
		    BPanel.setVisible(true);
		 panel.add(BPanel,BorderLayout.SOUTH);
	     BPanel.setVisible(false);
		 panel.addComponentListener(new ComponentAdapter() 
		 {  
		         public void componentResized(ComponentEvent evt) {
		             Component c = (Component)evt.getSource();
		           //  glCanvas.setSize(c.getWidth(), c.getHeight());
		             flag=true;
		         }
		 });
		 
		 
	     //Panelleft is actually useless
	     panelLeft.setLayout(new BorderLayout(0,0));
	     panelLeft.add(panel,BorderLayout.CENTER);
	     //panelLeft.add(panelOptions,BorderLayout.SOUTH); IN CASE YOU NEED PANEL OPTION (WE NEEDN'T)
	  
	     
	     frame.getContentPane().add(panelHelp,BorderLayout.EAST);
	     frame.getContentPane().add(panelLeft,BorderLayout.CENTER);
	      
	     
	     panelHelp.setVisible(true);
	     frame.pack();
	     frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
		  //  frame.setMinimumSize(new Dimension(765,frame.getHeight()));
		  //  frame.setMaximumSize(new Dimension(1280,frame.getHeight()));
	     
	     
	     
	   // this.createMenuBar(); If you needed a menubar...
	     
	     frame.setVisible(true);
	     this.renderMenu();
	     enableOSXFullscreen(frame);
	}
	
	
	//Found here : https://gist.github.com/dohpaz42/4200907
	@SuppressWarnings({"unchecked", "rawtypes"})
    public static void enableOSXFullscreen(Window window) {
        try {
            Class util = Class.forName("com.apple.eawt.FullScreenUtilities");
            Class params[] = new Class[]{Window.class, Boolean.TYPE};
            Method method = util.getMethod("setWindowCanFullScreen", params);
            method.invoke(util, window, true);
        } catch (Exception e) {
            FasT.getFasT().getLogger().warning("Could not enable fullscreen mode (maybe you are working on windows ?) ERROR : " + e.getMessage());
        }
    }
	

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void requestToggleFullScreen(Window window)
    {
        try {
            Class appClass = Class.forName("com.apple.eawt.Application");
            Class params[] = new Class[]{};

            Method getApplication = appClass.getMethod("getApplication", params);
            Object application = getApplication.invoke(appClass);
            Method requestToggleFulLScreen = application.getClass().getMethod("requestToggleFullScreen", Window.class);

            requestToggleFulLScreen.invoke(application, window);
        } catch (Exception e) {
        	 FasT.getFasT().getLogger().warning("Could not request fullscreen mode (maybe you are working on windows ?) ERROR : " + e.getMessage());
        }
    }
	
	
public static KeyListener getSliderKeyListener()
{
	return new KeyListener()
	{
		boolean r=false;
		@Override
		public void keyTyped(KeyEvent e) {}
		@Override
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode()==KeyEvent.VK_SHIFT)
			{
				r=!r;
				((JSlider) e.getSource()).setSnapToTicks(r);
			}
		}
		@Override
		public void keyReleased(KeyEvent e) {
			/*if(e.getKeyCode()==KeyEvent.VK_SHIFT)
			{
				((JSlider) e.getSource()).setSnapToTicks(true);
			}*/
		}};
}

private void createMenuBar() {

    JMenuBar menubar = new JMenuBar();
  //  ImageIcon icon = new ImageIcon("exit.png");

    JMenu file = new JMenu("File");
    file.setMnemonic(KeyEvent.VK_F);

    JMenuItem eMenuItem = new JMenuItem("Exit");
    eMenuItem.setMnemonic(KeyEvent.VK_E);
    eMenuItem.setToolTipText("Exit application");
    file.add(eMenuItem);
    menubar.add(file);

    frame.setJMenuBar(menubar);
}

//JLabel masseVolumiqueLabel = new JLabel();

public void updateLabels()
{
	//masseVolumiqueLabel.setText("masse volumique(kg/m^3)="+Maths.dfloor(((Box) FasT.getFasT().getEntityHandler().get(FasT.getFasT().theBox)).getLiquid().getMasseVolumique()));
}

public  JMenu liquid = new JMenu("liquid");

public void renderMenu()
{

    JMenu file = new JMenu("file");
    JMenuItem exit = new JMenuItem("exit");
    exit.addActionListener(new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent e) {
			 FasT.getFasT().quit();
		}
    });
  
    JMenuItem about = new JMenuItem("about");
    about.addActionListener(new ActionListener(){
    	public void actionPerformed(ActionEvent e) {
 	     frameAbout.setVisible(true);
 	   }
   });
    JMenu options = new JMenu("options");
   
    JCheckBoxMenuItem showHelp = new JCheckBoxMenuItem("Help Panel");
    showHelp.setState(this.panelHelp.isVisible());
    showHelp.addItemListener(new ItemListener()
    		{
				@Override
				public void itemStateChanged(ItemEvent e) {
					FasT.getFasT().getRender().panelHelp.setVisible(showHelp.getState());
				}
    		}
    	);
    
    
    JCheckBoxMenuItem showTails = new JCheckBoxMenuItem("Show Tails");
    showTails.setState(this.showTails);
    showTails.addItemListener(new ItemListener()
    		{
				@Override
				public void itemStateChanged(ItemEvent e) {
					FasT.getFasT().getRender().showTails=showTails.getState();
				}
    		}
    	);
		
    
    JCheckBoxMenuItem showArrows = new JCheckBoxMenuItem("Show Arrows");
    showArrows.setState(this.showArrows);
    showArrows.addItemListener(new ItemListener()
    		{
				@Override
				public void itemStateChanged(ItemEvent e) {
					FasT.getFasT().getRender().showArrows=showArrows.getState();
				}
    		}
    	);
		
    
    JCheckBoxMenuItem debug = new  JCheckBoxMenuItem("Debug");
    debug.setState(FasT.getFasT().debug);
    debug.addItemListener(new ItemListener()
	{
		@Override
		public void itemStateChanged(ItemEvent e) {
			FasT.getFasT().debug=debug.getState();
		}
	});

    
    JCheckBoxMenuItem log = new  JCheckBoxMenuItem("Log");
    log.setState(FasT.getFasT().getLogger().shallLog);
    log.addItemListener(new ItemListener()
	{
		@Override
		public void itemStateChanged(ItemEvent e) {
			FasT.getFasT().getLogger().shallLog=log.getState();
		}
	});
   
    options.add(showHelp);
    options.add(showTails);
    options.add(showArrows);
    options.add(debug);
    options.add(log);
    
    file.add(exit);
    file.add(about);
    file.add(options);    
    play.setText("play");
    play.addActionListener(new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent e) {
			FasT.getFasT().setPaused(!FasT.getFasT().isPaused());
		}
    });
    game.add(play);
    
    
   
    
    String[] states = {"Sans forces","Univers","Chute libre","Chute avec frottements"};
    JRadioButtonMenuItem[] items = new JRadioButtonMenuItem[states.length];
    ButtonGroup modeGroup = new ButtonGroup();
    
    ActionListener modeListener = new ActionListener(){
		public void actionPerformed(ActionEvent e) {
			  for ( int i = 0; i < items.length; i++ )
			  {
		            if ( e.getSource() == items[ i ] ) {
		               FasT.getFasT().getPhysicsHandler().simulationLevel=i-1;
		               return;
		            }
			  }
		}
    };


    for (int i = 0; i < items.length; i++) {
    	items[i]=new JRadioButtonMenuItem(states[i]);
    	game.add(items[i]);
    	modeGroup.add(items[i]);
    	items[i].addActionListener(modeListener);
    }
    
    items[FasT.getFasT().getPhysicsHandler().simulationLevel+1].setSelected(true);

    JCheckBoxMenuItem rebonds = new  JCheckBoxMenuItem("Rebonds");
    rebonds.setState(FasT.getFasT().getPhysicsHandler().rebonds);
    rebonds.addItemListener(new ItemListener()
	{
		@Override
		public void itemStateChanged(ItemEvent e) {
			FasT.getFasT().getPhysicsHandler().rebonds=rebonds.getState();
		}
	});
    
    
   // JSlider masseVolumiqueSlider = new JSlider();
   // masseVolumiqueSlider.setMinimum(500);
  /*  masseVolumiqueSlider.setMajorTickSpacing(300);
    masseVolumiqueSlider.setPaintTicks(true);
    masseVolumiqueSlider.setSnapToTicks(true);*/
   // masseVolumiqueSlider.setMaximum(1500);
   /* masseVolumiqueSlider.addChangeListener(new ChangeListener()
    		{
				@Override
				public void stateChanged(ChangeEvent e) {
					((Box) FasT.getFasT().getEntityHandler().get(FasT.getFasT().theBox)).getLiquid().setMasseVolumique(masseVolumiqueSlider.getValue());
					updateLabels();
				}
    		});*/
   
    
    //liquid.add(masseVolumiqueLabel);
   // liquid.add(masseVolumiqueSlider);

    JMenu spawn = new JMenu("spawn");
    JMenuItem ball = new JMenuItem("ball");
    JMenuItem box = new JMenuItem("box");
    JMenuItem wall = new JMenuItem("wall");
 
    wall.addActionListener(new ActionListener(){
  		@Override
  		public void actionPerformed(ActionEvent e) {
  			FasT.getFasT().getEntityHandler().spawn(new Wall(new Point(Mouse.getX(),Mouse.getY()).mouseToReal(), 2, new Angle(0.1), FasT.getFasT().getEntityHandler()));
  		}
      });
    ball.addActionListener(new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent e) {
			FasT.getFasT().getEntityHandler().spawn(new Ball(new Point(Mouse.getX(),Mouse.getY()).mouseToReal(),FasT.getFasT().getEntityHandler()));
		}
    });
    box.addActionListener(new ActionListener(){
  		@Override
  		public void actionPerformed(ActionEvent e) {//new C(new Angle(Math.PI),4)
  			FasT.getFasT().getEntityHandler().spawn(new Box(new Point(Mouse.getX(),Mouse.getY()).mouseToReal(),new Point(Mouse.getX()+20,Mouse.getY()+20).mouseToReal(),1,Liquid.WATER(),FasT.getFasT().getEntityHandler()));
  		}
      });
      
    
    spawn.add(ball);
    spawn.add(box);
    spawn.add(wall);
    
    
    popupMenu.add(game);
    popupMenu.add(spawn);
    popupMenu.add(file);
  

	glCanvas.addMouseListener(new MouseListener() {

		 		public void mouseClicked(MouseEvent e) {}
		 		public void mouseEntered(MouseEvent e) {}
		 		public void mouseExited(MouseEvent e) {}
		 		
		 	    public void mousePressed( MouseEvent e )
		         {checkForTriggerEvent( e ); }

		     public void mouseReleased( MouseEvent e )
		         { checkForTriggerEvent( e ); }

		      private void checkForTriggerEvent( MouseEvent e )
		      {
		         if ( e.isPopupTrigger() )
		         {
		        	 Entity entity;
		        	 if((entity=FasT.getFasT().getEntityHandler().getEntityUnder(new Point(e.getX(),glCanvas.getHeight()-e.getY()).mouseToReal()))!=null && entity.shouldMenu(new Point(e.getX(),glCanvas.getHeight()-e.getY()).mouseToReal()))
		        	 {
		        		 entity.getPopupMenu().show(e.getComponent(),e.getX(),e.getY());
		        	 }
		        	 else
		        	 {
		        		   popupMenu.show(e.getComponent(),e.getX(), e.getY());
		        	 }
		         }
		      }
		     	   
		       });

//SOURCE : http://www.coderanch.com/t/450603/GUI/java/display-click-popup-menu-JPanel
}
       

	public void resetGL()
	{
		try {
			Display.setDisplayMode(new DisplayMode(Display.getParent().getWidth(),Display.getParent().getHeight()));
		} catch (LWJGLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(-Display.getWidth()/2, Display.getWidth()/2,-Display.getHeight()/2,Display.getHeight()/2, -1.0, 1.0);
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

		if(flag)
			this.resetGL();
		flag=false;

			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_STENCIL_BUFFER_BIT);
		 GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		 GL11.glClearColor(1.0F, 1.0F, 1.0F, 1.0F);
		 
		 GL11.glColor3d(0.0F, 0.0F, 0.0F);
		 GL11.glPushMatrix();
		 GL11.glScaled(Normal.toPlan(1), Normal.toPlan(1), 1);
		 GL11.glTranslated(Normal.rx, Normal.ry, 0);
		// double zoom = 0.00001;
		// GL11.glOrtho( -width/2*zoom, width/2*zoom, -height/2*zoom, height/2*zoom, -1, 1 );
		 
		 GL11.glColor3d(0.4, 0.9, 0.1);
		 
		 //TODO : Make color class with getGreenFloat and getGreenRGB and chromatic wheel
		// this.drawLine(new Point(20,20), new Point(20+100,20));
		 //FasT.getFasT().getLogger().debug("1 meter = " + Normal.normal(100, Unit.cm));
		// this.drawLine(new Point(20,40), new Point(20+Normal.toPlan(1),40));
		 GL11.glColor3d(0,1,0);
		 this.drawSquare(new Point(-1,-1).toReal(), new Point(1,1).toReal());
		 
		// this.drawLine(new Point(1,1).toReal(), new Point(1,1).toReal().add(new Point(1,0)));
		// this.drawLine(new Point(1,10), new Point(20,10));
		 
		 GL11.glColor3d(0.02,0.8,0.95);
		 this.drawLine(new Point(10,10).mouseToReal(), new Point(10,10).mouseToReal().add(new Point(1,0)));
		 
		 //Draw text to show this is 1 meter
	}
	
	
	
	public void drawSquare(Point min, Point max)
	{
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
		//GL11.glColor3d(0,245,51);
		GL11.glBegin(GL11.GL_LINE_STRIP);
		for(Point position : positions)
		{
			//FasT.getFasT().getLogger().error(position.getX());
			GL11.glVertex2d(position.getX(), position.getY());
		}
		GL11.glEnd();
		
	}
	
	public void drawLine(Point p, C c)
	{
		drawLine(p, new Point(p.getX()+c.getRe(),p.getY()+c.getIm()));
	}
	
	public void drawLine(Point p, double angle,double length)
	{
		drawLine(p,new C(new Angle(angle),length));
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

	public void drawCorners(Point pos,double radius, float[] color) {
		GL11.glColor3d(color[0], color[1], color[2]);
		double d = radius/2;
		
		GL11.glBegin(GL11.GL_LINE_STRIP);
		GL11.glVertex2d(pos.getX()-radius, pos.getY()+d);
		GL11.glVertex2d(pos.getX()-radius, pos.getY()+radius);
		GL11.glVertex2d(pos.getX()-d, pos.getY()+radius);
		GL11.glEnd();
		
		GL11.glBegin(GL11.GL_LINE_STRIP);
		GL11.glVertex2d(pos.getX()+radius, pos.getY()-d);
		GL11.glVertex2d(pos.getX()+radius, pos.getY()-radius);
		GL11.glVertex2d(pos.getX()+d, pos.getY()-radius);
		GL11.glEnd();
		
		GL11.glBegin(GL11.GL_LINE_STRIP);
		GL11.glVertex2d(pos.getX()-radius, pos.getY()-d);
		GL11.glVertex2d(pos.getX()-radius, pos.getY()-radius);
		GL11.glVertex2d(pos.getX()-d, pos.getY()-radius);
		GL11.glEnd();
		
		GL11.glBegin(GL11.GL_LINE_STRIP);
		GL11.glVertex2d(pos.getX()+radius, pos.getY()+d);
		GL11.glVertex2d(pos.getX()+radius, pos.getY()+radius);
		GL11.glVertex2d(pos.getX()+d, pos.getY()+radius);
		GL11.glEnd();
	}
	
	
	/*
	

 * 


 * Here are folded unused part of code
	 * 
	 */

	/* HTMLEDITOR KIT panelText.add(textPane);*
	JEditorPane editorPane = new JEditorPane("text/html", "The rain in <a href='http://foo.com/'>"
				+"Spain</a> falls mainly on the <a href='http://bar.com/'>plain</a>.");
	// editorPane.setText("<img src=\"http://latex.codecogs.com/svg.latex?1+sin(x)\" border=\"0\">");
	 editorPane.setEditable(false);
	/* editorPane.addHyperlinkListener(new HyperlinkListener() {
		 public void hyperlinkUpdate(HyperlinkEvent hle) {
		 if (HyperlinkEvent.EventType.ACTIVATED.equals(hle.getEventType())) {
			 try {
				editorPane.setPage(getClass().getClassLoader().getResource("ressources/Helloworld.html"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		 }
		 }
		 });
	 Path currentRelativePath = Paths.get("");
	 String s = currentRelativePath.toAbsolutePath().toString();
	 System.out.println("Current relative path is: " + s);
		ClassLoader classLoader = getClass().getClassLoader();
		URL baseURL = classLoader.getResource("ressources/CarnetDeBord.html");
		

	 JScrollPane editorScrollPane = new JScrollPane(editorPane);


	 HTMLEditorKit kit = new HTMLEditorKit();
	    editorPane.setEditorKit(kit);
	    // add some styles to the html
	   
	    // create a document, set it on the jeditorpane, then add the html
	    Document doc = kit.createDefaultDocument();
	    editorPane.setDocument(doc);
	   try{
		   editorPane.setPage(getClass().getClassLoader().getResource("ressources/test/test.html"));
	   }catch(Exception e)
	   {
		   e.printStackTrace();
	   }

	 //panelHelp.add(editorScrollPane);
	 In case you want HTMLEDITORKIT */
	
	
	 /*THIS IS PANEL OPTION STUFF 
     * 
     * 
     *   

    JToggleButton btnPause = new JToggleButton("Play");
    btnPause.addItemListener(new ItemListener(){

		@Override
		public void itemStateChanged(ItemEvent e) {
			if(e.getStateChange() == ItemEvent.SELECTED)
			{
				FasT.getFasT().setPaused(false);
				btnPause.setText("Pause");
			}
			else if(e.getStateChange() == ItemEvent.DESELECTED)
			{
				FasT.getFasT().setPaused(true);
				btnPause.setText("Play");
			}
			
		}});
    
     panelOptions.setLayout(new FlowLayout(FlowLayout.LEFT));
  
     panelOptions.setPreferredSize(new Dimension(this.getWidth(),150));
  
     panelOptions.setBackground(new Color(90, 95, 105));
     
     panelOptions.add(btnPause);
     
     * 
     * End of panel option
     * 
     */
     
     
	public boolean flag = false;
	public void EndRender() {
		//Render debug
		if(this.showArrows)
		{
		for(logItem item : FasT.getFasT().getLogger().getVLog())
		{
			Point p = item.getPoint();
			C c = item.getVec();
			double[] color = item.getColor();
			
			GL11.glColor3d(color[0],color[1],color[2]);
			drawLine(p,c);
			GL11.glColor3d(0.1,0.4,0.8);
			
			Point max = new Point(p.getX()+c.getRe(),p.getY()+c.getIm());
			drawLine(max, c.getTheta().getRad()-3*Math.PI/4,Normal.toReal(6));
			drawLine(max, c.getTheta().getRad()+3*Math.PI/4,Normal.toReal(6));
			
			
			drawCircle(p,Normal.toReal(1),new float[] {0.1f,0.4f,0.9f});
			//drawLine(new Point(p.getX()+c.getRe(),p.getY()+c.getIm()),new C(c.getTheta().getRad()+Math.PI/4,0.1));
			
		}
		}
		
		
		
		GL11.glPopMatrix();
		
		Display.update();
		
	}

	public void setFlag() {
		
		//set the flag to resize GL
		this.flag=true;
		
	}

	



 
 
}
