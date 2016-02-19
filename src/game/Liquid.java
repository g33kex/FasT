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

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import game.entities.Box;
import physics.maths.Point;
import render.Render;

public class Liquid {
	private double masseVolumique; // g/L
	private double visc; // coefficient de frottement
	private double color[];
	private float level; // Niveau du liquide dans la zone d'expérience en pourcent. 
	
	public double getMasseVolumique()
	{
		return this.masseVolumique;
	}
	
	public double getVisc(){return this.visc;}
	
	public void setVisc(double visc) {
		this.visc = visc;
	}

	public void setMasseVolumique(double masseVolumique)
	{
		this.masseVolumique=masseVolumique;
	}
	
	public void setColor(double[] color2) {
		this.color=color2;
		
	}
	
	public Liquid(double masseVolumique,double visc, double[] color)
	{
		this.masseVolumique=masseVolumique; // kg/m^3
		this.setVisc(visc); // Pa/s
		this.color=color;
		this.setLevel(1);
	}
	
	//1.8*Math.pow(10, -3) for water but doesn't work in cinematic ???

	//public static final Liquid WATER = new Liquid(1000,1.4,new double[]{0.3,0.6,0.9,0.2});
	public static final Liquid WATER() {return new Liquid(1000,1.8,new double[]{0.3,0.6,0.9,0.2});}
	
	//public static final Liquid AIR() {return new Liquid(1.2,1.8*Math.pow(10, -5),new double[]{1,1,1,0.3});}
	public static final Liquid AIR() {return new Liquid(1.2,1.8*Math.pow(10, -2),new double[]{1,1,1,0.3});}
	
	public void render(Box box, Render render)
	{
		GL11.glColor4d(color[0], color[1], color[2],color[3]);
		render.drawSquare(box.getPosition(),new Point(box.getMax().getX(),box.getPosition().getY()+((box.getMax().getY()-box.getPosition().getY())*this.getLevel())));
	}
	

	public float getLevel() {
		return level;
	}

	public void setLevel(float level) {
		this.level = level;
	}

	
}
