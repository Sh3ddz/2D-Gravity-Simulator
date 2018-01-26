package Universe;

public class Position
{
	private float x;
	private float y;
	
	public Position()
	{
		this.x = 0;
		this.y = 0;
	}
	
	public Position(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	public float getX(){ return this.x; }
	public float getY(){ return this.y; }
	public void setX(float x){ this.x = x; }
	public void setY(float y){ this.y = y; }

}
