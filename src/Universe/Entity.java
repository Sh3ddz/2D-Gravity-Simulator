package Universe;

import java.awt.Graphics;

import main.Handler;

public abstract class Entity
{	
	protected Handler handler;
	
	protected float x, y; //where its at in space(WILL BE CALCULATED AT THE CENTER.)
	protected float vx, vy; //velocity
	
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
	
	protected void updateDrawPosition()
	{
		this.drawX =(int)((this.x-(this.radius/2)-handler.getCamera().getxOffset()) / handler.getCamera().getZoomLevel());
		this.drawY =(int)((this.y-(this.radius/2)-handler.getCamera().getyOffset()) / handler.getCamera().getZoomLevel());
		this.drawXCenter = (int)((this.drawX*handler.getCamera().getZoomLevel() +(this.radius/2))/handler.getCamera().getZoomLevel());
		this.drawYCenter = (int)((this.drawY*handler.getCamera().getZoomLevel()+(this.radius/2))/handler.getCamera().getZoomLevel());
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
		this.radius = density == 0 ? 0 : Math.sqrt(Math.PI * mass / density) /50;
	}
	
	public void combine(Entity e)
	{
		//using inelastic collision equation
		this.velocity.multiply(this.mass, this.mass);
		e.velocity.multiply(e.mass, e.mass);
		this.velocity.add(e.velocity);
		this.velocity.divide(this.mass+e.mass, this.mass+e.mass);
		
		//fixing camera centering
		if(handler.getUniverse().getEntities().indexOf(e) == handler.getController().entityCenteredOn)
			handler.getController().entityCenteredOn = handler.getUniverse().getEntities().indexOf(this);
		
		this.setMass(this.getMass() + e.getMass());
		this.calculateRadius(this.getMass());
	}
	
	//detecting if the two entities have collided
	public boolean collidesWith(Entity e)
	{
		if(!this.equals(e)) //so it doesnt interact with itself
		{
			double xDif = (this.getX() - e.getX()) /handler.getCamera().getZoomLevel();
			double yDif = (this.getY() - e.getY()) /handler.getCamera().getZoomLevel();
			double distanceSquared = xDif * xDif + yDif * yDif;
			boolean collision = distanceSquared < ((this.getRadius()/2)/handler.getCamera().getZoomLevel() + (e.getRadius()/2)/handler.getCamera().getZoomLevel()) * ((this.getRadius()/2)/handler.getCamera().getZoomLevel() + (e.getRadius()/2)/handler.getCamera().getZoomLevel());
			if(collision)
				return true;
		}
		
		return false;
	}
		
    protected void calculateAttraction() 
    {
        double distance, diffX, diffY, distance3;
        
    	Vector totalGravForce = new Vector();

        for(Entity e : handler.getUniverse().getEntities())
		{
	            if (e == this) 
	                continue;
	            diffX = e.getX() - x;
	            diffY = e.getY() - y;
	            distance = handler.distform(e.getX(), e.getY(), x, y);
	            distance3 = distance * distance * distance;
	            //Gravitational formula applied to each vector component.
	            totalGravForce.add((e.getMass() * diffX) / distance3, (e.getMass() * diffY) / distance3);
		}
        //Multiplying by G (gravitational constant
        totalGravForce.multiply(handler.getUniverse().GRAVITATIONAL_CONSTANT, handler.getUniverse().GRAVITATIONAL_CONSTANT);

    	//making the star uneffected by gravity
        //if(this instanceof Star)
        	//totalGravForce = new Vector(0, 0);
        
        netForce = totalGravForce;
        
        //System.out.println(netForce.getX()+","+netForce.getY());

        //System.out.println(netForce);
        applyAttractionVector();
    }
    
    //moves the entity based on the attraction vector
    protected void applyAttractionVector() 
    {
    	//Force = mass*acceleration :: acceleration = force/mass
    	//velocity = meters / second :: acceleration = meters / second / second
    	// SO: velocity changes by the acceleration every tick.
		if(!handler.getController().pausedGame)
		{
	    	acceleration.setVect((netForce.getX()/this.mass), (netForce.getY()/this.mass));
	    	velocity.add(acceleration);
	        x += velocity.getX();
	        y += velocity.getY();
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
