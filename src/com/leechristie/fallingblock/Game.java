package com.leechristie.fallingblock;

import java.awt.*;
import java.awt.image.*;

public class Game {
    
    public static final String VERSION = "2.2.0";
    
    private static final int TRANSITION_UP = 0;
    private static final int TRANSITION_DOWN = 1;
    private static final int TRANSITION_LEFT = 2;
    private static final int TRANSITION_RIGHT = 3;
    private static final int TRANSITION_CW = 4;
    private static final int TRANSITION_CCW = 5;
    
    public FallingBlock2 applet;
    
    public boolean isAnimating;
    public boolean isGameOver;
    public boolean isStarted;
    public boolean isPaused;
    public boolean isLocked;
    
    private static boolean borderPrerendered;
    private static Image theBorderFrame;
    private Image theGameAreaFrame;
    
    private XEngine xEngine;
    private YEngine yEngine;
    private YPushEngine yPushEngine;
    
    private Block theCurrent;
    private Block theNext;
    private SquarePile thePile;
    
    private int theLines;
    private int theScore;
    
    public int getLevel() {
        
        return getLines() / 10;
        
    }
    
    public int getLines() {
        
        return theLines;
        
    }
    
    public void setLines(int lines) {
        
        theLines = lines;
        
    }
    
    public void giveLines(int lines) {
        
        setLines(getLines() + lines);
        
    }
    
    private Image getGameAreaFrame() {
        
        return theGameAreaFrame;
        
    }
    
    private void setGameAreaFrame(Image frame) {
        
        theGameAreaFrame = frame;
        
    }
    
    private Image getBorderFrame() {
        
        return theBorderFrame;
        
    }
    
    private void setBorderFrame(Image frame) {
        
        theBorderFrame = frame;
        
    }
    
    public Block getCurrent() {
        
        return theCurrent;
        
    }
    
    public void setCurrent(Block current) {
        
        theCurrent = current;
        
    }
    
    public Block getNext() {
        
        return theNext;
        
    }
    
    public void setNext(Block next) {
        
        theNext = next;
        
    }
    
    public SquarePile getPile() {
        
        return thePile;
        
    }
    
    private void setPile(SquarePile pile) {
        
        thePile = pile;
        
    }
    
    public int getScore() {
        
        return theScore;
        
    }
    
    public void setScore(int score) {
        
        theScore = score;
        
    }
    
    public Game(FallingBlock2 applet) {
        
        // Prerender the background
        preRenderBackground();
        
        // Set the applet
        this.applet = applet;
        
        // New square pile
        setPile(new SquarePile());
        
        // New x engine
        xEngine = new XEngine(this);
        
        // New y engine
        yEngine = new YEngine(this);
        
        // New y push engine
        yPushEngine = new YPushEngine(this);
        
        // Create the blank image frame
        setGameAreaFrame(new BufferedImage(160, 320, BufferedImage.TYPE_INT_RGB));
        
        // Create the first frame
        newFrame();
        
    }
    
    public void start() {
        
        // Create engine threads
        yEngine.thread = new Thread(yEngine);
        
        // Start engines
        yEngine.thread.start();
        
    }
    
    public void stop() {
        
        // Destory engine and threads
        xEngine.thread = null;
        yEngine.thread = null;
        yPushEngine.thread = null;
        xEngine = null;
        yEngine = null;
        yPushEngine = null;
        
    }
    
    public void downPressed() {
        
        
        
    }
    
    public void axisLeft() {
        
        xEngine.axis = -1;
        if (xEngine.thread != null) return;
        xEngine.thread = new Thread(xEngine);
        xEngine.thread.start();
        
    }
    
    public void axisCenter() {
        
        xEngine.axis = 0;
        xEngine.thread = null;
        
    }
    
    public void axisRight() {
        
        xEngine.axis = 1;
        if (xEngine.thread != null) return;
        xEngine.thread = new Thread(xEngine);
        xEngine.thread.start();
        
    }
    
    public void downDown() {
        
        yPushEngine.pushed = true;
        if (yPushEngine.thread != null) return;
        yPushEngine.thread = new Thread(yPushEngine);
        yPushEngine.thread.start();
        
    }
    
