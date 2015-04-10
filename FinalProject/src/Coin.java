import java.util.Random;
import java.awt.Color;

public class Coin {
    private double xLoc;
    private double yLoc;
    private Color color;
    private Random rng;
    
    public Coin() {
        rng = new Random();
        xLoc = rng.nextInt(AstroidsWindow.width);
        yLoc = rng.nextInt(AstroidsWindow.height);
        color = Color.YELLOW;
    }
    public Coin(double x, double y){
        xLoc = x;
        yLoc = y;
        color = Color.YELLOW;
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
}
