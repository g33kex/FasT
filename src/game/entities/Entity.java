package game.entities;

import java.util.ArrayList;
import java.util.UUID;

import physics.BB;
import physics.BBCircle;
import physics.Physics;
import physics.maths.C;
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
		if(physics.update(this, deltat,entities))
			this.positions.add(this.position);
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
