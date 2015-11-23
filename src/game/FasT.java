package game;

import game.entities.Ball;
import game.entities.Entity;
import game.entities.EntityHandler;
import game.entities.Wall;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Label;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.UUID;

import javax.swing.JFrame;

import log.Logger;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import physics.BB;
import physics.Physics;
import physics.maths.Angle;
import physics.maths.C;
import physics.maths.Point;
import physics.maths.Vector;
import render.Render;


public class FasT {

	/*Engines (volatile to prevent being corrupted by threads)*/
	private volatile Logger log = new Logger();
	private volatile Render render = new Render();
	private volatile EntityHandler entityHandler = new EntityHandler();
	private volatile Physics physics = new Physics();
	private static volatile FasT theFasT; 
	
	public Logger getLogger()
	{
		return this.log;
	}
	
	public static FasT getFasT()
	{
		return theFasT;
	}
	//TIME
	private volatile boolean on = false;
	private volatile boolean pause = true;
	final private double periodinseconds=Math.pow(10, -3);
	final private double freq = 1/(periodinseconds*10);
	private double period = Math.pow(10, 8)/freq;
	final private int updatesBeforeRender = 5;
	final private double targetFPS = 60;
	//Higher is this, less CPU the program will consume but slower it will get (default is 100)
	final private int maxSleep = 20;
	final private double targetRenderPeriod = Math.pow(10,8)/targetFPS;
	private volatile int frameCount = 0;
	private volatile int fps = 60;
	
	private volatile double lastUpdateTime=-1;
	
	public void setPaused(boolean b) {this.pause=b;}
	
	//DISPLAY
	private final int width = 850;//1000;
	private final int height = 550;//650;
	private final String title = "FasT";
	
	private Point ballInit;
	
	
	private UUID theBall;
	
	public FasT() throws LWJGLException {
		this.on=true;
		//this.pause=false;
		FasT.theFasT = this;
		//Vector v = new Vector(-1,0,true);
		C c = new C(-1,1);
		log.info("STARTING");
		log.debug(c.toString());
		log.debug(c.toStringAng(true));
		//this.getLogger().debug(v.complex.ToPolar().toString());
		//throw new LWJGLException();
	}
	
	private void init() throws LWJGLException
	{
		render.init(this.width,this.height,this.title);

		this.theBall = entityHandler.spawn(new Ball(new Point(10,500)));
		//entityHandler.spawn(new Ball(new Point(40,500)));
	//	entityHandler.spawn(new Wall(new Point(0,20),new Point(this.width,90)));
		ballInit=entityHandler.get(this.theBall).getPosition();
	}
	
	
	public void run()
	{
		Thread loop = new Thread()
		{
			public void run()
			{
				try {
					init();
				} catch (LWJGLException e) {
					log.error(e.toString());
				}
				gameLoop();
			}
		};
		loop.start();
	}
	
	private void gameLoop()
	{
		double lastUpdateTime = System.nanoTime();
		double lastRenderTime = System.nanoTime();
		
		int lastSecondTime = (int) (lastUpdateTime / 1000000000);
		
		while(this.on) 
		{
			//if(Display.isCloseRequested())
			//	exit();
			
			double now = System.nanoTime();
			int updateCount = 0;
			
			 while( now - lastUpdateTime > this.period && updateCount < this.updatesBeforeRender)
	            {
	               this.update(Math.pow(10, -9)*(now-lastUpdateTime));
	               lastUpdateTime += this.period;
	               updateCount++;

	            }
			 
			 if ( now - lastUpdateTime > this.period)
	            {
	               lastUpdateTime = now - this.period;
	            }
			 
			// float interpolation = Math.min(1.0f, (float) ((now - lastUpdateTime) / this.period) );
	         //   double beforeRender = System.nanoTime();
             this.render();
	           lastRenderTime = now;
			 	//log.info("TIME TO RENDER : "+((System.nanoTime()-beforeRender)/1000000)+"ms");
	            //Update the frames we got.
	            int thisSecond = (int) (lastUpdateTime / 1000000000);
	            if (thisSecond > lastSecondTime)
	            {
	             //  log.info("NEW SECOND " + thisSecond + " " + frameCount);
	               fps = frameCount;
	               frameCount = 0;
	               lastSecondTime = thisSecond;
	            //   log.info("X distance = "+(entityHandler.get(this.theBall).getPosition().getX()-this.ballInit.getX())+"|Y distance = "+(entityHandler.get(this.theBall).getPosition().getY()-this.ballInit.getY()));
	               this.ballInit=entityHandler.get(this.theBall).getPosition();
	            }
	            //Yield until it has been at least the target time between renders. This saves the CPU from hogging.
	            while ( now - lastRenderTime < this.targetRenderPeriod && now - lastUpdateTime < this.period)
	            {
	               Thread.yield();
	               //This stops the app from consuming all your CPU. It makes this slightly less accurate, but is worth it.
	               //You can remove this line and it will still work (better), your CPU just climbs on certain OSes.
	               //FYI on some OS's this can cause pretty bad stuttering. Scroll down and have a look at different peoples' solutions to this.
	               try {Thread.sleep(this.maxSleep);} catch(Exception e) {} 
	               
	               now = System.nanoTime();
	            }
		}

	}
	
