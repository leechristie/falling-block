package com.leechristie.fallingblock;

import java.awt.*;       // To use AWT Graphics for rendering
import java.awt.image.*; // To use the BufferedImage class

/**
 * In the FallingBlock game, all the squares on the screen - except those which
 * have not yet landed (the falling block) - make up the 'square pile'. The
 * SquarePile class stores those squares and can be used to add squares (i.e.
 * when a block lands), detect and erase rows and render the square pile.
 */
public class SquarePile {
    /**
     * The level at which the pile was last rendered.
     */    
    private int thePileLevel;
    
    /**
     * The Image reflecting the current state of the pile - ready to be drawn to
     * the screen.
     */
    private Image theFrame;
    
    /**
     * The array that makes up the squares in the pile.
     */
    private Square[][] theSquares;
    
    /**
     * Returns the level (game stage) the pile was last rendered,
     * this is used to check if the entire pile needs to be re-rendered
     * to reflect the new colour scheme when a line is made.
     *
     * @return the level the pile was last rendered
     */
    public int getPileLevel()
    {   
        return thePileLevel;
    }
	
    /**
     * Sets the level (game stage) the pile was last rendered.
     *
     * @param level the level the pile was last rendered.
     */
    public void setPileLevel(int level)
    {
        thePileLevel = level;
    }
	
    /**
     * Returns an Image which contains the most up-to-date rendering of the
     * square pile.
     *
     * @return the Image reflecting the current state of the pile
     */
    public Image getFrame()
    {
        return theFrame;
    }

    /**
     * Sets the Image containing the most up-to-date rendering of the square
     * pile.
     *
     * @param frame the Image reflecting the current state of the pile
     */
    public void setFrame(Image frame)
    {
        theFrame = frame;
    }
	
    /**
     * Returns a 2-dimentional array of Squares, making up the square pile.
     * 
     * @return the squares making up the pile
     */
    public Square[][] getSquares()
    {
        return theSquares;
    }
	
    /**
     * Replaces the squares in the pile with a new 2-dimentioanl array of
     * Squares.
     *
     * @param squares the new array of Squares
     */
    public void setSquares(Square[][] squares)
    {
        theSquares = squares;
    }
	
    /**
     * Returns the square at the specified x, y co-ordinates.
     *
     * @param x the x co-ordinate of the square to return
     * @param y the y co-ordinate of the square to return
     * @return the requested square
     */    
    public Square getAt(int x, int y)
    {
        // Get the square from the array
        return getSquares()[x][y];
    }
    
    /**
     * Places the given square at the specified location on the square pile. 
     * The pile is re-rendered entirely if necisary to match the color scheme
     * of the existing squares to the new square on level-up.
     *
     * @param x the x co-ordinate at which to place the square
     * @param y the y co-ordinate at which to place the square
     * @param square the square to add to the pile
     * @param newLevel the level (game stage) after placing the square
     */
    public void setAt(int x, int y, Square square, int newLevel)
    {
        try
        {
            // Set the square
            getSquares()[x][y] = square;
	
            // Get the level
            int level = getPileLevel();
	
            // Get frame
            Image frame = getFrame();
		
            // Get frame graphics
            Graphics gFrame = frame.getGraphics();
	
            // Test to see if the level remains unchanged
            if (level == newLevel)
                // Render only the new square
                square.renderOn(gFrame, x, y, level, false);
            else
                // Re-render the pile, so that the entire pile, including
                //   squares which were already been placed now show the color
                //   scheme of the new level.
                newFrame(newLevel);	
        }
        catch (Exception e)
        {
        }
    }
    
    /**
     * Places the given square at the specified location on the square pile. 
     * The pile is not re-rendered, only the new square is rendered on the pile.
     *
     * @param x the x co-ordinate at which to place the square
     * @param y the y co-ordinate at which to place the square
     * @param square the square to add to the pile
     */
    public void setAt(int x, int y, Square square)
    {	
        try
        {
            // Set the square
            getSquares()[x][y] = square;
	
            // Get the level
            int level = getPileLevel();
	
            // Get frame
            Image frame = getFrame();
		
            // Get frame graphics
            Graphics gFrame = frame.getGraphics();
			
            // Render the square to the frame
            square.renderOn(gFrame, x, y, level, false);	
        }
        catch (Exception e)
        {
        }
    }
    
    /**
     * Class constructor.
     */
    public SquarePile()
    {		
        // Initialise the array of squares
        setSquares(new Square[10][20]);
		
        // Fill with gameover blocks, these represent the solid, filled-in
        //   look of the screen while there is no game in progress
        for (int x = 0; x < 10; x++)
            for (int y = 0; y < 20; y++)
                setAt(x, y, Square.gameover());
		
        // Create the blank image frame
        setFrame(new BufferedImage(160, 320, BufferedImage.TYPE_INT_RGB));
		
        // Create the first frame
        newFrame(0);
    }

