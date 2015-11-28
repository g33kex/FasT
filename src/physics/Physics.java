package physics;

import java.util.ArrayList;

import physics.maths.Angle;
import physics.maths.C;
import physics.maths.Normal;
import physics.maths.Normal.Unit;
import physics.maths.Point;
import game.FasT;
import game.entities.Ball;
import game.entities.Entity;

public class Physics {

	//private double g = 9.81; // N/kg
	private final C g = new C(new Angle(Angle.convertToRad(-90)),9.81);
	
	public boolean GROUND = false;
	
	
	public int simulationLevel = 1; // 1 = chute libre 2 = chute dans un liquide 3 = chute avec frottements 4 = chute avec rebonds
	
	
	public Physics()
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
		this.updatePosition(entity, deltat);
		
		return true;
		
	}
	
	private void collideEntityWithEntity(Entity entity, Entity entityColliding) {
		if(entityColliding.getMass()==-1)
		{
			//entity.velocity=0;
			//g=0;
		}
		
	}

	private ArrayList<Entity> getEntitiesCollidingWith(Entity entity,ArrayList<Entity> entities) {
		ArrayList<Entity> collideList = new ArrayList<Entity>();
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

	private void updatePosition(Entity entity, double deltat)
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
				C F = getForces(entity);
				for(C f : entity.getInstantForces())
				{
					F = F.sum(f.div(time));
					FasT.getFasT().getLogger().debug("FORCE="+F.getRho());	
				}

				
				//FasT.getFasT().getLogger().debug("Vitesse actuelle=" + entity.getVelocity().getRho());
				entity.setVelocity(entity.getVelocity().sum(this.integrate(this.acceleration(F, entity.getMass()),time)));
		
				
				//Equation horaire
				double x = entity.getPosition().getX()+Normal.normal(entity.getVelocity().getRe()*time,Unit.m);
				double y = entity.getPosition().getY()+Normal.normal(entity.getVelocity().getIm()*time,Unit.m);
				
				entity.setPosition(new Point(x,y));
	}
	
	private C getForces(Entity entity)
	{
		ArrayList<C> forces = new ArrayList<C>();
		forces.add(weight(entity.getMass())); // P=mg 
		//forces.add(archimede(1000,4/3*Math.PI*Math.pow(((Ball)entity).getRadius(),3),g));
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
	
	private C archimede(double fluid,double V,C g)
	{
		//b = constante de résistance (kg/s)
		return g.product(-fluid*V);
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
	
	private C integrate(C a,double deltat)
	{
		return a.product(deltat);
	}
	
	private C weight(double mass)
	{
		return g.product(mass);
	}
	
}
