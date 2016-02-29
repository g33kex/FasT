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

import java.util.ArrayList;

import physics.maths.Angle;
import physics.maths.C;
import physics.maths.Maths;
import physics.maths.Normal;
import physics.maths.Normal.Unit;
import physics.maths.Point;
import game.Color;
import game.FasT;
import game.Liquid;
import game.entities.Ball;
import game.entities.Box;
import game.entities.Entity;
import game.entities.Wall;

public class Physics {
//http://warmaths.fr/SCIENCES/unites/VOL%20CAPA/spher2.htm
	//private double g = 9.81; // N/kg
	private C g = new C(new Angle(Angle.convertToRad(-90)),9.81);
	private final double G = 6.67384*Math.pow(10,-11);
	
	//public boolean GROUND = false;
	
	
	public int simulationLevel = SimulationLevel.CHUTE_LIQUID; // -1 = Nothing 0 = Univers 1 = chute libre 2 = chute dans un liquide ///3 = chute avec frottements 4 = chute avec rebonds
	
	public boolean rebonds = true; // Do we handle that ?
	public boolean applyEverywhere = false; // Let's have a BIG box :)
	
	private class SimulationLevel
	{
		public static final int NOTHING=-1;
		public static final int UNIVERS=0;
		public static final int CHUTE_LIBRE=1;
		public static final int CHUTE_LIQUID=2;
	}
	
	public Physics()
	{
	//	g=6; FALLING ON THE MOON
	}

	//Regular collision methods
	
	public void updateCollisions(ArrayList<Entity> entities)
	{
		
		if(!this.rebonds) // REBONDS :)
		{
			return;
		}
		
		ArrayList<Entity> entities1 = new ArrayList<Entity>();
		ArrayList<Entity> entities2 = new ArrayList<Entity>();
		
		//Detect collisions (better method)
		for(Entity entity1 : entities)
		{
			if(!(entity1 instanceof Ball))
				continue;
			for(Entity entity2 : entities)
			{
				if(!(entity2 instanceof Ball))
					continue;
				

				if(entity1==entity2)
					continue;
				
				if(entity1.collidesWith(entity2))
				{
					/*if(entity1 instanceof Box && entity2 instanceof Ball)
					{
						collisionBox((Ball)entity2,(Box)entity1);
					}
					else if(entity2 instanceof Box && entity1 instanceof Ball)
					{
						collisionBox((Ball)entity1,(Box)entity2);
					}
					else
					{*/
					if(!(entities1.contains(entity1) | entities1.contains(entity2) | entities2.contains(entity1) | entities2.contains(entity2)))
					{
						if(!(entity1.wasCollidingWith(entity2) || entity2.wasCollidingWith(entity1)))
						{
						
							FasT.getFasT().getLogger().warning("Object "+entity1.getUUID() + " collides with "+entity2.getUUID());
							
							entity1.setCollidingWith(entity2);
							entity2.setCollidingWith(entity1);
							
							
							entities1.add(entity1);
							entities2.add(entity2);
						}
					//}
					}
				}
				else
				{
					
					entity1.wasntCollidingWith(entity2);
				}
			}
			
		}
		
		//Apply collisions
		for(Entity entity : entities1)
		{
			this.collideEntityWithEntity(entity, entities2.get(entities1.indexOf(entity)));
		}
		
	}
	
	private double getTheta(C V,C T, C N)
	{
		double vt = V.getRe();//V.scal(T);
		double vn = V.getIm();//V.scal(N);
		
		FasT.getFasT().getLogger().debug("vt="+vt+"     vn="+vn+"     Vx="+V.getRe()+"     Vy="+V.getIm());
		
		if(vn > 0 && vt > 0 )
		{
			return V.getTheta().getRad();
		}
		else if(vn > 0 && vt < 0)
		{
			return Math.PI-V.getTheta().getRad();
		}
		else if(vn < 0 && vt < 0)
		{
			return V.getTheta().getRad()-Math.PI;
		}
		else if(vn < 0 && vt > 0)
		{
			return (2*Math.PI)-V.getTheta().getRad();
		}
		else if(vt==0 && vn==0)
		{
			FasT.getFasT().getLogger().warning("Vitesse nulle dans getTheta");
			return 0;
		}
		else if(vt == 0)
		{
			return Math.PI/2;
		}
		else //vn==0
		{
			return 0;
		}
	}
	
