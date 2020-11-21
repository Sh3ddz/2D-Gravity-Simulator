package Universe;

import java.awt.Graphics;

import main.Application;
import main.Handler;

public abstract class Entity
{	
	protected Handler handler;
	
	protected float x, y; //where its at in space(WILL BE CALCULATED AT THE CENTER.)
	protected long  mass; //mass of the object.
	protected double radius; //how big the object is.
	protected double density; //how dense the object is (used in radius calculation)
	
	//Net force acting upon the entity
	protected Vector netForce;
	//Acceleration of the entity
	protected Vector acceleration;
	//Velocity of the entity
	protected Vector velocity;
	
	//Drawing components
	protected float drawX, drawY;//where its at on the screen(this has to be at the top left corner because thats how java draws)
	protected float drawXCenter, drawYCenter; //separate int for the center of where its drawn
	
	public Entity(Handler handler)
	{
		this.handler = handler;
		this.x = 0;
		this.y = 0;
		
		this.mass = 0;
		this.radius = 0;
		this.netForce = new Vector();
		this.acceleration = new Vector();
		this.velocity = new Vector();
	}
	
	public Entity(Handler handler, float x, float y, long mass)
	{
		this.handler = handler;
		this.x = x;
		this.y = y;
		
		this.mass = mass;
		calculateRadius(mass);
		this.netForce = new Vector();
		this.acceleration = new Vector();
		this.velocity = new Vector();
	}
	
	private void updateDrawPosition()
	{
		this.drawX =(int)((this.x-(this.radius)-handler.getCamera().getxOffset()) / handler.getCamera().getZoomLevel());
		this.drawY =(int)((this.y-(this.radius)-handler.getCamera().getyOffset()) / handler.getCamera().getZoomLevel());
		this.drawXCenter = (int)((this.drawX*handler.getCamera().getZoomLevel() +(this.radius))/handler.getCamera().getZoomLevel());
		this.drawYCenter = (int)((this.drawY*handler.getCamera().getZoomLevel()+(this.radius))/handler.getCamera().getZoomLevel());
	}
	
	//if it should be rendered
	public boolean isVisible()
	{
		if((this.drawXCenter<0 || this.drawXCenter>handler.getWidth()) || (this.drawYCenter<0 || this.drawYCenter>handler.getHeight()))
			return false;
		return true;
	}
	
	protected void calculateRadius(long mass)
	{
		this.radius = density == 0 ? 0 : Math.sqrt(Math.PI * mass / density) * 10;
	}
	
	private void combine(Entity e)
	{
		//using inelastic collision equation
		this.velocity.multiply(this.mass, this.mass);
		e.velocity.multiply(e.mass, e.mass);
		this.velocity.add(e.velocity);
		this.velocity.divide((this.mass)+e.mass, (this.mass)+e.mass);
		this.setMass(this.getMass() + e.getMass());
	}

	private void collide(Entity e)
	{
		if (this.getMass() >= e.getMass())
		{
			this.combine(e);
			handler.getUniverse().getEntities().remove(e);
		}
		else
		{
			e.combine(this);
			handler.getUniverse().getEntities().remove(this);
		}
	}

	protected void calculateAttraction()
	{
		double distance, xDist, yDist;
		double forceMag, nextStep;

		Vector totalGravForce = new Vector();

		//for(Entity e : handler.getUniverse().getEntities())
		for(int i = handler.getUniverse().getEntities().size()-1; i >= 0; i--)
		{
			Entity e = handler.getUniverse().getEntities().get(i);
			if(e == this) continue;
			xDist = e.getX() - x;
			yDist = e.getY() - y;
			distance = Math.sqrt(Math.pow(xDist, 2) + Math.pow(yDist, 2));
			//collision detection
			if(distance < this.radius + e.getRadius()) this.collide(e);
			else
			{
				forceMag = handler.getUniverse().GRAVITATIONAL_CONSTANT * ((this.mass * e.getMass()) / Math.pow(distance, 2));
				nextStep = (forceMag / this.mass) + (forceMag / e.getMass());
				//next step collision detection
				if(distance < nextStep) this.collide(e);
				else
				{
					double xComponent = (forceMag * xDist / distance);
					double yComponent = (forceMag * yDist / distance);
					totalGravForce.add(new Vector(xComponent, yComponent));
				}
			}
		}
		this.netForce = totalGravForce;
		applyAttractionVector();
	}
    
    //moves the entity based on the attraction vector
    private void applyAttractionVector()
    {
    	//Force = mass*acceleration :: acceleration = force/mass
    	//velocity = meters / second :: acceleration = meters / second / second
    	// SO: velocity changes by the acceleration every tick.
		if(!handler.getController().pausedGame)
		{
	    	this.acceleration.setVect((netForce.getX()/this.mass), (netForce.getY()/this.mass));
			//acceleration.setVect(0, 9.8/60);
			this.velocity.add(acceleration);
			this.x += velocity.getX();
			this.y += velocity.getY();
//			if(Application.FPS <= 60)
//			{
//				this.x += velocity.getX() * (60.0 / Application.FPS);
//				this.y += velocity.getY() * (60.0 / Application.FPS);
//			}
		}
        updateDrawPosition();
    }
    
    public float getX()
	{
		return this.x;
	}
	
	public float getY()
	{
		return this.y;
	}
	
	public float getDrawX()
	{
		return this.drawX;
	}
	
	public float getDrawY()
	{
		return this.drawY;
	}
	
	public long getMass()
	{
		return this.mass;
	}
	
	public double getRadius()
	{
		return this.radius;
	}
	
	public double getDensity()
	{
		return this.density;
	}
	
	public void setMass(long m)
	{
		this.mass = m;
		this.calculateRadius(this.getMass());
	}
	
	public void setRadius(double r)
	{
		this.radius = r;
	}
	
	public void setDensity(double d)
	{
		this.density = d;
	}
	
	public abstract void tick();
	
	public abstract void render(Graphics g);
}
