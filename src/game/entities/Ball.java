package game.entities;

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
		slidermv.setMaximum(750000000);
	//	slidermv.setMinorTickSpacing(50);
		slidermv.setMajorTickSpacing(50000000);
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
		
		this.addToPopupMenu();
	}
	
	public Ball(Point position,double radius,EntityHandler entityHandler)
	{
		super(position,0.0312,entityHandler);
		this.radius=radius;
		this.boundingBox = new BBCircle(this.position, radius);
		this.addToPopupMenu();
	}

	@Override
	public void render(Render render) {
		float[] color = {(float) 0.8,(float) 0.1, (float) 0.3};
		render.drawCircle(this.position,this.radius,color);	
		render.drawLines(this.positions);
		//render.drawLine(this.getPosition(), new Point(this.getPosition().getX()+this.getVelocity().getRe(),this.getPosition().getY()+this.getVelocity().getIm()));
	}

}
