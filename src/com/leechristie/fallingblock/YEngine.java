package com.leechristie.fallingblock;

public class YEngine implements Runnable {

	private static final int TRANSITION_UP = 0;
	private static final int TRANSITION_DOWN = 1;
	private static final int TRANSITION_LEFT = 2;
	private static final int TRANSITION_RIGHT = 3;
	private static final int TRANSITION_CW = 4;
	private static final int TRANSITION_CCW = 5;

	public Game game;
	public Thread thread;

	public YEngine(Game game) {
	
		this.game = game;
		this.thread = null;
	
	}

	public void run() {
		
		mainloop: while (thread != null) {
		
			// Render a new frame
			game.newFrame();
		
			// Sleep for until ready to drop again
			try {thread.sleep(game.ySleepTime());}
			catch(InterruptedException e) {}
			finally {if (thread == null) break mainloop;}
			
			// Wait until the game is started and unlocked and unpaused and unanimating
			while(!game.isStarted || game.isLocked || game.isPaused || game.isAnimating) {
				try {thread.sleep(20);}
				catch(InterruptedException e) {}
				finally {if (thread == null) break mainloop;}
			}
			
			// Lock the game
			game.isLocked = true;
			
			if (game.getNext() == null) {
				
				// Get the pile
				SquarePile pile = game.getPile();

				game.isAnimating = true;
				//System.out.println("Animating ON");

				// For each row
				for (int y = 0; y < 20; y++) {
						
					// Clear next
					game.setNext(null);
						
					// Clear the row
					for (int x = 0; x < 10; x++) {
						if (x % 2 == 1)
							pile.setAt(x, y, Square.black());
						else
							pile.setAt(x, 19-y, Square.black());
					}
					
					// Render a new frame
					game.newFrame();
						
					// Sleep
					try {thread.sleep(50);}
					catch(InterruptedException e) {}
					finally {if (thread == null) break mainloop;}
					
				}
				
				game.isAnimating = false;
				//System.out.println("Animating OFF");

				// New block
				game.setNext(Block.next());
				
				// Unlock the game
				game.isLocked = false;
				
				// Loop around
				continue mainloop;
				
			}
			
			if (game.getCurrent() == null) {
				
				// Get the pile
				SquarePile pile = game.getPile();
				
				// Will it fit?
				if (game.getNext().positionOkay(pile)) {
				
					// New block
					game.setCurrent(game.getNext());
					game.setNext(Block.next());
				
					// Unlock the game
					game.isLocked = false;
				
					// Loop around
					continue mainloop;
				
				} else {
					
					game.applet.stopMusic();
		
					game.isAnimating = true;
					//System.out.println("Animating ON");
					
					// For each row
					for (int y = 0; y < 20; y++) {
						
						// Clear next
						game.setNext(null);
						
						// Clear the row
						for (int x = 0; x < 10; x++) {
							if (x % 2 == 0)
								pile.setAt(x, y, Square.gameover());
							else
								pile.setAt(x, 19-y, Square.gameover());
						}
					
						// Render a new frame
						game.newFrame();
						
						game.applet.endSound();
						
						// Sleep
						try {thread.sleep(50);}
						catch(InterruptedException e) {}
						finally {if (thread == null) break mainloop;}
					
						game.applet.endSoundOver();
					
					}
					
					game.isAnimating = false;
					//System.out.println("Animating OFF");
				
					game.isGameOver = true;
					//System.out.println("Gameover ON");
					
					// Render a new frame
					game.newFrame();
					
					game.isLocked = false;
					
					// Break
					break mainloop;
				
				}
				
			}
			
			// Get current block
			Block current = game.getCurrent();
			
			// Get the pile
			SquarePile pile = game.getPile();
			
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
			
			// Unlock the game
			game.isLocked = false;
			
		}
			
		// Unlock the game
		game.isLocked = false;
	
	}

}
