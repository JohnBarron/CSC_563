/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csc563;

/**
 *
 * @author Renu
 */
import java.io.*;
import java.net.*;
import java.util.*;
 
public class MulticastAsteroidsServerThread extends AsteroidsServerThread {
 
    public MulticastAsteroidsServerThread() throws IOException {
        super("MulticastAstroidsServerThread");
    }
 
    public void run() {
        boolean runningThread = true, send = true, receive = true;
        while (runningThread) {
            try {
                byte[] buf = new byte[256];

                if(receive) {
                   //do some with the packets
                }
                
                if(send) {
                    // send it
                    InetAddress group = InetAddress.getByName("230.0.0.1");
                    DatagramPacket packet = new DatagramPacket(buf, buf.length, group, 4446);
                    socket.send(packet);
                }
 
            } catch (IOException e) {
                e.printStackTrace();
                runningThread = false;
            }
        }
        socket.close();
    }
}