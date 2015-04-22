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
 
public class AsteroidsServerThread extends Thread {
 
    protected DatagramSocket socket = null;
    protected BufferedReader in = null;
    protected boolean moreQuotes = true;
 
    public AsteroidsServerThread() throws IOException {
        this("AstroidsServerThread");
    }
 
    public AsteroidsServerThread(String name) throws IOException {
        super(name);
        socket = new DatagramSocket(4445);
 
    }
 
    public void run() {
 
        boolean runningThread = true;
        while (runningThread) {
            try {
                byte[] buf = new byte[256];
 
                // receive request
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
 
                // figure out response
 
                // send the response to the client at "address" and "port"
                InetAddress address = packet.getAddress();
                int port = packet.getPort();
                packet = new DatagramPacket(buf, buf.length, address, port);
                socket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
                runningThread = false;
            }
        }
        socket.close();
    }
 }
