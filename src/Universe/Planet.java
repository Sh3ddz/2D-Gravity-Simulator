package Universe;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import main.Handler;

public class Planet extends Entity
{
	protected int randDir; //random direction for the AI
	protected Color color;
	
	private ArrayList<Position> positions;
	
	public Planet(Handler handler, float x, float y, long mass)
	{
		super(handler, x, y, mass);
		color = new Color(handler.randomWithRange(0,255),handler.randomWithRange(0,255),handler.randomWithRange(0,255));
		this.density = 20;
		calculateRadius(mass);
		this.positions = new ArrayList<Position>();
	}
	
	public Planet(Handler handler, float x, float y, long mass, Vector v)
	{
		super(handler, x, y, mass);
		color = new Color(handler.randomWithRange(0,255),handler.randomWithRange(0,255),handler.randomWithRange(0,255));
		this.density = 20;
		calculateRadius(mass);
		this.positions = new ArrayList<Position>();
		this.velocity = v;
	}
	
	private void addPositions()
	{
		Position p = new Position(this.x, this.y);
		
		if(positions.size() < 60)
		{
			positions.add(p);
		}
		else
		{
			positions.remove(0);
			positions.add(p);
		}
	}
	
	@Override
	public void tick()
	{
		calculateAttraction();
		if(!handler.getController().pausedGame)//just so the trail doesnt disappear while paused
			addPositions();
	}
	
	@Override
	public void render(Graphics g)
	{
		//drawX / drawY effected by zoom
		int zoomRadius = (int) (radius/handler.getCamera().getZoomLevel());
		g.setColor(color);
		if(zoomRadius >= 2)
			g.fillOval((int)(drawX), (int)(drawY), zoomRadius, zoomRadius);
		else
			g.fillOval((int)(drawX), (int)(drawY), 2, 2);

		if(handler.getController().drawTails)
			drawTail(g);
		
		if(handler.getApplication().debugMode)
		{
			drawDebugVectors(g);
			g.setColor(Color.WHITE);
			g.drawString(mass+"",(int)(drawX), (int)(drawY));
			g.drawString(velocity.getMagnitude()+"",(int)(drawX), (int)(drawY-10));
		}
	}
	
	public void drawTail(Graphics g)
	{
		g.setColor(color);
		for(int i = 0; i < positions.size(); i++)
		{
			//if(i%5==0)
				//g.fillOval((int)positions.get(i).getX()-5, (int)positions.get(i).getY()-5, 10, 10);
			if(i != 0)
			{
				int drawX1 = (int)(positions.get(i).getX()-handler.getCamera().getxOffset()) / handler.getCamera().getZoomLevel();
				int drawY1 = (int)(positions.get(i).getY()-handler.getCamera().getyOffset()) / handler.getCamera().getZoomLevel();
				int drawX2 = (int)(positions.get(i-1).getX()-handler.getCamera().getxOffset()) / handler.getCamera().getZoomLevel();
				int drawY2 = (int)(positions.get(i-1).getY()-handler.getCamera().getyOffset()) / handler.getCamera().getZoomLevel();
				//if the line is on screen
				if((drawX1<0 || drawX1>handler.getWidth()) || (drawY1<0 || drawY1>handler.getHeight()))
					continue;
				else
				{
					//g.drawOval(drawX1-2, drawY1-2, 5, 5);
					g.drawLine(drawX1, drawY1, drawX2, drawY2);
				}
			}
		}
	}
	
	public void drawDebugVectors(Graphics g)
	{
		g.setColor(Color.RED);
		g.drawLine((int)drawXCenter, (int)drawYCenter, (int)drawXCenter + (int)(this.velocity.getX()*30), (int)drawYCenter + (int)(this.velocity.getY()*30));
		g.setColor(Color.GREEN);
		g.drawLine((int)drawXCenter, (int)drawYCenter, (int)drawXCenter + (int)(this.netForce.getX()/300),(int)drawYCenter + (int)(this.netForce.getY()/300));
	}
}
