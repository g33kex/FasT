package physics.maths;

public class Vector {
	
	//a^2+b^2 = (a+bi)(a-bi)
	
//	private Direction direction;
//	private double length;
//	private int way;
	
	public C complex;
	
	public Vector(Angle angle,double length)
	{
		complex = (new C(angle,length));
	}
	
	public Vector(double a,double b, boolean ab)
	{
		complex = new C(ab ? a : b,ab ? b : a);
	}
	
	
}
