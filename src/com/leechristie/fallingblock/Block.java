package com.leechristie.fallingblock;

import java.awt.*;

public class Block {

	private static final int TRANSITION_UP = 0;
	private static final int TRANSITION_DOWN = 1;
	private static final int TRANSITION_LEFT = 2;
	private static final int TRANSITION_RIGHT = 3;
	private static final int TRANSITION_CW = 4;
	private static final int TRANSITION_CCW = 5;
	
	private static final int SHAPE_UNDEFINED = -1;
	private static final int SHAPE_T = 0;
	private static final int SHAPE_J = 1;
	private static final int SHAPE_Z = 2;
	private static final int SHAPE_O = 3;
	private static final int SHAPE_S = 4;
	private static final int SHAPE_L = 5;
	private static final int SHAPE_I = 6;
	private static final int NUMBER_OF_SHAPES = 7;

	private int theRotation;
	private int theShape;
	private int theX;
	private int theY;
	private BlockSquare[] theSquares;

	public int getRotation() {
	
		return theRotation;
	
	}
	
	public void setRotation(int rotation) {
	
		theRotation = rotation;
	
	}
	
	public int rotations() {
	
		// Get the shape
		int shape = getShape();
		
		// Creates squares in the array
		switch (shape) {
			
		case Block.SHAPE_Z:
		case Block.SHAPE_S:
		case Block.SHAPE_I:
		
			return 2;
		
		case Block.SHAPE_O:
		
			return 1;
		
		default:
		
			return 4;
		
		}
	
	}

	public int getShape() {
	
		return theShape;
	
	}
	
	public void setShape(int shape) {
	
		theShape = shape;
		
		// Arrange the blocks
		createArrangement();
		
		setRotation(0);
		if (shape == SHAPE_I)
			setRotation(1);
	
	}

	public int getX() {
	
		return theX;
	
	}
	
	public void setX(int x) {
	
		theX = x;
	
	}
	
	public int getY() {
		
		return theY;
		
	}
	
	public void setY(int y) {
		
		theY = y;
		
	}
	
	public BlockSquare[] getSquares() {
		
		return theSquares;
		
	}
	
	public void setSquares(BlockSquare[] squares) {
		
		theSquares = squares;
		
	}
	
	private Block(int shape) {
		
		// Set default rotation
		setRotation(0);
		
		// Create an array of 4 squares
		BlockSquare[] squares = new BlockSquare[4];
		
		// Set the square array to the new block object
		setSquares(squares);
		
		// Set the shape of the block (needed for rotation hacks)
		setShape(shape);
		
		// Position the block at the starting position
		moveToStartingPosition();
		
	}

	public static Block next() {
	
		// Get a random shape
		int shape = (int) (Math.random() * NUMBER_OF_SHAPES);
		
		// Create and return a block of that shape
		return new Block(/*SHAPE_I*/shape);
	
	}

	private void moveToStartingPosition() {
	
		// Set the position of the block
		setX(5);
		setY(0);
	
	}
	
	public void createArrangement() {
	
		// Get the squares
		BlockSquare[] squares = getSquares();
	
		// Get the shape
		int shape = getShape();
		
		// Get the square
		Square square = Square.getSquare(shape);
	
		// Creates squares in the array
		switch (shape) {
		case Block.SHAPE_T:
		
			squares[0] = new BlockSquare(-1, 0, square);
			squares[1] = new BlockSquare(0, 0, square);
			squares[2] = new BlockSquare(1, 0, square);
			squares[3] = new BlockSquare(0, 1, square);
		
		break;
		case Block.SHAPE_J:
		
			squares[0] = new BlockSquare(-1, 0, square);
			squares[1] = new BlockSquare(0, 0, square);
			squares[2] = new BlockSquare(1, 0, square);
			squares[3] = new BlockSquare(1, 1, square);
		
		break;
		case Block.SHAPE_Z:
		
			squares[0] = new BlockSquare(-1, 0, square);
			squares[1] = new BlockSquare(0, 0, square);
			squares[2] = new BlockSquare(0, 1, square);
			squares[3] = new BlockSquare(1, 1, square);
		
		break;
		case Block.SHAPE_O:
		
			squares[0] = new BlockSquare(-1, 0, square);
			squares[1] = new BlockSquare(0, 0, square);
			squares[2] = new BlockSquare(-1, 1, square);
			squares[3] = new BlockSquare(0, 1, square);
		
		break;
		case Block.SHAPE_S:
		
			squares[0] = new BlockSquare(1, 0, square);
			squares[1] = new BlockSquare(0, 0, square);
			squares[2] = new BlockSquare(0, 1, square);
			squares[3] = new BlockSquare(-1, 1, square);
		
		break;
		case Block.SHAPE_L:
		
			squares[0] = new BlockSquare(1, 0, square);
			squares[1] = new BlockSquare(0, 0, square);
			squares[2] = new BlockSquare(-1, 0, square);
			squares[3] = new BlockSquare(-1, 1, square);
		
		break;
		case Block.SHAPE_I:
		
			squares[0] = new BlockSquare(-2, 0, square);
			squares[1] = new BlockSquare(-1, 0, square);
			squares[2] = new BlockSquare(0, 0, square);
			squares[3] = new BlockSquare(1, 0, square);
		
		break;
		}
		
	}
	
