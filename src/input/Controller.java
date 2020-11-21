package input;

import java.awt.Color;
import java.awt.Graphics;

import Universe.*;
import main.Handler;

public class Controller
{
	private Handler handler;
	private float x, y; //actual position in space
	private float lastMX, lastMY; //The last mouse position
	private float drawX, drawY; //where its drawn at on screen
	
	public boolean drawTails = false;
	public boolean pausedGame = false;

	private long planetMass = 20;
	private long starMass = 150;
	private long blackHoleMass = 500;
		
	private boolean placingPlanet = false;
	private boolean placingStar = false;
	private boolean placingBlackHole = false;
	private boolean placingAsteroid = false;

	public Controller(Handler handler)
	{
		this.handler = handler;
		this.x = 0;
		this.y = 0;
		this.lastMX = 0;
		this.lastMY = 0;
		this.drawX = 0;
		this.drawY = 0;
	}
	
	private void getInput()
	{
		getMouseInput();
		getKeyboardInput();
	}
	
	private void getMouseInput()
	{
		this.drawX = handler.getKeyManager().mX;
		this.drawY = handler.getKeyManager().mY;
		this.x = (this.drawX + handler.getCamera().getxOffset() / handler.getCamera().getZoomLevel()) * handler.getCamera().getZoomLevel();
		this.y = (this.drawY + handler.getCamera().getyOffset() / handler.getCamera().getZoomLevel()) * handler.getCamera().getZoomLevel();
		float cX = (handler.getKeyManager().cX + handler.getCamera().getxOffset() / handler.getCamera().getZoomLevel()) * handler.getCamera().getZoomLevel();
		float cY = (handler.getKeyManager().cY + handler.getCamera().getyOffset() / handler.getCamera().getZoomLevel()) * handler.getCamera().getZoomLevel();
		float mX = (handler.getKeyManager().mX + handler.getCamera().getxOffset() / handler.getCamera().getZoomLevel()) * handler.getCamera().getZoomLevel();
		float mY = (handler.getKeyManager().mY + handler.getCamera().getyOffset() / handler.getCamera().getZoomLevel()) * handler.getCamera().getZoomLevel();

		//placing entities
		if(handler.getKeyManager().leftClick)
			placingPlanet = true;
		if(handler.getKeyManager().rightClick)
			placingStar = true;
		if(handler.getKeyManager().middleClick)
			placingBlackHole = true;
		if(handler.getKeyManager().leftClick && handler.getKeyManager().shift)
		{
			placingAsteroid = true;
			placingPlanet = false;
		}
		if(placingPlanet)
		{
			if(handler.getKeyManager().holding)
				planetMass += planetMass*.05;
			if(handler.getKeyManager().released)
			{
				Vector initialVector = new Vector((this.x - cX)/20, (this.y - cY)/20);
				Planet tempPlanet = new Planet(handler, cX, cY, planetMass, initialVector);
				handler.getUniverse().addEntity(tempPlanet);
				handler.getKeyManager().leftClick = false;
				planetMass = 20;
				placingPlanet = false;
			}
		}

		if(placingStar)
		{
			if(handler.getKeyManager().holding)
				starMass += starMass*.05;
			if(handler.getKeyManager().released)
			{
				Vector initialVector = new Vector((this.x - cX)/20, (this.y - cY)/20);
				Star tempStar = new Star(handler, cX, cY, starMass, initialVector);
				handler.getUniverse().addEntity(tempStar);
				handler.getKeyManager().rightClick = false;
				starMass = 150;
				placingStar = false;
			}
		}

		if(placingBlackHole)
		{
			if(handler.getKeyManager().holding)
				blackHoleMass += blackHoleMass*.05;
			if(handler.getKeyManager().released)
			{
				Vector initialVector = new Vector((this.x - cX)/20, (this.y - cY)/20);
				BlackHole tempBlackHole = new BlackHole(handler, cX, cY, blackHoleMass, initialVector);
				handler.getUniverse().addEntity(tempBlackHole);
				handler.getKeyManager().middleClick = false;
				blackHoleMass = 500;
				placingBlackHole = false;
			}
		}

		if(placingAsteroid)
		{
			if(handler.getKeyManager().holding)
			{
				for(int i = 0; i < 1; i++)
				{
					float randX =(float)handler.randomWithRange((int)this.x-30, (int)this.x+30);
					float randY =(float)handler.randomWithRange((int)this.y-30, (int)this.y+30);
					Vector initialVector = new Vector((mX-lastMX)/5, (mY-lastMY)/5);
					Asteroid asteroid = new Asteroid(handler, randX, randY, 1, initialVector);
					handler.getUniverse().addEntity(asteroid);
				}
				//Vector initialVector = new Vector((this.x - cX)/20, (this.y - cY)/20);
				//handler.getKeyManager().leftClick = false;
				placingAsteroid = false;
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

		this.lastMX = mX;
		this.lastMY = mY;
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
			handler.getKeyManager().r = false;
		}
	}
	
	public void tick()
	{
		getInput();
	}
	
	public void render(Graphics g)
	{
		g.setColor(Color.RED);
		g.fillOval((int)drawX-5, (int)drawY-5, 10, 10);
		
		renderEntityPlacement(g);
	}
	
	private void renderEntityPlacement(Graphics g)
	{
		g.setColor(Color.WHITE);
		if(placingPlanet)
		{
			//drawing mass
			double radius =  ((Math.sqrt(Math.PI * planetMass / 20) * 20) / handler.getCamera().getZoomLevel())/2;
			g.drawOval((int)(handler.getKeyManager().cX-radius), (int)(handler.getKeyManager().cY-radius), (int)(radius*2), (int)(radius*2));
			//drawing initial vector
			g.drawLine(this.handler.getKeyManager().cX, this.handler.getKeyManager().cY, (int)this.drawX, (int)this.drawY);
		}
		
		if(placingStar)
		{
			//drawing mass
			double radius =  (Math.sqrt(Math.PI * starMass / 40) * 20) / handler.getCamera().getZoomLevel();
			g.drawOval((int)(handler.getKeyManager().cX-radius), (int)(handler.getKeyManager().cY-radius), (int)(radius*2), (int)(radius*2));
			//drawing initial vector
			g.drawLine(this.handler.getKeyManager().cX, this.handler.getKeyManager().cY, (int)this.drawX, (int)this.drawY);
		}
		
		if(placingBlackHole)
		{			
			//drawing mass
			long radius = (long) (Math.sqrt(Math.PI * blackHoleMass / 500) * 20) / handler.getCamera().getZoomLevel();
			g.drawOval((int)(handler.getKeyManager().cX-radius), (int)(handler.getKeyManager().cY-radius), (int)(radius*2), (int)(radius*2));
			//drawing initial vector
			g.drawLine(this.handler.getKeyManager().cX, this.handler.getKeyManager().cY, (int)this.drawX, (int)this.drawY);
		}
	}
}
