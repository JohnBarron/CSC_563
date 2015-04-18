import java.util.Random;
import java.awt.Color;

public class PhysicsSpace {
    private Ship[] ship;
    private FixedStar[] star;
    private Planet[] planet;
    private Coin[] coin;
    //private double[][] previousPeriodDistance;
    //private double[][] previousAngle;
    private double minDistanceToCursor;
    private double dTemp; // used in findNearestPlanet()
    private int numShips, numStars, numPlanets, numCoins;
    public SpaceToWindow window;
    private Random rng;
    int i = 0, j = 0;
    double g, gAngle;
    double G;
    //double currentPairDistance;
    //double orbitDistanceDelta;
    //double orbitDistanceThreshold;
    
    public PhysicsSpace(){
        G = 1;
        window = new SpaceToWindow();
        numShips = 1;
        numStars = 1;
        numPlanets = 1;
        numCoins = 0;
        //xMax = 500; yMax = 500;
        ship = new Ship[numShips];
        star = new FixedStar[numStars];
        planet = new Planet[numPlanets];
        for(i=0; i<numShips; i++){
            ship[i] = new Ship();
        }
        for(i=0; i<numStars; i++){
            star[i] = new FixedStar();
        }
        for(i=0; i<numPlanets; i++){
            planet[i] = new Planet();
        }
    }
    
    public PhysicsSpace(int numShips, int numStars, int numPlanets, int numCoins){
        G = 16;
        window = new SpaceToWindow();
        this.numShips = numShips;
        this.numStars = numStars;// exeptions for bad numbers
        this.numPlanets = numPlanets;
        this.numCoins = numCoins;
        ship = new Ship[numShips];
        star = new FixedStar[numStars];
        planet = new Planet[numPlanets];
        coin = new Coin[numCoins];
        //previousPeriodDistance = new double[numPlanets][numPlanets];
        //previousAngle = new double[numPlanets][numPlanets];
        //orbitDistanceThreshold = 0.1;
        rng = new Random();
        int x, y;
        double m;
        if(numShips == 1){
            ship[0] = new Ship();
        }
        if(numStars == 1){
            star[0] = new FixedStar();
        }
        if(numStars == 2){
            star[0] = new FixedStar(500, 400, 1);
            star[1] = new FixedStar(1200, 400, 1);
        }
        if(numStars == 3){
            star[0] = new FixedStar();
            star[1] = new FixedStar(500, 400, 1);
            star[2] = new FixedStar(1200, 400, 1);
        }
        for(i=0; i<numPlanets; i++){
            x = rng.nextInt(AstroidsWindow.width / 4) + AstroidsWindow.width * 3 / 8;
            y = rng.nextInt(AstroidsWindow.height / 4) + AstroidsWindow.height * 3 / 8;
            //x = rng.nextInt(AstroidsWindow.width);
            //y = rng.nextInt(AstroidsWindow.height);
            //x = (int)(rng.nextGaussian() * .5 - 1) * AstroidsWindow.width;
            //y = (int)(rng.nextGaussian() * .5 - 1) * AstroidsWindow.height;
            
            //m = rng.nextDouble() * 2 - 1;
            //m = rng.nextGaussian() * .5 - 1;
            //m = -1;
            m = 1;
            planet[i] = new Planet(x, y, m, new Color(rng.nextInt(256),rng.nextInt(256),rng.nextInt(256)));
            //planet[i].setxAcceleration(.1 * (rng.nextDouble() * 2 - 1));
            //planet[i].setyAcceleration(.1 * (rng.nextDouble() * 2 - 1));
        }
        for(i=0; i<numCoins; i++){
            coin[i] = new Coin();
        }
    }
    
    public Ship[] getShip(){
        return ship;
    }
    
    public FixedStar[] getStar(){
        return star;
    }
    
    public Planet[] getPlanet(){
        return planet;
    }
    
    public Coin[] getCoin(){
        return coin;
    }
    
    public void simStep(){
        applyForces();
        //applyForceField();
        updateState();
    }
    
    private void updateState(){
        for(i=0; i<numShips; i++){
            ship[i].move();
        }
        for(i=0; i<numPlanets; i++){
            planet[i].move();
        }
    }
    