    private void collideEntityWithEntity(Entity entity1, Entity entity2) {
		
		if(entity1.positions.size()>1 && entity1 instanceof Ball)
		{
			entity1.positions.remove(entity1.positions.size()-1);
			entity1.setPosition(entity1.positions.get(entity1.positions.size()-1));
		}
		if(entity2.positions.size()>1 && entity2 instanceof Ball)
		{
			entity2.positions.remove(entity2.positions.size()-1);
			entity2.setPosition(entity2.positions.get(entity2.positions.size()-1));
		}
		
		
		/*if(entity1.getUUID()!=FasT.getFasT().theBall)
			return;*/
		/*if(entity1.wasCollided() || entity2.wasCollided())
		{
			FasT.getFasT().getLogger().debug("Whooooppss two collisions");
			return;
		}*/
			C velocity1 = entity1.getVelocity();
			C velocity2 = entity2.getVelocity();
		
			double x1 = entity1.getPosition().getX(); // Position X de l'objet 1
			double y1 = entity1.getPosition().getY(); // Position Y de l'objet 1
			double x2 = entity2.getPosition().getX(); // Position X de l'objet 2
			double y2 = entity2.getPosition().getY(); // Position Y de l'objet 2

			
			//Normale
			C n = new C(x2-x1,y2-y1); // On calcule le vecteur P1, qui représente la droite passant par le centre des objets
			n = new C(n.getTheta(),1);
			C t = new C(new Angle(n.getTheta().getRad()-(Math.PI/2)),n.getRho());
		
			//FasT.getFasT().getLogger().debugV(entity1.getPosition(),n,Color.BLACK,2);
			
		//	C P1 = new C(new Angle(t.getTheta().getRad()+Math.PI),n.getRho());//new C(new Angle((Math.PI/2)-n.getTheta().getRad()),n.getRho());
			
	
			//FasT.getFasT().getLogger().debugV(entity1.getPosition(),P1,Color.BLUE,2);

			
			double Θ1R = velocity1.getTheta().getRad()-t.getTheta().getRad(); // Calcul de theta 1
			double Θ2R = velocity2.getTheta().getRad()-t.getTheta().getRad(); // Calcul de theta 2
			
			C velocity1R = new C(new Angle(Θ1R),velocity1.getRho());
			C velocity2R = new C(new Angle(Θ2R),velocity2.getRho());
			
			double Θ1 = velocity1R.getTheta().getRad();//this.getTheta(velocity1R,t,n);
			double Θ2 = -velocity2R.getTheta().getRad();//this.getTheta(velocity2R,t,n);
			
			/*if(entity1.getMass()==-1)
			{
				this.setNewVelocityFixed2(entity2,entity1);
			}
			else if(entity2.getMass()==-1)
			{
				this.setNewVelocityFixed2(entity1,entity2);
			}*/
			//else
			//{
			if(entity2.getMass()!=-1 && entity1.getMass()!=-1)
			{
				FasT.getFasT().getLogger().debugV(entity1.getPosition(),t,Color.BLACK,2);
				FasT.getFasT().getLogger().debugV(entity1.getPosition(),n,Color.BLACK,2);
				this.setNewVelocity(Θ1, Θ2, entity1, entity2, t,n);
			}
			//}
				
				//FasT.getFasT().getLogger().debug("v1="+v1+"|V1="+V1+"|Θ1="+Θ1+"|O1="+O1+"|V1b="+V1b.getTheta().getRad());
				
				if(FasT.getFasT().debug)
				{
				FasT.getFasT().setPaused(true);	
				}
	}
	
	public void collisionBox(Ball ball,Box box)
	{
		//FasT.getFasT().getLogger().debug("BOXCOLL");
		for(Wall wall : box.getWalls())
		{
			
			//FasT.getFasT().getLogger().debug("WALLCOLL");
			if(ball.collidesWith(wall))
			{
				if(!box.wasCollidingWith(wall))
				{

					FasT.getFasT().getLogger().warning("Object "+ball.getUUID() + " collides with wallbox"+wall.getUUID());
					
					ball.setCollidingWith(wall);
					
					collideEntityWithEntity(ball,wall);
				}
			}
			else
			{
				ball.wasCollidingWith(wall);
			}
		}
	}
	
	private C getVectOriented(C v1,C V,C T, C N)
	{
		double v1t = v1.getRe();//v1.scal(T);
		double v1n = v1.getIm();//v1.scal(N);
		
		double Vx;
		double Vy;
		
		if(v1t > 0)
		{
			Vx = V.getRho()*Math.cos(V.getTheta().getRad());
		}
		else if(v1t < 0)
		{
			Vx = -V.getRho()*Math.cos(V.getTheta().getRad());
		}
		else
		{
			Vx = 0;
		}
		
		if(v1n > 0)
		{
			Vy = -V.getRho()*Math.sin(V.getTheta().getRad());
		}
		else if(v1n < 0)
		{
			Vy = V.getRho()*Math.sin(V.getTheta().getRad());
		}
		else
		{
			Vy = 0;
		}
		
		return new C(Vx,Vy);
	}
	
