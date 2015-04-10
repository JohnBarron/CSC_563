import java.awt.Color;
import java.awt.Graphics;

public class FixedStar {
    private double xLoc;
    private double yLoc;
    private int xPixel;
    private int yPixel;
    private double mass;
    
    public FixedStar(){
        xLoc = AstroidsWindow.width/2;
        yLoc = AstroidsWindow.height/2;
        xPixel = AstroidsWindow.width/2;
        yPixel = AstroidsWindow.height/2;
        mass = 1;
    }
    
    public FixedStar(double x, double y, double m){
        xLoc = x;
        yLoc = y;
        xPixel = (int)xLoc;
        yPixel = AstroidsWindow.height - (int)yLoc;
        mass = m;
    }
    
    public double getMass(){
        return mass;
    }
    public double getxLoc(){
        return xLoc;
    }
    public double getyLoc(){
        return yLoc;
    }
    
//    public void draw(Graphics g){
//        g.setColor(Color.WHITE);
//        g.fillOval(xPixel, yPixel, 10, 10);
//    }
}
