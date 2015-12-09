package physics.maths;

public class Angle {
	
	
	//CONVERTIR AUTO EN MULTIPLE POSITIF DE 2PI
	private double value;
	
	public Angle(double value)
	{
		this.value=value;
	}

	public double getRad() {
		return this.value;
	}
	
	public String toString()
	{
		return this.value + "";
	}

	public String toStringDeg()
	{
		return this.getDeg() + "°";
	}
	
	public double getDeg() {
		return this.getDeg(this.value);
	}
	
	private double getDeg(double rad)
	{
		return convertToDeg(rad);
	}

	public static double convertToRad(double deg) {
		return Math.toRadians(deg);
		//return Math.toRadians(deg)/Math.PI;
	}
	
	public static double convertToDeg(double rad)
	{
		return Math.toDegrees(rad);
		//return Math.toDegrees(rad*Math.PI);
	}
	
}
