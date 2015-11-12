package game.entities;

import game.FasT;
import physics.BBCircle;
import physics.maths.Angle;
import physics.maths.C;
import physics.maths.Point;
import physics.maths.Vector;
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
	private final double radius; // m
	private final static double masseVolumique = 0.1; //7500 (acier) 20 (coton) 700 (acajou)
	
	public double getRadius()
	{
		return this.radius;
	}
	
	public Ball(Point position)
	{
		super(position,0.0312);
		FasT.getFasT().getLogger().debug("MASSE=" + masseVolumique*4/3*Math.PI*Math.pow(10,3));
		
		this.radius=10;
		this.boundingBox = new BBCircle(this.position, radius);
		//this.velocity=new C(new Angle(Angle.convertToRad(45)),35);
		//this.velocity=new C(new Angle(Angle.convertToRad(-90)),20).sum(new C(new Angle(Angle.convertToRad(0)),10));
		//this.velocity=new C(new Angle(Angle.convertToRad(90)),20).sum(new C(new Angle(Angle.convertToRad(0)),30));
		//this.applyForce(new C(new Angle(Angle.convertToRad(35)),100000000));
		this.positions.add(this.position);
	}
	
	public Ball(Point position,double radius)
	{
		super(position,-1);
		this.radius=radius;
		this.boundingBox = new BBCircle(this.position, radius);
	}

	@Override
	public void render(Render render) {
		float[] color = {(float) 0.8,(float) 0.1, (float) 0.3};
		render.drawCircle(this.position,this.radius,color);	
		render.drawLines(this.positions);
	}
	

}
