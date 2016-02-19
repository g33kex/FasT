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
import physics.maths.Point;

public class BBCircle extends BB {

	private Point center;
	private double radius;
	
	public Point getCenter() {
		return center;
	}
	public double getRadius() {
		return radius;
	}
	
	
	public BBCircle(Point center, double radius)
	{
		this.center = center;
		this.radius = radius;
	}
	
	public boolean collidesWith(BBCircle circle)
	{
		return false;
	}
	
	public boolean collidesWith(BBSquare square)
	{
		/*if(square.containsPoint(this.center))
		{
			FasT.getFasT().getLogger().debug("I'm colliding with a square");
			return true;
		}*/
		if(this.center.getY()<=100)
			return true;
		
		//FasT.getFasT().getLogger().error("Y"+center.getY()+"SY"+square.getMax().getY());
		return false;
	}
	@Override
	protected boolean containsPoint(Point point) {
		return false;
	}
	
}
