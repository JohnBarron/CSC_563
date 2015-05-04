package com.csc563;

import java.util.Random;
import java.awt.Color;

public class Coin {
    private double xLoc;
    private double yLoc;
    private Color color;
    private int size;
    private Random rng;
    
    public Coin(int width, int height) {
        rng = new Random();
        xLoc = rng.nextInt(width);
        yLoc = rng.nextInt(height);
        color = Color.YELLOW;
        size = 5;
    }
    public Coin(double x, double y){
        xLoc = x;
        yLoc = y;
        color = Color.YELLOW;
        size = 5;
    }
    
    public double getxLoc(){
        return xLoc;
    }
    public double getyLoc(){
        return yLoc;
    }
    public Color getColor(){
        return color;
    }
    public int getSize(){
        return size;
    }
}
