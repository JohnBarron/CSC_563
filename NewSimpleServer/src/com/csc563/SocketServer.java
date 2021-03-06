package com.csc563;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.*;
import java.util.*;

public class SocketServer {
    public final static int DEFAULT_PORT = 4446;
    public int port;
    public Socket s;
    public ArrayList ConnectedClients = new ArrayList<>();
    public static int arenaHeight;
    public static int arenaWidth;
    public static final double speedFactor = 16;
    public int numCoins;
    public int numStars;
    public int numPlanets;
    
    private PhysicsSpace s1 = null;
	
    public SocketServer(){
        this(DEFAULT_PORT);
    }
	
    public SocketServer(int port){ 
        this.port = port;
    }
	
    public void startServer(){
        try 
        {
            readConfigFile();
            ServerSocket server = new ServerSocket(this.port);
            System.out.println("Server has been started...");

            while (true)
            {												
                s = server.accept();

                System.out.println("Client has attempted a connection from " + s.getLocalAddress().getHostName());

                Client client = new Client(this);
                Thread thread = new Thread(client);

                thread.start();
            }
        } 
        catch (Exception e) 
        {
            System.out.println("An error occured.");//IF AN ERROR OCCURED THEN PRINT IT
            e.printStackTrace();
        }
    }
	
    public void SendOutputToAll(String output) {
        for(Object cl : this.ConnectedClients){
            Client c = (Client)cl;
            c.out.println(output);
            c.out.flush();
        }
    }
	
    public void createPhysicsSpace() {
        s1 = new PhysicsSpace(ConnectedClients, numStars, numPlanets, numCoins, arenaWidth, arenaHeight, speedFactor);
        int i = 0;
        String output = "";
        String temp = "";
        for(Object cl : this.ConnectedClients){
            Client c = (Client)cl;
            int xLoc = (int)c.ship.getxLoc();
            int yLoc = (int)c.ship.getyLoc();
            if(i == 0) {
                temp = "5|" + c.playerName + "|" + c.ship.colorRandom + "|" +
                        xLoc + "|" + yLoc + "|" + c.ship.coinsCollected;
            }
            else {
                temp = ",5|" + c.playerName + "|" + c.ship.colorRandom + "|" +
                        xLoc + "|" + yLoc + "|" + c.ship.coinsCollected;
            }
            output += temp;
            i++;
            System.out.println(output);
        }
        SendOutputToAll(output);
        output = "";
        for(i=0; i < numCoins; i++){
            int xLoc = (int)s1.getCoin()[i].getxLoc();
            int yLoc = (int)s1.getCoin()[i].getyLoc();
            if(i == 0) {
                temp = "6|" + s1.getCoin()[i].getColor().getRGB() + "|" + 
                        xLoc + "|" + yLoc;
            }
            else {
                temp = ",6|" + s1.getCoin()[i].getColor().getRGB() + "|" + 
                        xLoc + "|" + yLoc;
            }
            output += temp;
            i++;
        }
        System.out.println(output);
        SendOutputToAll(output);

    }
    
    public void checkForCollision(Ship ship) {
        for(int j=0; j<numCoins; j++){
            if(org.apache.commons.math.util.FastMath.sqrt((ship.getxLoc() - s1.getCoin()[j].getxLoc()) * (ship.getxLoc() - s1.getCoin()[j].getxLoc()) + (ship.getyLoc() - s1.getCoin()[j].getyLoc()) * (ship.getyLoc() - s1.getCoin()[j].getyLoc())) < ship.getSize() + s1.getCoin()[j].getSize()){
                //ship[i].setFuel(ship[i].getFuel() + 1);
                ship.coinsCollected++;
                s1.getCoin()[j] = new Coin(arenaWidth, arenaHeight);
            }
        }
        String output = "";
        String temp = "";
        for(int i=0; i < numCoins; i++){
            int xLoc = (int)s1.getCoin()[i].getxLoc();
            int yLoc = (int)s1.getCoin()[i].getyLoc();
            if(i == 0) {
                temp = "6|" + s1.getCoin()[i].getColor().getRGB() + "|" + 
                        xLoc + "|" + yLoc;
            }
            else {
                temp = ",6|" + s1.getCoin()[i].getColor().getRGB() + "|" + 
                        xLoc + "|" + yLoc;
            }
            output += temp;
            i++;
        }
        System.out.println(output);
        SendOutputToAll(output);
    }
    
    public void readConfigFile() {
        File configFile = new File("config.properties");

        try {
            FileReader reader = new FileReader(configFile);
            Properties props = new Properties();
            props.load(reader);

            numStars = new Integer(props.getProperty("Stars"));
            numPlanets = new Integer(props.getProperty("Planets"));
            numCoins = new Integer(props.getProperty("Coins"));

            System.out.println("numstars: " + numStars);
            System.out.println("numPlanets: " + numPlanets);
            System.out.println("numCoins: " + numCoins);
            reader.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Config file not found.");
            // file does not exist
        } catch (IOException ex) {
            System.out.println("you have a reading error");
            // I/O error
        }
    }
}

