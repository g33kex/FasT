/*
 *  FasT -- A Fast algorithm for simulaTions. 
 * 
 *  Copyright © 2016 Tourdetour
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *  
 *  Contact me by email : <TPEFasT@mail.com>
 */

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
