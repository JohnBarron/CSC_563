package csc563;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Renu
 */

import java.io.*;
import java.net.*;
import java.util.*;

public class MulticastAsteroidsClient {

    public static void main(String[] args) throws IOException {

        MulticastSocket socket = new MulticastSocket(4446);
        InetAddress address = InetAddress.getByName("230.0.0.1");
	socket.joinGroup(address);

        DatagramPacket packet;
    
        boolean runningThread = true;
        while (runningThread) {
            try {
                if(send) {
                    //do something here to send data to the server
                }
                
                if(receive) {
                // receive it
                byte[] buf = new byte[256];
                packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                }
 
            } catch (IOException e) {
                e.printStackTrace();
                runningThread = false;
            }
            
        }
	socket.leaveGroup(address);
	socket.close();
    }

}