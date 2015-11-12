package main;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import game.FasT;
import log.Logger;
import physics.maths.C;

public class Main
{
	
	public static void main(String args[]) throws LWJGLException
	{
		Logger log = new Logger();
		log.info("Hello, world !");
		
		C c = new C(3,1);
		log.info(c.getMod());
		log.debug(c.getConj());
		log.debug(c.sum(new C(9,10)));
		
		
		FasT fasT = new FasT();
		fasT.run();
	}
}