    /**
     * Determains whether it is alright to add a square to the pile at the
     * specified location.
     *
     * @param x the x co-ordinate of the location to test
     * @param y the y co-ordinate of the location to test
     *
     * @return true if it is okay to add a square at this location, false if it
     * is not okay to add a square to this location
     */
    public boolean emptyAt(int x, int y)
    {
        /* Test to see if the specified co-ordinate is off the edges */
        
        if (y >= 20)
            return false; // It is NOT okay for a square to exist below the
                          //   bottom of the area.
        
        if (y < 0)
            return true;  // It is always okay for a square to exist above the
                          //   top of the area.
        
        if (x >= 10)
            return false; // It is NOT okay for a square to exist right of the
                          //   right edge of the area.
        
        if (x < 0)
            return false; // It is NOT okay for a square to exist left of the
                          //   left edge of the area.
		
        // Return whether the square the is null or equal to Square.black(), if
        //   it is, then this space is free for a square to be inserted (return
        //   value true), if not the square is occupied (return value false).
        return (getAt(x, y) == Square.black() || getAt(x, y) == null);
    }

    /**
     * Updates the stored frame to visually reflect the new state of the square
     *   pile.
     *
     * @param level the current level
     */
    public void newFrame(int level)
    {
        // Set the level at which the pile was last rendered to the specified
        //   level
        setPileLevel(level);
	
        // Get the frame
        Image frame = getFrame();
		
        // Get graphics associated with the frame
        Graphics gFrame = frame.getGraphics();
	
        // Create a new buffer image
        Image buffer = new BufferedImage(160, 320, BufferedImage.TYPE_INT_RGB);
		
        // Get graphics associated with the buffer
        Graphics gBuffer = buffer.getGraphics();
		
        // Draw the last frame to the buffer
        gBuffer.drawImage(frame, 0, 0, null);
	
        // Draw each square to the buffer
        for (int x = 0; x < 10; x++)
            for (int y = 0; y < 20; y++)
                if (emptyAt(x, y))
                    Square.black().renderOn(gBuffer, x, y, 0, false);
                else
                    getAt(x, y).renderOn(gBuffer, x, y, level, false);
	
        // Draw the buffer back to the frame
        gFrame.drawImage(buffer, 0, 0, null);
    }

    /**
     * Renders the square pile on the Graphics object passed.
     *
     * @param g the Graphics object on which to render the square pile
     */
    public void renderOn(Graphics g)
    {
        // Get the current frame and draw it on the given Graphics object
        g.drawImage(getFrame(), 0, 0, null);
    }

    /**
     * Renders a blank square pile on the Graphics object passed. This can be
     * used to obtain a blank frame for displaying a pause screen.
     *
     * @param g the Graphics object on which to render a blank square pile
     */
    public static void renderBlank(Graphics g)
    {
        // Loop through each location on the area
        for (int x = 0; x < 10; x++)
            for (int y = 0; y < 20; y++)
                // Render the black (empty space) square at this location
                Square.black().renderOn(g, x, y, 0, false);
    }
	
