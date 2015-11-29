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