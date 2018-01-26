package gfx;

import Universe.Entity;
import Universe.Universe;
import main.Application;
import main.Handler;

public class Camera 
{
	private final int ZOOM_MAX = 100;
	private final int ZOOM_MIN = 1;
	
	private Application app;
	private Handler handler;
	private float xOffset, yOffset;
	private int zoomLevel;
	
	public Camera(Application app, Universe universe, Handler handler)
	{
		this.app = app;
		this.handler = handler;
		this.xOffset = 0;
		this.yOffset = 0;
		this.zoomLevel = 1;
	}
	
	public void move(float xAmt, float yAmt)
	{
		this.xOffset += xAmt * zoomLevel;
		this.yOffset += yAmt * zoomLevel;
	}
	
	public void setPosition(float x, float y)
	{
		this.xOffset = x;
		this.yOffset = y;
	}
	
	public void centerOnEntity(Entity e)
	{
		xOffset = (int)(e.getX() - app.getWidth() / ((double)2/zoomLevel));
		yOffset = (int)(e.getY() - app.getHeight() / ((double)2/zoomLevel)); //+ 24;
	}

	public float getxOffset() 
	{
		return xOffset;
	}

	public void setxOffset(float xOffset) 
	{
		this.xOffset = xOffset;
	}

	public float getyOffset()
	{
		return yOffset;
	}

	public void setyOffset(float yOffset) 
	{
		this.yOffset = yOffset;
	}
	
	public int getZoomLevel()
	{
		return this.zoomLevel;
	}
	
	public void setZoomLevel(int z)
	{
		if(z < ZOOM_MIN)
			z = ZOOM_MIN;
		if(z > ZOOM_MAX)
			z = ZOOM_MAX;
		if(zoomLevel < z)
		{
			for(int i = 0; i <= z - zoomLevel; i++)
			{
				zoomIn();
			}
		}
		else
			if(zoomLevel > z)
			{
				for(int i = 0; i <= zoomLevel - z; i++)
				{
					zoomOut();
				}
			}
	}
	
	//zooms towards the mouse position
	public void zoomIn()
	{
		if(zoomLevel > ZOOM_MIN)
		{

			//int mousePosX = handler.getKeyManager().mX;
			//int mousePosY = handler.getKeyManager().mY;
			//System.out.println("x:"+mousePosX+", y:"+mousePosY+" | width:"+handler.getWidth()+", height:"+handler.getHeight());
			//this.xOffset = (float) ((xOffset - mousePosX));
			//this.yOffset = (float) ((yOffset - mousePosY));
			//System.out.println("x:"+xOffset+", y:"+yOffset);
			this.xOffset = (float) (xOffset) + ((app.getWidth()/2));
			this.yOffset = (float) (yOffset) + ((app.getHeight()/2));
			this.zoomLevel--;
		}
	}
	
	//zooms away from the center of the screen
	public void zoomOut()
	{
		if(zoomLevel < ZOOM_MAX)
		{
			this.xOffset = (float) (xOffset) - ((app.getWidth()/2));
			this.yOffset = (float) (yOffset) - ((app.getHeight()/2));
			this.zoomLevel++;
		}
	}
	
	public void zoomReset()
	{
		setZoomLevel(1);
	}
	
	public void cameraReset()
	{
		this.xOffset = 0;
		this.yOffset = 0;
		this.zoomLevel = 1;
	}
}