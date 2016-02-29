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

package game.entities;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import game.FasT;
import game.Liquid;
import physics.BB;
import physics.maths.Angle;
import physics.maths.C;
import physics.maths.Maths;
import physics.maths.Point;
import render.Render;

//Box, 3 or 4 walls, can be full of liquids
public class Box extends Entity {

	private Point max;
	private int sides;
	private Liquid liquid;
	private C g;
	
	private ArrayList<Wall> walls = new ArrayList<Wall>();
	
	public Point getMax()
	{
		return this.max;
	}
	
	public C g()
	{
		return this.g;
	}
	
	public Box(Point min, Point max,int sides,Liquid liquid,EntityHandler entityHandler) {
		super(min, -1, entityHandler);
		
		this.max=max;
		this.sides=sides;
		this.liquid=liquid;
		this.g=new C(new Angle(3*Math.PI/2),9.81);
		setWalls();
		this.initPopupMenu();
	}
	

	public Box(Point min, Point max,int sides,Liquid liquid,C g,EntityHandler entityHandler) {
		super(min, -1, entityHandler);
		this.max=max;
		this.sides=sides;
		this.liquid=liquid;
		this.g=g;
		setWalls();
		this.initPopupMenu();
	}
	
	public Box copy()
	{
		return new Box(this.getPosition().copy(),this.getMax().copy(),this.sides,this.getLiquid(),this.g,this.entityHandler);
	}

	public void updateLabels()
	{
		mvLabel.setText("masse volumique(kg/m^3)="+this.getLiquid().getMasseVolumique());
		fillLabel.setText("fill (%)="+Maths.dfloor(this.getLiquid().getLevel()*100));
		vLabel.setText("Viscosite(Pa.s)="+this.getLiquid().getVisc());
		gLabel.setText("g(m/s^2)="+this.g().getRho());
	}

	JSlider mvSlider = new JSlider();
	JLabel mvLabel = new JLabel();
	JLabel fillLabel = new JLabel();
	JSlider fillSlider = new JSlider();
	JLabel vLabel = new JLabel();
	JSlider vSlider = new JSlider();
	JSlider gSlider = new JSlider();
	JLabel gLabel = new JLabel();
	//JMenuItem red = new JMenuItem("MAKE IT RED");
	
	JMenu tweak3 = new JMenu("tweak");
	
	@Override
	protected void initPopupMenu()
	{
		popupMenu.remove(speedLabel);
		popupMenu.remove(ecLabel);
		
		getPopupMenuFrom(tweak3);
		this.popupMenu.add(tweak3);
	    updateLabels();
		this.popupMenu.pack();
		
	}
	
