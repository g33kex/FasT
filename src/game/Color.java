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

package game;

public class Color {

	private final double red;
	private final double green;
	private final double blue;
	
	public Color(double r, double g, double b)
	{
		this.red=r;
		this.green=g;
		this.blue=b;
	}

	
	public double getRed()
	{
		return this.red;
	}
	
	public double getGreen()
	{
		return this.green;
	}
	
	public double getBlue()
	{
		return this.blue;
	}
	
	public static final Color RED = new Color(1,0,0);
	public static final Color GREEN = new Color(0.0,0.9,0.2);
	public static final Color FLASHGREEN = new Color(0,1,0);
	public static final Color BLUE = new Color(0,0,1);
	
	public static final Color PINK = new Color(1,0.2,0.8);
	public static final Color ORANGE = new Color(0.95,0.25,0);

	public static final Color GOLD = new Color(0.90,0.6,0.04);
	
	public static final Color BLUELIGHT = new Color(0.07,0.7,0.35);
	
	public static final Color BLACK = new Color(0,0,0);
	
	
	public static final Color PURPLEDARK = new Color(0.4,0.1,0.2);
	
//	public static final Color SCALEGREEN = new Color(0.4, 0.9, 0.1);
}