	public void setNewVelocity(double Θ1, double Θ2,Entity entity1,Entity entity2, C t, C n)
	{
		C velocity1 = entity1.getVelocity(); // Vecteur vitesse de l'objet 1
		C velocity2 = entity2.getVelocity(); // Vecteur vitesse de l'objet 2
	
	
		double v1 = velocity1.getRho(); // Vitesse avant collision de l'objet 1
		double v2 = velocity2.getRho(); // Vitesse avant collision de l'objet 2
		
		double m1 = entity1.getMass(); // Masse de l'objet 1
		double m2 = entity2.getMass(); // Masse de l'objet 2
		
		
		double V1 = 0; // Vitesse après collision de l'objet 1
		double V2 = 0; // Vitesse après collision de l'objet 2
		double O1 = 0; // Theta après collision de l'objet 1
		double O2 = 0; // Theta après collision de l'objet 2
		
		double T2 = 0;
		double T1 =0;
		
		double M = (m1-m2)/(m1+m2);
		
		double M21 = (2*m1)/(m1+m2);
		double M22 = (2*m2)/(m1+m2);
		
		//Calcul des théta après collision
		if(v1!=0)
		{
			T1 = 
					(Math.tan(Θ1) * M) + 
					(
							M22 * 
							(v2/v1) * 
							(Math.sin(Θ2) / Math.cos(Θ1))
					);
		O1 = Angle.atan(T1);
		}
		if(v2!=0)
		{
		
			T2 = (Math.tan(Θ2) * (-M)) +
					(
							M21 * 
							(v1/v2) * 
						    (Math.sin(Θ1)/Math.cos(Θ2))
					);
		O2 = Angle.atan(T2);
		}	
		
	//	O2 = -Math.abs(O2);
		//O1 = Math.abs(O1);
		O2=-O2;
		O1=-Math.abs(O1);
		
		FasT.getFasT().getLogger().debug("Θ1="+Math.toDegrees(Θ1)+"     Θ2="+Math.toDegrees(Θ2)+"     O'1="+Math.toDegrees(O1)+"	O'2="+Math.toDegrees(O2));
		
		//Calcul des V après collision

		V1 = Math.sqrt(
				Math.pow(
						(M*v1*Math.sin(Θ1))+
						(M22*v2*Math.sin(Θ2))
						, 2)
				+ Math.pow(
						v1*Math.cos(Θ1)
						, 2)
				);
		
		V2 = Math.sqrt(
				Math.pow(
						((-M)*v2*Math.sin(Θ2))+
						(M21*v1*Math.sin(Θ1))
						, 2)
				+ Math.pow(
						v2*Math.cos(Θ2)
						, 2)
				);
		
		C velocity1R = new C(new Angle(O1),V1);//getVectOriented(velocity1,new C(new Angle(O1),V1),t,n);
		C velocity2R = new C(new Angle(O2),V2);//getVectOriented(velocity2,new C(new Angle(O2),V2),t,n);
		
		
		//System.out.println("\nΘ1="+Math.toDegrees(Θ1)+"\nΘ2="+Math.toDegrees(Θ2)+"\nT1="+Math.toDegrees(T1)+"\nT2="+Math.toDegrees(T2)+"\nΘ'1="+Math.toDegrees(O1)+"\nΘ'2="+Math.toDegrees(O2));
	
		
		
	//	double V1a = (v1*M)+(v2*M22);
		
		//FasT.getFasT().getLogger().debug("V1Theta="+V1+" OR V'1a="+V1a);
		
		
			//On reconvertit en coordonnées (x,y) en ajoutant P1
		//	C V1b = new C(new Angle(O1+t.getTheta().getRad()),V1);
		//	C V2b = new C(new Angle(O2+t.getOpposite().getTheta().getRad()),V2);
			C V1b = new C(new Angle(velocity1R.getTheta().getRad()+t.getTheta().getRad()),velocity1R.getRho());
			C V2b = new C(new Angle(velocity2R.getTheta().getRad()+t.getTheta().getRad()),velocity2R.getRho());
			
			entity1.setVelocity(V1b);
			entity2.setVelocity(V2b);
			
			if(Math.abs(V1b.scal(n))<=0.001)
			{
				entity1.noforces=true;
			}
			if(Math.abs(V2b.scal(n))<=0.001)
			{
				entity2.noforces=true;
			}

			/*FasT.getFasT().getLogger().debug("\n\n------[Engaging collision]------\n\n"
					+ "v1 = "+ velocity1.getTheta().toStringDeg()
					+ "\nv2 = "+ velocity2.getTheta().toStringDeg()
					+ "\nΘ1 = "+ Math.toDegrees(Θ1)
					+ "\nΘ2 = "+ Math.toDegrees(Θ2)
					+ "\n"
					+ "\nΘ'1 = "+ Math.toDegrees(O1)
					+ "\nΘ'2 = "+ Math.toDegrees(O2)
					+ "\nv'1 = "+ V1b.getTheta().toStringDeg()
					+ "\nv'2 = "+ V2b.getTheta().toStringDeg()
					+ "\n\n\n"

					);*/
			
			if(m1>m2)
			{
			FasT.getFasT().getLogger().debugV(entity2.getPosition(),V2b,Color.GOLD,2);
			}
			else
			{
			FasT.getFasT().getLogger().debugV(entity1.getPosition(),V1b,Color.GOLD,2);
			}
		
	}
	
	public void setNewVelocityFixed2(Entity entity1, Entity entity2)
	{
		
			if(entity2 instanceof Wall)
			{
				Wall wall = (Wall) entity2;
				C t = new C(wall.getAngle(),1).getOpposite();
				
				C n = new C(new Angle(t.getTheta().getRad()-Math.PI/2),1);
				

			//DEBUG	FasT.getFasT().getLogger().debugV(entity1.getPosition(),t,Color.BLACK,2);
			//DEBUG	FasT.getFasT().getLogger().debugV(entity1.getPosition(),n,Color.PINK,2);
				
				C v = entity1.getVelocity();
				
				double Θv = v.getTheta().getRad()-t.getTheta().getRad();
				
			//	C V = t.product(Math.cos(Θv)).sum(n.product(Math.sin(Θv))).sum(v.getRho());
				
				C cost = t.product(Math.cos(Θv));
				C sinn = n.product(Math.sin(Θv));
				
				C Vf = (sinn.sum(cost)).product(v.getRho());
			
				entity1.setVelocity(Vf);
			}
	}
	
	
	//End regular collisions
	
	
	//Real collisions
	
