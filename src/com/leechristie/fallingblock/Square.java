package com.leechristie.fallingblock;

import java.awt.*;
import java.awt.image.*;

public class Square {

	// Constants representing the shape/colour groups
	private static final int SHAPE_BLACK = -1;
	private static final int SHAPE_GAMEOVER = -2;
	private static final int SHAPE_BG1 = -3;
	private static final int SHAPE_BG2 = -4;
	private static final int SHAPE_TOI = 0;
	private static final int SHAPE_JS = 1;
	private static final int SHAPE_ZL = 2;

	// Are the squares aleady rendered?
	private static boolean isPrerendered;
	// Are the squares aleady created?
	private static boolean isCreated;
	
	// Array of prerendered images for the squares
	private static Image[][] theBlockImages;
	private static Image[][] theGhostImages;
	private static Image theBlackBlockImage;
	private static Image theGameoverBlockImage;
	private static Image theBG1BlockImage;
	private static Image theBG2BlockImage;
	
	// Array of posable squares
	private static Square[] theSquares;
	private static Square theBlackSquare;
	private static Square theGameoverSquare;
	private static Square theBG1Square;
	private static Square theBG2Square;

	private int theShape;
	
	private int getShape() {
	
		return theShape;
	
	}
	
	private void setShape(int shape) {
		
		theShape = shape;
		
	}
	
	public static Square black() {
		
		// Create squares if not already created
		if (isCreated)
			createSquares();
			
		return theBlackSquare;
	
	}
	
	public static Square gameover() {
		
		// Create squares if not already created
		if (isCreated)
			createSquares();
			
		return theGameoverSquare;
	
	}
	
	public static Square bG1() {
		
		// Create squares if not already created
		if (isCreated)
			createSquares();
			
		return theBG1Square;
	
	}
	
	public static Square bG2() {
		
		// Create squares if not already created
		if (isCreated)
			createSquares();
			
		return theBG2Square;
	
	}
	
	public static Square getSquare(int shape) {
	
		// Create squares if not already created
		if (isCreated)
			createSquares();
		
		// Shape wraps around 3 different values
		if (shape > 2)
			shape %=3;
	
		// Return reference to the desired square
		return theSquares[shape];
	
	}		
	
	private Square(int shape) {
		
		// Set the shape
		setShape(shape);
		
	}
	
	private static Image getImage(int level, int shape, boolean ghost) {
		
		// Return the black shape if required
		if (shape == SHAPE_BLACK)
			return theBlackBlockImage;
		
		// Return the gameover shape if required
		if (shape == SHAPE_GAMEOVER)
			return theGameoverBlockImage;
		
		// Return the BG1 shape if required
		if (shape == SHAPE_BG1)
			return theBG1BlockImage;
		
		// Return the BG2 shape if required
		if (shape == SHAPE_BG2)
			return theBG2BlockImage;
		
		// Level clips off after 9
		if (level > 9)
			level = 9;
		
		// Shape wraps around 3 different values
		if (shape > 2)
			shape %=3;
	
		// return the corrisponding image
		return ghost?theGhostImages[level][shape]:theBlockImages[level][shape];
	
	}
	
	public static void createSquares() {
	
		// Return from the method if already created
		if (isCreated)
			return;
		
		// Create the array of squares
		theSquares = new Square[3];
		
		// For each level
		for (int shape = 0; shape < 3; shape++)
			// Create a square for that level
			theSquares[shape] = new Square(shape);
		
		// The black block
		theBlackSquare = new Square(SHAPE_BLACK);
		
		// The gameover block
		theGameoverSquare = new Square(SHAPE_GAMEOVER);
		
		// The BG1 block
		theBG1Square = new Square(SHAPE_BG1);
		
		// The BG1 block
		theBG2Square = new Square(SHAPE_BG2);
		
		// Set flag that the squares have been created
		isCreated = true;
	
	}

