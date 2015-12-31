package game.entities;

import java.awt.AWTException;
import java.awt.Cursor;
import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Dictionary;
import java.util.Enumeration;

import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MenuKeyEvent;
import javax.swing.event.MenuKeyListener;

import org.lwjgl.input.Keyboard;

import game.FasT;
import javafx.scene.input.KeyCode;
import physics.BBCircle;
import physics.maths.Angle;
import physics.maths.C;
import physics.maths.Maths;
import physics.maths.Normal;
import physics.maths.Point;
import physics.maths.Vector;
import physics.maths.Normal.Unit;
import render.Render;

public class Ball extends Entity {

	//Put init method like so 
	/*
	  abstract class A {
  public final void init() {
    // insert prologue here
    initImpl();
    // insert epilogue here
  }
  protected abstract void initImpl();
}

class B extends A {
  protected void initImpl() {
    // ...
  }
}
 
	 */
	private double radius; // m
	private double masseVolumique = 3000; //7500 (acier) 20 (coton) 700 (acajou) kg/m^3
	
	public double getRadius()
	{
		return this.radius;
	}
	private void setRadius(double radius)
	{
		this.radius=radius;
		updateSizes();
	}
	
	private void setMV(double mv)
	{
		this.masseVolumique=mv;
		this.updateSizes();
	}
	
	
	//DO NOT OVERRIDE (Override getVolume each time)
	@Override
	public double getMass()
	{
		return masseVolumique*getVolume();
	}
	
	public double getVolume()
	{
		return (4*Math.PI*Math.pow(this.radius,3))/3;
	}
	
	public double getAire() {
		return 4*Math.PI*Math.pow(this.getRadius(),2);
	}
	
	@Override
	public double getFlow()
	{
		return this.radius*2;
	}
	
	public void updateSizes()
	{
		radiusLabel.setText("radius(m)="+Maths.dfloor(getRadius()));
		mvLabel.setText("m/V(kg/m^3)="+Maths.dfloor(masseVolumique));
		massLabel.setText("mass(kg)="+Maths.dfloor(this.getMass()));
		volumeLabel.setText("volume(m^3)="+Maths.dfloor(this.getVolume()));
	}
	
	public JSlider slidermv = new JSlider();
	
	JLabel radiusLabel = new JLabel();
	JLabel mvLabel = new JLabel();
	JLabel massLabel = new JLabel();
	JLabel volumeLabel = new JLabel();
	
