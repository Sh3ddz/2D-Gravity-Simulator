package Universe;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import main.Handler;

//the universe the sim is in
//holds all the entities / does updates on their movements.
public class Universe
{
	//CHANGE THIS TOO DETERMINE HOW FAST OBJECTS MOVE
	public final float GRAVITATIONAL_CONSTANT = 6.674f; //Higher number = faster but more inaccurate simulation. Lower number = slower but more accurate.

	private Handler handler;
	private ArrayList<Entity> entities;

	public Universe(Handler handler)
	{
		this.handler = handler;
		this.entities = new ArrayList<Entity>();
	}
	
	public void addEntity(Entity e)
	{
		this.entities.add(e);
		//if you want to sort the entities, uncomment this.
		//sortEntities(entities);
	}
	
	public ArrayList<Entity> getEntities()
	{
		return this.entities;
	}
	
	public void reset()
	{
		this.entities.clear();
		handler.getCamera().cameraReset();
	}

	public void tick()
	{
		//collisionDetection();
		for(int i = 0; i < entities.size(); i++)
			entities.get(i).tick();
	}
	
	public void render(Graphics g)
	{
		//filling in background (just black)
		drawBackground(g);
				
		//rendering entities
		for(int i = 0; i < entities.size(); i++)
			//if the planet is on screen, then render it
			if(entities.get(i).isVisible())
				entities.get(i).render(g);
	}
	
	public void drawBackground(Graphics g)
	{
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, handler.getWidth(), handler.getHeight());
	}
}
