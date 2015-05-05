package com.csc563;

import com.csc563.Coin;
import com.csc563.FixedStar;
import com.csc563.Planet;
import com.csc563.SpaceToWindow;
import java.util.Random;
import java.awt.Color;
import java.util.ArrayList;

public class PhysicsSpace {
    private Ship[] ship;
    private FixedStar[] star;
    private Planet[] planet;
    private Coin[] coin;
    //private double[][] previousPeriodDistance;
    //private double[][] previousAngle;
    private double minDistanceToCursor;
    private double dTemp; // used in findNearestPlanet()
    public int numShips, numStars, numPlanets, numCoins;
    SpaceToWindow window;
    private Random rng;
    int i = 0, j = 0;
    double g, gAngle;
    double G; //Like the universal gravitational constant of physics
    //double currentPairDistance;
    //double orbitDistanceDelta;
    //double orbitDistanceThreshold;
    int arenaWidth;
    int arenaHeight;
    double speedFactor;
    
    /*
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
    */
    
    public PhysicsSpace(ArrayList connectedClients, int numStars, int numPlanets, int numCoins, int width, int height, double spdFactor){
        G = 32;
        window = new SpaceToWindow(0, 0, width, height);
        this.numShips = connectedClients.size();
        this.numStars = numStars;// exeptions for bad numbers
        this.numPlanets = numPlanets;
        this.numCoins = numCoins;
        ship = new Ship[numShips];
        star = new FixedStar[numStars];
        planet = new Planet[numPlanets];
        coin = new Coin[numCoins];
        this.arenaWidth = width;
        this.arenaHeight = height;
        this.speedFactor = spdFactor;
        //previousPeriodDistance = new double[numPlanets][numPlanets];
        //previousAngle = new double[numPlanets][numPlanets];
        //orbitDistanceThreshold = 0.1;
        rng = new Random();
        int x, y;
        double m;
        for(int i = 0; i < numShips; i++) {
            ship[i] = new Ship((double)width/((i+1)*4), (double)height/((i+1)*2), spdFactor);
            ((Client)connectedClients.get(i)).ship = ship[i];
        }
        /*
        }
        if(numShips == 2){
            ship[0] = new Ship(width/4,);
        if(numShips == 1){
            ship[0] = new Ship();
            ship[1] = new Ship(2, width, height, spdFactor);
        }
        */
        if(numStars == 1){
            star[0] = new FixedStar(width, height);
        }
        /*
        if(numStars == 2){
            star[0] = new FixedStar(500, 400, 1);
            star[1] = new FixedStar(1200, 400, 1);
        }
        if(numStars == 3){
            star[0] = new FixedStar(width, height);
            star[1] = new FixedStar(500, 400, 1);
            star[2] = new FixedStar(1200, 400, 1);
        }
        */
        for(i=0; i<numPlanets; i++){
            x = rng.nextInt(width / 4) + width * 3 / 8;
            y = rng.nextInt(height / 4) + height * 3 / 8;
            //x = rng.nextInt(AstroidsWindow.width);
            //y = rng.nextInt(AstroidsWindow.height);
            //x = (int)(rng.nextGaussian() * .5 - 1) * AstroidsWindow.width;
            //y = (int)(rng.nextGaussian() * .5 - 1) * AstroidsWindow.height;
            
            //m = rng.nextDouble() * 2 - 1;
            //m = rng.nextGaussian() * .5 - 1;
            //m = -1;
            m = 1;
            planet[i] = new Planet(x, y, m, new Color(rng.nextInt(256),rng.nextInt(256),rng.nextInt(256)), spdFactor);
            //planet[i].setxAcceleration(.1 * (rng.nextDouble() * 2 - 1));
            //planet[i].setyAcceleration(.1 * (rng.nextDouble() * 2 - 1));
        }
        for(i=0; i<numCoins; i++){
            coin[i] = new Coin(width, height);
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
    
    private void updateState() {
        for (i = 0; i < numShips; i++) {
            //ship[i].move();
//            if (AstroidsWindow.forwards) {
                ship[i].setFuel(ship[i].getFuel() - ship[i].getThrust());

                ship[i].setxAcceleration(ship[i].getxAcceleration() + ship[i].getThrust() * org.apache.commons.math.util.FastMath.cos(ship[i].getThrustAngle()));
                ship[i].setyAcceleration(ship[i].getyAcceleration() + ship[i].getThrust() * org.apache.commons.math.util.FastMath.sin(ship[i].getThrustAngle()));

                ship[i].setxSpeed(ship[i].getxSpeed() + ship[i].getxAcceleration());
                ship[i].setySpeed(ship[i].getySpeed() + ship[i].getyAcceleration());

                ship[i].setxLoc(ship[i].getxLoc() + /*AstroidsWindow.timerDelay * */ speedFactor * ship[i].getxSpeed());
                ship[i].setyLoc(ship[i].getyLoc() + /*AstroidsWindow.timerDelay * */ speedFactor * ship[i].getySpeed());
                
                /*
                if(org.apache.commons.math.util.FastMath.sqrt((ship[i].getxLoc() - ship[j].getxLoc()) * (ship[i].getxLoc() - ship[j].getxLoc()) + (ship[i].getyLoc() - ship[j].getyLoc()) * (ship[i].getyLoc() - ship[j].getyLoc())) < ship[i].getSize() + ship[j].getSize()){
                    //i.e. If the distance between the 2 ships is less than the sum of their radii
                    //Then there is a collision
                    //elastically bounce
                    //
                    //gAngle is the angle of the line between the 2 ships.
                    //Write the velocities of the ships in the basis of gAngle and the perpendicular angle.
                    //Do nothing to the component of velocity in the perpendicular direction
                    //Reverse (-1*) the component of velocity in the gAngle direction
                    //Apply to the horizontal-vertical basis
                    ship[i].bounce(gAngle - Math.PI/2);
                    ship[j].bounce(gAngle + Math.PI/2);
                    
                    ship[i].setxLoc(ship[i].getxLoc() + /*AstroidsWindow.timerDelay *  AstroidsWindow.speedFactor * ship[i].getxSpeed());
                    ship[i].setyLoc(ship[i].getyLoc() + /*AstroidsWindow.timerDelay *  AstroidsWindow.speedFactor * ship[i].getySpeed());
                }
                
            } else {
                ship[i].setFuel(ship[i].getFuel() + ship[i].getThrust());

                ship[i].setxAcceleration(ship[i].getxAcceleration() - ship[i].getThrust() * org.apache.commons.math.util.FastMath.cos(ship[i].getThrustAngle()));
                ship[i].setyAcceleration(ship[i].getyAcceleration() - ship[i].getThrust() * org.apache.commons.math.util.FastMath.sin(ship[i].getThrustAngle()));

                ship[i].setxSpeed(ship[i].getxSpeed() - ship[i].getxAcceleration());
                ship[i].setySpeed(ship[i].getySpeed() - ship[i].getyAcceleration());

                ship[i].setxLoc(ship[i].getxLoc() - /*AstroidsWindow.timerDelay *  AstroidsWindow.speedFactor * ship[i].getxSpeed());
                ship[i].setyLoc(ship[i].getyLoc() - /*AstroidsWindow.timerDelay *  AstroidsWindow.speedFactor * ship[i].getySpeed());
                
            }
                */

/*            fuelColor = fuel * 255 / 10;
            if (fuelColor > 255) {
                fuelColor = 255;
            } else if (fuelColor < 0) {
                fuelColor = 0;
            }*/
        //color = new Color(255 - (int)fuelColor,(int)fuelColor,0);

        //xAcceleration += thrust * org.apache.commons.math.util.FastMath.cos(thrustAngle);
            //yAcceleration += thrust * org.apache.commons.math.util.FastMath.sin(thrustAngle);
            //xSpeed += AstroidsWindow.forwards*xAcceleration;
            //ySpeed += AstroidsWindow.forwards*yAcceleration;
            ship[i].setxAcceleration(0);// g reaccumualted in PhysiscsSpace.applyForces()
            ship[i].setyAcceleration(0);
        }
        for (i = 0; i < numPlanets; i++) {
            planet[i].move();
        }
    }
    
    private void applyForces(){
        for(i=0; i<numShips; i++){
            for(j=i+1; j<numShips; j++){// ship-ship interactions
                if(org.apache.commons.math.util.FastMath.sqrt((ship[i].getxLoc() - ship[j].getxLoc()) * (ship[i].getxLoc() - ship[j].getxLoc()) + (ship[i].getyLoc() - ship[j].getyLoc()) * (ship[i].getyLoc() - ship[j].getyLoc())) < ship[i].getSize() + ship[j].getSize()){
                    //i.e. If the distance between the 2 ships is less than the sum of their radii
                    //Then there is a collision
                    //elastically bounce
                    //
                    //gAngle is the angle of the line between the 2 ships.
                    //Write the velocities of the ships in the basis of gAngle and the perpendicular angle.
                    //Do nothing to the component of velocity in the perpendicular direction
                    //Reverse (-1*) the component of velocity in the gAngle direction
                    //Apply to the horizontal-vertical basis
                    ship[i].bounce(gAngle - Math.PI/2);
                    ship[j].bounce(gAngle + Math.PI/2);
                }
                // force from ship[j] acting on ship[i]:
                g = G * /*AstroidsWindow.forwards*-1.0**/ship[j].getMass()/((ship[j].getxLoc()-ship[i].getxLoc())*(ship[j].getxLoc()-ship[i].getxLoc())+(ship[j].getyLoc()-ship[i].getyLoc())*(ship[j].getyLoc()-ship[i].getyLoc()));
                gAngle = org.apache.commons.math.util.FastMath.atan2(ship[j].getyLoc()-ship[i].getyLoc(), ship[j].getxLoc()-ship[i].getxLoc());
                ship[i].setxAcceleration(ship[i].getxAcceleration() + g * org.apache.commons.math.util.FastMath.cos(gAngle));
                ship[i].setyAcceleration(ship[i].getyAcceleration() + g * org.apache.commons.math.util.FastMath.sin(gAngle));
                // force from ship[i] acting on ship[j]:
                g = /*AstroidsWindow.forwards**/-1.0 * g / ship[j].getMass() * ship[i].getMass();
                ship[j].setxAcceleration(ship[j].getxAcceleration() + g * org.apache.commons.math.util.FastMath.cos(gAngle));
                ship[j].setyAcceleration(ship[j].getyAcceleration() + g * org.apache.commons.math.util.FastMath.sin(gAngle));

            }
            for(j=0; j<numStars; j++){// ship-star interactions
                if(org.apache.commons.math.util.FastMath.sqrt((ship[i].getxLoc() - star[j].getxLoc()) * (ship[i].getxLoc() - star[j].getxLoc()) + (ship[i].getyLoc() - star[j].getyLoc()) * (ship[i].getyLoc() - star[j].getyLoc())) < ship[i].getSize() + star[j].getSize()){
                    //i.e. If the distance between the ship and the star is less than the sum of their radii
                    //Then there is a collision with the ship and the star
                    ship[i].bounce(gAngle + Math.PI/2);
                    //game over for Ship[i]
                    //TODO: make gameOver() method
                }
                g = G * /*AstroidsWindow.forwards*-1.0**/star[j].getMass()/((star[j].getxLoc()-ship[i].getxLoc())*(star[j].getxLoc()-ship[i].getxLoc())+(star[j].getyLoc()-ship[i].getyLoc())*(star[j].getyLoc()-ship[i].getyLoc()));
                gAngle = org.apache.commons.math.util.FastMath.atan2(star[j].getyLoc()-ship[i].getyLoc(), star[j].getxLoc()-ship[i].getxLoc());
                ship[i].setxAcceleration(ship[i].getxAcceleration() + g * org.apache.commons.math.util.FastMath.cos(gAngle));
                ship[i].setyAcceleration(ship[i].getyAcceleration() + g * org.apache.commons.math.util.FastMath.sin(gAngle));

            }
            for(j=0; j<numPlanets; j++){// ship-planet interactions
                // force from planet acting on ship:
                g = G * /*AstroidsWindow.forwards*-1.0**/planet[j].getMass()/((planet[j].getxLoc()-ship[i].getxLoc())*(planet[j].getxLoc()-ship[i].getxLoc())+(planet[j].getyLoc()-ship[i].getyLoc())*(planet[j].getyLoc()-ship[i].getyLoc()));
                gAngle = org.apache.commons.math.util.FastMath.atan2(planet[j].getyLoc()-ship[i].getyLoc(), planet[j].getxLoc()-ship[i].getxLoc());
                ship[i].setxAcceleration(ship[i].getxAcceleration() + g * org.apache.commons.math.util.FastMath.cos(gAngle));
                ship[i].setyAcceleration(ship[i].getyAcceleration() + g * org.apache.commons.math.util.FastMath.sin(gAngle));
                // force from ship acting on planet:
                g = /*AstroidsWindow.forwards**/-1.0 * g / planet[j].getMass() * ship[i].getMass();
                planet[j].setxAcceleration(planet[j].getxAcceleration() + g * org.apache.commons.math.util.FastMath.cos(gAngle));
                planet[j].setyAcceleration(planet[j].getyAcceleration() + g * org.apache.commons.math.util.FastMath.sin(gAngle));
            }
        }
        for(i=0; i<numPlanets; i++){// planet-planet interactions
            for(j=i+1; j<numPlanets; j++){
                // force from planet[j] acting on planet[i]:
                //currentPairDistance = distance(planet[i].getxLoc(), planet[i].getyLoc(), planet[j].getxLoc(), planet[j].getyLoc());
                g = G * /*AstroidsWindow.forwards*(-1.0)**/planet[j].getMass()/*(currentPairDistance*currentPairDistance);*/ / ((planet[j].getxLoc()-planet[i].getxLoc())*(planet[j].getxLoc()-planet[i].getxLoc())+(planet[j].getyLoc()-planet[i].getyLoc())*(planet[j].getyLoc()-planet[i].getyLoc()));
                gAngle = org.apache.commons.math.util.FastMath.atan2(planet[j].getyLoc()-planet[i].getyLoc(), planet[j].getxLoc()-planet[i].getxLoc());
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
                g = /*AstroidsWindow.forwards**/-1.0 * g / planet[j].getMass() * planet[i].getMass();
                planet[j].setxAcceleration(planet[j].getxAcceleration() + g * org.apache.commons.math.util.FastMath.cos(gAngle));
                planet[j].setyAcceleration(planet[j].getyAcceleration() + g * org.apache.commons.math.util.FastMath.sin(gAngle));
          //      previousAngle[i][j] = gAngle;
            }
            for(j=0; j<numStars; j++){// planet-star interactions
                g = G * /*AstroidsWindow.forwards*-1.0**/star[j].getMass()/((star[j].getxLoc()-planet[i].getxLoc())*(star[j].getxLoc()-planet[i].getxLoc())+(star[j].getyLoc()-planet[i].getyLoc())*(star[j].getyLoc()-planet[i].getyLoc()));
                gAngle = org.apache.commons.math.util.FastMath.atan2(star[j].getyLoc()-planet[i].getyLoc(), star[j].getxLoc()-planet[i].getxLoc());
                planet[i].setxAcceleration(planet[i].getxAcceleration() + g * org.apache.commons.math.util.FastMath.cos(gAngle));
                planet[i].setyAcceleration(planet[i].getyAcceleration() + g * org.apache.commons.math.util.FastMath.sin(gAngle));
            }
        }
        for(i=0; i<numShips; i++){// ship-coin interactions
            for(j=0; j<numCoins; j++){
                if(org.apache.commons.math.util.FastMath.sqrt((ship[i].getxLoc() - coin[j].getxLoc()) * (ship[i].getxLoc() - coin[j].getxLoc()) + (ship[i].getyLoc() - coin[j].getyLoc()) * (ship[i].getyLoc() - coin[j].getyLoc())) < ship[i].getSize() + coin[j].getSize()){
                    ship[i].setFuel(ship[i].getFuel() + 1);
                    coin[j] = new Coin(arenaWidth, arenaHeight);
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
