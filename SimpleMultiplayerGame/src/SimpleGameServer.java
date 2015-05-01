import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Timer;

public class SimpleGameServer {
    private static final int PORT = 4446;
    private static ArrayList<ClientHandler> clientHandler = new ArrayList<>(3);
    private static boolean allReady = false;
    
    private static javax.swing.Timer timer , secTimer;
    private static int timerDelay = 16, secTimerDelay = 1000;
    private static int elapsedSeconds = 0;
    
    private static DatagramPacket datagramOut;
    private static byte[] outByte = new byte[1], inByte = new byte[1];
    
    
        static ActionListener forceTimer = new ActionListener() {
            public void actionPerformed(ActionEvent e){
                
            }
        };
        
        static ActionListener secClock = new ActionListener() {
            public void actionPerformed(ActionEvent e){
                //put 60 - elapsedSeconds as a byte in the datagram
                outByte[0] = new Integer(60 - elapsedSeconds).byteValue();
                for(ClientHandler clienti : clientHandler){
                    datagramOut = new DatagramPacket(outByte, outByte.length, clienti.UDPsoc.getInetAddress(), PORT);
                    try {
                        clienti.UDPsoc.send(datagramOut);
                    } catch (IOException ex) {
                        System.out.println(ex);
                    }
                }
            }
        };
    
    public static void main(String[] args) throws Exception {
        System.out.println("The simple game server is running.");
        ServerSocket ss = new ServerSocket(PORT);
        try {
            while (true) {
                clientHandler.add(new ClientHandler(ss.accept(), clientHandler.size()));
                clientHandler.get(clientHandler.size()-1).start();
            }
        } finally {
            ss.close();
        }
    }
    
    private static class ClientHandler extends Thread {

        private int playerNum;
        private Socket socket;
        private DatagramSocket UDPsoc;
        private DatagramPacket datagramIn;
        private InetAddress clientIP;
        private BufferedReader in;
        private PrintWriter out;
        private boolean ready;

        public ClientHandler(Socket socket, int numPlayers) {
            this.socket = socket;
            playerNum = numPlayers;
            ready = false;
            datagramIn = new DatagramPacket(inByte, inByte.length);
        }

        public void run() {
            String input;
            
            try {
                in = new BufferedReader(new InputStreamReader(
                        socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                clientIP = socket.getInetAddress();
                
                timer = new Timer(timerDelay, forceTimer);
                secTimer = new Timer(secTimerDelay, secClock);
                
                while (!allReady) {
                    input = in.readLine();
                    if (input == null) {
                        return;
                    }
                    ready = input.equals("R");
                    allReady = true;
                    for (ClientHandler clienti : clientHandler) {
                        clienti.out.println(clientHandler.size());
                        for (ClientHandler clientj : clientHandler) {
                            clienti.out.println(clientj.playerNum + " " + clientj.ready);
                        }
                        allReady = allReady && clienti.ready;
                    }
                    //if (allReady) {
                    //    //start the game
                    //    timer.start();
                    //}
                }
                in.close();
                out.close();
                socket.close();
                UDPsoc = new DatagramSocket(PORT, clientIP);
                //game has started
                if(!timer.isRunning()){
                    timer.start();
                }
                if(!secTimer.isRunning()){
                    secTimer.start();
                }
                while(allReady){
                    UDPsoc.receive(datagramIn);
                }
            } catch (IOException e) {
                System.out.println(e);
            } finally {
                try {
                    socket.close();
                    clientHandler.remove(this);
                } catch (IOException e) {
                }
            }
        }
    }
}