    public void downUp() {
        
        yPushEngine.pushed = false;
        yPushEngine.thread = null;
        
    }
    
    public void buttonZPressed() {
        
        // Return if no current
        if (getCurrent() == null)
            return;
        
        // Return if locked or paused or animating
        if (isLocked || isPaused || isAnimating)
            return;
        
        // Lock
        isLocked = true;
        
        // Get current
        Block current = getCurrent();
        
        // Move if you can
        current.checkThenRotate(getPile(), TRANSITION_CW);
        
        // Render a new frame
        newFrame();
        
        // Unlock
        isLocked = false;
        
    }
    
    public void buttonXPressed() {
        
        // Return if no current
        if (getCurrent() == null)
            return;
        
        // Return if locked or paused or animating
        if (isLocked || isPaused || isAnimating)
            return;
        
        // Lock
        isLocked = true;
        
        // Get current
        Block current = getCurrent();
        
        // Move if you can
        current.checkThenRotate(getPile(), TRANSITION_CCW);
        
        // Render a new frame
        newFrame();
        
        // Unlock
        isLocked = false;
        
    }
    
    public void buttonCheatPressed() {
        
        
        
    }
    
    public void buttonSpacePressed() {
        
        //System.out.println("Space");
        
        if (!isStarted) {
            isStarted = true;
            //System.out.println("Started ON");
            applet.startMusic();
        } else if (isGameOver) {
            applet.replaceGame();
        } else if (!isLocked && !isAnimating) { // Running but not locked and not animating
            
            // (un)Pause
            isPaused = !isPaused;
            
            // Render a new frame
            newFrame();
            
        }
        
    }
    
    public int ySleepTime() {
        
        // Get the level
        int level = getLevel();
        
        // Return the interval
        if (level == 0)
            return 800;
        else if (level == 1)
            return 718;
        else if (level == 2)
            return 636;
        else if (level == 3)
            return 554;
        else if (level == 4)
            return 472;
        else if (level == 5)
            return 390;
        else if (level == 6)
            return 308;
        else if (level == 7)
            return 226;
        else if (level == 8)
            return 144;
        else
            return 85; // 75, 62
        
    }
    
    public void newFrame() {
        
        // Render the game area
        newGameAreaFrame();
        
    }
    
    private void preRenderBackground() {
        
        // Chekc flag
        if (borderPrerendered)
            return;
        
        // Create buffer
        Image buffer = new BufferedImage(288, 352, BufferedImage.TYPE_INT_RGB);
        
        // Get graphics
        Graphics g = buffer.getGraphics();
        
        // Get squares to draw with
        Square[] squares = {Square.bG1(), Square.bG2()};
        
        for (int y = 0; y < 22; y++)
            for (int x = 0; x < 18; x++)
                squares[(x+y)%2].renderOn(g, x, y, 0, false);
        
        g.setFont(new Font("Serif", Font.BOLD + Font.ITALIC, 30));
        
        g.setColor(Color.white);
        g.drawString("Falling", 179, 39);
        g.drawString("Falling", 179, 41);
        g.drawString("Falling", 181, 39);
        g.drawString("Falling", 181, 41);
        g.drawString("Block", 209, 54);
        g.drawString("Block", 209, 56);
        g.drawString("Block", 211, 54);
        g.drawString("Block", 211, 56);
        
        g.setColor(Color.red);
        g.drawString("Falling", 180, 40);
        g.setColor(Color.blue);
        g.drawString("Block", 210, 55);
        
        g.setFont(new Font("SansSerif", Font.PLAIN, 10));
        g.setColor(Color.white);
        g.drawString(VERSION, 186, 53);
        g.drawString(VERSION, 186, 55);
        g.drawString(VERSION, 188, 53);
        g.drawString(VERSION, 188, 55);
        g.setColor(Color.black);
        g.drawString(VERSION, 187, 54);
        
        // Square to draw status boxes with
        Square square = Square.black();
        for (int y = 15; y < 21; y++)
            for (int x = 12; x < 17; x++)
                square.renderOn(g, x, y, 0, false);
        
        g.setFont(new Font("SansSerif", Font.PLAIN, 11));
        g.setColor(Color.white);
        g.drawString("© 2004", 197, 310);
        g.drawString("Lee Christie", 197, 320);
        g.drawString("leechristie.com", 197, 330);
        
        g.setFont(new Font("SansSerif", Font.PLAIN, 10));
        g.drawString("Arrows: Move", 198, 255);
        g.drawString("Z/X: Rotate", 198, 266);
        g.drawString("Down: Drop", 198, 277);
        g.drawString("Space: Pause", 198, 288);
        
        for (int y = 4; y < 14; y++)
            for (int x = 12; x < 17; x++)
                square.renderOn(g, x, y, 0, false);
        
        g.setFont(new Font("SansSerif", Font.PLAIN, 11));
        g.setColor(Color.white);
        g.drawString("Level", 198, 6*16-5);
        g.drawString("Score", 198, 9*16-5);
        g.drawString("Lines", 198, 12*16-5);
        
        // Copy Buffer
        setBorderFrame(buffer);
        
        // Set flag
        borderPrerendered = true;
        
    }
    
