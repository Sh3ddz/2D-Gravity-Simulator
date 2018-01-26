package Universe;

import java.util.ArrayList;


//IMPORTANT NOTE: THIS CLASS IS MANUALLY FLIPPED IN THE Y AXIS, 
//				  SINCE JAVA's 2dGRAPHICS COORDINATE SYSTEM THE Y VALUE INCREASES AS IT GOES DOWNWARDS.
public class Vector
{
	private double magnitude;// magnitude overall (hypotenuse of the triangle the vector makes)
	private double x; //x value diff (magnitude in the x direction)
	private double y; //y value diff (magnitude in the y direction)
	private double direction; //0-360 (in degrees) in a circle around the entity(I.E what direction the vector is pointing in)
	
	public Vector()
	{
		this.magnitude = 0;
		this.direction = 0;
		this.x = 0;
		this.y = 0;
	}
	
	public Vector(double x, double y)
	{
		this.x = x;
		this.y = y;
		calculateMagnitude();
		calculateDirection();
	}
	
	public double getMagnitude()
	{
		return this.magnitude;
	}
	
	public double getDirection()
	{
		return this.direction;
	}
	
	public double getX()
	{
		return this.x;
	}
	
	public double getY()
	{
		return this.y;
	}
	
	public void setX(double x)
	{
		this.x = x;
		calculateMagnitude();
		calculateDirection();
	}
	
	public void setY(double y)
	{
		this.y = y;
		calculateMagnitude();
		calculateDirection();
	}
	
	public void setVect(double x, double y)
	{
		this.x = x;
		this.y = y;
		calculateMagnitude();
		calculateDirection();
	}
	
	public void setDirection(double d)
	{
		this.direction = d;
		calculateXAndY();
	}
	
	public void setMagnitude(double m)
	{
		this.magnitude = m;
		calculateXAndY();
	}
	
	public void calculateXAndY()
	{
		this.y = Math.sin(this.direction) * this.magnitude;
		this.x = Math.cos(this.direction) * this.magnitude;
		
		
		
		if(this.direction < 90)
		{
			this.y *= -1;
		}
		else
			if(this.direction < 180 && this.direction > 90)
			{
				this.x *= -1;
			}
			else
				if(this.direction < 270 && this.direction > 180)
				{
					this.x *= -1;
				}
				else
					if(this.direction < 360 && this.direction > 270)
					{
						this.y *= -1;
					}
				
			
	}
	
	public void calculateDirection()
	{	
		//if its on the middle of a quadrant ie. 90,180,270,360/0
		if(this.x == 0 || this.y == 0)
		{
			if(this.x > 0 && this.y == 0)
				this.direction = 0;
			else
				if(this.x == 0 && this.y > 0) //originally 90, flipped because java's dumb coordinate system.
					this.direction = 270;
				else
					if(this.x < 0 && this.y == 0)
						this.direction = 180;
					else
						if(this.x == 0 && this.y < 0) //originally 270, flipped because java's dumb coordinate system.
							this.direction = 90;
		}
		else
		{
			/*
			this isn't how the calculations are in the real world because java's coordinate system is fucked up.
			it looks like this (Java's y axis is flipped for some reason) so i changed the formulas to accomidate for that.:
			
					y-															   y+	
				q2	|   q1													   q2  |   q1
					|															   |
			x- <---------> x+	  when in real life it looks like this->   x- <---------> x+
					|															   |
				q3	|   q4													   q3  |   q4
					y+															   y-
			 */
			
			if(this.x > 0 && this.y < 0)//quadrant1
				this.direction = Math.toDegrees(Math.atan(Math.abs(this.y)/this.x)); //default is radians so have to translate it to deg
			else
				if(this.x < 0 && this.y < 0)//quadrant2 so flip it horizontally
					this.direction = 180 - Math.toDegrees(Math.atan(Math.abs(this.y)/Math.abs(this.x))); //default is radians so have to translate it to deg
				else
					if(this.x < 0 && this.y > 0)//quadrant3 so mirror it
						this.direction = Math.toDegrees(Math.atan(this.y/Math.abs(this.x))) + 180; //default is radians so have to translate it to deg
					else
						if(this.x > 0 && this.y > 0)//quadrant4 so flip it vertically
							this.direction = 360 - Math.toDegrees(Math.atan(this.y/this.x)); //default is radians so have to translate it to deg
		}
	}
	
	public void calculateMagnitude()
	{
		this.magnitude = Math.sqrt((Math.pow(x, 2)) + (Math.pow(y, 2)));
	}
	
	public void add(double x, double y)
	{
		this.x += x;
		this.y += y;
		calculateMagnitude();
		calculateDirection();
	}
		
	public void add(Vector v)
	{
		this.x += v.getX();
		this.y += v.getY();
		calculateMagnitude();
		calculateDirection();
	}
	
	public void add(ArrayList<Vector> v) //adds all of the vectors together including the orignal object
	{
		for(int i = 0; i < v.size(); i++)
		{
			this.x += v.get(i).getX();
			this.y += v.get(i).getY();
		}
		calculateMagnitude();
		calculateDirection();
	}
	
	public void subtract(double x, double y)
	{
		this.x -= x;
		this.y -= y;
		calculateMagnitude();
		calculateDirection();
	}
	
	public void multiply(double x, double y)
	{
		this.x *= x;
		this.y *= y;
		calculateMagnitude();
		calculateDirection();
	}
	
	public void divide(double x, double y)
	{
		this.x /= x;
		this.y /= y;
		calculateMagnitude();
		calculateDirection();
	}
	
	public String toString()
	{
		return "x:"+this.x+", y:"+this.y+" | magnitude:"+this.magnitude+", direction:"+this.direction;
	}
}
