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

import org.lwjgl.opengl.Display;

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
	
	public Point mouseToReal()
	{
		return new Point(Normal.toReal(this.getX()-Display.getWidth()/2-Normal.getXOffset()),Normal.toReal(this.getY()-Display.getHeight()/2-Normal.getYOffset()));
	}

	public Point add(Point p) {
		return new Point(this.x+p.x,this.y+p.y);
	}
	
}
