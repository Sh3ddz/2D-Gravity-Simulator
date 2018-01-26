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
		int zoomRadius = (int) (radius/handler.getCamera().getZoomLevel());
		g.setColor(Color.YELLOW);
		if(zoomRadius >= 2)
			g.fillOval((int)(drawX), (int)(drawY), zoomRadius, zoomRadius);
		else
			g.fillOval((int)(drawX), (int)(drawY), 2, 2);

		if(handler.getApplication().debugMode)
		{
			drawDebugVectors(g);
			g.setColor(Color.WHITE);
			g.drawString(mass+"",(int)(drawX), (int)(drawY));
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
