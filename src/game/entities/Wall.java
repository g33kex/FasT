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

package game.entities;

import java.awt.Cursor;
import java.util.UUID;

import org.lwjgl.opengl.GL11;

import game.FasT;
import physics.BB;
import physics.BBSquare;
import physics.maths.Angle;
import physics.maths.C;
import physics.maths.Point;
import render.Render;

public class Wall extends Entity {

	//private Angle angle;
	//private double longueur;
	private Point posMax;
	
	public Wall(Point position,double longueur,Angle angle,EntityHandler entityHandler) {
		super(position, -1,entityHandler);
		this.name="wall";
		//this.angle=angle;
		//this.longueur = longueur;
		C c = new C(angle,longueur);
		this.posMax = new Point(position.getX()+c.getRe(),position.getY()+c.getIm());
	}
	
	public Wall(Point min, Point max, EntityHandler entityHandler) {
		super(min, -1,entityHandler);
		this.name="wall";
		//C c = new C(max.getX()-min.getX(),max.getY()-min.getY());
		this.posMax=max;
	//	this.angle=c.getTheta();
		//this.longueur =c.getRe();
	}

	@Override
	public void render(Render render) {
		GL11.glColor3d(0.5, 0.1, 0.8);
		render.drawLine(this.position,this.posMax);
	}

	/*public Angle getAngle() {
		return this.angle;
	}*/

	public Point getMaxPos()
	{
		return this.posMax;
	}

	/*@Override
	public void setPosition(Point p)
	{
		super.setPosition(p);
		//C c = new C(angle,longueur);
		//this.posMax = new Point(position.getX()+c.getRe(),position.getY()+c.getIm());
	}*/
	
	public void setMax(Point max)
	{
		//C c = new C(max.getX()-this.getPosition().getX(),max.getY()-this.getPosition().getY());
		this.posMax=max;
		//this.angle=c.getTheta();
		//this.longueur =c.getRe();
	}



	public boolean shouldMenu(Point p)
	{
		if(BB.distanceBetweenTwoPoints(this.position.toPlan(), p.toPlan())<10)
			return true;
		return false;
	}

	public boolean isEntityUnder(Point p)
	{
		return BB.distanceBetweenTwoPoints(this.position.toPlan(), p.toPlan())<10 || BB.distanceBetweenTwoPoints(this.getMaxPos().toPlan(), p.toPlan())<10;
	}
	
	

	public boolean hoover(Point p)
	{
		if(BB.distanceBetweenTwoPoints(this.position.toPlan(), p.toPlan())<20)
		{
			FasT.getFasT().getRender().getCanvas().setCursor(java.awt.Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			return true;
		}
		if(BB.distanceBetweenTwoPoints(this.getMaxPos().toPlan(), p.toPlan())<20)
		{
			FasT.getFasT().getRender().getCanvas().setCursor(java.awt.Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR));
			return true;
		}
		return false;
	}
	
	private int dragMode = 0;
	
	@Override
	public void setBeingDragged(boolean b,Point p)
	{
		if(!b)
		{
			dragMode=0;
			return;
		}
		if(BB.distanceBetweenTwoPoints(this.position.toPlan(), p.toPlan())<10)
		{
			dragMode=1;
		}
		if(BB.distanceBetweenTwoPoints(this.getMaxPos().toPlan(), p.toPlan())<10)
		{
			dragMode=2;
		}
		
	}
	
	@Override
	public boolean getBeingDragged()
	{
		return this.dragMode>0;
	}
	
	@Override
	public boolean drag(Point p,long currentNanoTime)
	{
		if(dragMode==1)
		{
			this.setPosition(this.getPosition().add(p));
			this.posMax=this.getMaxPos().add(p);
		}
		if(dragMode==2)
		{
			this.setMax(this.getMaxPos().add(p));
			/*if(this.getMaxPos().add(p).toPlan().getX()-this.getPosition().toPlan().getX()>=40)
			{
				//if(this.getMaxPos().add(p).toPlan().getY()-this.getPosition().toPlan().getY()>=40)
				{
					
				}
				//else
				{
					//this.setMax(new Point(this.getMaxPos().toPlan().getX(),this.getPosition().toPlan().getY()+40).toReal());
				}
			}
			//else
			{
				//this.setMax(new Point(this.getPosition().toPlan().getX()+40,this.getMaxPos().toPlan().getY()).toReal());
			}*/
		}
		return true;
	}

	public Angle getAngle() {
		return new C(this.getMaxPos().getX()-this.getPosition().getX(),this.getMaxPos().getY()-this.getPosition().getY()).getTheta();
	}



}
