package Universe;

import java.awt.Color;
import java.awt.Graphics;

import main.Handler;

public class Star extends Entity
{	
	public Star(Handler handler, float x, float y, long mass)
	{
		super(handler, x, y, mass);
		this.density = 40;
		calculateRadius(mass);
	}
	
	public Star(Handler handler, float x, float y, long mass, Vector v)
	{
		super(handler, x, y, mass);
		this.density = 40;
		calculateRadius(mass);
		this.velocity = v;
	}

	@Override
	public void tick()
	{
		calculateAttraction();
	}

	@Override
	public void render(Graphics g)
	{
		//drawX / drawY effected by zoom
		int zoomDiameter = (int) (radius/handler.getCamera().getZoomLevel())*2;
		g.setColor(Color.YELLOW);
		if(zoomDiameter >= 2)
			g.fillOval((int)(drawX), (int)(drawY), zoomDiameter, zoomDiameter);
		else
			g.fillOval((int)(drawX), (int)(drawY), 2, 2);

		if(handler.getApplication().debugMode)
		{
			drawDebugVectors(g);
			g.setColor(Color.WHITE);
			g.drawString(velocity.getMagnitude()+"",(int)(drawX), (int)(drawY-10));
		}
	}
	
	public void drawDebugVectors(Graphics g)
	{
		g.setColor(Color.RED);
		g.drawLine((int)drawXCenter, (int)drawYCenter, (int)drawXCenter + (int)(this.velocity.getX()*20), (int)drawYCenter + (int)(this.velocity.getY()*20));
		g.setColor(Color.GREEN);
		g.drawLine((int)drawXCenter, (int)drawYCenter, (int)drawXCenter + (int)(this.netForce.getX()),(int)drawYCenter + (int)(this.netForce.getY()));
	}
}