	public boolean canTransition(SquarePile pile, int transition) {
	
		// Get the squares
		BlockSquare[] squares = getSquares();
	
		// Return false if any sqaure can't transition
		for (int i = 0; i < 4; i++)
			if (!squares[i].canTransition(pile, this, transition))
				return false;
		
		// All true		
		return true;
		
	}
	
	public boolean canRotate(SquarePile pile, int transition) {
	
		// Get the squares
		BlockSquare[] squares = getSquares();
	
		int rotation = getRotation();
		int rotations = rotations();
	
		if (rotations < 2)
			return false;
			
		if ((rotations == 2 && rotation == 1 && transition == TRANSITION_CW)
				|| (rotations == 2 && rotation == 0 && transition == TRANSITION_CCW))
			transition = oppositeOf(transition);
	
		// Return false if any sqaure can't transition
		for (int i = 0; i < 4; i++)
			if (!squares[i].canTransition(pile, this, transition))
				return false;
		
		// All true		
		return true;
		
	}
	
	public void forcedRotation(int transition) {

		// Get the squares
		BlockSquare[] squares = getSquares();
	
		int rotation = getRotation();
		int rotations = rotations();
	
		if (rotations < 2)
			return;
			
		if ((rotations == 2 && rotation == 1 && transition == TRANSITION_CW)
				|| (rotations == 2 && rotation == 0 && transition == TRANSITION_CCW))
			transition = oppositeOf(transition);
			
		// Change rotation value
		if (transition == TRANSITION_CW)
			setRotation((rotation + 1) % rotations);
		else
			setRotation((rotation - 1) % rotations);
			

		// Transition each square
		for (int i = 0; i < 4; i++)
			squares[i].transition(transition);
	
	}
	
	public void checkThenTransition(SquarePile pile, int transition) {

		// If can't transition, return
		if (!canTransition(pile, transition))
			return;
			
		// Transition
		forcedTransition(transition);
			
	
	}
	
	public void checkThenRotate(SquarePile pile, int transition) {

		// If can't rotate, return
		if (!canRotate(pile, transition))
			return;

		// Rotate
		forcedRotation(transition);
			
	
	}
	
	public void forcedTransition(int transition) {

		int newX = xAfter(transition);
		int newY = yAfter(transition);
		setX(newX);
		setY(newY);
	
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
	
	private int oppositeOf(int transition) {
		
		if (transition == TRANSITION_UP)
			return TRANSITION_DOWN;
		else if (transition == TRANSITION_DOWN)
			return TRANSITION_UP;
		else if (transition == TRANSITION_LEFT)
			return TRANSITION_RIGHT;
		else if (transition == TRANSITION_RIGHT)
			return TRANSITION_LEFT;
		else if (transition == TRANSITION_CW)
			return TRANSITION_CCW;
		else // CCW
			return TRANSITION_CW;
			
	}
	
	public void addToPile(SquarePile pile, Game game) {
		
		// Get the squares
		BlockSquare[] squares = getSquares();
		
		// Add each
		for (int i = 0; i < 4; i++)
			squares[i].addToPile(pile, this, game.getLevel()); // Add code for checking if leveled up
	
	}
	
	public void renderOn(Graphics g, int level, boolean ghost) {
	
		// Get the squares
		BlockSquare[] squares = getSquares();
		
		// Render each
		for (int i = 0; i < 4; i++)
			squares[i].renderOn(g, this, level, ghost);
	
	}
	
	public boolean positionOkay(SquarePile pile) {

		// Get the squares
		BlockSquare[] squares = getSquares();

		// Check each
		for (int i = 0; i < 4; i++)
			if (!squares[i].positionOkay(pile, this))
				return false;

		// Okay
		return true;
		
	}

}