	JLabel bouncingLabel = new JLabel("bouncing");

	
	private void addToPopupMenu()
	{
		JSlider sliderRadius = new JSlider();
		sliderRadius.setMinimum(10);
		sliderRadius.setMaximum(400);
		sliderRadius.setMajorTickSpacing(40);
		sliderRadius.setPaintTicks(true);
		sliderRadius.setSnapToTicks(true);
		sliderRadius.setValue((int)(this.getRadius()*100));
		sliderRadius.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e) {
				setRadius((double)sliderRadius.getValue()/100);
			}
		});
		slidermv.setMinimum(0);
		slidermv.setMaximum(7500);
	//	slidermv.setMinorTickSpacing(50);
		slidermv.setMajorTickSpacing(500);
		slidermv.setPaintTicks(true);
		slidermv.setSnapToTicks(true);
		slidermv.setValue((int)this.masseVolumique);
		slidermv.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e) {
				setMV(slidermv.getValue()==0 ? 1 : slidermv.getValue());
			}	
		});
	
				
		
		tweak.add(radiusLabel);
		tweak.add(sliderRadius);
		tweak.add(mvLabel);
		tweak.add(slidermv);
		tweak.add(bouncingLabel);
		
		tweak.add(massLabel);
		tweak.add(volumeLabel);

		this.updateSizes();
		//sliderRadius.setFocusable(false);
		//slidermv.setFocusable(false);
		slidermv.addKeyListener(new KeyListener()
				{
					boolean BOOST = false;
					@Override
					public void keyTyped(KeyEvent e) {}
					@Override
					public void keyPressed(KeyEvent e) {
						if(e.getKeyCode()==KeyEvent.VK_SHIFT)
						{
								((JSlider) e.getSource()).setSnapToTicks(false);
						}
						else if(e.getKeyCode()==KeyEvent.VK_ALT)
						{
							BOOST = ! BOOST;
							((JSlider) e.getSource()).setMaximum(BOOST ? 750000000 : 7500);
							((JSlider)e.getSource()).setValue(((JSlider)e.getSource()).getValue() * (BOOST ? 100000 : 1/100000));
							((JSlider) e.getSource()).setMajorTickSpacing(BOOST ? 50000000 : 500);
						}
					}
					@Override
					public void keyReleased(KeyEvent e) {
						if(e.getKeyCode()==KeyEvent.VK_SHIFT)
						{
							((JSlider) e.getSource()).setSnapToTicks(true);
						}
					}});
		sliderRadius.addKeyListener(new KeyListener()
		{
			@Override
			public void keyTyped(KeyEvent e) {}
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_SHIFT)
				{
						((JSlider) e.getSource()).setSnapToTicks(false);
				}}
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_SHIFT)
				{
					((JSlider) e.getSource()).setSnapToTicks(true);
				}}});

		
		
		popupMenu.updateUI();
		/*tweak.addMenuKeyListener(new MenuKeyListener(){
			@Override
			public void menuKeyTyped(MenuKeyEvent e) {
				FasT.getFasT().getLogger().error("key");
			}
			@Override
			public void menuKeyPressed(MenuKeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_SHIFT)
				{
						slidermv.setSnapToTicks(false);
						slidermv.updateUI();
				}}
			@Override
			public void menuKeyReleased(MenuKeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_SHIFT)
				{
					slidermv.setSnapToTicks(true);
					slidermv.updateUI();
				}}});*/
	}
	
	public Ball(Point position, EntityHandler entityHandler)
	{
		super(position,0.0312,entityHandler);
		//FasT.getFasT().getLogger().debug("MASSE=" + masseVolumique*4/3*Math.PI*Math.pow(10,3));
		
		this.setRadius(0.1);
		this.boundingBox = new BBCircle(this.position, radius);
		//this.velocity=new C(new Angle(Angle.convertToRad(45)),35);
		//this.velocity=new C(new Angle(Angle.convertToRad(-90)),20).sum(new C(new Angle(Angle.convertToRad(0)),10));
		//this.velocity=new C(new Angle(Angle.convertToRad(90)),20).sum(new C(new Angle(Angle.convertToRad(0)),30));
		//this.applyForce(new C(new Angle(Angle.convertToRad(35)),100000000));
		this.positions.add(this.position);
		this.velocity=new C(new Angle(Angle.convertToRad(90)),8.33);
		this.addToPopupMenu();
	}
	
	public Ball(Point position,double radius,EntityHandler entityHandler)
	{
		super(position,0.0312,entityHandler);
		this.radius=radius;
		this.boundingBox = new BBCircle(this.position, radius);
		this.setMV(1.9*Math.pow(10, 27));
		this.addToPopupMenu();
	}

	@Override
	public void render(Render render) {
		float[] color = {(float) 0.8,(float) 0.1, (float) 0.3};
		render.drawCircle(this.position,this.radius,color);	
		render.drawLines(this.positions);
		//render.drawLine(this.getPosition(), new Point(this.getPosition().getX()+this.getVelocity().getRe(),this.getPosition().getY()+this.getVelocity().getIm()));
	}
	
	public double getVolumeImmerged(Box box) {
		double dh = this.getPosition().getY()-(box.getPosition().getY()+((box.getMax().getY()-box.getPosition().getY())*box.getLiquid().getLevel()));
		if(dh>=this.getRadius() || box.getLiquid().getLevel()==0)
		{
			return 0;
		}
		if(dh<=-this.getRadius())
		{
			return this.getVolume();
		}

		double h = this.getRadius()-dh;

		return Math.PI/3*Math.pow(h, 2)*(3*this.getRadius()-h);
	}

	@Override
	public boolean drag(Point p,long currentNanoTime)
	{
		double constante = 3*2;
		double time = currentNanoTime/Math.pow(10, 9);
		double deltat = time-lastUpdateTime;
		//FasT.getFasT().getLogger().debug(deltat);
		
		C c = new C(p.getX(),p.getY());
		C c1 = new C(c.getTheta(),c.getRho()/deltat);
		
		//FasT.getFasT().getLogger().debug(deltat);
		
		
		this.setVelocity(c1);
		this.setPosition(this.getPosition().add(p));
		//USE RHO BETWEEN LAST POINT AND THISONE TO CALCULATE SPEED THEN PROCESS THE ANGLE WITH THE VECTOR 
		this.positions.clear();
		lastUpdateTime=time;
		return true;
	}
	

	public boolean hoover(Point p)
	{
	//	FasT.getFasT().getLogger().debug("hoovering ball" + (FasT.getFasT().getRender().getCanvas().hasFocus() ? " focus" : " no focus"));
		//FasT.getFasT().getRender().getFrame().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		FasT.getFasT().getRender().getCanvas().setCursor(java.awt.Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		return true;
	}
	
	public boolean shouldMenu(Point p)
	{
		return true;
	}
	
	
}
