package com.csc563;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.*;
import javax.swing.Timer;
import java.lang.Math;
import java.lang.Thread;
import java.net.Socket;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
// John Barron
// 1-5-12

public class SpaceRaceWindow extends Frame{
    private static final Toolkit toolkit = Toolkit.getDefaultToolkit();
    private static final Dimension screenSize = toolkit.getScreenSize();
    public static final int width = 1366, height = 768;
    public static final int arenaHeight = height;
    public static final int arenaWidth = height;
    public static final int HUDheight = height; //not sure if these should all be public static final
    public static final int HUDwidth = width - height; //Assume width > height
    private static final int HUDy = 0, HUDx = width - HUDwidth;
    //The game arena is now a square of size height, with room on the right of the screen for the heads up display (HUD).
    private int numShips = 1;
    private int numStars = 0;
    private int numPlanets = 0;
    private int numCoins = 10;
    private int focusPlanetIndex;
    private Image dbImage; // For double buffer
    private Graphics dbg;
    private javax.swing.Timer timer, frameTimer;
    public static int timerDelay = 16;
    public static final double speedFactor = 16;
    private int frameTimerDelay = 16;
    private boolean trails = false;
    public static boolean forwards = true;
    private boolean planetFocus = false;
    private boolean bouncyEdges = true;
    private Random rng;

    public SocketClient client;
    public String serverAddr;
    public String playerName;
    public int port; 
    public Thread clientThread;
    public String messageOnScreen = "";
    public String playerStatusString = "";
    public String coinStatusString = "";

    private JFrame frame = new JFrame("NewFinalProject");
    private int i, xPixel, yPixel;
    private int tempSize;
    
    public SpaceRaceWindow() throws IOException {
        super("Astroids");
        setSize(width, height);
        setLocation(0, 0);
        setUndecorated(true);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        
        serverAddr = getServerAddress();
        playerName = getPlayerName();
        port = 4446;
        client = new SocketClient(this);
        clientThread = new Thread(client);
        clientThread.start();
//        serverIP = socket.getInetAddress();

        // Register the window with a keyboard listener
        addKeyListener(new keyListener());
//        addMouseListener(new MousePressListener());
        //addMouseMotionListener(new MouseMoveListener());
        
        ActionListener forceTimer = new ActionListener() {
            public void actionPerformed(ActionEvent e){
                if (bouncyEdges) {
                    //The following for blocks make ships and planets bounce off the edges of the screen.
                    for (i = 0; i < numPlanets; i++) {
                        //s1.getPlanet()[i].getxLoc();
                        //s1.getPlanet()[i].getyLoc();
                        //s1.window.getX();
                        /*
                        if (s1.getPlanet()[i].getxLoc() < s1.window.getX() && s1.getPlanet()[i].getxSpeed() < 0) {
                            s1.getPlanet()[i].setxSpeed(s1.getPlanet()[i].getxSpeed() * -1);
                        }
                        if (s1.getPlanet()[i].getyLoc() < s1.window.getY() && s1.getPlanet()[i].getySpeed() < 0) {
                            s1.getPlanet()[i].setySpeed(s1.getPlanet()[i].getySpeed() * -1);
                        }
                        if (s1.getPlanet()[i].getxLoc() >= s1.window.getX() + s1.window.getWindowWidth() && s1.getPlanet()[i].getxSpeed() > 0) {
                            s1.getPlanet()[i].setxSpeed(s1.getPlanet()[i].getxSpeed() * -1);
                        }
                        if (s1.getPlanet()[i].getyLoc() >= s1.window.getY() + s1.window.getWindowHeight() && s1.getPlanet()[i].getySpeed() > 0) {
                            s1.getPlanet()[i].setySpeed(s1.getPlanet()[i].getySpeed() * -1);
                        }
                        */
                    }
                    for (i = 0; i < numShips; i++) {
                        //s1.getShip()[i].getxLoc();
                        //s1.getShip()[i].getyLoc();
                        //s1.window.getX();
                        /*
                        if (s1.getShip()[i].getxLoc() - s1.getShip()[i].getSize() < s1.window.getX() && s1.getShip()[i].getxSpeed() < 0) {
                            s1.getShip()[i].setxSpeed(s1.getShip()[i].getxSpeed() * -1);
                            //s1.getShip()[i].bounce(0);
                        }
                        if (s1.getShip()[i].getyLoc() - s1.getShip()[i].getSize() < s1.window.getY() && s1.getShip()[i].getySpeed() < 0) {
                            s1.getShip()[i].setySpeed(s1.getShip()[i].getySpeed() * -1);
                            //s1.getShip()[i].bounce(Math.PI/2);
                        }
                        if (s1.getShip()[i].getxLoc() + s1.getShip()[i].getSize() >= s1.window.getX() + s1.window.getWindowWidth() && s1.getShip()[i].getxSpeed() > 0) {
                            s1.getShip()[i].setxSpeed(s1.getShip()[i].getxSpeed() * -1);
                            //s1.getShip()[i].bounce(Math.PI);
                        }
                        if (s1.getShip()[i].getyLoc() + s1.getShip()[i].getSize() >= s1.window.getY() + s1.window.getWindowHeight() && s1.getShip()[i].getySpeed() > 0) {
                            s1.getShip()[i].setySpeed(s1.getShip()[i].getySpeed() * -1);
                            //s1.getShip()[i].bounce(3*Math.PI/2);
                        }
                        */
                    }
                }
                //s1.simStep();
                //repaint();//moved to slower timer
            }
        };
        ActionListener repaintTimer = new ActionListener(){
            //Client should have a repaintTimer every 16 ms
            //Server should have an updateClientsTimer instead, at least as frequent
            public void actionPerformed(ActionEvent e){
                repaint();
            }
        };
        timer = new Timer(timerDelay, forceTimer);
        frameTimer = new Timer(frameTimerDelay, repaintTimer);
        //timer.setInitialDelay(initialDelay);
        timer.start();
        frameTimer.start();
        setVisible(true);
    }