	public void getPopupMenuFrom(JMenu tweak3)
	{
	
		/*red.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e) {
				double[] color = {0.9,0.3,0.1,0.7};
				liquid.setColor(color);
			}
			
		});*/



	    mvSlider.setMinimum(0);
	    mvSlider.setMajorTickSpacing(400);
	    mvSlider.setPaintTicks(true);
	    mvSlider.setSnapToTicks(true);
	    mvSlider.setMaximum(5000);
	    mvSlider.setValue((int) this.getLiquid().getMasseVolumique());
	    mvSlider.addChangeListener(new ChangeListener()
	    		{
					@Override
					public void stateChanged(ChangeEvent e) {
						liquid.setMasseVolumique(mvSlider.getValue());
						updateLabels();
					}
	    		});
	    
	    mvSlider.addKeyListener(Render.getSliderKeyListener());

	  
	    fillSlider.setMinimum(0);
	    fillSlider.setMajorTickSpacing(10);
	    fillSlider.setMinorTickSpacing(5);
	    fillSlider.setPaintTicks(true);
	    fillSlider.setSnapToTicks(true);
	    fillSlider.setMaximum(100);
	    fillSlider.setValue((int) (this.getLiquid().getLevel()*100));
	    fillSlider.addChangeListener(new ChangeListener()
	    		{
					@Override
					public void stateChanged(ChangeEvent e) {
						liquid.setLevel((float)fillSlider.getValue()/100);
						updateLabels();
					}
	    		});
	    
	    vSlider.setMinimum(1);
	    vSlider.setMaximum(10000);
	    vSlider.setMajorTickSpacing(1000);
	    vSlider.setSnapToTicks(true);
	    vSlider.setPaintTicks(true);
	    vSlider.setValue((int) this.getLiquid().getVisc()*1000);
	    vSlider.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
				liquid.setVisc(vSlider.getValue()/1000.0);
				updateLabels();
			}
	    });
	    vSlider.addKeyListener(Render.getSliderKeyListener());
	    
	    gSlider.setMinimum(0);
	    gSlider.setMaximum(100);
	    gSlider.setMajorTickSpacing(5);
	    gSlider.setSnapToTicks(true);
	    gSlider.setPaintTicks(true);
	    gSlider.setValue((int) this.g().getRho());
	    gSlider.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
				g=new C(g.getTheta(),gSlider.getValue());
				updateLabels();
			}
	    });
	    

	    gSlider.addKeyListener(Render.getSliderKeyListener());

	  

	    
	    //tweak3.add(red);
		
		tweak3.add(mvLabel);
		tweak3.add(mvSlider);
		
		tweak3.add(vLabel);
		tweak3.add(vSlider);
		
		tweak3.add(fillLabel);
		tweak3.add(fillSlider);
		
		tweak3.add(gLabel);
		tweak3.add(gSlider);
		
		
	}
	
	
	public void fillWith(Liquid liquid)
	{
		this.liquid=liquid;
	}
	
	public Liquid getLiquid() {
		return this.liquid;
	}

	@Override
	public void render(Render render) {
		switch(this.sides)
		{
		case 4:
			GL11.glColor4d(1, 0.2, 0.0,0.001);
			render.drawSquare(this.position, max);
		case 1:
			byte[] b = {61,8,122,126};
			GL11.glColor4b(b[0],b[1],b[2],b[3]);
			ArrayList<Point> p = new ArrayList<Point>();
			p.add(this.getMax());
			p.add(new Point(this.getMax().getX(),this.getPosition().getY()));
			p.add(this.getPosition());
			p.add(new Point(this.getPosition().getX(),this.getMax().getY()));
			render.drawLines(p);
		}
		
		this.liquid.render(this,render);
	}
	
	public ArrayList<Wall> getWalls()
	{
		return this.walls;
	}
	
	public void setWalls()
	{
		walls.clear();
		double minx = this.getPosition().getX();
		double miny = this.getPosition().getY();
		double maxx = this.getMax().getX();
		double maxy = this.getMax().getY();
		switch(this.sides)
		{
		case 4:
			//Bottom
			walls.add(new Wall(this.getPosition(),new Point(maxx,miny),entityHandler));
			//Top
			walls.add(new Wall(new Point(minx,maxy),this.getMax(),entityHandler));
			
			//Left
			walls.add(new Wall(this.getPosition(),new Point(minx,maxy),entityHandler));
			//Right
			walls.add(new Wall(new Point(maxx,miny),this.getMax(),entityHandler));
			
			break;
		case 1:
			//Bottom
			walls.add(new Wall(this.getPosition(),new Point(maxx,miny),entityHandler));
			
			
			//Left
			walls.add(new Wall(this.getPosition(),new Point(minx,maxy),entityHandler));
			//Right
			walls.add(new Wall(new Point(maxx,miny),this.getMax(),entityHandler));
			break;
		}
	}
	
	
	
	public boolean hoover(Point p)
	{
		if(BB.distanceBetweenTwoPoints(this.position.toPlan(), p.toPlan())<20)
		{
			FasT.getFasT().getRender().getCanvas().setCursor(java.awt.Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			return true;
		}
		if(BB.distanceBetweenTwoPoints(this.max.toPlan(), p.toPlan())<20)
		{
			FasT.getFasT().getRender().getCanvas().setCursor(java.awt.Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR));
			return true;
		}
		return false;
	}
	
	private int dragMode = 0;
	
	@Override
	public void setBeingDragged(boolean b,Point p)
	{
		if(!b)
		{
			dragMode=0;
			return;
		}
		if(BB.distanceBetweenTwoPoints(this.position.toPlan(), p.toPlan())<20)
		{
			dragMode=1;
		}
		if(BB.distanceBetweenTwoPoints(this.max.toPlan(), p.toPlan())<20)
		{
			dragMode=2;
		}
		
	}
	
	@Override
	public boolean getBeingDragged()
	{
		return this.dragMode>0;
	}
	
	@Override
	public boolean drag(Point p,long currentNanoTime)
	{
		if(dragMode==1)
		{
			this.setPosition(this.getPosition().add(p));
			this.max=this.max.add(p);
		}
		if(dragMode==2)
		{
			if(this.max.add(p).toPlan().getX()-this.getPosition().toPlan().getX()>=40)
			{
				if(this.max.add(p).toPlan().getY()-this.getPosition().toPlan().getY()>=40)
				{
					this.setMax(this.max.add(p));
				}
				else
				{
					this.setMax(new Point(this.max.toPlan().getX(),this.getPosition().toPlan().getY()+40).toReal());
				}
			}
			else
			{
				this.setMax(new Point(this.getPosition().toPlan().getX()+40,this.max.toPlan().getY()).toReal());
			}
		}
		return true;
	}

	public boolean shouldMenu(Point p)
	{
		if(BB.distanceBetweenTwoPoints(this.position.toPlan(), p.toPlan())<20)
			return true;
		return false;
	}

	@Override
	public void setPosition(Point position)
	{
		super.setPosition(position);
		this.setWalls();
		//FasT.getFasT().getLogger().debug("setting pos");
	}
	
	public void setMax(Point max)
	{
		//FasT.getFasT().getLogger().debug("setting max");
		this.max=max;
		this.setWalls();
	}
	
	public boolean isEntityUnder(Point p)
	{
		return BB.distanceBetweenTwoPoints(this.position.toPlan(), p.toPlan())<10 || BB.distanceBetweenTwoPoints(this.max.toPlan(), p.toPlan())<10;
	}
	
}
