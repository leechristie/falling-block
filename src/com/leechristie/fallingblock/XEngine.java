package com.leechristie.fallingblock;

public class XEngine implements Runnable {

	private static final int TRANSITION_UP = 0;
	private static final int TRANSITION_DOWN = 1;
	private static final int TRANSITION_LEFT = 2;
	private static final int TRANSITION_RIGHT = 3;
	private static final int TRANSITION_CW = 4;
	private static final int TRANSITION_CCW = 5;

	private boolean running;
	public int axis;
	public Game game;
	public Thread thread;

	public XEngine(Game game) {
	
		this.axis = 0;
		this.game = game;
		this.thread = null;
	
	}
	public void run() {
	
		if (running)
			return;
			
		running = true;
	
		mainloop: while (thread != null) {
		
			
			// Loop until the game is started and unlocked and unpaused and unanimating
			while(!game.isStarted || game.isLocked || game.isPaused || game.isAnimating) {
				try {thread.sleep(10);}
				catch(InterruptedException e) {}
				finally {if (thread == null || axis == 0) break mainloop;}
				continue mainloop;
			}
			
			// Lock the game
			game.isLocked = true;
			
			// Break if no block
			if (game.getCurrent() == null) {
				
				// Unlock the game
				game.isLocked = false;
				
				// Loop around
				break mainloop;
				
			}
			
			// Get current block
			Block current = game.getCurrent();
			
			// Get the pile
			SquarePile pile = game.getPile();
			
			// Get transition
			int transition = axis<0?TRANSITION_LEFT:TRANSITION_RIGHT;
			
			// Move if you can
			current.checkThenTransition(pile, transition);
	
			// Render a new frame
			game.newFrame();
			
			// Unlock the game
			game.isLocked = false;
		
			// Sleep for until ready to move again
			try {thread.sleep(150);}
			catch(InterruptedException e) {}
			finally {if (thread == null || axis == 0) break mainloop;}
			
		}	
		
		running = false;
	
	}

}
