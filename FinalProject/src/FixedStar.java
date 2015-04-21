import java.awt.Color;
import java.awt.Graphics;

public class FixedStar {
    private double xLoc;
    private double yLoc;
    private int xPixel;
    private int yPixel;
    private double mass;
    private Color color;
    private int size;
    
    public FixedStar(){
        xLoc = AstroidsWindow.arenaWidth/2;
        yLoc = AstroidsWindow.arenaHeight/2;
        xPixel = AstroidsWindow.arenaWidth/2;
        yPixel = AstroidsWindow.arenaHeight/2;
        mass = 1;
        color = Color.WHITE;
        size = 20;
    }
    
    public FixedStar(double x, double y, double m){
        xLoc = x;
        yLoc = y;
        xPixel = (int)xLoc;
        yPixel = AstroidsWindow.height - (int)yLoc;
        mass = m;
        color = Color.WHITE;
        size = 1;
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
    public Color getColor(){
        return color;
    }
    public int getSize(){
        return size;
    }
    
//    public void draw(Graphics g){
//        g.setColor(Color.WHITE);
//        g.fillOval(xPixel, yPixel, 10, 10);
//    }
}