	public double predictCollisions(Entity e1, ArrayList<Entity> entities, double deltat)
	{
		if(!this.rebonds || !(e1 instanceof Ball))
			return -1;
	
	Ball ball = (Ball) e1;
	
	ArrayList<Wall> walls = new ArrayList<Wall>();
	
	for(Entity entity : entities)
	{
		if(entity==ball)
			continue;
		
		if(entity instanceof Wall)
			walls.add((Wall) entity);
		
		if(entity instanceof Box)
			walls.addAll(((Box)entity).getWalls());
	}
	
	
	double minT = deltat+1;
	Point minPosition = null;
	Wall wallColliding = null;
	for(Wall wall : walls)
	{
		Object[] var = predictCollision(ball.getVelocity(),ball.getPosition(),ball.getRadius(),wall,deltat);
		if(var!=null)
		{
			
			double currentT = (double) var[0];
			Point currentPosition = (Point) var[1];
			//FasT.getFasT().getLogger().debug("Collision on :"+currentT);
		if(currentT < minT)
		{
			minT=currentT;
			wallColliding = wall;
			minPosition=currentPosition;
		}
		else if(currentT == minT)
		{
			//FasT.getFasT().getLogger().debug("two collisions at the same time !");
		}
		}
	}
	if(minPosition!=null)
	{
		ball.setPosition(minPosition);
		ball.positions.add(minPosition);
		this.setNewVelocityFixed2(ball, wallColliding);
//		FasT.getFasT().getLogger().debug("Detecting collision between ball "+ball.getUUID()+" and wall "+wallColliding.getUUID());
		return deltat-minT;
	}
	

	return -1;	
	}

	public Object[] predictCollision(C velocity, Point position, double radius,Wall wall,double deltat)
	{
	/*	C velocity = ball.getVelocity();
		Point position = ball.getPosition();
		double radius = ball.getRadius();*/
		C Va = velocity;
		C Ra0 = position.getC();
		
		C Rp0 = wall.getPosition().getC();
		C Rp1 = wall.getMaxPos().getC();

		C Rt = Rp1.substracte(Rp0).div(Rp1.substracte(Rp0).getMod()); // P1-P0 / ||P1-P0||
		C Rn = new C(new Angle(Rt.getTheta().getRad()+Math.PI/2),1);
		
		//FasT.getFasT().getLogger().debugV(position, Rn, Color.BLACK);
	
		
		//Equation du second degré
		
		C R0 = Ra0.substracte(Rp0);
		
		//FasT.getFasT().getLogger().debugV(wall.getPosition(), R0, Color.BLUE);
		
		double r0 = R0.scal(Rn);
		
		//FasT.getFasT().getLogger().debugV(wall.getPosition(), new C(Rn.getTheta(),r0), Color.GOLD);
		
		double v = Va.scal(Rn);
		
		//FasT.getFasT().getLogger().debugV(position, new C(Rn.getTheta(),v), Color.BLACK);
		
		
		//double D = Math.abs(r0+v*deltat);
		

		
		//FasT.getFasT().getLogger().debug(Maths.dfloor(D)+"<=>"+radius);
	/*	FasT.getFasT().getLogger().debugV(position, new C(Rn.getOpposite().getTheta(),v*deltat+radius), Color.GOLD);
		//D>radius
		if(v*deltat+radius<r0)
		{
			return deltat;
		}
		else if(true)
		{
			return 0.001;
			}
		*/
		
		
		double D = radius;
					
		double A = v*v; 
		double B = 2*r0*v; 
	//	double C = r0*r0-D*D;
		
		//FasT.getFasT().getLogger().debugV(new Point(0,0),new C(new Angle(Rn.getTheta().getRad()-Math.PI/2),4*A*C), Color.GOLD);
		//FasT.getFasT().getLogger().debugV(new Point(0,0),new C(Rn.getTheta(),B*B), Color.PINK);
		
		//FasT.getFasT().getLogger().debugV(new Point(0,0),new C(new Angle(Rn.getTheta().getRad()-Math.PI/4),B*B-4*A*C), Color.PINK);
		
		//Discriminant
		//double Δ = Maths.Δ(A, B, C);
		double Δ = 4*v*v*D*D; 
		
		double sqrtΔ = Math.abs(2*v*D);
		
		
		//FasT.getFasT().getLogger().warning("Δ="+Δ + " and 4v^2D^2="+ 4*v*v*D*D);
		
		if(v==0)
		{
			FasT.getFasT().getLogger().warning("Impossible, droites parralèles");
			return null;
		}
		else if(Δ==0)
		{
			FasT.getFasT().getLogger().warning("La balle ne possède qu'un point");
			return null;
		}
		else
		{
			double t1 = (-B+sqrtΔ) / (2*A);
			double t2 = (-B-sqrtΔ) / (2*A);
			
	
			
			//FasT.getFasT().getLogger().debug("T1="+t1+" and T2="+t2);
			
			double t = -1;
			if(t1>0 && t2>0)
			{
				t = Math.min(t1, t2);
			}
			else if(t1 <0 && t2 >0)
			{
				t= t2; 
			}
			else if(t2<0 && t1>0)
			{
				t= t1;
			}
			
			double d0 = R0.scal(Rn);
			double van = Va.scal(Rn);
			
			if(d0<0)
			{
				if(van>0)
				{
					t=Math.min(t1, t2);
				}
				else
				{
					return null;
				}
			}
			else if(d0>0)
			{

				if(van<0)
				{
					t=Math.min(t1, t2);
				}
				else
				{
					return null;
				}
			}
			else
			{
				return null;
			}
			
			
			//double ZERO = Math.pow(10, -10); // Patch de correction merdique
			
			if(t>=0 && t<=deltat)
			{

				FasT.getFasT().getLogger().debug("t1="+t1+"     t2="+t2+"     t="+t);
				
				C Ra1 = Ra0.sum(Va.product(t));
				C R1 = Ra1.substracte(Rp0);
				
				
				
				
				
				double r1 = R1.scal(Rt);
				double d1 = R1.scal(Rn);
				
			/*	double van = Va.scal(Rn);
				
				if(d1<0)
				{
					if(van<0)
					{
						FasT.getFasT().getLogger().debug("d1<0 et van<0");
						return null;
					}
				}
				else if(d1>0)
				{

					if(van>0)
					{
						FasT.getFasT().getLogger().debug("d1>0 et van>0");
						return null;
					}
				}
				else
				{
					return null;
				}*/
				
				//FasT.getFasT().getLogger().warning("D1="+d1);
				
				Point p = new Point(Ra1.getRe(),Ra1.getIm());
				// && 
				FasT.getFasT().getLogger().debugV(wall.getPosition(),new C(new Angle(Rt.getTheta().getRad()),Math.abs(R1.scal(Rn))), Color.BLUE);	
				
				if(r1>-radius && r1<(Rp1.substracte(Rp0).getMod()+radius))
				{
				//if(Math.abs(R1.scal(Rn))>radius/2)
				//{
					return new Object[] {t,p};
				//}
				}
				
				
				
			}
			
			return null; 
		}

	}