	public static void prerender() {
		
		// Return from the method if already prerendered
		if (isPrerendered)
			return;
	
		// The colour of the small highlight on the non-ghosted blocks
		Color reflectionColor = new Color(255, 255, 255, 127);
		
		// Arrays to store the colors
		Color[][] edgeColor = new Color[10][3];
		Color[][] fillColor = new Color[10][3];
		
		// Level 0 colours
		edgeColor[0][SHAPE_TOI] = new Color(50, 50, 255);
		edgeColor[0][SHAPE_JS] = new Color(50, 50, 140);
		edgeColor[0][SHAPE_ZL] = new Color(50, 50, 255);
		fillColor[0][SHAPE_TOI] = new Color(255, 255, 255);
		fillColor[0][SHAPE_JS] = new Color(50, 50, 255);
		fillColor[0][SHAPE_ZL] = new Color(50, 140, 167);
		
		// Level 1 colours
		edgeColor[1][SHAPE_TOI] = new Color(0, 255, 0);
		edgeColor[1][SHAPE_JS] = new Color(0, 127, 0);
		edgeColor[1][SHAPE_ZL] = new Color(0, 255, 0);
		fillColor[1][SHAPE_TOI] = new Color(240, 255, 255);
		fillColor[1][SHAPE_JS] = new Color(0, 255, 0);
		fillColor[1][SHAPE_ZL] = new Color(0, 255, 127);
		
		// Level 2 colours
		edgeColor[2][SHAPE_TOI] = new Color(255, 127, 127);
		edgeColor[2][SHAPE_JS] = new Color(200, 100, 100);
		edgeColor[2][SHAPE_ZL] = new Color(255, 127, 127);
		fillColor[2][SHAPE_TOI] = new Color(255, 255, 255);
		fillColor[2][SHAPE_JS] = new Color(255, 127, 127);
		fillColor[2][SHAPE_ZL] = new Color(255, 200, 200);
		
		// Level 3 colours
		edgeColor[3][SHAPE_TOI] = new Color(0, 0, 255);
		edgeColor[3][SHAPE_JS] = new Color(50, 50, 140);
		edgeColor[3][SHAPE_ZL] = new Color(0, 255, 0);
		fillColor[3][SHAPE_TOI] = new Color(255, 255, 255);
		fillColor[3][SHAPE_JS] = new Color(50, 50, 255);
		fillColor[3][SHAPE_ZL] = new Color(0, 255, 127);
		
		// Level 4 colours
		edgeColor[4][SHAPE_TOI] = new Color(255, 50, 100);
		edgeColor[4][SHAPE_JS] = new Color(255, 0, 50);
		edgeColor[4][SHAPE_ZL] = new Color(0, 255, 255);
		fillColor[4][SHAPE_TOI] = new Color(255, 255, 255);
		fillColor[4][SHAPE_JS] = new Color(255, 50, 100);
		fillColor[4][SHAPE_ZL] = new Color(0, 200, 200);
		
		// Level 5 colours
		edgeColor[5][SHAPE_TOI] = new Color(0, 200, 200);
		edgeColor[5][SHAPE_JS] = new Color(0, 255, 255);
		edgeColor[5][SHAPE_ZL] = new Color(75, 75, 220);
		fillColor[5][SHAPE_TOI] = new Color(255, 255, 255);
		fillColor[5][SHAPE_JS] = new Color(0, 200, 200);
		fillColor[5][SHAPE_ZL] = new Color(75, 100, 200);
		
		// Level 6 colours
		edgeColor[6][SHAPE_TOI] = new Color(255, 50, 50);
		edgeColor[6][SHAPE_JS] = new Color(255, 0, 0);
		edgeColor[6][SHAPE_ZL] = new Color(125, 125, 125);
		fillColor[6][SHAPE_TOI] = new Color(255, 255, 255);
		fillColor[6][SHAPE_JS] = new Color(255, 50, 50);
		fillColor[6][SHAPE_ZL] = new Color(200, 200, 200);
		
		// Level 7 colours
		edgeColor[7][SHAPE_TOI] = new Color(140, 40, 140);
		edgeColor[7][SHAPE_JS] = new Color(140, 40, 140);
		edgeColor[7][SHAPE_ZL] = new Color(127, 127, 127);
		fillColor[7][SHAPE_TOI] = new Color(255, 255, 255);
		fillColor[7][SHAPE_JS] = new Color(250, 40, 250);
		fillColor[7][SHAPE_ZL] = new Color(200, 200, 200);
		
		// Level 8 colours
		edgeColor[8][SHAPE_TOI] = new Color(0, 0, 255);
		edgeColor[8][SHAPE_JS] = new Color(0, 0, 127);
		edgeColor[8][SHAPE_ZL] = new Color(255, 0, 0);
		fillColor[8][SHAPE_TOI] = new Color(255, 255, 255);
		fillColor[8][SHAPE_JS] = new Color(0, 0, 255);
		fillColor[8][SHAPE_ZL] = new Color(255, 50, 50);
		
		// Level 9 and above colours
		edgeColor[9][SHAPE_TOI] = new Color(255, 50, 50);
		edgeColor[9][SHAPE_JS] = new Color(255, 0, 0);
		edgeColor[9][SHAPE_ZL] = new Color(220, 105, 75);
		fillColor[9][SHAPE_TOI] = new Color(255, 255, 255);
		fillColor[9][SHAPE_JS] = new Color(255, 50, 50);
		fillColor[9][SHAPE_ZL] = new Color(255, 140, 90);
		
		// Construct the arrays to store the prerendered images
		theBlockImages = new Image[10][3];
		theGhostImages = new Image[10][3];
		
		// For each level
		for (int level = 0; level < 10; level++)
			// For each block variation
			for (int shape = 0; shape < 3; shape++) {
			
				// Get the block colors
				Color currentBlockEdgeColor = edgeColor[level][shape];
				Color currentBlockFillColor = fillColor[level][shape];
				
				// Create ghost colours with half intensity of the block colours
				Color currentGhostEdgeColor
						= new Color(currentBlockEdgeColor.getRed() / 2,
						            currentBlockEdgeColor.getGreen() / 2,
						            currentBlockEdgeColor.getBlue() / 2);
				Color currentGhostFillColor
						= new Color(currentBlockFillColor.getRed() / 2,
						            currentBlockFillColor.getGreen() / 2,
						            currentBlockFillColor.getBlue() / 2);
			
				// The block and ghost images
				BufferedImage currentBlockImage =
						new BufferedImage(16, 16, BufferedImage.TYPE_INT_RGB);
				BufferedImage currentGhostImage =
						new BufferedImage(16, 16, BufferedImage.TYPE_INT_RGB);
				
				// Get the graphics
				Graphics blockGraphics = currentBlockImage.getGraphics();
				Graphics ghostGraphics = currentGhostImage.getGraphics();
				
				// Draw the fills
				blockGraphics.setColor(currentBlockFillColor);
				blockGraphics.fillRect(0, 0, 15, 15);
				ghostGraphics.setColor(currentGhostFillColor);
				ghostGraphics.fillRect(0, 0, 15, 15);
		
				// Draw the edges
				blockGraphics.setColor(currentBlockEdgeColor);
				blockGraphics.draw3DRect(0, 0, 15, 15, true);
				blockGraphics.draw3DRect(1, 1, 13, 13, true);
				ghostGraphics.setColor(currentGhostEdgeColor);
				ghostGraphics.draw3DRect(0, 0, 15, 15, false);
				ghostGraphics.draw3DRect(1, 1, 13, 13, false);
	
				// Draw a Reflection of light on the block
				blockGraphics.setColor(reflectionColor);
				blockGraphics.drawLine(3, 1, 1, 3);
				
				// Dispose of graphics
				blockGraphics.dispose();
				ghostGraphics.dispose();
				
				// Add the images into the arrays
				theBlockImages[level][shape] = currentBlockImage;
				theGhostImages[level][shape] = currentGhostImage;
			
			}
			
			// Render the black block for erasing lines
			theBlackBlockImage =
					new BufferedImage(16, 16, BufferedImage.TYPE_INT_RGB);
			Graphics g = theBlackBlockImage.getGraphics();
			g.setColor(new Color(40, 40, 40));
			g.fillRect(0, 0, 16, 16);
			g.draw3DRect(0, 0, 15, 15, true);
			g.draw3DRect(1, 1, 13, 13, false);
			g.dispose();

			// Render the gameover block for the end
			theGameoverBlockImage =
					new BufferedImage(16, 16, BufferedImage.TYPE_INT_RGB);
			g = theGameoverBlockImage.getGraphics();
			g.setColor(Color.darkGray);
			g.fillRect(0, 0, 16, 16);
			g.draw3DRect(0, 0, 15, 15, true);
			g.draw3DRect(1, 1, 13, 13, true);
			g.dispose();

			// Render the backgroud blocks
			theBG1BlockImage =
					new BufferedImage(16, 16, BufferedImage.TYPE_INT_RGB);
			g = theBG1BlockImage.getGraphics();
			g.setColor(new Color(200, 200, 255));
			g.fillRect(0, 0, 16, 16);
			g.draw3DRect(0, 0, 15, 15, true);
			g.draw3DRect(1, 1, 13, 13, true);
			g.dispose();
			theBG2BlockImage =
					new BufferedImage(16, 16, BufferedImage.TYPE_INT_RGB);
			g = theBG2BlockImage.getGraphics();
			g.setColor(new Color(240, 235, 245));
			g.fillRect(0, 0, 16, 16);
			g.draw3DRect(0, 0, 15, 15, true);
			g.draw3DRect(1, 1, 13, 13, true);
			g.dispose();
		
		// Set flag to show the squares have been prerendered
		isPrerendered = true;
		
	}
	
	public void renderOn
			(Graphics g, int column, int row, int level, boolean ghost) {
		
		// Prerender if not already done so
		if (!isPrerendered)
			prerender();
	
		// Calculate the position
		int x = column * 16;
		int y = row * 16;
		
		// Get the shape
		int shape = getShape();
		
		// Get the image
		Image image = getImage(level, shape, ghost);
	
		// Draw the image
		g.drawImage(image, x, y, null);
	
	}

}