        /**
     * Prompt for and return the address of the server.
     */
    private String getServerAddress() {
        return JOptionPane.showInputDialog(
                this,
                "Enter IP Address of the Server:",
                JOptionPane.QUESTION_MESSAGE);
    }

    private String getPlayerName() {
        return JOptionPane.showInputDialog(
                this,
                "Choose a screen name:",
                "Screen name selection",
                JOptionPane.PLAIN_MESSAGE);
    }
    
    // Override update() to do double buffering instead of clearing the screen at every repaint()
    public void update(Graphics g) {
        if (dbImage == null) {
            dbImage = createImage(width, height);
            dbg = dbImage.getGraphics();
        }
        if (!trails) {
            dbg.setColor(Color.GRAY);
            dbg.fillRect(0, 0, width, height);
        }
        paint(dbg);
        g.drawImage(dbImage, 0, 0, this);
    }
    
    public void paint(Graphics g) {
        super.paint(g);
        /*
        for (i = 0; i < numShips; i++) {
            //s1.getShip()[i].draw(g);
            g.setColor(s1.getShip()[i].getColor()/*new Color(78,255,0));
            xPixel = s1.window.xPixel(s1.getShip()[i].getxLoc());
            yPixel = s1.window.yPixel(s1.getShip()[i].getyLoc());
            tempSize = s1.getShip()[i].getSize();
            g.fillOval(xPixel-tempSize, yPixel-tempSize, tempSize*2, tempSize*2);
        }
        for (i = 0; i < numStars; i++) {
            g.setColor(s1.getStar()[i].getColor());
            //g.setColor(Color.WHITE);
            xPixel = s1.window.xPixel(s1.getStar()[i].getxLoc());
            yPixel = s1.window.yPixel(s1.getStar()[i].getyLoc());
            //s1.getStar()[i].draw(g);
            tempSize = s1.getStar()[i].getSize();
            g.fillOval(xPixel-tempSize, yPixel-tempSize, tempSize*2, tempSize*2);
        }
        for(i=0; i < numPlanets; i++){
            //s1.getPlanet()[i].draw(g);
            g.setColor(s1.getPlanet()[i].getColor()/*new Color(78,255,0));
            //g.drawOval(xPixel, yPixel, 10, 10);
            xPixel = s1.window.xPixel(s1.getPlanet()[i].getxLoc());
            yPixel = s1.window.yPixel(s1.getPlanet()[i].getyLoc());
            if(planetFocus && i == focusPlanetIndex){
                s1.window.panRight(xPixel - arenaWidth / 2);
                s1.window.panDown(yPixel - arenaHeight / 2);
            }
            g.drawLine(xPixel, yPixel, xPixel, yPixel);
        }
        for(i=0; i < numCoins; i++){
            g.setColor(s1.getCoin()[i].getColor());
            xPixel = s1.window.xPixel(s1.getCoin()[i].getxLoc());
            yPixel = s1.window.yPixel(s1.getCoin()[i].getyLoc());
            tempSize = s1.getCoin()[i].getSize();
            g.drawOval(xPixel-tempSize, yPixel-tempSize, tempSize*2, tempSize*2);
        }
        //Paint the HUD:
        if (numShips > 0) {
            g.setColor(Color.WHITE);
            g.drawLine(HUDx, HUDy, HUDx, HUDheight);
            g.drawString("Ship 1 Fuel: " + String.format("%.3f", s1.getShip()[0].getFuel()), HUDx + 10, HUDy + 100);
            if (numShips > 1){
                g.drawString("Ship 2 Fuel: " + String.format("%.3f", s1.getShip()[1].getFuel()), HUDx + 10, HUDy + 200);
            }
        }
//        s1.getStar()[1].draw(g);
//        s1.getStar()[2].draw(g);
        */
        g.drawLine(HUDx, HUDy, HUDx, HUDheight);
        if(messageOnScreen != "") {
            String[] playerStatus = messageOnScreen.split(",");
            String[] parts = playerStatus[0].split("\\|");
            g.setColor(Color.WHITE);
            g.drawLine(HUDx, HUDy, HUDx, HUDheight);
            switch (parts[0]) {
                //In the protocol, the cases of messages from the server to the normal client are 1, 4, 5, and 8.
                //What if the user types a '|' in a message?
                case "3":
                    g.drawString(parts[1], HUDx + 10, HUDy + 100);
                    break;
                case "1":
                    //game time
                    g.drawString(parts[1], HUDx + 300, HUDy + 10);
                    break;
                case "4":
                    for(int i = 1; i < parts.length; i++) {
                        g.drawString(parts[i], HUDx + 10, HUDy + (i * 100));
                    }
                    break;
                case "5":
                    playerStatusString = messageOnScreen;
                    displayPlayerStatus(playerStatusString, g);
                    messageOnScreen = "";
                    break;
                case "6":
                    coinStatusString = messageOnScreen;
                    messageOnScreen = "";
                default:
                    break;
            }
        }
        if(playerStatusString != "") {
            g.drawLine(HUDx, HUDy, HUDx, HUDheight);
            displayPlayerStatus(playerStatusString, g);
        }
        if(coinStatusString != "") {
            g.drawLine(HUDx, HUDy, HUDx, HUDheight);
            displayCoinStatus(coinStatusString, g);
        }
    }

