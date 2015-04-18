import java.lang.Math.*;
import java.awt.Color;
import java.awt.Graphics;

public class Ship {
    private double xLoc;
    private double yLoc;
    private int xPixel;
    private int yPixel;
    private double xSpeed; // units pixels per sim timer tic
    private double ySpeed;
    private double thrust;
    private double xAcceleration;
    private double yAcceleration;
    private double thrustAngle;
    private double mass;
    private Color color;
    private double fuel; // units sim timer tics of standard thrust
    private double fuelColor;
//    private double g;
    // to be moved:
//    private double starMass;
//    private double starX;
//    private double starY;
//    private double gAngle;
    
    public Ship(){
        xLoc = 150;
        yLoc = 250;
        xSpeed = 0;
        ySpeed = 0.01;
        thrust = 0;
        xAcceleration = 0;
        yAcceleration = 0;
        thrustAngle = 0;
        mass = -1;
        color = new Color(78,255,0);
        fuel = 1;
    }
    
    public void setThrustAngle(double a){
        thrustAngle = a;
    }
    
    public void setThrust(double t){
        thrust = t;
    }
    
    public void setxAcceleration(double a){
        xAcceleration = a;
    }
    public double getxAcceleration(){
        return xAcceleration;
    }
    public void setyAcceleration(double a){
        yAcceleration = a;
    }
    public double getyAcceleration(){
        return yAcceleration;
    }
    
    public int getxPixel(){
        return xPixel;
    }
    public int getyPixel(){
        return yPixel;
    }
    public double getxLoc(){
        return xLoc;
    }
    public double getyLoc(){
        return yLoc;
    }
    
    public double getMass(){
        return mass;
    }
    public Color getColor(){
        return color;
    }
    public void setFuel(double f){
        fuel = f;
    }
    public double getFuel(){
        return fuel;
    }
    public double getxSpeed(){
        return xSpeed;
    }
    public double getySpeed(){
        return ySpeed;
    }
    public void setxSpeed(double x){
        xSpeed = x;
    }
    public void setySpeed(double y){
        ySpeed = y;
    }
    
    //public void setG(double G){
    //    g = G;
    //}
    
    public void move(){
        //g = -1.0*starMass/((starX-xLoc)*(starX-xLoc)+(starY-yLoc)*(starY-yLoc));
        //gAngle = java.lang.Math.atan2((yLoc-starY),(xLoc-starX));
        
        //xAcceleration += thrust * java.lang.Math.cos(thrustAngle);// + g * java.lang.Math.cos(gAngle);
        //yAcceleration += thrust * java.lang.Math.sin(thrustAngle);// + g * java.lang.Math.sin(gAngle);
        
        if(AstroidsWindow.forwards){
            fuel -= thrust;
            
            xAcceleration += thrust * org.apache.commons.math.util.FastMath.cos(thrustAngle);
            yAcceleration += thrust * org.apache.commons.math.util.FastMath.sin(thrustAngle);
            
            xSpeed += xAcceleration;
            ySpeed += yAcceleration;
            
            xLoc += /*AstroidsWindow.timerDelay * */AstroidsWindow.speedFactor * xSpeed;
            yLoc += /*AstroidsWindow.timerDelay * */AstroidsWindow.speedFactor * ySpeed;
        } else {
            fuel += thrust;
            
            xAcceleration -= thrust * org.apache.commons.math.util.FastMath.cos(thrustAngle);
            yAcceleration -= thrust * org.apache.commons.math.util.FastMath.sin(thrustAngle);
            
            xLoc -= /*AstroidsWindow.timerDelay * */AstroidsWindow.speedFactor * xSpeed;
            yLoc -= /*AstroidsWindow.timerDelay * */AstroidsWindow.speedFactor * ySpeed;
            
            xSpeed -= xAcceleration;
            ySpeed -= yAcceleration;
        }
        
        fuelColor = fuel * 255 / 10;
        if(fuelColor > 255){
            fuelColor = 255;
        } else if(fuelColor < 0){
            fuelColor = 0;
        }
        color = new Color(255 - (int)fuelColor,(int)fuelColor,0);
        //xAcceleration += thrust * org.apache.commons.math.util.FastMath.cos(thrustAngle);
        //yAcceleration += thrust * org.apache.commons.math.util.FastMath.sin(thrustAngle);
        //xSpeed += AstroidsWindow.forwards*xAcceleration;
        //ySpeed += AstroidsWindow.forwards*yAcceleration;
        
        xAcceleration = 0;// g reaccumualted in PhysiscsSpace.applyForces()
        yAcceleration = 0;
        
        //xLoc += AstroidsWindow.forwards*xSpeed;
        //yLoc += AstroidsWindow.forwards*ySpeed;
//        xPixel = (int)xLoc;
//        yPixel = AstroidsWindow.height - (int)yLoc;
//        
//        if(xPixel >= AstroidsWindow.width){
//            xSpeed *= -1;
//            //xPixel -= AstroidsWindow.width;
//            //xLoc -= AstroidsWindow.width;
//        }
//        else if(xPixel < 0){
//            xSpeed *= -1;
//            //xPixel += AstroidsWindow.width;
//            //xLoc += AstroidsWindow.width;
//        }
//        else if(yPixel >= AstroidsWindow.height){
//            ySpeed *= -1;
//            //yPixel -= AstroidsWindow.height;
//            //yLoc += AstroidsWindow.height;
//        }
//        else if(yPixel < 0){
//            ySpeed *= -1;
//            //yPixel += AstroidsWindow.height;
//            //yLoc -= AstroidsWindow.height;
//        }
    }
    
//    public void draw(Graphics g){
//        g.setColor(color/*new Color(78,255,0)*/);
//        //g.drawOval(xPixel, yPixel, 10, 10);
//        g.drawLine(xPixel, yPixel, xPixel, yPixel);
//    }
}
