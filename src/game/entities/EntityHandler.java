package game.entities;

import java.util.ArrayList;
import java.util.UUID;

import org.lwjgl.input.Mouse;

import physics.BB;
import physics.maths.Point;

public class EntityHandler {

	private volatile ArrayList<Entity> entities = new ArrayList<Entity>();
	
	public EntityHandler()
	{
		
	}
	
	public UUID spawn(Entity e)
	{
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
		}
		return null;
	}


	public ArrayList<Entity> getEntities() {
		return new ArrayList<Entity>(this.entities);
	}

}
