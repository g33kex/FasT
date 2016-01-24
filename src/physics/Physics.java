package physics;

import java.util.ArrayList;

import physics.maths.Angle;
import physics.maths.C;
import physics.maths.Normal;
import physics.maths.Normal.Unit;
import physics.maths.Point;
import game.FasT;
import game.Liquid;
import game.entities.Ball;
import game.entities.Box;
import game.entities.Entity;

public class Physics {
//http://warmaths.fr/SCIENCES/unites/VOL%20CAPA/spher2.htm
	//private double g = 9.81; // N/kg
	private C g = new C(new Angle(Angle.convertToRad(-90)),9.81);
	private final double G = 6.67384*Math.pow(10,-11);
	
	public boolean GROUND = false;
	
	
	public int simulationLevel = SimulationLevel.NOTHING; // -1 = Nothing 0 = Univers 1 = chute libre 2 = chute dans un liquide 3 = chute avec frottements 4 = chute avec rebonds
	
	private class SimulationLevel
	{
		public static final int NOTHING=-1;
		public static final int UNIVERS=0;
		public static final int CHUTE_LIBRE=1;
		public static final int CHUTE_LIQUID=2;
		public static final int CHUTE_FRICTION=3;
	}
	
	Physics()
	{
	//	g=6; FALLING ON THE MOON
	}

	public boolean update(Entity entity, double deltat,ArrayList<Entity> entities) {
		
		for(Entity entityColliding : this.getEntitiesCollidingWith(entity,entities))
		{
			this.collideEntityWithEntity(entity,entityColliding);
		}
		
		if(entity.getMass()==-1)
			return false;
		this.updatePosition(entity, deltat, entities);
		
		return true;
		
	}
	
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
				
			}*/	
			
			
			//stop=true;
			
			/*entity.setVelocity(entity.getVelocity().getConj());
			entityColliding.setVelocity(entityColliding.getVelocity().getConj());*/
			
			
		}
		
		
		
	}

	boolean stop = false;

	private ArrayList<Entity> getEntitiesCollidingWith(Entity entity,ArrayList<Entity> entities) {
		
		ArrayList<Entity> collideList = new ArrayList<Entity>();
		if(stop)
			return collideList;
		for(Entity entity1: entities)
		{
			if(entity==entity1)
				continue;
			if(entity.collidesWith(entity1))
			{
				FasT.getFasT().getLogger().warning("Object "+entity.getUUID() + " collides with object "+entity1.getUUID());
				collideList.add(entity1);
			}
		}
		return collideList;
	}

	private void updatePosition(Entity entity, double deltat,ArrayList<Entity> entities)
	{
		//FasT.getFasT().getLogger().info("New tic after " + deltat );//+ "("+deltat2+")" + "'"+(deltat2-deltat)+"'");
				double time = deltat; // s
				
				
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
				
				
				if(GROUND)
				{
					this.GROUND=false;
					//double Ec = Math.pow(entity.getVelocity().getMod(),2)*entity.getMass()/2; // Ec = 1/2(mv^2)
					entity.setVelocity(entity.getVelocity().getConj());
					//F = F.sum(reaction).sum(new C(reaction.getTheta(),Ec));
				}
				
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
				
				
				
				
				
				//Intégrale de a
				C F = getForces(entity,entities);
				for(C f : entity.getInstantForces())
				{
					F = F.sum(f.div(time));
					FasT.getFasT().getLogger().debug("FORCE="+F.getRho());	
				}

				C acceleration = this.acceleration(F, entity.getMass());
				//FasT.getFasT().getLogger().debug("Vitesse actuelle=" + entity.getVelocity().getRho());
				
				C d = new C();
				if(this.simulationLevel==SimulationLevel.CHUTE_LIBRE)
				{
					d = analytiqueCL(acceleration,entity.getVelocity(),deltat);
					entity.setVelocity(entity.getVelocity().sum(entity.getVelocity().product(deltat)));
				}
				else
				{
					//euler
					entity.setVelocity(entity.getVelocity().sum(this.euler(acceleration,time)));
					//Equation horaire
				
					d=entity.getVelocity().product(time);
				}
			//	double x = entity.getPosition().getX()+entity.getVelocity().getRe()*time;
			//	double y = entity.getPosition().getY()+entity.getVelocity().getIm()*time;
				C pos = new C(entity.getPosition().getX(),entity.getPosition().getY());
				C newPos = pos.sum(d);
				
				entity.setPosition(new Point(newPos.getRe(),newPos.getIm()));
			
	}
	
	
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
			for(Box box : this.getBoxAround(entity,entities))
			{
				forces.add(weight(entity.getMass(),box.g()));
			}
			//FasT.getFasT().getLogger().debug("weight="+weight(entity.getMass()));
		}
		if(simulationLevel>=2)
		{
			for(Box box : this.getBoxAround(entity,entities))
			{
				forces.add(archimede(box.getLiquid().getMasseVolumique(),((Ball) entity).getVolumeImmerged(box),g));
			}
		}
		if(simulationLevel>=3)
		{
			for(Box box : this.getBoxAround(entity,entities))
			{
				if(((Ball)entity).getVolumeImmerged(box)>0)
				{
				/*if(box.getUUID()==FasT.getFasT().theBox)
					continue;*/
				forces.add(frottements(box.getLiquid(),entity.getVelocity(),entity.getFlow(),((Ball)entity).getAire()));//Math.pow(((Ball)entity).getRadius(),2)*Math.PI));
				}
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
			FasT.getFasT().getLogger().debug(fluid.getVisc());
			return v.product(-k);
		}
		else if(v.getMod()>5 && v.getMod()<20)
		{
			double k = 0.5*fluid.getVisc()*0.45*aire;
			FasT.getFasT().getLogger().debug(v.product(k));
			C l = new C(v.getTheta(),Math.pow(v.getRho(),2)*(-k));//1.4 ??
			//FasT.getFasT().getLogger().debug("l="+l);
			return l;
		}
		else
		{
			FasT.getFasT().getLogger().debug("VERY FAST");
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
	
	private C acceleration(C F,double mass)
	{
		return F.div(mass);
	}
	
	private C euler(C a,double deltat)
	{
		return a.product(deltat);
	}
	
	private C analytiqueCL(C a, C v1,double deltat)
	{
		return v1.product(deltat).sum((a.product(Math.pow(deltat, 2))).div(2));
	}
	
	private C weight(double mass)
	{
		return g.product(mass);
	}
	
	private C weight(double mass, C g1)
	{
		return g1.product(mass);
	}
	
}
