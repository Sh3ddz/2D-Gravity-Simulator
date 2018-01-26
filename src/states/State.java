package states;

import java.awt.Graphics;
import java.io.IOException;

import main.Handler;

public abstract class State 
{

	private static State currentState = null;
	
	public static void setState(State state)
	{
		currentState = state;
	}
	
	public static State getState()
	{
		return currentState;
	}
	
	//CLASS
	
	protected Handler handler;
	
	public State(Handler handler)
	{
		this.handler = handler;
	}
	
	public abstract void tick() throws IOException;
	
	public abstract void render(Graphics g);
	
}