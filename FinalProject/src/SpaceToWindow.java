// Represent a viewing window within a space
// and map points in space to pixels on a screen window.
public class SpaceToWindow {
    // The viewing window within a 2d space has the following location and size.
    private double X, Y;
    private double windowWidth, windowHeight;
    
    public SpaceToWindow(){
        X = 0; //AstroidsWindow.width / 2;
        Y = 0; //AstroidsWindow.height / 2;
        windowWidth = AstroidsWindow.width;
        windowHeight = AstroidsWindow.height;
    }
    
    public SpaceToWindow(double x, double y, double W, double H){
        X = x;
        Y = y;
        windowWidth = W;
        windowHeight = H;
    }
    
    public double getX(){
        return X;
    }
    public double getY(){
        return Y;
    }
    public void setX(double x){
        X = x;
    }
    public void setY(double y){
        Y = y;
    }
    public double getWindowWidth(){
        return windowWidth;
    }
    public double getWindowHeight(){
        return windowHeight;
    }
    
    public void panLeft(double x){
        X -= x * windowWidth / AstroidsWindow.width;
    }
    public void panRight(double x){
        X += x * windowWidth / AstroidsWindow.width;
    }
    public void panUp(double y){
        Y += y * windowHeight / AstroidsWindow.height;
    }
    public void panDown(double y){
        Y -= y * windowHeight / AstroidsWindow.height;
    }
    
    public void zoom(double scalingFactor){
        // scalingFactor > 1 means zoom out.
        // scalingFactor < 1 means zoom in.
        if (scalingFactor > 1) {
            X -= windowWidth * scalingFactor / 4;
            Y -= windowHeight * scalingFactor / 4;
        }
        if (scalingFactor < 1) {
            X += windowWidth * scalingFactor / 2;
            Y += windowHeight * scalingFactor / 2;
        }
        windowWidth *= scalingFactor;
        windowHeight *= scalingFactor;
    }
    
    public int xPixel(double x){
        return (int)((x - X) * AstroidsWindow.width / windowWidth);
    }
    public int yPixel(double y){
        return (int)(AstroidsWindow.height - (y - Y) * AstroidsWindow.height / windowHeight);
    }
    public double xLoc(int xPixel){
        return (double)(xPixel * windowWidth / AstroidsWindow.width + X);
    }
    public double yLoc(int yPixel){
        return (double)((AstroidsWindow.height - yPixel) * windowHeight / AstroidsWindow.height + Y);
    }
}
