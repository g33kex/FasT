package physics;

import physics.maths.Angle;
import physics.maths.C;
import physics.maths.Point;

public abstract class BB {

	public boolean collidesWith(BB boundingBox)
	{
		if(boundingBox instanceof BBCircle)
		{
			return this.collidesWith((BBCircle) boundingBox);
		}
		if(boundingBox instanceof BBSquare)
		{
			return this.collidesWith((BBSquare) boundingBox);
		}
		return false;
	}
	
	public abstract boolean collidesWith(BBCircle boundingBox);
	public abstract boolean collidesWith(BBSquare boundingBox);
	
	protected abstract boolean containsPoint(Point point);

	public static boolean collisionTwoBalls(Point position, double radius,Point position2, double radius2) {
		return distanceBetweenTwoPoints(position,position2) <= radius+radius2;
	}
	
	public static double distanceBetweenTwoPoints(Point a, Point b)
	{
		return new C(b.getY()-a.getY(),b.getX()-a.getX()).getMod();
	}
	

	public static boolean collisionBallWall(Point position, double radius, Point position2, Angle angle) {
		return position.getY()-radius<=position2.getY();
	}

	public static boolean collisionBallSquare(Point position, double radius, Point position2, Point max) {
		return position.getX()+radius>=position2.getX() && position.getY()+radius>=position2.getY() && position.getX()<=max.getX()+radius && position.getY()<=max.getY()+radius;
	}

	public static boolean pointInSquare(Point position, Point position2, Point max) {
		return position.getX()>=position2.getX() && position.getY()>=position2.getY() && position.getX()<=max.getX() && position.getY()<=max.getY();
	}
	

	
}
