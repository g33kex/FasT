package physics;

import game.FasT;
import physics.maths.Point;

public class BBCircle extends BB {

	private Point center;
	private double radius;
	
	public Point getCenter() {
		return center;
	}
	public double getRadius() {
		return radius;
	}
	
	
	public BBCircle(Point center, double radius)
	{
		this.center = center;
		this.radius = radius;
	}
	
	public boolean collidesWith(BBCircle circle)
	{
		return false;
	}
	
	public boolean collidesWith(BBSquare square)
	{
		/*if(square.containsPoint(this.center))
		{
			FasT.getFasT().getLogger().debug("I'm colliding with a square");
			return true;
		}*/
		if(this.center.getY()<=100)
			return true;
		
		//FasT.getFasT().getLogger().error("Y"+center.getY()+"SY"+square.getMax().getY());
		return false;
	}
	@Override
	protected boolean containsPoint(Point point) {
		return false;
	}
	
}
