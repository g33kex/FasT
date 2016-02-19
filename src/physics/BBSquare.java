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

import physics.maths.Point;

public class BBSquare extends BB {

	private Point min,max;
	
	//Getters
	public Point getMin() {
		return min;
	}

	public Point getMax() {
		return max;
	}
	
	
	public BBSquare(Point min, Point max)
	{
		this.min = min;
		this.max = max;
	}

	@Override
	public boolean collidesWith(BBCircle boundingBox) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean collidesWith(BBSquare boundingBox) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean containsPoint(Point point) {
		return (point.getX()>=this.min.getX() && point.getY()>=this.min.getY() && point.getX()<=this.max.getX() && point.getY()<=this.max.getY());
	}
}
