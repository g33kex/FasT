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
import log.Logger;

public class Main
{
	
	public static void main(String args[]) throws LWJGLException
	{
		Logger log = new Logger();
		log.info("Hello, world !");
		
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
}