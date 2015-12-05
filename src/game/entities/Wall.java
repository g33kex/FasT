package game.entities;

import java.util.UUID;

import org.lwjgl.opengl.GL11;

import physics.BBSquare;
import physics.maths.Angle;
import physics.maths.C;
import physics.maths.Point;
import render.Render;

public class Wall extends Entity {

	private Angle angle;
	private double longueur;
	private Point posMax;
	
	public Wall(Point position,double longueur,Angle angle,EntityHandler entityHandler) {
		super(position, -1,entityHandler);
		this.name="wall";
		this.angle=angle;
		this.longueur = longueur;
		C c = new C(angle,longueur);
		this.posMax = new Point(position.getX()+c.getRe(),position.getY()+c.getIm());
	}
	
	@Override
	public void render(Render render) {
		GL11.glColor3d(0.5, 0.1, 0.8);
		render.drawLine(this.position,this.posMax);
	}

	public Object getAngle() {
		return this.angle;
	}

}
