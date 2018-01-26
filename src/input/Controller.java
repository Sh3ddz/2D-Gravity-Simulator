package input;

import java.awt.Color;
import java.awt.Graphics;

import Universe.BlackHole;
import Universe.Planet;
import Universe.Star;
import Universe.Vector;
import main.Handler;

public class Controller
{
	private Handler handler;
	private float x, y; //actual position in space
	private float cX, cY; //clicked position
	private float dispX, dispY; //where its drawn at on screen
	
	public boolean drawTails = false;
	public boolean pausedGame = false;
	public int entityCenteredOn = -1;
	
	private long planetMass = 2000000;
	private long starMass = 150000000;
	private long blackHoleMass = 500000000;
		
	private boolean placingPlanet = false;
	private boolean placingStar = false;
	private boolean placingBlackHole = false;

	public Controller(Handler handler)
	{
		this.handler = handler;
		this.x = 0;
		this.y = 0;
		this.dispX = 0;
		this.dispY = 0;
	}
	
	private void getInput()
	{
		getMouseInput();
		getKeyboardInput();
	}
	
	private void getMouseInput()
	{
		this.dispX = handler.getKeyManager().mX;
		this.dispY = handler.getKeyManager().mY;
		this.x = (this.dispX + handler.getCamera().getxOffset() / handler.getCamera().getZoomLevel()) * handler.getCamera().getZoomLevel();
		this.y = (this.dispY + handler.getCamera().getyOffset() / handler.getCamera().getZoomLevel()) * handler.getCamera().getZoomLevel();
		this.cX = (handler.getKeyManager().cX + handler.getCamera().getxOffset() / handler.getCamera().getZoomLevel()) * handler.getCamera().getZoomLevel();
		this.cY = (handler.getKeyManager().cY + handler.getCamera().getyOffset() / handler.getCamera().getZoomLevel()) * handler.getCamera().getZoomLevel();
		
		//placing entities
		if(handler.getKeyManager().leftClick)
		{
			placingPlanet = true;
		}
		if(placingPlanet)
		{
			if(handler.getKeyManager().holding)
			{
				planetMass += planetMass*.05;
			}
			if(handler.getKeyManager().released)
			{
				Vector initialVector = new Vector((this.x - this.cX)/20, (this.y - this.cY)/20);
				Planet tempPlanet = new Planet(handler, this.cX, this.cY, planetMass, initialVector);
				handler.getUniverse().addEntity(tempPlanet);
				handler.getKeyManager().leftClick = false;
				planetMass = 2000000;
				placingPlanet = false;
			}
		}
		
		if(handler.getKeyManager().rightClick)
		{
			placingStar = true;
		}
		if(placingStar)
		{
			if(handler.getKeyManager().holding)
			{
				starMass += starMass*.05;
			}
			if(handler.getKeyManager().released)
			{
				Vector initialVector = new Vector((this.x - this.cX)/20, (this.y - this.cY)/20);
				Star tempStar = new Star(handler, this.cX, this.cY, starMass, initialVector);
				handler.getUniverse().addEntity(tempStar);
				handler.getKeyManager().rightClick = false;
				starMass = 150000000;
				placingStar = false;
			}
		}
		
		if(handler.getKeyManager().middleClick)
		{
			placingBlackHole = true;
		}
		if(placingBlackHole)
		{
			if(handler.getKeyManager().holding)
			{
				blackHoleMass += blackHoleMass*.05;
			}
			if(handler.getKeyManager().released)
			{
				Vector initialVector = new Vector((this.x - this.cX)/20, (this.y - this.cY)/20);
				BlackHole tempBlackHole = new BlackHole(handler, this.cX, this.cY, blackHoleMass, initialVector);
				handler.getUniverse().addEntity(tempBlackHole);
				handler.getKeyManager().middleClick = false;
				blackHoleMass = 500000000;
				placingBlackHole = false;
			}
		}
		
		if(handler.getKeyManager().mouseWheelUp)
		{
			handler.getCamera().zoomIn();
			handler.getKeyManager().mouseWheelUp = false;
		}
		
		if(handler.getKeyManager().mouseWheelDown)
		{
			handler.getCamera().zoomOut();
			handler.getKeyManager().mouseWheelDown = false;
		}
	}
	
