package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.io.IOException;

import display.Display;
import gfx.Camera;
import input.Controller;
import input.KeyManager;
import states.State;
import states.UniverseState;

public class Application implements Runnable
{

    private Display display;
    private int width, height;
    public String title;
	
    private boolean running = false;
    private Thread thread;
	
    private BufferStrategy bs;
    private Graphics g;
	
	//Input
    private KeyManager keyManager;
    private Controller controller;
   
	//States
	public State menuState;
	public State universeState;
	
    //Camera
    private Camera camera;
	
	//config
    private int TPS = 0;
    public static int FPS = 0;
    private int seconds = 0;
    private int minutes = 0;
    private int hours = 0;
    public boolean debugMode = false;
    
	//Handler
	private Handler handler;
	
    public Application(String title, int width, int height)
    {
    	this.width = width;
    	this.height = height;
    	this.title = title;
    	keyManager = new KeyManager();
    }
	
    private void init()
    {
    	//initializing the display
    	this.display = new Display(title, width, height);
    	display.getFrame().addKeyListener(keyManager);
    	display.getFrame().addMouseListener(keyManager);
    	display.getFrame().addMouseMotionListener(keyManager);
    	display.getFrame().addMouseWheelListener(keyManager);
		display.getFrame().setFocusTraversalKeysEnabled(false);
    	display.getCanvas().addMouseListener(keyManager);
    	display.getCanvas().addMouseMotionListener(keyManager);
		display.getCanvas().addMouseWheelListener(keyManager);
		display.getCanvas().setFocusTraversalKeysEnabled(false);
    	//handler
		this.handler = new Handler(this);
		this.controller = new Controller(handler);
		this.handler.setController(controller);

		this.camera = new Camera(this, handler.getUniverse(), handler);

    	//Setting the default state
		this.universeState = new UniverseState(handler);
		State.setState(universeState);
    }
	 
    private void tick() throws IOException
    {
	 	keyManager.tick();
 		if(State.getState() != null)
	 		State.getState().tick();
    }
	
    private void render()
    {
		 bs = display.getCanvas().getBufferStrategy();
	 	 if(bs == null)
		 {
			display.getCanvas().createBufferStrategy(3); //original is 3
			return;
		 }
	 	 g = bs.getDrawGraphics();
	 	 //Clear Screen
	 	 g.clearRect(0, 0, width, height);
	 	 //Draw Here!
	 	 width = display.getFrame().getWidth();
	 	 height = display.getFrame().getHeight();
		
	 	 if(State.getState() != null)
			State.getState().render(g);
	 	 
	 	 if(debugMode)
	 	 {
		 	 g.setColor(Color.YELLOW);
		 	 g.drawString("FPS: "+FPS, 0, 12);
		 	 g.drawString("number of entities: "+handler.getUniverse().getEntities().size(), 1, 24);
		 	 g.drawString("zoom: "+handler.getCamera().getZoomLevel(), 0, 36);
		 	 g.drawString("paused: "+handler.getController().pausedGame, 0, 48);
		 	 g.drawString("drawtails: "+handler.getController().drawTails, 0, 60);
	 	 }
		
	 	 //End Drawing!
	 	 bs.show();
	 	 g.dispose();
    }
	
    public void run()
    {
   	
    	init();
   	
    	double tps = 60; //60 ticks per second.
	    double maxFps = Integer.MAX_VALUE; //unlimited frames per second.
    	double timePerTick = 1000000000 / tps;
    	double timePerRender = 1000000000 / maxFps;
    	double delta = 0;
    	double delta2 = 0;
    	long now;
    	long lastTime = System.nanoTime();
    	long timer = 0;
    	int ticks = 0;
    	int frames = 0;
    	
    	while(running)
    	{
    		now = System.nanoTime();
    		delta += (now - lastTime) / timePerTick;
    		delta2 += (now - lastTime) / timePerRender;
    		timer += now - lastTime;
    		lastTime = now;
      	
    		if(delta >= 1)//ticking
    		{
    			try
    			{
    				tick();
    			} catch (IOException e)
    			{
    				e.printStackTrace();
    			}
    			ticks++;
    			delta--;
    		}
    		
    		if(delta2 >= 1)//rendering
    		{
    			render();
    			frames++;
    			delta2--;
    		}
    		
    		if(timer >= 1000000000)
    		{
    			seconds++;
    			if(seconds == 60)
    			{
    				seconds = 0;
    				minutes++;
    			}
    			if(minutes == 60)
    			{
    				minutes = 0;
    				hours++;
    			}
    			System.out.println("Ticks: " + ticks);
    			System.out.println("Frames: " + frames);
    			this.TPS = ticks;
    			this.FPS = frames;
    			ticks = 0;
    			frames = 0;
    			timer = 0;
    		}
    	}
    	
    	stop();
    	
    }
    
    public int getWidth()
    {
    	return this.width;
    }
    
    public int getHeight()
    {
    	return this.height;
    }
    
    public Camera getCamera()
    {
    	return this.camera;
    }
    
    public KeyManager getKeyManager()
    {
   	   	return this.keyManager;
    }
    
    public synchronized void start()
    {
    	if(running)
    		return;
    	running = true;
    	thread = new Thread(this);
    	thread.start();
    }
    
    public synchronized void stop()
    {
    	if(!running)
    		return;
    	running = false;
    	try 
    	{
    		thread.join();
    	} 
    	catch (InterruptedException e) 
    	{
    		e.printStackTrace();
    	}
    }
}
