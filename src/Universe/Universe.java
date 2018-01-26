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
	public final float GRAVITATIONAL_CONSTANT = 66.74f; //Higher number = faster but more inaccurate simulation. Lower number = slower but more accurate.

	private Handler handler;
	private ArrayList<Entity> entities;
	
	//for the background
	private ArrayList<Position> starPositions;
	private ArrayList<Integer> starSizes;
		
	public Universe(Handler handler)
	{
		this.handler = handler;
		this.entities = new ArrayList<Entity>();
		
		//background init
		this.starPositions = new ArrayList<Position>();
		this.starSizes = new ArrayList<Integer>();
		for(int i = 0; i < 10000; i++)
		{
			Position p = new Position(handler.randomWithRange(-10000, 10000), handler.randomWithRange(-10000, 10000));
			starPositions.add(p);
			int size = handler.randomWithRange(3, 7);
			starSizes.add(size);
		}
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

	//to make smaller objects render behind larger ones
	//using recursive merge sort
	private ArrayList<Entity> sortEntities(ArrayList<Entity> entities)
	{
	    ArrayList<Entity> left = new ArrayList<Entity>();
	    ArrayList<Entity> right = new ArrayList<Entity>();
	    int center;
	    
	    if (entities.size() == 1) 
	    {    
	        return entities;
	    } 
	    else 
	    {
	        center = entities.size()/2;
	        // copy the left half of whole into the left.
	        for (int i=0; i<center; i++) 
	        {
	            left.add(entities.get(i));
	        }
	 
	        //copy the right half of whole into the new arraylist.
	        for (int i=center; i<entities.size(); i++) 
	        {
	        	right.add(entities.get(i));
	        }
	 
	        // Sort the left and right halves of the arraylist.
	        left  = sortEntities(left);
	        right = sortEntities(right);
	 
	        // Merge the results back together.
	        merge(left, right, entities);
	    }
	    
	    return entities;
	}
	
	private void merge(ArrayList<Entity> left, ArrayList<Entity> right, ArrayList<Entity> whole) 
	{
	    int leftIndex = 0;
	    int rightIndex = 0;
	    int wholeIndex = 0;
	 
	    // As long as neither the left nor the right ArrayList has
	    // been used up, keep taking the smaller of left.get(leftIndex)
	    // or right.get(rightIndex) and adding it at both.get(bothIndex).
	    while (leftIndex < left.size() && rightIndex < right.size()) 
	    { 
	        if((left.get(leftIndex).getRadius() > right.get(rightIndex).getRadius()))
	        {
	            whole.set(wholeIndex, left.get(leftIndex));
	            leftIndex++;
	        } 
	        else 
	        {
	            whole.set(wholeIndex, right.get(rightIndex));
	            rightIndex++;
	        }
	        wholeIndex++;
	    }
	 
	    ArrayList<Entity> rest;
	    int restIndex;
	    if (leftIndex >= left.size()) 
	    {
	        // The left ArrayList has been use up...
	        rest = right;
	        restIndex = rightIndex;
	    } 
	    else 
	    {
	        // The right ArrayList has been used up...
	        rest = left;
	        restIndex = leftIndex;
	    }
	 
	    // Copy the rest of whichever ArrayList (left or right) was not used up.
	    for (int i=restIndex; i<rest.size(); i++) 
	    {
	        whole.set(wholeIndex, rest.get(i));
	        wholeIndex++;
	    }
	}
	
    //detects if two entities collide. if they do the larger entity (based on mass) eats up the smaller one.
    protected void collisionDetection()
    {
        for (Entity e : this.entities) 
        {
            for (Entity e2 : this.entities) 
            {
            	if(!e.equals(e2))
            	{
            		if(e.collidesWith(e2))
            		{
            			if (e.getMass() >= e2.getMass())
            			{
            				e.combine(e2);
            				entities.remove(e2);
            			}
            			else
            			{
            				e2.combine(e);
            				entities.remove(e);
            			}
            			return;
            		}
            	}
            }
        }
    }
	
	public void tick()
	{
		collisionDetection();
		for(int i = 0; i < entities.size(); i++)
		{
			entities.get(i).tick();
		}
	}
	
	public void render(Graphics g)
	{
		//filling in background (just black)
		drawBackground(g);
				
		//rendering entities
		for(int i = 0; i < entities.size(); i++)
		{
			//if the planet is on screen, then render it
			if(entities.get(i).isVisible())
				entities.get(i).render(g);
		}
	}
	
	public void drawBackground(Graphics g)
	{
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, handler.getWidth(), handler.getHeight());
		
		//rendering in star field very laggy, not sure how to implement this to look good with camera zoom
		//size between 3-10
		/*
		int drawX, drawY;
		g.setColor(Color.WHITE);
		for(int i = 0; i < starPositions.size(); i++)
		{
			drawX = (int)(starPositions.get(i).getX() - handler.getCamera().getxOffset()) / handler.getCamera().getZoomLevel();
			drawY = (int)(starPositions.get(i).getY() - handler.getCamera().getyOffset()) / handler.getCamera().getZoomLevel();
			if((drawX<0 || drawX>handler.getWidth()) || (drawY<0 || drawY>handler.getHeight()))
				continue;
			else
			{
				if( starSizes.get(i)/ handler.getCamera().getZoomLevel() <= 1)
					g.fillOval(drawX-starSizes.get(i)/2, drawY-starSizes.get(i)/2, 2, 2);
				else
					g.fillOval(drawX-starSizes.get(i)/2, drawY-starSizes.get(i)/2, starSizes.get(i)/ handler.getCamera().getZoomLevel(), starSizes.get(i)/ handler.getCamera().getZoomLevel());
			}
		}
		*/
	}
}