	/*Point A = ball.getPosition();
	Point B = point;

	//y=ax+b
	double a = A.coef(B);
	double b = A.getY()-a*A.getX();*/
	
	/*Point C = wall.getPosition();
	Point D = wall.getMaxPos();
	
	//y=cx+d
	double c = C.coef(D);
	double d = C.getY()-c*C.getX();
	
	//Get segment intersection*/
	

	
	//End real collisions

	
	//Mise a jour des entités
	
	private int depth = 0;
	public boolean update(Entity entity, double deltat,ArrayList<Entity> entities) {
		
		/*for(Entity entityColliding : this.getEntitiesCollidingWith(entity,entities))
		{
			if(entity.getUUID() == FasT.getFasT().theBall)
				this.collideEntityWithEntity(entity,entityColliding);
		}*/
		
		if(entity.getMass()==-1)
			return false;
		this.updatePosition(entity, deltat, entities);
		
		return true;
		
	}
	private void updatePosition(Entity entity, double deltat,ArrayList<Entity> entities)
	{
		//FasT.getFasT().getLogger().info("New tic after " + deltat );//+ "("+deltat2+")" + "'"+(deltat2-deltat)+"'");
				double time = deltat; // s

				/*ArrayList<Wall> walls = new ArrayList<Wall>();
				
				for(Entity entity1 : entities)
				{
					if(entity1==entity)
						continue;
					
					if(entity1 instanceof Wall)
						walls.add((Wall) entity1);
					
					if(entity1 instanceof Box)
						walls.addAll(((Box)entity1).getWalls());
				}
				
				/*if(entity.noforces && entity instanceof Ball)
				{
					entity.noforces=false;
					for(Wall wall : walls)
					{
					if(BB.collisionBallWall(entity.getPosition(), ((Ball)entity).getRadius(), wall.getPosition(), wall.getMaxPos()))
					{
						entity.noforces=true;
					}
					}
					return;
				}
				boolean flag = false;
				
				for(Wall wall : walls)
					{
					if(BB.collisionBallWall(entity.getPosition(), ((Ball)entity).getRadius(), wall.getPosition(), wall.getMaxPos()))
					{
						flag = true;
					}
					}*/
				
				//Méthode générique
				//1. Somme des forces
			//	C F = new C(new Angle(Angle.convertToRad(270)),g*time).sum(new C(new Angle(Angle.convertToRad(180)),6*time)); //On ajoute un vecteur accélération arbitraire du au vent parceque c'est drole
				
				
				
				//Calcul des forces dynamiques
				
				
				
				//Forces absolues
			//	C vent = new C(new Angle(Angle.convertToRad(0)),300); // Force du vent = 6 Newton
				//C trounoirquivaengloutirlaterre = new C(new Angle(Angle.convertToRad(45)),2);
			//	C L = new C(new Angle(Angle.convertToRad(10)),600);
				//C L1 = L.getOpposite();
				
				
			//	C reaction = P.getOpposite();
				//C lune = new C(new Angle(Angle.convertToRad(45)),2);
				//C F = P;//.sum(trounoirquivaengloutirlaterre);//.sum(P.getOpposite().div(2));//.sum(lune);
				
				
			/*	if(GROUND)
				{
					this.GROUND=false;
					//double Ec = Math.pow(entity.getVelocity().getMod(),2)*entity.getMass()/2; // Ec = 1/2(mv^2)
					entity.setVelocity(entity.getVelocity().getConj());
					//F = F.sum(reaction).sum(new C(reaction.getTheta(),Ec));
				}*/
				
				//F = l'ensemble des forces
				//F = ma => a=F/m (kg/ms^2 / kg = ms^2)


				
				
				
				
				
					//double speedh = 20; // m/s default : 62
				//FasT.getFasT().getLogger().error("("+speedh*deltat+")"+"("+speedh*deltat2+")");
					//double x = entity.getPosition().getX()+speedh*time;
				
				
				//On calcule la nouvelle vitesse avec la formule F=ma (chute libre donc a=g)
				
				//Utiliser la méthode d'Euler et faire la Somme de tout les vecteurs force puis F=ma, une fois qu'on a le vecteur accélération, l'appliquer
				
			
				//P=F en chute libre = Somme de toutes les forces qui s'appliquent sur l'objet
				
				
				//On recalcule la vitesse à partir d'une équation horaire avec une adition de vecteurs
				//entity.setVelocity(entity.getVelocity().sum(a));
				
				
				
				
					//entity.velocity=entity.velocity-g*time; //On calcule la nouvelle vitesse à partir de la nouvelle accélération (lim v+1/dt)
				
				
					//double y = entity.getPosition().getY()+entity.velocity*time;
				//double y = entity.getPosition().getY();
				
				
				//C oldVelocity = entity.getVelocity();
				
				
				//Somme des forces
				C F = getForces(entity,entities);
				for(C f : entity.getInstantForces())
				{
					F = F.sum(f.div(time));
					//FasT.getFasT().getLogger().debug("FORCE="+F.getRho());	
				}

				//Get accélération with F = ma
				C acceleration = this.acceleration(F, entity.getMass());
				
				C velocity = this.integralA(entity.getVelocity(),acceleration,time);
				
			
				C d = new C();
				if(this.simulationLevel==SimulationLevel.CHUTE_LIBRE)
				{
					d = integralV(entity.getVelocity(),acceleration,time);
				}
				else
				{
					//euler
					d=euler(velocity,time);
				}
				
				
				//Set new velocity
				entity.setVelocity(velocity);
				
				//Check collisions
				double newDeltat = -1;
				//if(!(entity instanceof Ball) || !this.predictCollisions((Ball) entity, new Point(newPos.getRe(),newPos.getIm()), entities));
				if((newDeltat = this.predictCollisions(entity, entities,deltat)) > 0)
				{
					depth++;
					if(depth<3)
					 this.updatePosition(entity, newDeltat, entities);
					//FasT.getFasT().getLogger().debug("Collision");
					if(FasT.getFasT().debug)
					{
						FasT.getFasT().setPaused(true);	
					}
					FasT.getFasT().getLogger().debugV(entity.getPosition(), entity.getVelocity(),Color.GREEN);
					return;
					//deltat=newDeltat;//Quick fix
				}
			/*	else if(flag)
				{
					entity.setVelocity(new C(oldVelocity.getRe(),0));
				}
				depth=0;
				*/
			//	double x = entity.getPosition().getX()+entity.getVelocity().getRe()*time; // OLD STYLE
			//	double y = entity.getPosition().getY()+entity.getVelocity().getIm()*time;
				
				
				C pos = new C(entity.getPosition().getX(),entity.getPosition().getY());
				C newPos = pos.sum(d);
				
				entity.setPosition(new Point(newPos.getRe(),newPos.getIm()));

				
				
				//FasT.getFasT().setPaused(true);
		//		FasT.getFasT().getLogger().debugV(entity.getPosition(), entity.getVelocity(),Color.GREEN);
			
	}
	
	
	//Somme des forces
	
