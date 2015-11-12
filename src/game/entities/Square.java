package game.entities;

import physics.maths.Point;
import render.Render;

public class Square extends Entity {

	public Square(Point position,double width,double lenght,double mass)
	{
		super(position,mass);
	}
	
	@Override
	public void render(Render render) {

	}

}
