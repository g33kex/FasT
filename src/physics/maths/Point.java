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

import game.FasT;

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

	public double coef(Point b)
	{
		//FasT.getFasT().getLogger().debug("(b.getX()-this.getX())="+(b.getX()-this.getX())+"||||||(b.getY()-this.getY())="+(b.getY()-this.getY()));
		return (b.getY()-this.getY())/(b.getX()-this.getX());
	}
	
	public double distance(Point b)
	{
		return Math.sqrt(Math.pow(b.getX()-this.getX(),2)+Math.pow(b.getY()-this.getY(),2));
	}
	
	public C getC()
	{
		return new C(this.getX(),this.getY());
	}

	public Point copy() {
		return new Point(this.getX(),this.getY());
	}
	
}
