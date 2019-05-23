package com.leechristie.fallingblock;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;

public class Main {
    
    public static void main(String[] args) {
        
        FallingBlock2 applet = new FallingBlock2();
        
        applet.init();
        
        applet.setPreferredSize(new Dimension(288, 352));
        
        Frame frame = new Frame();
        frame.setLayout(new BorderLayout());
        
        frame.add(BorderLayout.CENTER, applet);
        frame.pack();
        
        frame.setVisible(true);
        
        applet.start();
        
    }
    
}
