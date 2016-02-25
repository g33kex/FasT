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

package physics.maths;

public class C {

	private double re,im;
	private double rho;
	private Angle theta;
	
	public C(double re, double im)
	{
		this.re = re;
		this.im=im;
		this.rho=this.getMod();
		this.theta=this.getArgument();
	}
	
	public C(Angle theta, double rho)
	{
		this.theta=theta;
		this.rho=rho;
		this.re = getRe(theta,rho);
		this.im = getIm(theta,rho);
	}

	

	public C() {
		this.theta=new Angle(0);
		this.rho=0;
		this.re=0;
		this.im=0;
	}

	//Getters
	public double getIm()
	{
		return this.im;
	}
	
	public double getRe()
	{
		return this.re;
	}
	
	public double getRho()
	{
		return this.rho;
	}
	
	public Angle getTheta()
	{
		return this.theta;
	}
	
	//Shape
	public String toString()
	{
		return this.toStringCart(this);
	}
	
	public String toStringAng(boolean rad)
	{
		return this.toStringAng(this,rad);
	}
	
	public C getConj() {
		return this.conjuge(this);
	}

	public C getOpposite() {
		return this.opposite(this);
	}
	
	public double getMod() {
		return this.module(this);
	}
	
	public Angle getArgument() 
	{
		return this.argument(this);
	}
	
	//Operations
	public C sum(C c2)
	{
		return(this.sum(this,c2));
	}
	
	public C substracte(C c2)
	{
		return(this.sum(this,c2.product(-1))); 
	}
	
	public C substracte(double real)
	{
		return(this.sum(this, -real));
	}
	
	public C sum(double real)
	{
		return(this.sum(this,real));
	}
	
	public C product(C c2)
	{
		return(this.product(this,c2));
	}
	
	public C product(double real)
	{
		return(this.product(this,real));
	}
	
	public C div(double real)
	{
		return this.div(this,real);
	}
	
	public C square()
	{
		return this.product(this);
	}
	
	//Private operations
	private C sum(C c1,C c2)
	{
		return(new C(c1.re+c2.re,c1.im+c2.im));
	}
	
	private C sum(C c1,double real)
	{
	//	return(new C(c1.re+real,c1.im));
		return(new C(new Angle(this.theta.getRad()+real),this.rho));
	}

	private String toStringAng(C c1,boolean rad)
	{
		return c1.rho + "*(cos " + (rad ? c1.theta.toString():c1.theta.toStringDeg()) + "+i*sin "+ (rad ? c1.theta.toString():c1.theta.toStringDeg()) +")";
	}
	
	private C product(C c1, double real)
	{
		return(new C(c1.getRe()*real,c1.getIm()*real));
	}
	
	private C product(C c1,C c2)
	{
		return(new C(c1.getRe()*c2.getRe()-c1.getIm()*c2.getIm(),c1.getRe()*c2.getIm()+c2.getRe()*c1.getIm()));
	}
	
	private C div(C c1,double real)
	{
		return product(c1,1/real);
	}
	
	private C conjuge(C c1)
	{
		return new C(c1.re,-c1.im);
	}
	
	private C opposite(C c1)
	{
		return new C(-c1.getRe(),-c1.getIm());
	}
	
	private double module(C c1)
	{
		return Math.sqrt(Math.pow(c1.re,2)+Math.pow(c1.im, 2));
	}
	
	private Angle argument(C c1)
	{
		//return new Angle(Math.atan(c1.getIm()/c1.getRe()));
		return new Angle(Math.atan2(c1.getIm(), c1.getRe()));
	}

	
	//Vectors methods
	
	public double scal(C rn) {
		return scal(this,rn);
	}
	
	private double scal(C V1, C V2)
	{
		return V1.getRe()*V2.getRe()+V1.getIm()*V2.getIm();
	}
	
	//End
	
	

	private double getRe(Angle theta, double rho) {
		return rho*Math.cos(theta.getRad());
	}
	
	private double getIm(Angle theta, double rho) {
		return rho*Math.sin(theta.getRad());
	}
	
	private String toStringCart(C c1)
	{
		if(c1.im > 0)
			return (c1.re==0 ? "" : c1.re + "+") + c1.im + "i";
		if(c1.im < 0)
			return (c1.re==0 ? "" : c1.re) + "-" + -c1.im + "i";
		return c1.re + "";
	}


	
	
	/*public C ToXY() 
	{
		return new C(Math.sin(this.getRe())*this.getIm(),Math.cos(this.getRe())*this.getIm());
	}
	
	public C ToPolar()
	{
		return new C(this.getAngle(),this.getMod());
	}
	
	private double getAngle()
	{
		return Math.asin(this.getIm()/this.getMod());
	}*/
	
	
	
	
}