	private void getKeyboardInput()
	{
		//Moving the camera
		//WASD KEYS
		if(handler.getKeyManager().up && !handler.getKeyManager().down)
			handler.getCamera().move(0,-10);
		if(handler.getKeyManager().down && !handler.getKeyManager().up)
			handler.getCamera().move(0,10);
		if(handler.getKeyManager().left && !handler.getKeyManager().right)
			handler.getCamera().move(-10,0);
		if(handler.getKeyManager().right && !handler.getKeyManager().left)
			handler.getCamera().move(10,0);
	
		//ARROW KEYS
		if(handler.getKeyManager().upArrow && !handler.getKeyManager().downArrow)
			handler.getCamera().move(0,-1);
		if(handler.getKeyManager().downArrow && !handler.getKeyManager().upArrow)
			handler.getCamera().move(0,1);
		if(handler.getKeyManager().leftArrow && !handler.getKeyManager().rightArrow)
			handler.getCamera().move(-1,0);
		if(handler.getKeyManager().rightArrow && !handler.getKeyManager().leftArrow)		
			handler.getCamera().move(1,0);	
		
		if(handler.getKeyManager().debugMode)
		{
			handler.getApplication().debugMode = !handler.getApplication().debugMode;
			handler.getKeyManager().debugMode = false;
		}
		if(handler.getKeyManager().t)
		{
			this.drawTails = !drawTails;
			handler.getKeyManager().t = false;
		}
		if(handler.getKeyManager().p)
		{
			this.pausedGame = !pausedGame;
			handler.getKeyManager().p = false;
		}
		if(handler.getKeyManager().r)
		{
			handler.getUniverse().reset();
			this.entityCenteredOn = -1;
			handler.getKeyManager().r = false;
		}
		
		if(handler.getKeyManager().tab)
		{
			this.entityCenteredOn++;
			if(entityCenteredOn > handler.getUniverse().getEntities().size()-1 || entityCenteredOn > handler.getUniverse().getEntities().size())
				this.entityCenteredOn = 0;
			if(handler.getUniverse().getEntities().size() == 1)
				this.entityCenteredOn = 0;
			if(handler.getUniverse().getEntities().size() == 0)
				this.entityCenteredOn = -1;

			handler.getKeyManager().tab = false;
		}
	}
	
	public void tick()
	{
		getInput();
		if(entityCenteredOn != -1)
			if(entityCenteredOn < handler.getUniverse().getEntities().size())
				handler.getCamera().centerOnEntity(handler.getUniverse().getEntities().get(entityCenteredOn));
		//if(handler.getUniverse().getEntities().size() > 0)
			//handler.getCamera().centerOnEntity(handler.getUniverse().getEntities().get(handler.getUniverse().getEntities().size()-1));
	}
	
	public void render(Graphics g)
	{
		g.setColor(Color.RED);
		g.fillOval((int)dispX-5, (int)dispY-5, 10, 10);
		
		renderEntityPlacement(g);
	}
	
	public void renderEntityPlacement(Graphics g)
	{
		if(placingPlanet)
		{
			//drawing mass
			double radius =  (Math.sqrt(Math.PI * planetMass / 20) /50) / handler.getCamera().getZoomLevel();
			g.setColor(Color.WHITE);
			g.drawOval((int)(handler.getKeyManager().cX-radius/2), (int)(handler.getKeyManager().cY-radius/2), (int)(radius), (int)(radius));
			
			//drawing initial vector
			g.drawLine((int)(this.handler.getKeyManager().cX), (int)(this.handler.getKeyManager().cY), (int)this.dispX, (int)this.dispY);
		}
		
		if(placingStar)
		{
			//drawing mass
			double radius =  (Math.sqrt(Math.PI * starMass / 40) /50) / handler.getCamera().getZoomLevel();
			g.setColor(Color.WHITE);
			g.drawOval((int)(handler.getKeyManager().cX-radius/2), (int)(handler.getKeyManager().cY-radius/2), (int)(radius), (int)(radius));
			
			//drawing initial vector
			g.drawLine((int)(this.handler.getKeyManager().cX), (int)(this.handler.getKeyManager().cY), (int)this.dispX, (int)this.dispY);
		}
		
		if(placingBlackHole)
		{			
			//drawing mass
			long radius = (long) (Math.sqrt(Math.PI * blackHoleMass / 500) /50) / handler.getCamera().getZoomLevel();
			System.out.println(radius+", "+blackHoleMass);
			g.setColor(Color.WHITE);
			g.drawOval((int)(handler.getKeyManager().cX-radius/2), (int)(handler.getKeyManager().cY-radius/2), (int)(radius), (int)(radius));
			
			//drawing initial vector
			g.drawLine((int)(this.handler.getKeyManager().cX), (int)(this.handler.getKeyManager().cY), (int)this.dispX, (int)this.dispY);
		}
	}
}
