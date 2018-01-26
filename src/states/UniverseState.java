package states;

import java.awt.Graphics;

import Universe.Universe;
import input.Controller;
import main.Handler;

public class UniverseState extends State
{
	private Universe universe;
	private Controller controller;
	
	public UniverseState(Handler handler)
	{
		super(handler);
		this.universe = new Universe(handler);
		handler.setUniverse(universe);
		this.controller = handler.getController();
	}
	
	@Override
	public void tick()
	{
		controller.tick();
		universe.tick();
	}

	@Override
	public void render(Graphics g) 
	{
		universe.render(g);
		controller.render(g);
	}

}