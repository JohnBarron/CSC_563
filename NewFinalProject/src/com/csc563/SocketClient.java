package com.csc563;

import com.csc563.AstroidsWindow;
import java.io.*;
import java.net.*;
import java.util.Date;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.util.Scanner;

public class SocketClient implements Runnable {

    public int port;
    public String serverAddr;
    public Socket socket;
    public AstroidsWindow ui;
    BufferedReader In;
    PrintWriter Out;
    private boolean keepRunning;

    public SocketClient(AstroidsWindow frame) throws IOException {
        ui = frame;
        this.serverAddr = ui.serverAddr;
        this.port = ui.port;
        socket = new Socket(InetAddress.getByName(serverAddr), port);
        In = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        Out = new PrintWriter(socket.getOutputStream(), true);
    }

    @Override
    public void run() {
        keepRunning = true;
        String message = "";
        String transID = "";
        while (keepRunning) {
            try {
                //System.out.println("waiting for input from server");
                message = In.readLine();
                String[] parts = message.split("\\|");
                //transID = message.substring(0, 1); //The first character of the message
                transID = parts[0];
                System.out.println("Incoming : " + message);

                switch (transID) {
                    //In the protocol, the cases of messages from the server to the normal client are 1, 4, 5, and 8.
                    //What if the user types a '|' in a message?
                    case "3":
                        ui.messageOnScreen = message;
                        ui.paint(ui.getGraphics());
                        break;
                    case "1":
                        break;
                    case "4":
                        ui.messageOnScreen = message;
                        ui.paint(ui.getGraphics());
                        break;
                    case "5":
                        break;
                    case "8":
                        break;
                    default:
                        break;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                ui.clientThread.stop();

            }
        }
    }

    public void send(String msg) {
        try {
            Out.println(msg);
            System.out.println("Outgoing : " + msg);

        } catch (Exception ex) {
            System.out.println("Exception SocketClient send()");
        }
    }
    
    public void stop(){
        keepRunning = false;
    }
    
    public boolean isRunning(){
        return keepRunning;
    }

    public void closeThread(Thread t) {
        t = null;
    }
}
