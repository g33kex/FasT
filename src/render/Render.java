package render;

import game.FasT;
import game.Liquid;
import game.entities.Ball;
import game.entities.Entity;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.plaf.basic.BasicComboBoxUI.ItemHandler;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import physics.maths.Maths;
import physics.maths.Normal;
import physics.maths.Point;
import physics.maths.Normal.Unit;



public class Render {

	 private String windowTitle;
	 private int width,height;
	
	
	 private JFrame frame = new JFrame();
	 private final JPanel panel = new JPanel();
	 private final Canvas glCanvas = new Canvas();
	 private final JPanel panelLeft = new JPanel();
	 private final JPanel panelOptions = new JPanel();
	 private final JPanel panelHelp = new JPanel();
	 
	 public final JMenuItem play = new JMenuItem();
     
     public final JPopupMenu popupMenu = new JPopupMenu();
	
	 public JFrame getFrame()
	 {
		 return this.frame;
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
	public void initGUI()
	{
		//Creating JFRAME
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle(this.getWindowTitle());

		//Setting dimension and putting it on the center of the screen
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	    frame.setResizable(true);
	    frame.setMaximumSize(new Dimension(1280,773));
	    frame.setMinimumSize(new Dimension(765,465));
	    
	    //Set the layout of the JFRAME
	    frame.getContentPane().setLayout(new BorderLayout(0,0));
	    
	    //End with JFRAME -----
	    

	   
	    //Starting Help panel
	     panelHelp.setLayout(new BorderLayout(0,0));
	     
	     panelHelp.setPreferredSize(new Dimension(400,this.getHeight()));
	     
	     panelHelp.setBackground(Color.RED);
	  
         	HelpBrowser browser = new HelpBrowser();
         	browser.setHomePage("http://www.example.com/");
           
         panelHelp.add(browser);
	     
         //We create the main panel
	     panel.setLayout(new BorderLayout(0, 0));
	     panel.setPreferredSize(new Dimension(this.getWidth(),this.getHeight()));
	     panel.setBackground(Color.DARK_GRAY);
	     panel.setRequestFocusEnabled(true);
	     
	     //With the canvas
	     	glCanvas.setFocusable(true);
	     	glCanvas.setBackground(Color.DARK_GRAY);
	     	glCanvas.setIgnoreRepaint(true);
	     	
	     panel.add(glCanvas);

	     //Panelleft is actually useless
	     panelLeft.setLayout(new BorderLayout(0,0));
	     panelLeft.add(panel,BorderLayout.CENTER);
	     //panelLeft.add(panelOptions,BorderLayout.SOUTH); IN CASE YOU NEED PANEL OPTION (WE NEEDN'T)
	  
	     
	     frame.getContentPane().add(panelHelp,BorderLayout.EAST);
	     frame.getContentPane().add(panelLeft,BorderLayout.CENTER);
	     
	     frame.pack();
	     frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
	     
	     
	     
	   // this.createMenuBar(); If you needed a menubar...
	     
	     frame.setVisible(true);
	     this.renderMenu();

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

JLabel masseVolumiqueLabel = new JLabel();
public void updateLabels()
{
	masseVolumiqueLabel.setText("masse volumique(kg/m^3)="+Maths.dfloor(FasT.getFasT().getPhysicsHandler().liquid.getMasseVolumique()));
}

private void renderMenu()
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
    
    file.add(exit);
    file.add(about);
    
    JMenu game = new JMenu("game");
    play.setText("play");
    play.addActionListener(new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent e) {
			FasT.getFasT().setPaused(!FasT.getFasT().isPaused());
		}
    });
    game.add(play);
    
    
    
    String[] states = {"chute libre","chute dans un liquide","chute avec frottements","chute avec rebonds"};
    JRadioButtonMenuItem[] items = new JRadioButtonMenuItem[states.length];
    ButtonGroup modeGroup = new ButtonGroup();
    
    ActionListener modeListener = new ActionListener(){
		public void actionPerformed(ActionEvent e) {
			  for ( int i = 0; i < items.length; i++ )
			  {
		            if ( e.getSource() == items[ i ] ) {
		               FasT.getFasT().getPhysicsHandler().simulationLevel=i+1;
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
    
    items[0].setSelected(true);
    
    JMenu liquid = new JMenu("liquid");
    JSlider masseVolumiqueSlider = new JSlider();
    masseVolumiqueSlider.setMinimum(500);
  /*  masseVolumiqueSlider.setMajorTickSpacing(300);
    masseVolumiqueSlider.setPaintTicks(true);
    masseVolumiqueSlider.setSnapToTicks(true);*/
    masseVolumiqueSlider.setMaximum(1500);
    masseVolumiqueSlider.addChangeListener(new ChangeListener()
    		{
				@Override
				public void stateChanged(ChangeEvent e) {
					FasT.getFasT().getPhysicsHandler().liquid = new Liquid(masseVolumiqueSlider.getValue(),FasT.getFasT().getPhysicsHandler().liquid);
					updateLabels();
				}
    		});
    
    liquid.add(masseVolumiqueLabel);
    liquid.add(masseVolumiqueSlider);
    
    game.add(liquid);
    
    
    JMenu spawn = new JMenu("spawn");
    JMenuItem ball = new JMenuItem("ball");
    ball.addActionListener(new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent e) {
			FasT.getFasT().getEntityHandler().spawn(new Ball(new Point(Mouse.getX(),Mouse.getY()).mouseToReal(),FasT.getFasT().getEntityHandler()));
		}
    });
    
    spawn.add(ball);
    
    
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
		        	 if((entity=FasT.getFasT().getEntityHandler().getEntityUnder(new Point(e.getX(),height-e.getY()).mouseToReal()))!=null)
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
		 this.drawSquare(new Point(-1,-1), new Point(1,1));
		 
		 this.drawLine(new Point(1,1).toReal(), new Point(1,1).toReal().add(new Point(1,0)));
		 this.drawLine(new Point(1,10), new Point(20,10));
		 
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

	/*
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
     
     

	public void EndRender() {
		GL11.glPopMatrix();

		Display.update();
	}
 
 
}