    private void applyForces(){
        for(i=0; i<numShips; i++){
            for(j=i+1; j<numShips; j++){// ship-ship interactions
                // force from ship[j] acting on ship[i]:
                g = G * /*AstroidsWindow.forwards**/-1.0*ship[j].getMass()/((ship[j].getxLoc()-ship[i].getxLoc())*(ship[j].getxLoc()-ship[i].getxLoc())+(ship[j].getyLoc()-ship[i].getyLoc())*(ship[j].getyLoc()-ship[i].getyLoc()));
                gAngle = org.apache.commons.math.util.FastMath.atan2(ship[i].getyLoc()-ship[j].getyLoc(), ship[i].getxLoc()-ship[j].getxLoc());
                ship[i].setxAcceleration(ship[i].getxAcceleration() + g * org.apache.commons.math.util.FastMath.cos(gAngle));
                ship[i].setyAcceleration(ship[i].getyAcceleration() + g * org.apache.commons.math.util.FastMath.sin(gAngle));
                // force from ship[i] acting on ship[j]:
                g = G * /*AstroidsWindow.forwards**/-1.0 * g / ship[j].getMass() * ship[i].getMass();
                ship[j].setxAcceleration(ship[j].getxAcceleration() + g * org.apache.commons.math.util.FastMath.cos(gAngle));
                ship[j].setyAcceleration(ship[j].getyAcceleration() + g * org.apache.commons.math.util.FastMath.sin(gAngle));
            }
            for(j=0; j<numStars; j++){// ship-star interactions
                g = G * /*AstroidsWindow.forwards**/-1.0*star[j].getMass()/((star[j].getxLoc()-ship[i].getxLoc())*(star[j].getxLoc()-ship[i].getxLoc())+(star[j].getyLoc()-ship[i].getyLoc())*(star[j].getyLoc()-ship[i].getyLoc()));
                gAngle = org.apache.commons.math.util.FastMath.atan2(ship[i].getyLoc()-star[j].getyLoc(), ship[i].getxLoc()-star[j].getxLoc());
                ship[i].setxAcceleration(ship[i].getxAcceleration() + g * org.apache.commons.math.util.FastMath.cos(gAngle));
                ship[i].setyAcceleration(ship[i].getyAcceleration() + g * org.apache.commons.math.util.FastMath.sin(gAngle));
                if(org.apache.commons.math.util.FastMath.sqrt((ship[i].getxLoc() - coin[j].getxLoc()) * (ship[i].getxLoc() - coin[j].getxLoc()) + (ship[i].getyLoc() - coin[j].getyLoc()) * (ship[i].getyLoc() - coin[j].getyLoc())) < 10){
                    //collision with ship and star
                    //game over for Ship[i]
                    //TODO: make gameOver() method
                }
            }
            for(j=0; j<numPlanets; j++){// ship-planet interactions
                // force from planet acting on ship:
                g = G * /*AstroidsWindow.forwards**/-1.0*planet[j].getMass()/((planet[j].getxLoc()-ship[i].getxLoc())*(planet[j].getxLoc()-ship[i].getxLoc())+(planet[j].getyLoc()-ship[i].getyLoc())*(planet[j].getyLoc()-ship[i].getyLoc()));
                gAngle = org.apache.commons.math.util.FastMath.atan2(ship[i].getyLoc()-planet[j].getyLoc(), ship[i].getxLoc()-planet[j].getxLoc());
                ship[i].setxAcceleration(ship[i].getxAcceleration() + g * org.apache.commons.math.util.FastMath.cos(gAngle));
                ship[i].setyAcceleration(ship[i].getyAcceleration() + g * org.apache.commons.math.util.FastMath.sin(gAngle));
                // force from ship acting on planet:
                g = G * /*AstroidsWindow.forwards**/-1.0 * g / planet[j].getMass() * ship[i].getMass();
                planet[j].setxAcceleration(planet[j].getxAcceleration() + g * org.apache.commons.math.util.FastMath.cos(gAngle));
                planet[j].setyAcceleration(planet[j].getyAcceleration() + g * org.apache.commons.math.util.FastMath.sin(gAngle));
            }
        }
        for(i=0; i<numPlanets; i++){// planet-planet interactions
            for(j=i+1; j<numPlanets; j++){
                // force from planet[j] acting on planet[i]:
                //currentPairDistance = distance(planet[i].getxLoc(), planet[i].getyLoc(), planet[j].getxLoc(), planet[j].getyLoc());
              //  g = G * /*AstroidsWindow.forwards**/(-1.0)*planet[j].getMass()/(currentPairDistance*currentPairDistance); //((planet[j].getxLoc()-planet[i].getxLoc())*(planet[j].getxLoc()-planet[i].getxLoc())+(planet[j].getyLoc()-planet[i].getyLoc())*(planet[j].getyLoc()-planet[i].getyLoc()));
                gAngle = org.apache.commons.math.util.FastMath.atan2(planet[i].getyLoc()-planet[j].getyLoc(), planet[i].getxLoc()-planet[j].getxLoc());
                //if(previousAngle[i][j] != 0 && (gAngle < 0 && previousAngle[i][j] >= 0) || (gAngle >= 0 && previousAngle[i][j] < 0)){
                    // if the pair of planets has come full circle:
                //    orbitDistanceDelta = currentPairDistance - previousPeriodDistance[i][j];
                //    if(orbitDistanceDelta < orbitDistanceThreshold * currentPairDistance && orbitDistanceDelta > -1*orbitDistanceThreshold * currentPairDistance){
                //        //Then its an "orbit"!
                //        System.out.println(i + " " + j);
                //    }
                //    previousPeriodDistance[i][j] = currentPairDistance;
               // }
                planet[i].setxAcceleration(planet[i].getxAcceleration() + g * org.apache.commons.math.util.FastMath.cos(gAngle));
                planet[i].setyAcceleration(planet[i].getyAcceleration() + g * org.apache.commons.math.util.FastMath.sin(gAngle));
                // force from planet[i] acting on planet[j]:
                g = G * /*AstroidsWindow.forwards**/-1.0 * g / planet[j].getMass() * planet[i].getMass();
                planet[j].setxAcceleration(planet[j].getxAcceleration() + g * org.apache.commons.math.util.FastMath.cos(gAngle));
                planet[j].setyAcceleration(planet[j].getyAcceleration() + g * org.apache.commons.math.util.FastMath.sin(gAngle));
          //      previousAngle[i][j] = gAngle;
            }
            for(j=0; j<numStars; j++){// planet-star interactions
                g = G * /*AstroidsWindow.forwards**/-1.0*star[j].getMass()/((star[j].getxLoc()-planet[i].getxLoc())*(star[j].getxLoc()-planet[i].getxLoc())+(star[j].getyLoc()-planet[i].getyLoc())*(star[j].getyLoc()-planet[i].getyLoc()));
                gAngle = org.apache.commons.math.util.FastMath.atan2(planet[i].getyLoc()-star[j].getyLoc(), planet[i].getxLoc()-star[j].getxLoc());
                planet[i].setxAcceleration(planet[i].getxAcceleration() + g * org.apache.commons.math.util.FastMath.cos(gAngle));
                planet[i].setyAcceleration(planet[i].getyAcceleration() + g * org.apache.commons.math.util.FastMath.sin(gAngle));
            }
        }
        for(i=0; i<numShips; i++){// ship-coin interactions
            for(j=0; j<numCoins; j++){
                if(org.apache.commons.math.util.FastMath.sqrt((ship[i].getxLoc() - coin[j].getxLoc()) * (ship[i].getxLoc() - coin[j].getxLoc()) + (ship[i].getyLoc() - coin[j].getyLoc()) * (ship[i].getyLoc() - coin[j].getyLoc())) < 10){
                    ship[i].setFuel(ship[i].getFuel() + 1);
                    coin[j] = new Coin();
                }
            }
        }
    }
    