    private void displayPlayerStatus(String playerStatusStr, Graphics g) {
        String[] playerStatus = playerStatusStr.split(",");
        for(int i = 0; i < playerStatus.length; i++) {
            String[] statusParts = playerStatus[i].split("\\|");
            g.setColor(new Color(new Integer(statusParts[2])));
            //g.setColor(Color.WHITE);
            xPixel = new Integer(statusParts[3]);
            yPixel = new Integer(statusParts[4]); 
            tempSize = 20;
            g.fillOval(xPixel-tempSize, yPixel-tempSize, tempSize*2, tempSize*2);
            String playerInfo = statusParts[1] + ":" + statusParts[5];
            //System.out.println(playerInfo);
            g.drawString(playerInfo, HUDx + 10, HUDy + ((i+1) * 100));
        }
        
    }

    private void displayCoinStatus(String coinStatusStr, Graphics g) {
        String[] coinStatus = coinStatusStr.split(",");
        for(int i = 0; i < coinStatus.length; i++) {
            String[] statusParts = coinStatus[i].split("\\|");
            g.setColor(new Color(new Integer(statusParts[1])));
            xPixel = new Integer(statusParts[2]);
            yPixel = new Integer(statusParts[3]); 
            tempSize = 5;
            g.drawOval(xPixel-tempSize, yPixel-tempSize, tempSize*2, tempSize*2);
        }
    }
    // public void paint(graphics g){}

