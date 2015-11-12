package physics;

import physics.maths.Point;

public class BBSquare extends BB {

	private Point min,max;
	
	//Getters
	public Point getMin() {
		return min;
	}

	public Point getMax() {
		return max;
	}
	
	
	public BBSquare(Point min, Point max)
	{
		this.min = min;
		this.max = max;
	}

	@Override
	public boolean collidesWith(BBCircle boundingBox) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean collidesWith(BBSquare boundingBox) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean containsPoint(Point point) {
		return (point.getX()>=this.min.getX() && point.getY()>=this.min.getY() && point.getX()<=this.max.getX() && point.getY()<=this.max.getY());
	}
}
