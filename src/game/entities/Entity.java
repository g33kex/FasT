package game.entities;

import java.util.ArrayList;
import java.util.UUID;

import game.FasT;
import physics.BB;
import physics.BBCircle;
import physics.Physics;
import physics.maths.C;
import physics.maths.Normal;
import physics.maths.Normal.Unit;
import physics.maths.Point;
import physics.maths.Vector;
import render.Render;

public abstract class Entity 
{
	private final UUID uuid;
	
	protected Point position;
	protected C velocity;

	protected ArrayList<Vector> F;
	
	
	protected BB boundingBox;
	
	protected final double mass;
	
	
	public ArrayList<Point> positions = new ArrayList<Point>();
	
	private ArrayList<C> instantForces = new ArrayList<C>();
	
	
	private boolean isBeingDragged = false;
	
	public Entity(Point position, double mass) {
		this.uuid = UUID.randomUUID();
		this.position=position;
		this.mass=mass;
		this.velocity=new C(0,0);
	}
	
	public void setPosition(Point p)
	{
		this.position = p;
	}
	
	public void setBeingDragged(boolean isBeingDragged)
	{
		this.isBeingDragged=isBeingDragged;
	}
	
	public boolean getbeingDraged()
	{
		return this.isBeingDragged;
	}
	
	private double lastUpdateTime=0;
	public void drag(int X, int Y,long currentNanoTime) {
		double constante = 3*2;
		double time = currentNanoTime/Math.pow(10, 9);
		double deltat = time-lastUpdateTime;
		//FasT.getFasT().getLogger().debug(deltat);
		
		C c = new C(X,Y);
		C c1 = new C(c.getTheta(),Normal.normal(c.getRho()/deltat/constante,Unit.cm));
		
		//FasT.getFasT().getLogger().debug(deltat);
		
		
		this.setVelocity(c1);
		this.setPosition(new Point(this.getPosition().getX()+X,this.getPosition().getY()+Y));
		//USE RHO BETWEEN LAST POINT AND THISONE TO CALCULATE SPEED THEN PROCESS THE ANGLE WITH THE VECTOR 
		this.positions.clear();
		lastUpdateTime=time;
	}

	//TODO : m/S = cm/S ???? 
	public Point getPosition()
	{
		return this.position;
	}
	
	public double getMass()
	{
		return this.mass;
	}
	
	
	public C getVelocity() {
		return velocity;
	}

	public void setVelocity(C velocity) {
		this.velocity = velocity;
	}
	
	public void applyForce(C c)
	{
		this.instantForces.add(c);
	}
	
	public ArrayList<C> getInstantForces() {
		return this.instantForces;
	}
	
	public abstract void render(Render render);
	
	public final void update(Physics physics,double deltat,ArrayList<Entity> entities)
	{
		/*if(this instanceof Ball)
		{
			this.boundingBox = new BBCircle(this.position, 10);
		}*/
		if(!this.isBeingDragged)
		{
			if(physics.update(this, deltat,entities))
			{
				this.positions.add(this.position);
				//lastUpdateTime=System.nanoTime()/Math.pow(10, 9);
			}
		}
		this.instantForces.clear();
	}

	
	
	public UUID getUUID() {
		return uuid;
	}

	public boolean collidesWith(Entity entity1) {
		if(this instanceof Ball && entity1 instanceof Ball)
		{
			return BB.collisionTwoBalls(this.getPosition(),((Ball) this).getRadius(),entity1.getPosition(),((Ball)entity1).getRadius());
		}

		//BB.collisionTwoSquares();
		//BB.collisionSquareBall();
		//BB.collisionCircleWall();
		//BB.collisionSquareWall();
		
		return false;
		
		
	}



}
