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


package main;

import org.lwjgl.LWJGLException;
//import chrriis.dj.nativeswing.NativeSwing;
import game.FasT;
import game.entities.Ball;
import game.entities.EntityHandler;
import log.Logger;
import physics.Physics;
import physics.maths.Angle;
import physics.maths.C;
import physics.maths.Point;

public class Main
{
	
	public static void main(String args[]) throws LWJGLException
	{
		
		//testAngles();
		//if(true)
		//return;
		/*Logger log = new Logger();
		log.info("Hello, world !");
		
		double l = 3*Math.PI/4;
		double a = Math.tan(l);
		
		double b = Math.atan(a);
		log.info("TEST: atan("+a+"(tan"+Math.toDegrees(l)+"°)) = "+Math.toDegrees(b)+"° = "+ b + " rad");
	
		
		double angle = 3*Math.PI/4;
		C c = new C(new Angle(angle),1);
		double arctan = Math.atan2(c.getIm(), c.getRe());
		log.info("ATAN2="+Math.toDegrees(arctan));
		
		
		if(true)
		return;*/
		
		
		/*C c = new C(-1,2);
		log.error(c.getArgument().getDeg());
		//log.info(c.getMod());
		log.info(c);
		log.info(c.toStringAng(true));
		log.info(new C(c.getTheta(),c.getRho()));
		log.info((new C(c.getTheta(),c.getRho())).toStringAng(true));
		//log.debug(c.getConj());
		//log.debug(c.sum(new C(9,10)));
		
		//System.exit(0);*/
		FasT fasT = new FasT();
		

		fasT.run();
	}
	
	/*public static void testAngles()
	{
		Physics physics = new Physics();
		EntityHandler handler = new EntityHandler();
		Ball entity1 = new Ball(new Point(0,0),handler);
		entity1.setVelocity(new C(new Angle(0),1));
		
		
		C t = new C(new Angle(0),1);
		
		final double pi = Math.PI;
		
		double[] O = new double[] {pi/4,3*pi/4,5*pi/4,7*pi/4};
		
		for(double Θ1 : O)
		{
		
			for(double Θ2 : O)
			{
				physics.setNewVelocity(Θ1, Θ2, entity1, entity1, t);
			}
		}
	}*/
}