	private void handleKeyboard()
	{
		while(Keyboard.next())
		{
			if(!Keyboard.getEventKeyState())
				continue;
			
			log.info("Key '"+Keyboard.getEventCharacter()+ "' pressed ! ");
			
			if(Keyboard.getEventKey() == Keyboard.KEY_ESCAPE)
			{
				exit();
			}
			if(Keyboard.getEventKey() == Keyboard.KEY_SPACE)
			{
				this.pause=!this.pause;
			}
			if(Keyboard.getEventKey() == Keyboard.KEY_G)
			{
				this.physics.GROUND=!this.physics.GROUND;
			}
			if(Keyboard.getEventKey() == Keyboard.KEY_T)
			{
				entityHandler.spawn(new Ball(new Point(10,500)));
				this.period = Math.pow(10, 8)/0.1;
			}
			if(Keyboard.getEventKey() == Keyboard.KEY_1)
			{
				entityHandler.spawn(new Ball(new Point(Mouse.getX(),Mouse.getY()),30));
			}
			if(Keyboard.getEventKey() == Keyboard.KEY_2)
			{
				entityHandler.spawn(new Ball(new Point(Mouse.getX(),Mouse.getY()),70));
			}
			if(Keyboard.getEventKey() == Keyboard.KEY_3)
			{
				entityHandler.spawn(new Ball(new Point(Mouse.getX(),Mouse.getY()),150));
			}
			if(Keyboard.getEventKey() == Keyboard.KEY_L)
			{
				//Coup spécial : les 10 camion qui te tombe sur la tête : 19620 Newtons dans ta geulle 
				entityHandler.get(this.theBall).applyForce(new C(new Angle(Angle.convertToRad(90)),0.1));
			}
			if(Keyboard.getEventKey() == Keyboard.KEY_P)
			{
				//log.info("Width : " + render.getFrame().getWidth() + " & Height = " + render.getFrame().getHeight());
				try {
					Display.setDisplayMode(new DisplayMode(Display.getParent().getWidth(),Display.getParent().getHeight()-1));
				} catch (LWJGLException e) {
					
					e.printStackTrace();
				}
				
			//	Display.setLocation(Display.getParent().getX(), Display.getParent().getY());
				//render.getFrame().setSize(new Dimension(render.getFrame().getWidth(), render.getFrame().getHeight()+1));
		//		render.getFrame().pack();
			}
			if(Keyboard.getEventKey() == Keyboard.KEY_D)
			{
				this.entityHandler.destroy(this.entityHandler.getEntityUnderMouse());
			}
		}
	}
	
	//Force du rebond + réaction du support
	
	private void handleMouse() 
	{
		
		//TODO : Mouse
		while(Mouse.next())
		{
			if(!Mouse.isButtonDown(0))
			{
				for(Entity entity : this.entityHandler.getEntities()) {
					entity.setBeingDragged(false);
				}
			}
			
			
			if(Mouse.getEventButtonState())
			{	
				Entity e;
				if(( e = entityHandler.getEntityUnderMouse()) != null)
				{
					e.setBeingDragged(true);
				}
			}

			for(Entity b : this.entityHandler.getEntities())
			{
				if(b.getbeingDraged())
				{
					b.drag(Mouse.getEventDX(),Mouse.getEventDY(),Mouse.getEventNanoseconds());
					break;
				}
			}
		}
	}
	
	
	private void update(double deltat) 
	{
		this.handleKeyboard();
		this.handleMouse();
		
		if(!this.pause)
		{
		double currentTime = System.nanoTime();
		double deltatime = currentTime-this.lastUpdateTime;
		double deltatimeinseconds = deltatime*Math.pow(10, -9);
		//log.debug("Hello, " + deltatimeinseconds);
		if(this.lastUpdateTime==-1) 
			deltatimeinseconds=0;

		for(Entity e : this.entityHandler.getEntities())
		{
			e.update(physics,deltatimeinseconds,this.entityHandler.getEntities());
		}
		}
		this.lastUpdateTime=System.nanoTime();
	}
	
	public void render()
	{
		render.StartRender();
		
	//	render.drawSquare(new physics.maths.Point(20,20), new physics.maths.Point(200,200)); //Just a little square test
		
		for(Entity e : this.entityHandler.getEntities())
		{
			e.render(render);
		}
		
		render.EndRender();
		
		frameCount++;
	}
	
	
	public void exit()
	{
		this.on = false;
		log.warning("Exiting !");
		Display.destroy();
		System.exit(0);
	}

	
}
