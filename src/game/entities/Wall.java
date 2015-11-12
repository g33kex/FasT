package game.entities;

import physics.BBSquare;
import physics.maths.Angle;
import physics.maths.C;
import physics.maths.Point;
import render.Render;

public class Wall extends Entity {

	private Angle angle;
	private double longueur;
	private Point posMax;
	
	public Wall(Point position,double longueur,Angle angle) {
		super(position, -1);
		this.angle=angle;
		this.longueur = longueur;
		C c = new C(angle,longueur);
		this.posMax = new Point(position.getX()+c.getRe(),position.getY()+c.getIm());
	}
	
	@Override
	public void render(Render render) {
		render.drawLine(this.position,this.posMax);
	}

}
