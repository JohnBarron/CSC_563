package com.csc563;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;


public class Client implements Runnable{

    public Socket socket;
    public SocketServer server;
    public boolean validated = false;
    public int clientType; // 0 = regular user, 1 = admin
    public PrintWriter out;
    public boolean readyState;
    public String playerName;
    public Ship ship;
	
    public Client(SocketServer s)
    {
        socket = s.s;
        server = s;
        try {
            out = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
    @Override
    public void run() 
    {
        try
        {

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while (true)
            {		
                String input = "";
                try  {
                    input = in.readLine();
                } catch(SocketException se) {
                    this.socket.close();
                    CheckConnectionAlive();
                }

                if(!input.contains("|")){
                    out.println("1|Please send a proper command.");
                    out.flush();
                    continue;
                }
    
                String[] parts = input.split("\\|");
                int command = Integer.parseInt(parts[0]);

                // if not validated, then only command is login
                // if don't successfully login, kick them off
                System.out.println("Client Sent -> " + input);
                String[] splitter = input.split("\\|");
                String output = "";

                // This is where we switch based on command and check permissions and decide whether to send output to all
                // If user has permissions, call the appropriate function, if not, stop them
                switch(command) {
                    case 2: // Ready State
                        if(!readyState) {
                            this.readyState = true;
                            this.playerName = splitter[1];
                            server.arenaWidth = new Integer(splitter[2]);
                            server.arenaHeight = new Integer(splitter[3]);
                            server.ConnectedClients.add(this);
                            System.out.println("Client is in ready state.");
                        }
                        if(server.ConnectedClients.size() == 1) {
                            output = "3|Wait for other user to join.";
                            server.SendOutputToAll(output);
//                            out.println(output);
                        } else {
                            output = "4";
                            for (Object cl : server.ConnectedClients) {
                                Client c = (Client) cl;
                                output += "|" + c.playerName;
                            }
                            server.SendOutputToAll(output);
                            server.createPhysicsSpace();
                        }
                        break;
                    case 3: // Post comment
                        out.println("1|Cannot post a comment as another user");
                        out.flush();
                        break;
                    case 7:  // Log out
                        out.println("1|You cannot log out another user.");
                        out.flush();
                        break;
                    case 10: // Add New User
                        out.println("1|Not authorized to add a new user");
                        out.flush();
                        break;
                    default:
                        out.println("1|Error on command syntax [" + input +"]");
                        out.flush();
                        break;
                }

                System.out.println(output + " <- has been sent to the client.");
            }

        } catch(IOException e) {
            e.printStackTrace();
        }
    } 
	
    public void CheckConnectionAlive() throws IOException{
        ArrayList clientsToRemove = new ArrayList<Client>();
	  
        for (Object cl : server.ConnectedClients) {
            Client c = (Client) cl;
            
            if(c.out.checkError()){
            	c.validated = false;
            	clientsToRemove.add(c);
            	c.socket.close();
            }
        }
        
        for(Object cl : clientsToRemove){
            Client c = (Client) cl;
            server.ConnectedClients.remove(c);
        }
    }
	
    public void close(){
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
	}
	out.close();
    }
}
