package game.entities;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.UUID;

import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import game.Color;
import game.FasT;
import game.Liquid;
import physics.BB;
import physics.BBCircle;
import physics.Physics;
import physics.maths.C;
import physics.maths.Maths;
import physics.maths.Normal;
import physics.maths.Normal.Unit;
import physics.maths.Point;
import physics.maths.Vector;
import render.Render;

public abstract class Entity 
{
	private final UUID uuid;
	protected String name = "entity";
	
	protected Point position;
	protected C velocity;
	protected Color color = Color.RED;

	protected ArrayList<Vector> F;
	
	
	protected BB boundingBox;
	
	protected final double mass;
	
	
	public ArrayList<Point> positions = new ArrayList<Point>();
	
	private ArrayList<C> instantForces = new ArrayList<C>();
	
	
	private boolean isBeingDragged = false;
	
	protected final JPopupMenu popupMenu = new JPopupMenu();
	
	public JPopupMenu getPopupMenu() { return this.popupMenu;}
	protected JMenu tweak = new JMenu("tweak");
	
	protected JLabel speedLabel = new JLabel();
	
	protected final void createPopupMenu()
	{
		JMenuItem delete = new JMenuItem("delete");
		delete.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				entityHandler.destroy(entity);
			}
		});
		

		this.popupMenu.add(tweak);
		this.popupMenu.add(speedLabel);
		this.popupMenu.add(delete);
		
	}
	
	protected final EntityHandler entityHandler;
	protected final Entity entity = this;
	
	public Entity(Point position, double mass,EntityHandler entityHandler) {
		this.uuid=UUID.randomUUID();
		this.position=position;
		this.mass=mass;
		this.setVelocity(new C(0,0));
		
		this.entityHandler=entityHandler;
		
		this.createPopupMenu();
	}
	
	public void setPosition(Point p)
	{
		this.position = p;
	}
	
	public void setBeingDragged(boolean isBeingDragged, Point point)
	{
		this.isBeingDragged=isBeingDragged;
	}
	
	public boolean getBeingDragged()
	{
		return this.isBeingDragged;
	}
	
	protected double lastUpdateTime=0;
	
	public boolean drag(Point p,long currentNanoTime) {
		return false;
	}
	
	public boolean hoover(Point p) {return false;}

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
		this.speedLabel.setText("speed(m/s)="+Maths.dfloor(this.getVelocity().getMod()));
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
		if(this instanceof Ball && entity1 instanceof Wall)
		{
			return BB.collisionBallWall(this.getPosition(),((Ball) this).getRadius(),entity1.getPosition(),((Wall)entity1).getAngle());
		}
		if(this instanceof Ball && entity1 instanceof Box)
		{
			
		}

		//BB.collisionTwoSquares();
		//BB.collisionSquareBall();
		//BB.collisionCircleWall();
		//BB.collisionSquareWall();
		
		return false;
		
		
	}
	
	public boolean isInside(Entity entity1)
	{
		if(this instanceof Ball && entity1 instanceof Box)
		{
			return BB.collisionBallSquare(this.getPosition(),((Ball)this).getRadius(),entity1.getPosition(),((Box) entity1).getMax());
		}
		
		return this.collidesWith(entity1);
	}




}
