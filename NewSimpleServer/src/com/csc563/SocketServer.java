package com.csc563;

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
	
    public String commandParser(int command, String argFromUser) {
        String output = "";

        switch(command) {
            case 2: 
                if(this.ConnectedClients.size() <= 1) {
                    
                }
                break;
            case 3: 
                break;
            case 7: 
                break;
            case 10: 
                break;
            case 11: 
                break;
            case 12:
                break;
        }

        return output;
    }
    
    public void createPhysicsSpace() {
        s1 = new PhysicsSpace(ConnectedClients, 0, 0, 100, arenaWidth, arenaHeight, speedFactor);
        int i = 0;
        String output = "";
        for(Object cl : this.ConnectedClients){
            Client c = (Client)cl;
            int xLoc = (int)c.ship.getxLoc();
            int yLoc = (int)c.ship.getyLoc();
            output = "5|" + c.playerName + "|" + c.ship.colorRandom + "|" +
                    xLoc + "|" + yLoc + "|0";
            System.out.println(output);
            for(Object c2 : this.ConnectedClients){
                Client client = (Client)c2;
                client.out.println(output);
                client.out.flush();
            }
        }
    }
}