	// . Animate the erasing of lines (using thread to sleep) <done>
	// . Clear the lines from the pile <done>
	// . Adjust the pile <done>
	// . Inc the number of lines and score in the game <done>
	// . Adjust the level on the pile <done>
	// . Completely rerender the pile if needed <done>
	// . Redraw the game <done>
    /**
     * Animates the erasing of any full rows which have been made while removing
     * them and adjusting the pile. The number of lines, level and score on the
     * passed Game is adjusted and the game re-rendered. The sound effect for
     * removal of lines is played using the Game object passed.
     *
     * @param game the Game to update
     *
     * @return true if lines were cleared, false if no lines were cleared
     */
    public boolean clearLines(Game game)
    {
        // The row-numbers of each row which needs to be cleared
        int[] linesToClear = new int[4];
        
        // The count of rows which need to be cleared
        int lineCount = 0;
        
        // The points to multiply the lines by to calculate the new score
        int[] pointBase = {40, 100, 300, 1200};
        
        // The score after the lines have been made
        int newGameScore;
        
        // The number of lines after these new lines have been made
        int newGameLines;

        // Loop through the rows until the last row is reached
        for (int i = 0; i < 20; i++)
        {
            // If the line count is less than 4, check if the current row is
            //   full (is a complete row) to be removed
            if (lineCount < 4 && lineIsFull(i))
            {
                // Add the row-number to the array
                linesToClear[lineCount] = i;
                
                // Increase the count of rows
                lineCount++;
            }
        }

        // If there is nothing to clear (no lines were made)
        if (lineCount == 0)
            // The function returns false to indicate no action was taken
            return false;

        // Calculate new number of lines
        newGameLines = game.getLines() + lineCount;

        // Calculate new score using the formulae:
        //   1 Line  : 40   x (LEVEL+1)
        //   2 Lines : 100  x (LEVEL+1)
        //   3 Lines : 300  x (LEVEL+1)
        //   4 Lines : 1200 x (LEVEL+1)
        newGameScore = game.getScore()
                + pointBase[lineCount-1] * (game.getLevel() + 1);

        // Set the flag to indicate that there is an animation in progress
        game.isAnimating = true;

        if (lineCount != 4) {

            for (int x = 4; x >= 0; x--) {

                // Clear the column
                for (int i = 0; i < lineCount; i++) {

                    // Get y
                    int y = linesToClear[i];

                    // Clear the square
                    setAt(x, y, Square.black());
                    setAt(9-x, y, Square.black());

                }

                // Render a new frame
                game.newFrame();

                // Line sound
                game.applet.lineSound();

                // Sleep
                try {Thread.sleep(64);}
                catch(InterruptedException e) {}

                // Line sound end
                game.applet.lineSoundOver();

            }

        } else { // The line count is 4

            final int DELAY_TIME = 42;

            int topRow = linesToClear[0];
            int secondRow = linesToClear[1];
            int thirdRow = linesToClear[2];
            int bottomRow = linesToClear[3];
            
            // Clear the top row
            animateHorizontalRightClearingOf(0, 10, topRow, DELAY_TIME, game);
            
            // Clear the 2nd row
            animateHorizontalLeftClearingOf(9, 10, secondRow, DELAY_TIME, game);
            
            // Clear the 3rd row
            animateHorizontalRightClearingOf(0, 10, thirdRow, DELAY_TIME, game);
            
            // Clear the bottom row
            animateHorizontalLeftClearingOf(9, 10, bottomRow, DELAY_TIME, game);

        }

        // Drop the blocks
        for (int i = 0; i < lineCount; i++) {

            // Get y
            int y = linesToClear[i];

            // Drop the blocks above
            dropAbove(y);

        }

        // Render a new frame
        game.newFrame();

        // ANIMATION ENDS HERE	

        // Add the lines and points
        game.setLines(newGameLines);
        game.setScore(newGameScore);

        // Render a new frame
        if (game.getLevel() != getPileLevel())
            newFrame(game.getLevel());
        game.newFrame();

        // Stop animating
        game.isAnimating = false;

        return true;
    }

    private void animateHorizontalRightClearingOf(int initialXValue,
            int theNumberOfSquaresToErase, int y, int delayTime, Game game)
    {
        // Loop from (initialXValue, y), to right of that position 10 squares
        for (int x = initialXValue;
                x < initialXValue + theNumberOfSquaresToErase; x++)
        {
            // Clear that square
            animateClearingOf(x, y, delayTime, game);
        }
    }

    private void animateHorizontalLeftClearingOf(int initialXValue,
            int theNumberOfSquaresToErase, int y, int delayTime, Game game)
    {
        // Loop from (initialXValue, y), to left of that position 10 squares
        for (int x = initialXValue;
                x > initialXValue - theNumberOfSquaresToErase; x--)
        {
            // Clear that square
            animateClearingOf(x, y, delayTime, game);
        }
    }
    
    private void animateClearingOf(int x, int y, int delayTime, Game game)
    {        
        // Clear the square
        setAt(x, y, Square.black());

        // Render a new frame
        game.newFrame();

        // Line sound
        game.applet.lineSound();

        // Sleep
        try {Thread.sleep(delayTime);}
        catch(InterruptedException e) {}

        // Line sound end
        game.applet.lineSoundOver();
    }
    
    /**
     * Checks whether a row is a complete row and ready to be cleared.
     *
     * @param row the row-number of the line to test
     *
     * @return true if the row needs to be cleared, false if it is not yet full
     */
    public boolean lineIsFull(int row)
    {	
        // False if any squares are empty in row
        for (int x = 0; x < 10; x++)
            if (emptyAt(x, row))
                return false;
				
        // True otherwise
        return true;
    }

    /**
     * Causes all the squares above the given row-number to fall down by one
     * row.
     *
     * @param row the row-number of the line above which the squares should be
     * lowered by one row
     */
    public void dropAbove(int row)
    {
        // Loop from row (given row number) to 1 (2nd row from the top)
        for (int y = row; y >= 1; y--)
        {
            // Loop through each of the blocks on this row and cause them to
            //   become the blocks from the above, thereby copying the blocks
            //   down by one row.
            for (int x = 0; x < 10; x++)
                setAt(x, y, getAt(x, y-1));
		
        }
		
        // Clear top row - as it currently contains a copy of the 2nd row
        for (int x = 0; x < 10; x++)
            setAt(x, 0, Square.black());
    }
}