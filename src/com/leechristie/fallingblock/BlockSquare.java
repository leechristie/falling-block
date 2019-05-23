package com.leechristie.fallingblock;

import java.awt.*;

public class BlockSquare {

	private static final int TRANSITION_UP = 0;
	private static final int TRANSITION_DOWN = 1;
	private static final int TRANSITION_LEFT = 2;
	private static final int TRANSITION_RIGHT = 3;
	private static final int TRANSITION_CW = 4;
	private static final int TRANSITION_CCW = 5;

	private Square theSquare;

	private int theX;
	private int theY;

	private Square getSquare() {
	
		return theSquare;
	
	}
	
	private void setSquare(Square square) {
	
		theSquare = square;
	
	}

	private int getX() {
	
		return theX;
	
	}
	
	private void setX(int x) {
	
		theX = x;
	
	}
	
	private int getY() {
		
		return theY;
		
	}
	
	private void setY(int y) {
		
		theY = y;
		
	}
	
	public BlockSquare(int x, int y, Square square) {
	
		setX(x);
		setY(y);
		setSquare(square);
	
	}
	
	public boolean positionOkay(SquarePile pile, Block block) {

		// Get the block's position
		int blockX = block.getX();
		int blockY = block.getY();
	
		// Calculate relative co-ordinates
		int relX = getX();
		int relY = getY();

		// Translate these into absolute co-ordinates
		int absX = relX + blockX;
		int absY = relY + blockY;

		// Ask the square pile if there is an available space there and return
		return pile.emptyAt(absX, absY);
	
	}
	
	public boolean canTransition
			(SquarePile pile, Block block, int transition) {
	
		// Get the block's position
		int blockX = block.getX();
		int blockY = block.getY();
	
		// Calculate relative co-ordinates after transition
		int relX = xAfter(transition);
		int relY = yAfter(transition);
		
		// Translate these into absolute co-ordinates
		int absX = relX + blockX;
		int absY = relY + blockY;
		
		// Ask the square pile if there is an available space there and return
		return pile.emptyAt(absX, absY);
	
	}
	
	public void addToPile(SquarePile pile, Block block, int newLevel) {
	
		// Get the block's position
		int blockX = block.getX();
		int blockY = block.getY();
	
		// Calculate relative co-ordinates of the square
		int relX = getX();
		int relY = getY();
		
		// Translate these into absolute co-ordinates
		int absX = relX + blockX;
		int absY = relY + blockY;
		
		// Get the square
		Square square = getSquare();
		
		// Add to pile
		pile.setAt(absX, absY, square, newLevel);
	
	}
	
	public void transition(int transition) {
	
		// Calculate relative co-ordinates after transition
		int relX = xAfter(transition);
		int relY = yAfter(transition);
		
		// Change position
		setX(relX);
		setY(relY);
	
	}
	
	private int xAfter(int transition) {
		
		// get relative co-ords before
		int x = getX();
		int y = getY();
		
		if (transition == TRANSITION_UP || transition == TRANSITION_DOWN)
			return x;
		else if (transition == TRANSITION_LEFT)
			return x - 1;
		else if (transition == TRANSITION_RIGHT)
			return x + 1;
		else if (transition == TRANSITION_CW)
			return y;
		else // CCW
			return -y;
		
	}
	
	private int yAfter(int transition) {
		
		// get relative co-ords before
		int x = getX();
		int y = getY();
		
		if (transition == TRANSITION_UP)
			return y - 1;
		else if (transition == TRANSITION_DOWN)
			return y + 1;
		else if (transition == TRANSITION_LEFT || transition == TRANSITION_RIGHT)
			return y;
		else if (transition == TRANSITION_CW)
			return -x;
		else // CCW
			return x;
		
	}
	
	public void renderOn(Graphics g, Block block, int level, boolean ghost) {
		
		// Get the block's position
		int blockX = block.getX();
		int blockY = block.getY();
	
		// Calculate relative co-ordinates
		int relX = getX();
		int relY = getY();

		// Translate these into absolute co-ordinates
		int x = relX + blockX;
		int y = relY + blockY;
		
		// Don't render if off edge
		if (y >= 20 || y < 0 || x >= 10 || x < 0)
			return;
			
		// Get the square
		Square square = getSquare();
		
		// Render the square
		square.renderOn(g, x, y, level, ghost);
	
	}

}
