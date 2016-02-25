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

package physics;

import game.FasT;
import game.entities.Ball;
import game.entities.Box;
import physics.maths.Angle;
import physics.maths.C;
import physics.maths.Point;

public abstract class BB {

	public boolean collidesWith(BB boundingBox)
	{
		if(boundingBox instanceof BBCircle)
		{
			return this.collidesWith((BBCircle) boundingBox);
		}
		if(boundingBox instanceof BBSquare)
		{
			return this.collidesWith((BBSquare) boundingBox);
		}
		return false;
	}
	
	public abstract boolean collidesWith(BBCircle boundingBox);
	public abstract boolean collidesWith(BBSquare boundingBox);
	
	protected abstract boolean containsPoint(Point point);

	public static boolean collisionTwoBalls(Point position, double radius,Point position2, double radius2) {
		return distanceBetweenTwoPoints(position,position2) <= radius+radius2;
	}
	
	public static double distanceBetweenTwoPoints(Point a, Point b)
	{
		return new C(b.getY()-a.getY(),b.getX()-a.getX()).getMod();
	}
	

	public static boolean collisionBallWall(Point O, double radius, Point A, Point B) {
		
		if(!BB.collisionBallSquare(O,radius,A,B))
		{
			return false;
		}
		
		//Vertical
		if(B.getX() == A.getX())
		{
			return Math.abs(O.getX()-B.getX())<=radius;
		}

		//Horizontal
		if(B.getY()==A.getY())
		{
			return Math.abs(O.getY()-B.getY())<=radius;
		}
		
		
		double c = A.coef(B);
		double d = A.getY()-c*A.getX();

		double xC = O.getX();
		double yC = c*xC+d;
		double yD = O.getY();
		double xD = (yD-d)/c;
		
		Point C = new Point(xC,yC);
		Point D = new Point(xD,yD);
		
		double h = Math.abs((C.distance(O)*D.distance(O))/(C.distance(D)));
		
		
	//	FasT.getFasT().getLogger().debug("H="+h+"|c"+c);
		return h<=radius;
		
	
		
		//return position.getY()-radius<=position2.getY();
	}

	public static boolean collisionBallSquare(Point position, double radius, Point position2, Point max) {

		return position.getX()+radius>=position2.getX() && position.getY()+radius>=position2.getY() && position.getX()<=max.getX()+radius && position.getY()<=max.getY()+radius;
	}

	public static boolean pointInSquare(Point position, Point position2, Point max) {
		return position.getX()>=position2.getX() && position.getY()>=position2.getY() && position.getX()<=max.getX() && position.getY()<=max.getY();
	}
	

	
}