	//Retourne la somme des forces qui s'appliquent sur l'objet (hors collisions) en fonction de sa position relative aux autres objets, la gravité, les forces de frottement, en fonction du niveau de simulation
 	private C getForces(Entity entity, ArrayList<Entity> entities)
	{
		ArrayList<C> forces = new ArrayList<C>();

		
		if(simulationLevel>=0)
		{
			for(Entity e : entities)
			{
				if(e==entity || !(entity instanceof Ball) || !(e instanceof Ball) || BB.distanceBetweenTwoPoints(entity.getPosition(),e.getPosition())<=(((Ball) e).getRadius())+((Ball)entity).getRadius())
					continue;
				//FasT.getFasT().getLogger().warning(G*e.getMass()*entity.getMass()/Math.pow(BB.distanceBetweenTwoPoints(e.getPosition(), entity.getPosition()),2));
				C pp = new C(entity.getPosition().getX()-e.getPosition().getX(),entity.getPosition().getY()-e.getPosition().getY());
				forces.add(new C(pp.getTheta(),-G*e.getMass()*entity.getMass()/Math.pow(BB.distanceBetweenTwoPoints(e.getPosition(), entity.getPosition()),2)));
			}
		}
		if(simulationLevel>=1)
		{
			//forces.add(weight(entity.getMass())); // P=mg 
			boolean isInLiquid = false;

			for(Box box : this.getBoxAround(entity,entities))
			{
				forces.add(weight(entity.getMass(),box.g()));
				isInLiquid=true;
				break;
			}
			if(!isInLiquid && this.applyEverywhere)
			{
				Box b = (Box) FasT.getFasT().getEntityHandler().get(FasT.getFasT().theBox);
				forces.add(weight(entity.getMass(),b.g()));
				
			}
			//FasT.getFasT().getLogger().debug("weight="+weight(entity.getMass()));
		}
		if(simulationLevel>=2)
		{
			boolean isInLiquid = false;
			for(Box box : this.getBoxAround(entity,entities))
			{
				if(((Ball)entity).getVolumeImmerged(box)>0)
				{
					isInLiquid=true;
					forces.add(archimede(box.getLiquid().getMasseVolumique(),((Ball) entity).getVolumeImmerged(box),g));
					forces.add(frottements(box.getLiquid(),entity.getVelocity(),entity.getFlow(),((Ball)entity).getAire()));//Math.pow(((Ball)entity).getRadius(),2)*Math.PI));
					break;
				}
			}
			
			if(!isInLiquid && this.applyEverywhere)
			{
				Box b = (Box) FasT.getFasT().getEntityHandler().get(FasT.getFasT().theBox);
				forces.add(archimede(b.getLiquid().getMasseVolumique(),((Ball) entity).getVolume(),g));
				forces.add(frottements(b.getLiquid(),entity.getVelocity(),entity.getFlow(),((Ball)entity).getAire()));
			}
		}
		
		
		//forces.add(drag(entity.getVelocity(),10000000));
		//FasT.getFasT().getLogger().debug(wind(Math.pow(((Ball)entity).getRadius(),2)*Math.PI));
		//forces.add(wind(Math.pow(((Ball)entity).getRadius(),2)*Math.PI));
		
		C sumForces=new C();
		
		for(C force : forces)
		{
			sumForces = sumForces.sum(force);
		}
		
		return sumForces;
	}
	
