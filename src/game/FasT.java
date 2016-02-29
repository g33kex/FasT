/*
 *  FasT -- A Fast algorithm for simulaTions. 
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

package game;

import game.entities.Ball;
import game.entities.Box;
import game.entities.Entity;
import game.entities.EntityHandler;
import game.entities.Wall;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Label;
import java.awt.Toolkit;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.UUID;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;

import log.Logger;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import com.sun.glass.ui.Cursor;

import physics.BB;
import physics.Physics;
import physics.maths.Angle;
import physics.maths.C;
import physics.maths.Normal;
import physics.maths.Point;
import physics.maths.Vector;
import render.Render;


public class FasT {
///CONFIG
	
	
	//Force du rebond + réaction du support
	private boolean pos=true;
	public boolean debug = false;
	
	
	private volatile boolean shouldExit = false;
	
	
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
	
	public EntityHandler getEntityHandler()
	{
		return this.entityHandler;
	}
	
	public Physics getPhysicsHandler()	{
		return this.physics;
	}

	public Render getRender()
	{
		return this.render;
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
	
	public void setPaused(boolean b) {this.pause=b;this.render.play.setText(b ? "play" : "pause");
	
	if(this.isPaused())
	{
		
		//TODO : Tweaking is here !
		
		/*double tmpy = 0;
		for(Point p : entityHandler.get(this.theBall).positions)
		{
			if(p.getY()>tmpy)
				tmpy=p.getY();
		}*/
		//double r = tmpy-entityHandler.get(this.theBall).positions.get(0).getY();
		//this.getLogger().debug("H = "+r);
	}
	
	
	}
	public boolean isPaused() {return this.pause;}

	
	//DISPLAY
	private final int width = 870;//850;//1000;
	private final int height = 660;//550;//650;
	private final String title = "FasT";
	
	private Point ballInit;
	
	
	public UUID theBall;
	public UUID theBox = UUID.randomUUID();
	
	public FasT() throws LWJGLException {
		this.on=true;
		//this.pause=false;
		FasT.theFasT = this;
		//Vector v = new Vector(-1,0,true);
		//C c = new C(-1,1);
		log.horodate("We'llcome too FasT !");
		log.info("[Starting]");
		
		
		//log.debug(c.toString());
		//log.debug(c.toStringAng(true));
		//this.getLogger().debug(v.complex.ToPolar().toString());
		//throw new LWJGLException();
	}
	
	private void init() throws LWJGLException
	{
		render.init(this.width,this.height,this.title);
	//	this.theBall = entityHandler.spawn(new Ball(new Point(1,3),this.getEntityHandler()));
		
		
		//DEFAULT
		this.theBall = entityHandler.spawn(new Ball(new Point(0,1),this.getEntityHandler()));
		
		//TWEAK
		//this.theBall = entityHandler.spawn(new Ball(new Point(0,50),this.getEntityHandler(),0.010));
		
		
		
		
		//VELOCITYentityHandler.get(theBall).setVelocity(new C(0.5,0));
		
		//BALL2 entityHandler.spawn(new Ball(new Point(1,0),this.getEntityHandler()));
		
		//entityHandler.spawn(new Ball(new Point(40,500)));
		//entityHandler.spawn(new Wall(new Point(0,20),new Point(this.width,90)));
		ballInit=entityHandler.get(this.theBall).getPosition();
		
		
		
		//INIT = 
		entityHandler.spawn(new Wall(new Point(-2,-0.5),4,new Angle(0),this.getEntityHandler()));
		entityHandler.spawn(new Box(new Point(-4,-3),new Point(4,-1),1,Liquid.WATER(), this.entityHandler));
		
		this.spawnWalls();
		
		
		render.updateLabels();
	}
	private void spawnWalls()
	{
		Box l = (Box) FasT.getFasT().getEntityHandler().get(FasT.getFasT().theBox);
		entityHandler.destroy(this.theBox);
	//	this.theBox=entityHandler.spawn(new Box(new Point(-this.width/2,-this.height/2).toReal(), new Point(this.width/2,this.height/2).toReal(),4,this.entityHandler));
		
		this.theBox=entityHandler.spawn(new Box(new Point(0,0).mouseToReal(),new Point(Display.getWidth(),Display.getHeight()).mouseToReal(),4,l==null ? Liquid.AIR() : l.getLiquid(), this.entityHandler),true);
		
	    this.getRender().game.remove(this.getRender().liquid);
	    
		this.getRender().liquid = new JMenu("Liquid");
		 
	    Box b = (Box) FasT.getFasT().getEntityHandler().get(FasT.getFasT().theBox);
	    b.getPopupMenuFrom(this.getRender().liquid);
	    
	    JCheckBoxMenuItem apply = new  JCheckBoxMenuItem("Apply Everywhere");
	    apply.setState(FasT.getFasT().getPhysicsHandler().applyEverywhere);
	    apply.addItemListener(new ItemListener()
		{
			@Override
			public void itemStateChanged(ItemEvent e) {
				FasT.getFasT().getPhysicsHandler().applyEverywhere=apply.getState();
			}
		});
	    this.getRender().liquid.add(apply);
	    
	    this.getRender().game.add(this.getRender().liquid);
	   // this.getRender().popupMenu.updateUI();
		
		/*entityHandler.clear("wall");
		entityHandler.spawn(new Wall(new Point(0,0).toReal(),Normal.toReal(this.width),new Angle(0),this.entityHandler));
		entityHandler.spawn(new Wall(new Point(1,0).toReal(),Normal.toReal(this.height),new Angle(Math.PI/2),this.entityHandler));
		entityHandler.spawn(new Wall(new Point(this.width,this.height-1).toReal(),Normal.toReal(this.width),new Angle(Math.PI),this.entityHandler));
		entityHandler.spawn(new Wall(new Point(this.width,this.height).toReal(),Normal.toReal(this.height),new Angle(Math.PI*3/2),this.entityHandler));*/
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
             
             if(Display.wasResized())
            	this.spawnWalls();
	           lastRenderTime = now;
			 	//log.info("TIME TO RENDER : "+((System.nanoTime()-beforeRender)/1000000)+"ms");
	            //Update the frames we got.
	            int thisSecond = (int) (lastUpdateTime / 1000000000);
	            if (thisSecond > lastSecondTime)
	            {
	               //log.info("NEW SECOND " + thisSecond + " " + frameCount);
	            	//this.TIME = this.TIME+1;
	            	
	               fps = frameCount;
	               frameCount = 0;
	               lastSecondTime = thisSecond;
	            //   log.info("X distance = "+(entityHandler.get(this.theBall).getPosition().getX()-this.ballInit.getX())+"|Y distance = "+(entityHandler.get(this.theBall).getPosition().getY()-this.ballInit.getY()));
	               if(this.entityHandler.get(this.theBall)!=null)
	               {
	            	   this.ballInit=entityHandler.get(this.theBall).getPosition();
	               }
	            }
	            //Yield until it has been at least the target time between renders. This saves the CPU from hogging.
	            while ( now - lastRenderTime < this.targetRenderPeriod && now - lastUpdateTime < this.period)
	            {
	               Thread.yield();
	               //-XX:PerfDataSamplingInterval=500
	               //This stops the app from consuming all your CPU. It makes this slightly less accurate, but is worth it.
	               //You can remove this line and it will still work (better), your CPU just climbs on certain OSes.
	               //FYI on some OS's this can cause pretty bad stuttering. Scroll down and have a look at different peoples' solutions to this.
	               try {Thread.sleep(this.maxSleep);} catch(Exception e) {} 
	               
	               now = System.nanoTime();
	            }
		}
		exit();

	}
	
	public double debugtimeinseconds = 0.2;
	
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
				this.setPaused(!this.isPaused());
				if(!this.pause)
					TIME = System.nanoTime();
			}
			if(Keyboard.getEventKey() == Keyboard.KEY_RIGHT)
			{
				this.setPaused(true);
				this.updateEntities(debugtimeinseconds);
			}
			else if(Keyboard.getEventKey() == Keyboard.KEY_LEFT)
			{
				this.setPaused(true);
				this.updateEntities(-debugtimeinseconds);
			}
			if(Keyboard.getEventKey() == 13)
			{
				Normal.unzoom(24);
				this.spawnWalls();
			}
			if(Keyboard.getEventKey() == 53)
			{
				Normal.zoom(24);
				this.spawnWalls();
			}
			
			if(!this.log.shallLog)
				return;
			
			if(Keyboard.getEventKey() == Keyboard.KEY_T)
			{
				entityHandler.spawn(new Ball(new Point(10,500).toPlan(),this.getEntityHandler()));
				this.period = Math.pow(10, 8)/0.1;
			}
			if(Keyboard.getEventKey() == Keyboard.KEY_1)
			{
				entityHandler.spawn(new Ball(new Point(Mouse.getX()-Normal.x,Mouse.getY()-Normal.y).toReal(),0.3,this.getEntityHandler()));
			}
			if(Keyboard.getEventKey() == Keyboard.KEY_2)
			{
				entityHandler.spawn(new Ball(new Point(Mouse.getX()-Normal.x,Mouse.getY()-Normal.y).toReal(),0.70,this.getEntityHandler()));
			}
			if(Keyboard.getEventKey() == Keyboard.KEY_3)
			{
				entityHandler.spawn(new Ball(new Point(Mouse.getX()-Normal.x,Mouse.getY()-Normal.y).toReal(),1.5,this.getEntityHandler()));
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
				this.entityHandler.destroy(this.entityHandler.getEntityUnder(new Point(Mouse.getEventX(),Mouse.getEventY())));
			}
		
			if(Keyboard.getEventKey() == Keyboard.KEY_Z)
			{
		        GL11.glOrtho(0, 5,0,5, -1.0, 1.0);
			}
			if(Keyboard.getEventKey() == Keyboard.KEY_Y)
			{
				  GL11.glOrtho(5, 0,5,0, -1.0, 1.0);
			}
			if(Keyboard.getEventKey() == Keyboard.KEY_F)
			{
			     this.getRender();
				Render.requestToggleFullScreen(this.getRender().getFrame());
			}
		}
	}

	private void handleMouse() 
	{
		boolean c = true;
		for(Entity entitye : this.getEntityHandler().getEntities())
		{
			 if(entitye.getBeingDragged())
				 c=false;
					 
		}
		Entity entityd;
		if(c && !((entityd = this.getEntityHandler().getEntityUnder(new Point(Mouse.getX(),Mouse.getY()).mouseToReal())) != null && (entityd.hoover(new Point(Mouse.getX(),Mouse.getY()).mouseToReal()))))
		{
			//FasT.getFasT().getRender().getFrame().setCursor(java.awt.Cursor.getDefaultCursor());
				this.getRender().getCanvas().setCursor(java.awt.Cursor.getDefaultCursor());
		}
			
		//TODO : Mouse
		while(Mouse.next())
		{
			/*for(Entity entityc : this.getEntityHandler().getEntities())
			{
				if(BB.distanceBetweenTwoPoints(new Point(Mouse.getX(),Mouse.getY()).mouseToReal().toPlan(),entityc.getPosition().toPlan())<=5)
				{
					FasT.getFasT().getRender().getFrame().setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.SW_RESIZE_CURSOR));
				}
			}*/
			
			
			//log.warning("Wheel speed="+Mouse.getEventDWheel());
			//Let's handle the mouse wheel (to change the zoom)
			if(pos==true)
			{
				if(Mouse.getEventDWheel()>1)
				{
					Normal.unzoom(1);
					this.spawnWalls();
					pos=true;
				}
				else
				{
					pos=false;
				}
			}
			else
			{
				if(Mouse.getEventDWheel()<-1)
				{
					Normal.zoom(1);
					this.spawnWalls();
					pos=false;
				}
				else
				{
					pos=true;
				}
			}
			
			if(Mouse.getEventButtonState() && Mouse.getEventButton()==1)
			{
				Point p = new Point(Mouse.getEventX(),this.getRender().glCanvas.getHeight()-Mouse.getEventY());
				Point pr = new Point(Mouse.getEventX(),Mouse.getEventY());
				 Entity entity;
	        	 if((entity=FasT.getFasT().getEntityHandler().getEntityUnder(new Point(pr.getX(),pr.getY()).mouseToReal()))!=null && entity.shouldMenu(new Point(pr.getX(),pr.getY()).mouseToReal()))
	        	 {
	        		 entity.getPopupMenu().show();
	        		 entity.getPopupMenu().show(this.getRender().glCanvas,(int)p.getX(),(int)p.getY());
	        	 }
	        	 else
	        	 {
	        		   render.popupMenu.show();
	        		   render.popupMenu.show(this.getRender().glCanvas,(int)p.getX(),(int)p.getY());
	        	 }
	        	 continue;
			}
			else if(Mouse.getEventButtonState())
			{
				render.popupMenu.hide();
				for(Entity entity : FasT.getFasT().getEntityHandler().getEntities())
				{
					entity.getPopupMenu().hide();
				}
			}
		
			
			if(Mouse.isButtonDown(0) && Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) && !Keyboard.isKeyDown(Keyboard.KEY_RMENU))
			{
				Normal.x+=Mouse.getEventDX();
				Normal.rx=Normal.toReal(Normal.x);
				Normal.y+=Mouse.getEventDY();
				Normal.ry=Normal.toReal(Normal.y);
				this.spawnWalls();
				continue;
			}
			
			//Handeling home made drag events
			if(!Mouse.isButtonDown(0) || Keyboard.isKeyDown(Keyboard.KEY_RMENU))
			{
				for(Entity entity : this.entityHandler.getEntities()) {
					if(Mouse.getEventDX()==Mouse.getEventDX() && Mouse.getEventDY()==Mouse.getEventDY())
					{
					entity.setBeingDragged(false, new Point(Mouse.getEventDX(),Mouse.getEventDY()).mouseToReal());
					}
				}
			}
			
			boolean r = false;
			if(Mouse.getEventButtonState() && Mouse.getEventButton()==0)
			{	
				Entity e;
				if(( e = entityHandler.getEntityUnder(new Point(Mouse.getEventX(),Mouse.getEventY()).mouseToReal())) != null)
				{
						if(Keyboard.isKeyDown(Keyboard.KEY_RMENU))
						{
						if(!Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
						{
							for(Entity l : this.entityHandler.getEntities())
							{
								if(l!=e)	
									l.setSelected(false);
							}
						}
						e.setSelected(!e.isSelected());
						r=true;
						}
						else
						{
							if(Mouse.getEventDX()==Mouse.getEventDX() && Mouse.getEventDY()==Mouse.getEventDY())
							e.setBeingDragged(true, new Point(Mouse.getEventX(),Mouse.getEventY()).mouseToReal());
						}
				}
				else if(!Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) && Keyboard.isKeyDown(Keyboard.KEY_RMENU))
				{
					for(Entity l : this.entityHandler.getEntities())
					{
						l.setSelected(false);
					}
				}
			}

			for(Entity b : this.entityHandler.getEntities())
			{
				if(b.getBeingDragged())
				{
					r=b.drag(new Point(Mouse.getEventDX(),Mouse.getEventDY()).toReal(),Mouse.getEventNanoseconds());
					break;
				}
			}
			
		//	if(r)
		//		continue;
			
			/*Entity entityd;
			if(!((entityd = this.getEntityHandler().getEntityUnder(new Point(Mouse.getX(),Mouse.getY()).mouseToReal())) != null && entityd.hoover(new Point(Mouse.getX(),Mouse.getY()).mouseToReal())))
				this.getRender().getCanvas().setCursor(java.awt.Cursor.getDefaultCursor());*/
			
			//Handle moving panel when draging with shift enabled
		
			
			
			/*if(Mouse.getEventButton()==1 && Mouse.getEventButtonState())
			{
				/* Entity entity;
	        	 if((entity=FasT.getFasT().getEntityHandler().getEntityUnder(new Point(e.getX(),e.getY())))!=null)
	        	 {
	        		 FasT.getFasT().getLogger().warning("THING UNDER MOUSE DETECTED");
	        	 }
	        	 else
	        	 {
	        		   this.render.popupMenu.show(Display.getParent(),Mouse.getEventX(), this.height-Mouse.getEventY()-10);
	        	 }
			}*/
		}
	}
	
	
	//Main method : Update the positions of the entities and handle the mouse / keyboard
	
	long TIME = 0;
	private void update(double deltat) 
	{
		if(this.shouldExit)
			exit();
		this.handleKeyboard();
		this.handleMouse();
		
		if(!this.isPaused())
		{
		double currentTime = System.nanoTime();
		double deltatime = currentTime-this.lastUpdateTime;
		double deltatimeinseconds = deltatime*Math.pow(10, -9);
		//log.debug("Hello, " + deltatimeinseconds);
		if(this.lastUpdateTime==-1) 
			deltatimeinseconds=0;

		this.updateEntities(deltatimeinseconds);
		
		//TODO : TWEAKHERE
		/*long t = System.nanoTime()-TIME;
		double time = (t*Math.pow(10, -9));
		
		if(time>=2 && time <=2.1)
		{
			FasT.getFasT().getLogger().debug("REPONSE QUAND T = 2 ="+this.getEntityHandler().get(this.theBall).getVelocity().getRho());
		}
		//FasT.getFasT().getLogger().debug(time);
		Ball ball = (Ball) this.getEntityHandler().get(this.theBall);
		if(ball.getPosition().getY()-ball.getRadius()<=0)
		{
			this.setPaused(true);
			FasT.getFasT().getLogger().debug("REPONSE AU SOL (T = "+time+")="+this.getEntityHandler().get(this.theBall).getVelocity().getRho());
		}*/
		
		
		}
		this.lastUpdateTime=System.nanoTime();
		
	}
	
	private void updateEntities(double deltatimeinseconds)
	{
		this.getLogger().resetV();
		for(Entity e : this.entityHandler.getEntities())
		{
			e.update(physics,deltatimeinseconds,this.entityHandler.getEntities());
		}
		
		this.getPhysicsHandler().updateCollisions(this.entityHandler.getEntities());
		
	}
	
	
	//Render entities and everything
	public void render()
	{
		boolean b = true;
		for(Entity entity : this.getEntityHandler().getEntities())
		{
			if(entity.isSelected())
			{
				if(!this.getRender().BPanel.isVisible())
				{
					this.getRender().BPanel.setVisible(true);
					this.getRender().panel.updateUI();
					this.getRender().setFlag();
				}
				b=false;
			}
		}
		if(b && this.getRender().BPanel.isVisible())
		{
			this.getRender().BPanel.setVisible(false);
			this.getRender().panel.updateUI();
			this.getRender().setFlag();
		}
		
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

	public void quit() {
		this.on=false;		
	}

	public void threadExit() {
		this.shouldExit=true;
	}

	
}
