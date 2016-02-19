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

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import game.FasT;

public class Normal {

	
	//SCALE = 1/40
	//OPEN GL SCALE 1/20
	
	//20cm*10cm -> 8m*4m
	public enum Unit
	{
		cm,m
	}

	//1m/s : parcourir 1m sur le dessin en 1 seconde !!!
	
	// 1 m = 10 points

	private static int zoomlevel = 0;
	private static int r = 1;
	private static final int dd = 100;
	private static int d = 100;
	
	static int level= 200;
	
	public static double x=0;//Display.getWidth()/2;
	public static double y=0;//Display.getHeight()/2;
	public static double rx=0;
	public static double ry=0;
	
	public static int getZoom()
	{
		return zoomlevel;
	}

	public static void zoom(int mul)
	{
		//ZOOM IN ON MOUSE ZOOM OUT ON WHOLE
		if(zoomlevel>level+mul)
			return;
		zoomlevel=zoomlevel+mul;
		//FasT.getFasT().getLogger().warning("MouseX="+Mouse.getX()+" MouseY="+Mouse.getY());
		double y1 = toReal(Mouse.getY()-Display.getHeight()/2);
		double x1 = toReal(Mouse.getX()-Display.getWidth()/2);

		d=dd+zoomlevel*dd/level;

		double y2 = toReal(Mouse.getY()-Display.getHeight()/2);
		double x2 = toReal(Mouse.getX()-Display.getWidth()/2);
		ry=ry+y2-y1;
		rx=rx+x2-x1;
		y=toPlan(ry);
		x=toPlan(rx);
		//x-=Display.getWidth()*x1/x2/2;
		//y-=Display.getHeight()*y1/y2/2;
		
	}
	
	
	public static void unzoom(int mul)
	{
		
		if(zoomlevel<=-level+mul)
		{
		/*	zoomlevel=-level+1;
			d=dd;
			double y1 = toReal(Display.getHeight()/2);
			double x1 = toReal(Display.getWidth()/2);
			d=dd+zoomlevel*dd/level;
			double y2 = toReal(Display.getHeight()/2);
			double x2 = toReal(Display.getWidth()/2);
			ry=ry+y2-y1;
			rx=rx+x2-x1;
			y=toPlan(ry);
			x=toPlan(rx);*/
			/*zoomlevel=-level;
			d=1;
			rx=0;
			ry=0;
			x=0;
			y=0;*/
			return;
		}
		zoomlevel=zoomlevel-mul;

		
		double y1 = toReal(Mouse.getY()-Display.getHeight()/2);
		double x1 = toReal(Mouse.getX()-Display.getWidth()/2);
		d=dd+zoomlevel*dd/level;
		double y2 = toReal(Mouse.getY()-Display.getHeight()/2);
		double x2 = toReal(Mouse.getX()-Display.getWidth()/2);
		ry=ry+y2-y1;
		rx=rx+x2-x1;
		y=toPlan(ry);
		x=toPlan(rx);
		/*double x1 = toReal(-Display.getWidth()/2);
		double y1 = toReal(-Display.getHeight()/2);
		d=dd+zoomlevel*dd/level;
		double x2 = toReal(-Display.getWidth()/2);
		double y2 = toReal(-Display.getHeight()/2);
		rx=x1-x2;
		ry=y1-y2;*/
		
	}
	
	
	
	
	/*public static double normal(double number,Unit unit)
	{
		//Reality to draw
		if(unit==Unit.cm)
		{
			number=number/100;
			//return number*20/40;
		}
		if(unit==Unit.m)
		{
			//return number*2;//40/20;
		}
		return number*d/r;
		
	}*/
	
	public static double toReal(double number)
	{
		
		return (number*r)/d;
	}
	
	public static double toPlan(double number)
	{
		return (number*d)/r;
	}

	public static double getYOffset() {
		return y;
	}
	
	public static double getXOffset()
	{
		return x;
	}
	
}
