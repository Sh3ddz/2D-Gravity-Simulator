package main;

import Universe.Vector;

public class Launcher 
{
   public static void main(String[] args)
   {
	   	Application app = new Application("2D Gravity Sim", 640, 480);
   		app.start();
	  
	  
	  //vector testing
	  Vector v = new Vector(0,0);
	  Vector v1 = new Vector(0.866, -0.5);
	  Vector v2 = new Vector(0.5, -0.866);
	  Vector v3 = new Vector(-0.5, -0.866);
	  Vector v4 = new Vector(-0.866, -0.5);
	  Vector v5 = new Vector(-0.866, 0.5);
	  Vector v6 = new Vector(-0.5, 0.866);
	  Vector v7 = new Vector(0.5, 0.866);
	  Vector v8 = new Vector(0.866, 0.5);
	  
	  Vector v11 = new Vector(1, 0);
	  Vector v12 = new Vector(0, -1);
	  Vector v13 = new Vector(-1, 0);
	  Vector v14 = new Vector(0, 1);
	  
	  System.out.println("v: "+v.getMagnitude()+", "+v.getDirection());
	  System.out.println("v1: "+v1.getMagnitude()+", "+v1.getDirection());
	  System.out.println("v2: "+v2.getMagnitude()+", "+v2.getDirection());
	  System.out.println("v3: "+v3.getMagnitude()+", "+v3.getDirection());
	  System.out.println("v4: "+v4.getMagnitude()+", "+v4.getDirection());
	  System.out.println("v5: "+v5.getMagnitude()+", "+v5.getDirection());
	  System.out.println("v6: "+v6.getMagnitude()+", "+v6.getDirection());
	  System.out.println("v7: "+v7.getMagnitude()+", "+v7.getDirection());
	  System.out.println("v8: "+v8.getMagnitude()+", "+v8.getDirection());
	  
	  System.out.println("v11: "+v11.getMagnitude()+", "+v11.getDirection());
	  System.out.println("v12: "+v12.getMagnitude()+", "+v12.getDirection());
	  System.out.println("v13: "+v13.getMagnitude()+", "+v13.getDirection());
	  System.out.println("v14: "+v14.getMagnitude()+", "+v14.getDirection());
	  
	  v.add(v4);
	  System.out.println("v+v4: "+v.getMagnitude()+", "+v.getDirection());
		
   }
}