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
	
	public Entity getEntityUnderMouse() {
		for(Entity b : this.getEntities())
		{
			if(BB.distanceBetweenTwoPoints(new Point(Mouse.getEventX(),Mouse.getEventY()),b.getPosition())<((Ball) b).getRadius())
			{
				return b;
			}	
		}
		return null;
	}
	

	public ArrayList<Entity> getEntities() {
		return new ArrayList<Entity>(this.entities);
	}

}