    private void applyForceField(){
        for(i=0; i<numPlanets; i++){
            g = G * -1.0 / (planet[i].getxLoc() * planet[i].getxLoc() + planet[i].getyLoc() * planet[i].getyLoc());
            gAngle = org.apache.commons.math.util.FastMath.atan2(planet[i].getyLoc(), planet[i].getxLoc());
            planet[i].setxAcceleration(planet[i].getxAcceleration() + g * org.apache.commons.math.util.FastMath.cos(gAngle));
            planet[i].setyAcceleration(planet[i].getyAcceleration() + g * org.apache.commons.math.util.FastMath.sin(gAngle));
        }
    }
    
    private void forceField(double x, double y){
        // return force and angle
    }
    
    public int findNearestPlanet(double x, double y){
        // for all planets, find the distance to the given x, y
        // find the minimum distance. return the Planet index with that distance
        dTemp = -1;
        i = 0;
        j = 0;// index of min
        minDistanceToCursor = distance(x, y, planet[0].getxLoc(), planet[0].getyLoc());
        for(i=1; i < numPlanets; i++){
            dTemp = distance(x, y, planet[i].getxLoc(), planet[i].getyLoc());
            if(dTemp < minDistanceToCursor){
                minDistanceToCursor = dTemp;
                j = i;
            }
        }
        return j;
    }
    
    public static double distance(double x1, double y1, double x2, double y2){
        return org.apache.commons.math.util.FastMath.sqrt((x2-x1)*(x2-x1)+(y2-y1)*(y2-y1));
    }
}
