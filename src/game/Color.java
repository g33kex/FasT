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

	private final int red;
	private final int green;
	private final int blue;
	
	public Color(int r, int g, int b)
	{
		this.red=r;
		this.green=g;
		this.blue=b;
	}
	
	public int getRed()
	{
		return this.red;
	}
	
	public int getGreen()
	{
		return this.green;
	}
	
	public int getBlue()
	{
		return this.blue;
	}
	
	public static final Color RED = new Color(255,0,0);
	public static final Color GREEN = new Color(0,255,0);
	public static final Color BLUE = new Color(0,0,255);
	
//	public static final Color SCALEGREEN = new Color(0.4, 0.9, 0.1);
}