    private void newGameAreaFrame() {
        
        // Get the level
        int level = getLevel();
        
        // Get the square pile
        SquarePile pile = getPile();
        
        // Get the blocks
        Block current = getCurrent();
        Block next = getNext();
        
        // Create buffer
        Image buffer = new BufferedImage(160, 320, BufferedImage.TYPE_INT_RGB);
        
        // Create graphics
        Graphics gBuffer = buffer.getGraphics();
        
        // Draw the square pile
        pile.renderOn(gBuffer);
        
        // Draw the next block as a ghost
        if (next != null)
            next.renderOn(gBuffer, level, true);
        
        // Draw the current block
        if (current != null)
            current.renderOn(gBuffer, level, false);
        
        if (!isStarted) {
            
            gBuffer.setFont(new Font("SansSerif", Font.PLAIN, 10));
            gBuffer.setColor(new Color(0, 0, 0, 127));
            gBuffer.drawString("Press Space To Play", 34, 190);
            gBuffer.setColor(Color.white);
            gBuffer.drawString("Press Space To Play", 30, 186);
            
        }
        
        if (isPaused) {
            
            SquarePile.renderBlank(gBuffer);
            
            gBuffer.setFont(new Font("SansSerif", Font.BOLD, 22));
            gBuffer.setColor(new Color(0, 0, 0, 127));
            gBuffer.drawString("Paused", 44, 104);
            gBuffer.setColor(Color.white);
            gBuffer.drawString("Paused", 40, 100);
            
        }
        
        if (isGameOver) {
            
            gBuffer.setFont(new Font("SansSerif", Font.BOLD, 22));
            gBuffer.setColor(new Color(0, 0, 0, 127));
            gBuffer.drawString("Game Over", 26, 104);
            gBuffer.setColor(Color.white);
            gBuffer.drawString("Game Over", 22, 100);
            
            gBuffer.setFont(new Font("SansSerif", Font.PLAIN, 10));
            gBuffer.setColor(new Color(0, 0, 0, 127));
            gBuffer.drawString("Press Space To Play", 34, 190);
            gBuffer.setColor(Color.white);
            gBuffer.drawString("Press Space To Play", 30, 186);
            
        }
        
        // Dispose of the buffer graphics
        gBuffer.dispose();
        
        // Get the frame
        Image frame = getGameAreaFrame();
        
        // Get the graphics
        Graphics gFrame = frame.getGraphics();
        
        // Draw the buffer
        gFrame.drawImage(buffer, 0, 0, null);
        
        // Call newFrame on the applet
        applet.newFrame();
        
    }
    
    public void renderOn(Graphics g) {
        
        // Get the border frame
        Image borderFrame = getBorderFrame();
        
        // Get the game area frame
        Image gameAreaFrame = getGameAreaFrame();
        
        // Draw the border frame
        g.drawImage(borderFrame, 0, 0, null);
        
        // Draw the game area frame
        g.drawImage(gameAreaFrame, 16, 16, null);
        
        // Draw the status
        g.setFont(new Font("SansSerif", Font.PLAIN, 13));
        g.setColor(Color.white);
        g.drawString(""+getLevel(), 202, 7*16-3);
        g.drawString(""+getScore(), 202, 10*16-3);
        g.drawString(""+getLines(), 202, 13*16-3);
        
    }
    
}