	private ArrayList<Box> getBoxAround(Entity entity,ArrayList<Entity> entities) {
		ArrayList<Box> boxes = new ArrayList<Box>();
		for(Entity e : entities)
		{
			if(e instanceof Box && entity instanceof Ball)
			{
				if(entity.isInside(e))
				{
					boxes.add((Box) e);
					return boxes;
				}
			}
		}
		return boxes;
	}

	private C archimede(double fluid,double V,C g)
	{
		//b = constante de résistance (kg/s)
		return g.product(-fluid*V);
	}
	
	private C frottements(Liquid fluid,C v,double diam,double aire)
	{
		//double Re = v.getMod()*r*fluid.getMasseVolumique()/fluid.getVisc();
		//FasT.getFasT().getLogger().debug("Re="+Re);
		//return Re;
/*if(true)
{
		double k = fluid.getVisc()*diam*3*Math.PI;// Loi de STOCKES
	//	FasT.getFasT().getLogger().debug("vk="+v.product(-k));
		FasT.getFasT().getLogger().debug("k="+k);
		FasT.getFasT().getLogger().debug(fluid.getVisc());
		return v.product(-k);
}*/
		
		if(v.getMod()<5)
		{
			double k = fluid.getVisc()*diam*3*Math.PI;// Formule de STOCKES
			//FasT.getFasT().getLogger().debug("vk="+v.product(-k));
			//FasT.getFasT().getLogger().debug(fluid.getVisc());
			return v.product(-k);
		}
		else if(v.getMod()>5 && v.getMod()<20)
		{
			double k = 0.5*fluid.getVisc()*0.45*aire;
			//FasT.getFasT().getLogger().debug(v.product(k));
			C l = new C(v.getTheta(),Math.pow(v.getRho(),2)*(-k));//1.4 ??
			//FasT.getFasT().getLogger().debug("l="+l);
			return l;
		}
		else
		{
		//	FasT.getFasT().getLogger().debug("VERY FAST");
			double k = 0.5*fluid.getVisc()*0.45*aire;
			C l = new C(v.getTheta(),Math.pow(v.getRho(), 3)*(-k));
			return l;
		}
		/*else if(v.getMod()>5 && v.getMod()<20)
		{
			double k = 0.44*fluid.getMasseVolumique()*aire;
			return v.product(-k);
		}
		else
		{
			FasT.getFasT().getLogger().error("FATAL ERROR OUT OF VELOCITY EXCEPTION");
			return new C();
		}*/

			/*FasT.getFasT().getLogger().debug("Re="+Re);
			double k = 0.44*Math.PI*fluid.getMasseVolumique()/8*Math.pow(diam, 2);
			return v.product(v).product(-k);*/
		
	}
	
