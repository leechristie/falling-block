package com.leechristie.fallingblock;

public class YPushEngine implements Runnable {

	private static final int TRANSITION_UP = 0;
	private static final int TRANSITION_DOWN = 1;
	private static final int TRANSITION_LEFT = 2;
	private static final int TRANSITION_RIGHT = 3;
	private static final int TRANSITION_CW = 4;
	private static final int TRANSITION_CCW = 5;

	private boolean running;
	public Game game;
	public Thread thread;
	public boolean pushed;

	public YPushEngine(Game game) {
	
		this.game = game;
		this.thread = null;
		this.pushed = false;
	
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
				finally {if (thread == null || !pushed) break mainloop;}
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
			
			if (current == null)
				continue mainloop;
			
			// Get the pile
			SquarePile pile = game.getPile();
			
			// Add one to score
			game.setScore(game.getScore()+1);
			
			// Can the block move down?
			if (current.canTransition(pile, TRANSITION_DOWN)) {
				
				// Move block down
				current.forcedTransition(TRANSITION_DOWN);
				
			} else {
			
				// Land block
				current.addToPile(pile, game);
				
				// Remove the current from the game
				game.setCurrent(null);
				
				// Clear any lines made
				if (!pile.clearLines(game))
					game.applet.landSound();
			
			}
	
			// Render a new frame
			game.newFrame();
			
			// Unlock the game
			game.isLocked = false;
		
			// Sleep for until ready to move again
			try {thread.sleep(50);}
			catch(InterruptedException e) {}
			finally {if (thread == null || !pushed) break mainloop;}
			
		}	
		
		running = false;
	
	}

}
