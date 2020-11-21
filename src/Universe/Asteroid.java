package Universe;

import main.Handler;

import java.awt.*;
import java.util.ArrayList;

public class Asteroid extends Entity
{
	public Asteroid(Handler handler, float x, float y, long mass)
	{
		super(handler, x, y, mass);
		this.density = 40;
		calculateRadius(mass);
	}

	public Asteroid(Handler handler, float x, float y, long mass, Vector v)
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
		int zoomDiameter = (int) (radius/handler.getCamera().getZoomLevel()*2);
		g.setColor(new Color(200,200,200));
		if(zoomDiameter >= 2)
			g.fillOval((int)(drawX), (int)(drawY), zoomDiameter, zoomDiameter);
		else
			g.fillOval((int)(drawX), (int)(drawY), 2, 2);
	}
}
