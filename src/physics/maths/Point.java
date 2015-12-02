package physics.maths;

public class Point {
	
	private double x,y;
	
	//Getters
	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	
	public Point(double x,double y) {
		this.x = x;
		this.y = y;
	}
	
	public Point toPlan()
	{
		return new Point(Normal.toPlan(this.getX()),Normal.toPlan(this.getY()));
	}

	public Point toReal() {
		return new Point(Normal.toReal(this.getX()),Normal.toReal(this.getY()));
	}

	public Point add(Point p) {
		return new Point(this.x+p.x,this.y+p.y);
	}
	
}
