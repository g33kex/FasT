package game.entities;

import java.util.ArrayList;
import java.util.UUID;

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

	public ArrayList<Entity> getEntities() {
		return new ArrayList<Entity>(this.entities);
	}
	
}