	private C wind(double surface)
	{
		double vitesse = 50;
		return new C(new Angle(Angle.convertToRad(180)),surface*1/2*vitesse*vitesse);
	}
	
	private C drag(C v,double b)
	{
		return v.product(-b);
	}
	
	
	//Calcul des intégrales...
	
	private C acceleration(C F,double mass)
	{
		return F.div(mass);
	}
	
	
	private C integralA(C v,C acceleration,double deltat)
	{
		return v.sum(acceleration.product(deltat));
	}
	

	private C euler(C v,double deltat)
	{
		return v.product(deltat);
	}
	
	private C integralV(C v1, C a,double deltat)
	{
		return v1.product(deltat).sum((a.product(Math.pow(deltat, 2))).div(2));
	}
	
	
	
	private C weight(double mass, C g1)
	{
		return g1.product(mass);
	}
	
	
	//Unused
	@Deprecated 
private ArrayList<Entity> getEntitiesCollidingWith(Entity entity,ArrayList<Entity> entities) {
		
		ArrayList<Entity> collideList = new ArrayList<Entity>();
	
		/*for(Entity entity1: entities)
		{
			if(entity==entity1)
				continue;
			if(entity.collidesWith(entity1))
			{
				if(!(entity.wasCollided() || entity1.wasCollided()))
				{
					FasT.getFasT().getLogger().warning("Object "+entity.getUUID() + " collides with object "+entity1.getUUID());
					collideList.add(entity1);
				}
			}
		}
		if(collideList.isEmpty())
		{
			entity.flagCollided(false);
		}*/
		return collideList;
	}
}
	/*******
	private void collideEntityWithEntity(Entity entity, Entity entityC) {
		if(stop)
			return;
		
		if(entity.getUUID()!=FasT.getFasT().theBall)
			return;
		
		if(false)//entityC.getMass()==-1)
		{
			//entity.velocity=0;
			//g=0;
		}
		else
		{
			
			C velocity1 = entity.getVelocity();
			C velocity2 = new C();//;entityC.getVelocity();
			
			double v1 = velocity1.getRho();
			double v2 = velocity2.getRho();
			
			//NV = V-P-PI/2 => V = NV+P+PI/2
			C P1 = new C(entityC.getPosition().getX()-entity.getPosition().getX(),entityC.getPosition().getY()-entity.getPosition().getY());
			
			//C T1 = velocity1.substracte(P1).substracte(Math.PI/2);
			double o1 = velocity1.getTheta().getRad()-P1.getTheta().getRad()-(Math.PI/2);
			
			//double o1 = T1.getTheta().getRad();
			
			
		//	double t1 = velocity1.getTheta().getRad();
		//	double t2 = velocity2.getTheta().getRad();
			
		//	double ab = Math.atan((entity.getPosition().getY()-entityC.getPosition().getY())/(entity.getPosition().getX()-entityC.getPosition().getX()));
			//double a = ab-(Math.PI/2);
			
		//	double o1 = ab+t1-(Math.PI/2);
		//	double o2 = a-t2;
			
					
			double m1 = entity.getMass();
			double m2 = 0;//entityC.getMass();
			
			double V1 = 0;
			double V2 = 0;
			double O1 = 0;
			double O2 = 0;
			
			if(true);//entityC.getVelocity().getRho()==0)
			{
				O1 = Math.atan(((m1-m2)/(m1+m2))*Math.tan(o1));
				V1 = Math.sqrt(Math.pow(((m1-m2)/(m1+m2)*v1*Math.sin(o1)), 2)+Math.pow(v1*Math.cos(o1), 2));
				
				//C V1a = new C(new Angle(O1),V1);
				
				//C V1b = V1a.sum(P1).sum(Math.PI/2);
				
				C V1b = new C(new Angle(V1+P1.getTheta().getRad()+(Math.PI/2)),V1);
				
				FasT.getFasT().getLogger().debug("v1="+v1+"|V1="+V1+"|o1="+o1+"|O1="+O1+"|V1b="+V1b.getTheta().getRad());
				entity.setVelocity(V1b);
				//entity.setVelocity(new C(new Angle(O1),v1));
			//	entityC.setVelocity(new C(new Angle(O2),V2));
				
			//	entity.setPosition(entity.positions.get(entity.positions.size()-2));
				//entity.setVelocity(new C(entity.getVelocity().getRe(),-entity.getVelocity().getIm()));
			}
			
		
			
			/*if(entity.getMass()==entityC.getMass())
			{
				
				O1 = Math.atan((v2/v1)*(Math.sin(o2)/Math.cos(o1)));
				O2 = Math.atan((v1/v2)*(Math.sin(o1)/Math.cos(o2)));
				
				V1 = (v1*((m1-m2)/(m1+m2)))+(v2*((2*m2)/(m1*m2)));
				V2 = (v1*((2*m1)/(m2+m1)))+(v2*((m2-m1)/(m1*m2)));
				
			}
			
			
			//stop=true;
			
			/*entity.setVelocity(entity.getVelocity().getConj());
			entityColliding.setVelocity(entityColliding.getVelocity().getConj());
			
			
		}
		
		
		
	}**********/

