package com.leechristie.fallingblock;

import java.awt.*;
import java.awt.image.*;
import java.applet.*;
import java.awt.event.*;

public class FallingBlock2 extends Applet implements KeyListener {
    
    private Image theFrame;
    private Game game;
    private boolean leftPressed;
    private boolean rightPressed;
    private boolean buttonZPressed;
    private boolean buttonXPressed;
    private boolean buttonSpacePressed;
    private boolean buttonDownPressed;
    //private AudioClip music, land, line, end;
    
    public void startMusic() {
        
        //System.out.println("LOOP MUSIC");
        //music.loop();
        
    }
    
    public void stopMusic() {
        
        //music.stop();
        
    }
    
    public void landSound() {
        
        //land.play();
        
    }
    
    public void lineSoundOver() {
        
        //line.stop();
        
    }
    
    public void lineSound() {
        
        //line.play();
        
    }
    
    public void endSoundOver() {
        
        //end.stop();
        
    }
    
    public void endSound() {
        
        //end.play();
        
    }
    
    private Image getFrame() {
        
        return theFrame;
        
    }
    
    private void setFrame(Image frame) {
        
        theFrame = frame;
        
    }
    
    public String getAppletInfo() {
        
        return "Falling Block " + Game.VERSION + "\n"
                + "Copyright 2004-2006, Lee Chrisite\n"
                + "http://www.leechristie.com/";
        
    }
    
    public void init() {
        
        //music = getAudioClip(new URL("/com/leechristie/fallingblock/tune.mid"));
        //land = getAudioClip(this.getDocumentBase(), "land.wav");
        //line = getAudioClip(this.getDocumentBase(), "line.wav");
        //end = getAudioClip(this.getDocumentBase(), "end.wav");
        
        // Create the blank image frame
        setFrame(new BufferedImage(
                288, 352, BufferedImage.TYPE_INT_RGB));
        
        // Create the first frame
        newFrame();
        
        // Set background colour of the applet to black
        setBackground(Color.black);
        
        // Buffer the 30 different variations of squares
        Square.prerender();
        
        // Create the 3 different static squares
        Square.createSquares();
        
        // Create the game
        game = new Game(this);
        
        // Listen for keys
        addKeyListener(this);
        
    }
    
    public void start() {
        
        game.start();
        
    }
    
    public void stop() {
        
        stopMusic();
        game.stop();
        
    }
    
    public void replaceGame() {
        
        // Lock current game
        game.isLocked = true;
        
        // Stop the game
        game.stop();
        
        // Remove the reference
        game = null;
        
        // Create the blank image frame
        setFrame(new BufferedImage(288, 352, BufferedImage.TYPE_INT_RGB));
        
        // Create the first frame
        newFrame();
        
        // Create the game
        game = new Game(this);
        
        // Start the game
        game.start();
        
        // Unpause the start frame
        game.isStarted = true;
        startMusic();
        
    }
    
    public void paint(Graphics g) {
        
        // Get the frame
        Image frame = getFrame();
        
        // Draw the frame
        g.drawImage(frame, 0, 0, null);
        
    }
    
    // Paints the current graphics on update, prevents flicker
    public void update(Graphics g) {
        
        paint(g);
        
    }
    
    public void newFrame() {
        
        if (game == null)
            return;
        
        // Get the frame
        Image frame = getFrame();
        
        // Get the graphics
        Graphics g = frame.getGraphics();
        
        // Draw the game
        game.renderOn(g);
        
        // Repaint the frame
        repaint();
        
    }
    
    public void keyTyped(KeyEvent e) {
        
    }
    
    public void keyReleased(KeyEvent e) {
        
        // Get axis position
        int axisBefore = getAxis();
        
        if (e.getKeyCode() == KeyEvent.VK_LEFT)
            leftPressed = false;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT)
            rightPressed = false;
        
        // Get axis position
        int axisAfter = getAxis();
        
        if (axisBefore != axisAfter) {
            if (axisAfter < 0)
                game.axisLeft();
            else if (axisAfter > 0)
                game.axisRight();
            else
                game.axisCenter();
        }
        
        if (e.getKeyCode() == KeyEvent.VK_Z)
            buttonZPressed = false;
        
        if (e.getKeyCode() == KeyEvent.VK_X)
            buttonXPressed = false;
        
        if (e.getKeyCode() == KeyEvent.VK_SPACE)
            buttonSpacePressed = false;
        
        if (e.getKeyCode() == KeyEvent.VK_DOWN)
            if (buttonDownPressed) {
            buttonDownPressed = false;
            game.downUp();
            }
        
    }
    
    public void keyPressed(KeyEvent e) {
        
        // Get axis position
        int axisBefore = getAxis();
        
        if (e.getKeyCode() == KeyEvent.VK_LEFT)
            leftPressed = true;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT)
            rightPressed = true;
        
        // Get axis position
        int axisAfter = getAxis();
        
        if (axisBefore != axisAfter) {
            if (axisAfter < 0)
                game.axisLeft();
            else if (axisAfter > 0)
                game.axisRight();
            else
                game.axisCenter();
        }
        
        if (e.getKeyCode() == KeyEvent.VK_Z)
            if (!buttonZPressed) {
            buttonZPressed = true;
            game.buttonZPressed();
            }
        
        if (e.getKeyCode() == KeyEvent.VK_X)
            if (!buttonXPressed) {
            buttonXPressed = true;
            game.buttonXPressed();
            }
        
        if (e.getKeyCode() == KeyEvent.VK_SPACE)
            if (!buttonSpacePressed) {
            buttonSpacePressed = true;
            game.buttonSpacePressed();
            }
        
        if (e.getKeyCode() == KeyEvent.VK_DOWN)
            if (!buttonDownPressed) {
            buttonDownPressed = true;
            game.downDown();
            }
        
    }
    
    private int getAxis() {
        
        if (leftPressed && !rightPressed)
            return -1;
        
        if (!leftPressed && rightPressed)
            return 1;
        
        return 0;
        
    }
    
}
