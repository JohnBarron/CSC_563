import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.Timer;
import java.lang.Math;
import java.lang.Thread;
// John Barron
// 1-5-12

public class AstroidsWindow extends Frame{
    private static final Toolkit toolkit = Toolkit.getDefaultToolkit();
    private static final Dimension screenSize = toolkit.getScreenSize();
    private int numShips = 1;
    private int numStars = 0;
    private int numPlanets = 0;
    private int numCoins = 10;
    private PhysicsSpace s1 = new PhysicsSpace(numShips, numStars, numPlanets, numCoins);
    private int focusPlanetIndex;
    //PhysicsSpace s1 = new PhysicsSpace();
    public static final int width = (int)screenSize.getWidth(), height = (int)screenSize.getHeight();
    private Image dbImage; // For double buffer
    private Graphics dbg;
    private javax.swing.Timer timer, frameTimer;
    public static int timerDelay = 16;
    public static final double speedFactor = 8;
    private int frameTimerDelay = 16;
    private boolean trails = false;
    public static boolean forwards = true;
    private boolean planetFocus = false;
    
    private int i, xPixel, yPixel;
    
    public AstroidsWindow() {
        super("Astroids");
        setSize(width, height);
        setLocation(0, 0);
        setUndecorated(true);
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        
        // draw first state of Ship

        // Register the window with a keyboard listener
        //if(numShips == 1){
        addKeyListener(new keyListener());
        addMouseListener(new MousePressListener());
        //}
        //addMouseMotionListener(new MouseMoveListener());
        //addMouseListener(new MousePressListener());
        
        ActionListener forceTimer = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                s1.simStep();//PhysicsSpace.move();
//                for (i = 0; i < numPlanets; i++) {
//                    s1.getPlanet()[i].getxLoc();
//                    s1.getPlanet()[i].getyLoc();
//                    s1.window.getX();
//                    if(s1.getPlanet()[i].getxLoc() < s1.window.getX() && s1.getPlanet()[i].getxSpeed() < 0){
//                        s1.getPlanet()[i].setxSpeed(s1.getPlanet()[i].getxSpeed() * -1);
//                    } if(s1.getPlanet()[i].getyLoc() < s1.window.getY() && s1.getPlanet()[i].getySpeed() < 0){
//                        s1.getPlanet()[i].setySpeed(s1.getPlanet()[i].getySpeed() * -1);
//                    } if(s1.getPlanet()[i].getxLoc() >= s1.window.getX() + s1.window.getWindowWidth() && s1.getPlanet()[i].getxSpeed() > 0){
//                        s1.getPlanet()[i].setxSpeed(s1.getPlanet()[i].getxSpeed() * -1);
//                    } if(s1.getPlanet()[i].getyLoc() >= s1.window.getY() + s1.window.getWindowHeight() && s1.getPlanet()[i].getySpeed() > 0){
//                        s1.getPlanet()[i].setySpeed(s1.getPlanet()[i].getySpeed() * -1);
//                    }
//                }
//                for (i = 0; i < numShips; i++) {
//                    s1.getShip()[i].getxLoc();
//                    s1.getShip()[i].getyLoc();
//                    s1.window.getX();
//                    if(s1.getShip()[i].getxLoc() < s1.window.getX() && s1.getShip()[i].getxSpeed() < 0){
//                        s1.getShip()[i].setxSpeed(s1.getShip()[i].getxSpeed() * -1);
//                    } if(s1.getShip()[i].getyLoc() < s1.window.getY() && s1.getShip()[i].getySpeed() < 0){
//                        s1.getShip()[i].setySpeed(s1.getShip()[i].getySpeed() * -1);
//                    } if(s1.getShip()[i].getxLoc() >= s1.window.getX() + s1.window.getWindowWidth() && s1.getShip()[i].getxSpeed() > 0){
//                        s1.getShip()[i].setxSpeed(s1.getShip()[i].getxSpeed() * -1);
//                    } if(s1.getShip()[i].getyLoc() >= s1.window.getY() + s1.window.getWindowHeight() && s1.getShip()[i].getySpeed() > 0){
//                        s1.getShip()[i].setySpeed(s1.getShip()[i].getySpeed() * -1);
//                    }
//                }
                //repaint();//moved to slower timer
            }
        };
        ActionListener repaintTimer = new ActionListener(){
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
    
//    public void run(){
//
//        addWindowListener(new WindowAdapter() {
//
//            public void windowClosing(WindowEvent e) {
//                System.exit(0);
//            }
//        });
//        
//        // draw first state of Ship
//
//        // Register the window with a keyboard listener
//        //if(numShips == 1){
//        addKeyListener(new keyListener());
//        //}
//        //addMouseMotionListener(new MouseMoveListener());
//        //addMouseListener(new MousePressListener());
//        
//        ActionListener forceTimer = new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                s1.simStep();//PhysicsSpace.move();
//                //repaint();//moved to slower timer
//            }
//        };
//        ActionListener repaintTimer = new ActionListener(){
//            public void actionPerformed(ActionEvent e){
//                repaint();
//            }
//        };
//        timer = new Timer(timerDelay, forceTimer);
//        frameTimer = new Timer(frameTimerDelay, repaintTimer);
//        //timer.setInitialDelay(initialDelay);
//        timer.start();
//        frameTimer.start();
//        setVisible(true);
//    }
    
    
    // Override update() to do double buffering instead of clearing the screen at every repaint()
    public void update(Graphics g) {
        if (dbImage == null) {
            dbImage = createImage(width, height);
            dbg = dbImage.getGraphics();
        }
        if (!trails) {
            dbg.setColor(Color.BLACK);
            dbg.fillRect(0, 0, width, height);
        }
        paint(dbg);
        g.drawImage(dbImage, 0, 0, this);
    }
    
    public void paint(Graphics g) {
        super.paint(g);
        
        //if(planetFocus){
        //    s1.window.setX(s1.getPlanet()[focusPlanetIndex].getxLoc());
        //    s1.window.setY(s1.getPlanet()[focusPlanetIndex].getyLoc());
        //}
        //g.setColor(Color.blue);
        //g.setColor(new Color(78,255,0));// each object to draw should draw itself
        //g.drawOval(s1.ship[0].getxPixel(), s1.ship[0].getyPixel(), 10, 10);
        //     s1.getShip()[0].draw(g);//for() all the ships, stars, planets, etc
        //     s1.getStar()[0].draw(g);
        for (i = 0; i < numShips; i++) {
            //s1.getShip()[i].draw(g);
            g.setColor(s1.getShip()[i].getColor()/*new Color(78,255,0)*/);
            //g.drawOval(xPixel, yPixel, 10, 10);
            xPixel = s1.window.xPixel(s1.getShip()[i].getxLoc());
            yPixel = s1.window.yPixel(s1.getShip()[i].getyLoc());
            //g.drawLine(xPixel, yPixel, xPixel, yPixel);
            g.fillOval(xPixel-5, yPixel-5, 10, 10);
        }
        for (i = 0; i < numStars; i++) {
            //s1.getStar()[i].draw(g);
        }
        for(i=0; i < numPlanets; i++){
            //s1.getPlanet()[i].draw(g);
            g.setColor(s1.getPlanet()[i].getColor()/*new Color(78,255,0)*/);
            //g.drawOval(xPixel, yPixel, 10, 10);
            xPixel = s1.window.xPixel(s1.getPlanet()[i].getxLoc());
            yPixel = s1.window.yPixel(s1.getPlanet()[i].getyLoc());
            if(planetFocus && i == focusPlanetIndex){
                s1.window.panRight(xPixel - width / 2);
                s1.window.panDown(yPixel - height / 2);
            }
            g.drawLine(xPixel, yPixel, xPixel, yPixel);
        }
        for(i=0; i < numCoins; i++){
            g.setColor(s1.getCoin()[i].getColor());
            xPixel = s1.window.xPixel(s1.getCoin()[i].getxLoc());
            yPixel = s1.window.yPixel(s1.getCoin()[i].getyLoc());
            g.drawOval(xPixel-5, yPixel-5, 10, 10);
        }
//        s1.getStar()[1].draw(g);
//        s1.getStar()[2].draw(g);
    }

    // public void paint(graphics g){}
    
    private class keyListener extends KeyAdapter {
        char c;
        /*public void keyTyped(KeyEvent e) {
            c = e.getKeyChar();
            if (c == 's') {
                s1.setThrustAngle(3 * Math.PI / 2);
            } else if (c == 'a') {
                s1.setThrustAngle(Math.PI);
            } else if (c == 'w') {
                s1.setThrustAngle(Math.PI/2);
            } else if (c == 'd') {
                s1.setThrustAngle(0.0);
            }
        }
        */
        public void keyPressed(KeyEvent e){
            c = e.getKeyChar();
            if (numShips > 0) {
                if(c == 's') {
                    s1.getShip()[0].setThrustAngle(3 * Math.PI / 2);
                    s1.getShip()[0].setThrust(.001);
                } else if(c == 'a') {
                    s1.getShip()[0].setThrustAngle(Math.PI);
                    s1.getShip()[0].setThrust(.001);
                } else if(c == 'w') {
                    s1.getShip()[0].setThrustAngle(Math.PI / 2);
                    s1.getShip()[0].setThrust(.001);
                } else if(c == 'd') {
                    s1.getShip()[0].setThrustAngle(0.0);
                    s1.getShip()[0].setThrust(.001);
                }
            } else {
                if(c == 'a'){
                    planetFocus = false;
                    s1.window.panLeft(10);
                } else if(c == 's'){
                    planetFocus = false;
                    s1.window.panDown(10);
                } else if(c == 'd'){
                    planetFocus = false;
                    s1.window.panRight(10);
                } else if(c == 'w'){
                    planetFocus = false;
                    s1.window.panUp(10);
                }
            }
            
            if(c == 'q'){
                if(trails){
                    trails = false;
                } else{
                    trails = true;
                }
            } else if(c == 'e'){
                if(forwards){
                    forwards = false;
                } else{
                    forwards = true;
                }
            } else if(c == 'r'){
                if(timer.isRunning()){
                    timer.stop();
                }else{
                    timer.start();
                }
            } else if(c == 'x'){
                //planetFocus = false;
                s1.window.zoom(2);
            } else if(c == 'z'){
                //planetFocus = false;
                s1.window.zoom(.5);
            } else if(c == 'g' && timerDelay >= 2){
                timerDelay /= 2;
                timer.setDelay(timerDelay);
            } else if(c == 'f'){
                timerDelay *= 2;
                timer.setDelay(timerDelay);
            }
        }
        
        public void keyReleased(KeyEvent e){
            if(numShips > 0){
                s1.getShip()[0].setThrust(0.0);
            }
        }
    }
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
                    s1.window.panRight(e.getX() - width / 2);
                    s1.window.panDown(e.getY() - height / 2);
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
}
