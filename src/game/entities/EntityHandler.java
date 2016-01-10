package game.entities;

import java.util.ArrayList;
import java.util.UUID;

import org.lwjgl.input.Mouse;

import game.FasT;
import physics.BB;
import physics.maths.Point;

public class EntityHandler {

	private volatile ArrayList<Entity> entities = new ArrayList<Entity>();
	
	public EntityHandler()
	{
		
	}
	
	public UUID spawn(Entity e)
	{
		if(e instanceof Ball)
		{
			this.entities.add(0, e);
		}
		else
		{
			this.entities.remove(alwaysTop);
			this.entities.add(e);
			if(alwaysTop!=null)
			this.entities.add(alwaysTop);
		}
		return e.getUUID();
	}
	
	private Entity alwaysTop;
	public UUID spawn(Entity e,boolean shouldAlwaysTop)
	{
		if(shouldAlwaysTop)
		{
			alwaysTop=e;
		}
		this.entities.add(e);
		return e.getUUID();
	}

	public void destroy(Entity e)
	{
		this.entities.remove(e);
	}
	
	public void clear()
	{
		this.entities.clear();
	}
	
	

	public void clear(String s)
	{
		ArrayList<Entity> buffer = new ArrayList<Entity>();
		for(Entity e : this.entities)
		{
			if(e.name ==s)
				buffer.add(e);
		}
		this.entities.removeAll(buffer);
	}
	
	public void destroy(UUID uuid)
	{
		this.entities.remove(get(uuid));
	}
	
	public Entity get(UUID uuid)
	{
		for(Entity e : this.entities)
		{
			if(e.getUUID() == uuid)
				return e;
		}
		return null;
	}
	
	public Entity getEntityUnder(Point pos) {
		for(Entity b : this.getEntities())
		{
			if(b instanceof Ball)
			{
			if(BB.distanceBetweenTwoPoints(pos,b.getPosition())<((Ball) b).getRadius())
			{
				return b;
			}	
			}
			else if(b instanceof Box)
			{
				if(BB.pointInSquare(pos,((Box)b).getPosition(),((Box)b).getMax()) && !(b.getUUID()==FasT.getFasT().theBox))
						{
					return b;
						}
			}
		}
		return null;
	}


	public ArrayList<Entity> getEntities() {
		return new ArrayList<Entity>(this.entities);
	}

}