    private class keyListener extends KeyAdapter {
        char c;
        public void keyPressed(KeyEvent e){
            //assume that only 1 key is pressed at a time
            //TODO: optionally make it work with multiple keys pressed
            //      as a player would expect ex. 'w' and 'd' makes angle = PI/4
            c = e.getKeyChar();
            if(c == 'a'){
                String aStr = "7|" + playerName + "|a";
                client.send(aStr);
                //planetFocus = false;
                //s1.window.panLeft(10);
            } else if(c == 's'){
                String sStr = "7|" + playerName + "|s";
                client.send(sStr);
                //planetFocus = false;
                //s1.window.panDown(10);
            } else if(c == 'd'){
                String dStr = "7|" + playerName + "|d";
                client.send(dStr);
                //planetFocus = false;
                //s1.window.panRight(10);
            } else if(c == 'w'){
                String wStr = "7|" + playerName + "|w";
                client.send(wStr);
                //planetFocus = false;
                //s1.window.panUp(10);
            } else if(c == 'r') {
                String loginAttempt = "2|" + playerName + "|" + arenaWidth + "|" + arenaHeight;
                client.send(loginAttempt);
            } else if(c == 'p'){
                clientThread.stop();
                
                System.exit(0);
            }
        }
        
        public void keyReleased(KeyEvent e){
            if(numShips > 0){
                //s1.getShip()[0].setThrust(0.0);
                if(numShips > 1){
                    //s1.getShip()[1].setThrust(0.0);
                }
            }
        }
    }
    /*
    private class MousePressListener extends MouseAdapter{
//        public void mouseMoved(MouseEvent e){
//            s1.getShip()[0].setThrustAngle(org.apache.commons.math.util.FastMath.atan2(s1.window.yPixel(s1.getShip()[0].getyLoc()) - e.getY(), e.getX() - s1.window.xPixel(s1.getShip()[0].getxLoc())));
//        }
        public void mousePressed(MouseEvent e){
            if (e.getButton() == MouseEvent.BUTTON1) {
                if (numShips > 0) {
                    s1.getShip()[0].setThrustAngle(org.apache.commons.math.util.FastMath.atan2(s1.window.yPixel(s1.getShip()[0].getyLoc()) - e.getY(), e.getX() - s1.window.xPixel(s1.getShip()[0].getxLoc())));
                    s1.getShip()[0].setThrust(.001);
                } else {
                    planetFocus = false;
                    s1.window.panRight(e.getX() - arenaWidth / 2);
                    s1.window.panDown(e.getY() - arenaHeight / 2);
                }
            } else if(e.getButton() == MouseEvent.BUTTON3){
                if(numShips <= 0){
                    planetFocus = true;
                    focusPlanetIndex = s1.findNearestPlanet(s1.window.xLoc(e.getX()), s1.window.yLoc(e.getY()));
                }
            }
        }
        public void mouseReleased(MouseEvent e){
            if(numShips > 0){
                s1.getShip()[0].setThrust(0.0);
            }
        }
    }
    */
}
