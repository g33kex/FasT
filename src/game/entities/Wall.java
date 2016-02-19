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

import java.util.UUID;

import org.lwjgl.opengl.GL11;

import physics.BBSquare;
import physics.maths.Angle;
import physics.maths.C;
import physics.maths.Point;
import render.Render;

public class Wall extends Entity {

	private Angle angle;
	private double longueur;
	private Point posMax;
	
	public Wall(Point position,double longueur,Angle angle,EntityHandler entityHandler) {
		super(position, -1,entityHandler);
		this.name="wall";
		this.angle=angle;
		this.longueur = longueur;
		C c = new C(angle,longueur);
		this.posMax = new Point(position.getX()+c.getRe(),position.getY()+c.getIm());
	}
	
	@Override
	public void render(Render render) {
		GL11.glColor3d(0.5, 0.1, 0.8);
		render.drawLine(this.position,this.posMax);
	}

	public Angle getAngle() {
		return this.angle;
	}

}
