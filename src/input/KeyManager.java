package input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.SwingUtilities;

public class KeyManager implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener
{
	private boolean[] keys;
	public boolean up, down, left, right, shift, upArrow, downArrow, leftArrow, rightArrow, debugMode, leftClick, rightClick, middleClick, mouseWheelUp, mouseWheelDown, dragging;
	public boolean t, p, r, tab, esc, backspace;
	public boolean holding, released;
	public int mX,mY,cX,cY,dX,dY;
	
	public KeyManager()
	{
		keys = new boolean[256];
	}
	
	public void tick()
	{
		up = keys[KeyEvent.VK_W];
		down = keys[KeyEvent.VK_S];
		left = keys[KeyEvent.VK_A];
		right = keys[KeyEvent.VK_D];
		shift = keys[KeyEvent.VK_SHIFT];
		upArrow = keys[KeyEvent.VK_UP];
		downArrow = keys[KeyEvent.VK_DOWN];
		leftArrow = keys[KeyEvent.VK_LEFT];
		rightArrow = keys[KeyEvent.VK_RIGHT];
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		keys[e.getKeyCode()] = true;
		if(e.getKeyCode() == KeyEvent.VK_F3)
			debugMode = true;
		if(e.getKeyCode() == KeyEvent.VK_T)
			t = true;
		if(e.getKeyCode() == KeyEvent.VK_P)
			p = true;
		if(e.getKeyCode() == KeyEvent.VK_R)
			r = true;
		if(e.getKeyCode() == KeyEvent.VK_TAB)
			tab = true;
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
			esc = true;
		if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE)
			backspace = true;
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		keys[e.getKeyCode()] = false;
	}

	@Override
	public void keyTyped(KeyEvent e) 
	{
		
	}

	@Override
	public void mouseDragged(MouseEvent e) 
	{
	    dragging = true;
		dY = e.getY();
	    dX = e.getX();
		mY = e.getY();
		mX = e.getX();
	}

	@Override
	public void mouseMoved(MouseEvent e) 
	{
	      mY=e.getY();
	      mX=e.getX();		
	}

	@Override
	public void mouseClicked(MouseEvent e) 
	{
	      cY=e.getY();
	      cX=e.getX();
	}

	@Override
	public void mouseEntered(MouseEvent e) 
	{
		
	}

	@Override
	public void mouseExited(MouseEvent e) 
	{
		
	}

	@Override
	public void mousePressed(MouseEvent e) 
	{
	    cY=e.getY();
	    cX=e.getX();
	    
		holding = true;
		released = false;
		if(SwingUtilities.isLeftMouseButton(e))
		{
			leftClick = true;
		}
		if(SwingUtilities.isRightMouseButton(e))
		{
			rightClick = true;
			//for right click camera movements
			cY = e.getY();
			cX = e.getX();
		}
		if(SwingUtilities.isMiddleMouseButton(e))
		{
			middleClick = true;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) 
	{
		dragging = false;
		holding = false;
		released = true;
		if(SwingUtilities.isLeftMouseButton(e))
		{
			leftClick = false;
		}
		if(SwingUtilities.isRightMouseButton(e))
		{
			rightClick = false;
		}
		if(SwingUtilities.isMiddleMouseButton(e))
		{
			middleClick = false;
		}
	}
	
	public void mouseWheelMoved(MouseWheelEvent e)
	{
	    int notches = e.getWheelRotation();
	    if (notches < 0)
	    {
	    	mouseWheelUp = true;
	    	mouseWheelDown = false;
	    } 
	    else 
	    {
	    	mouseWheelDown = true;
	    	mouseWheelUp = false;
	    }
	}

}