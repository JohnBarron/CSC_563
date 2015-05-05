package com.csc563;

import java.awt.Color;
import java.awt.Graphics;

public class Planet {
    private double xLoc;
    private double yLoc;
    private int xPixel;
    private int yPixel;
    private double xSpeed; // units pixels per sim timer tic
    private double ySpeed;
    private double xAcceleration;
    private double yAcceleration;
    private double mass;
    private Color color;
    private double speedFactor;
    
    public Planet(){
        xLoc = 350;
        yLoc = 550;
        xSpeed = 0;
        ySpeed = 0;
        xAcceleration = 0;
        yAcceleration = 0;
        mass = 1;
        color = new Color(255, 0, 0);
    }
    
    public Planet(double x, double y, double m) {
        xLoc = x;
        yLoc = y;
        mass = m;
        xSpeed = 0;
        ySpeed = 0;
        xAcceleration = 0;
        yAcceleration = 0;
        color = new Color(255, 0, 0);
    }
    
    public Planet(double x, double y, double m, Color c, double spdFactor) {
        xLoc = x;
        yLoc = y;
        mass = m;
        xSpeed = 0;
        ySpeed = 0;
        xAcceleration = 0;
        yAcceleration = 0;
        color = c;
        speedFactor = spdFactor;
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
    
    public double getMass(){
        return mass;
    }
    public Color getColor(){
        return color;
    }
    
    public void move(){
        
//        if(AstroidsWindow.forwards){
            xSpeed += xAcceleration;
            ySpeed += yAcceleration;
            
            xLoc += /*AstroidsWindow.timerDelay * */speedFactor * xSpeed;
            yLoc += /*AstroidsWindow.timerDelay * */speedFactor * ySpeed;
            /*
        } else {
            xLoc -= /*AstroidsWindow.timerDelay * AstroidsWindow.speedFactor * xSpeed;
            yLoc -= /*AstroidsWindow.timerDelay * AstroidsWindow.speedFactor * ySpeed;
            
            xSpeed -= xAcceleration;
            ySpeed -= yAcceleration;
        }
    */
        //xSpeed += AstroidsWindow.forwards*xAcceleration;
        //ySpeed += AstroidsWindow.forwards*yAcceleration;
        
        xAcceleration = 0;// g reaccumualted in PhysiscsSpace.applyForces()
        yAcceleration = 0;
        
        //xLoc += AstroidsWindow.forwards*xSpeed;
        //yLoc += AstroidsWindow.forwards*ySpeed;
//        xPixel = (int)xLoc;
//        yPixel = AstroidsWindow.height - (int)yLoc;
//        //xPixel = PhysicsSpace.window.xPixel(xLoc);
//        //yPixel = PhysicsSpace.window.yPixel(yLoc);
//        
//        if(xPixel >= AstroidsWindow.width && xSpeed > 0){
//            xSpeed *= -1;
//            //xPixel -= AstroidsWindow.width;
//            //xLoc -= AstroidsWindow.width;
//        }
//        else if(xPixel < 0 && xSpeed < 0){
//            xSpeed *= -1;
//            //xPixel += AstroidsWindow.width;
//            //xLoc += AstroidsWindow.width;
//        }
//        else if(yPixel >= AstroidsWindow.height && ySpeed < 0){
//            ySpeed *= -1;
//            //yPixel -= AstroidsWindow.height;
//            //yLoc += AstroidsWindow.height;
//        }
//        else if(yPixel < 0 && ySpeed > 0){
//            ySpeed *= -1;
//            //yPixel += AstroidsWindow.height;
//            //yLoc -= AstroidsWindow.height;
//        }
    }
    
//    public void draw(Graphics g){
//        g.setColor(color/*new Color(255, 0, 0)*/);
//        //g.fillOval(xPixel, yPixel, 10, 10);
//        g.drawLine(xPixel, yPixel, xPixel, yPixel);
//    }
}
