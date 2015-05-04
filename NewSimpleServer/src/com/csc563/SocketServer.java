package com.csc563;

import java.net.*;
import java.util.*;

public class SocketServer {
    public final static int DEFAULT_PORT = 4446;
    public int port;
    public Socket s;
    public ArrayList ConnectedClients = new ArrayList<>();
    public static final int arenaHeight = 1366;
    public static final int arenaWidth = 768;
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
        s1 = new PhysicsSpace(ConnectedClients.size(), 0, 0, 100, arenaWidth, arenaHeight, speedFactor);
        int i = 0;
        for(Object cl : this.ConnectedClients){
            Client c = (Client)cl;
            String output="5|" + c.playerName + "|" + s1.getShip()[i].colorRandom + "|" +
                    s1.getShip()[i].getxLoc() + "|" + s1.getShip()[i].getyLoc() + "|0";
            c.out.println(output);
            c.out.flush();
        }
    }
}

