import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.InputStream;
//import java.net.DatagramPacket;
//import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.*;
import javax.swing.Timer;

public class SimpleGameServer {
    private static ServerSocket ss;
    //private static DatagramSocket UDPsoc;
    private static final int PORT = 4446;
    //private static final int UDPport = 4447;
    private static ArrayList<ClientHandler> clientHandler = new ArrayList<>(6);
    private static boolean allReady = false;
    
    private static javax.swing.Timer timer , secTimer;
    private static int timerDelay = 16, secTimerDelay = 1000;
    private static volatile int elapsedSeconds = 0;
    
    //private static DatagramPacket datagramOut;
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
                    clienti.out.println(new Integer(60 - elapsedSeconds).toString());
                }
                elapsedSeconds++;
            }
        };
    
    public static void main(String[] args) throws Exception {
        System.out.println("The simple game server is running.");
        ss = new ServerSocket(PORT);
        //ss.setPerformancePreferences(0, 10, 1);//needs socket with no-arg constructor
        //UDPsoc = new DatagramSocket(UDPport);
        //ss
        try {
            while (true) {
                clientHandler.add(new ClientHandler(ss.accept(), clientHandler.size()));
                clientHandler.get(clientHandler.size()-1).start();
                //UDPsoc.bind(clientHandler.get(clientHandler.size()-1).socket.getRemoteSocketAddress());
            }
        } finally {
            ss.close();
        }
    }
    
    private static class ClientHandler extends Thread {

        private int playerNum;
        private Socket socket;
        //private DatagramSocket UDPsoc;
        //private DatagramPacket datagramIn;
        private InetAddress clientIP;
        private InputStream inStream;
        private BufferedReader in;
        private PrintWriter out;
        private boolean ready;

        public ClientHandler(Socket socket, int numPlayers) throws SocketException {
            this.socket = socket;
            this.socket.setTcpNoDelay(true);
            playerNum = numPlayers;
            ready = false;
            //datagramIn = new DatagramPacket(inByte, inByte.length);
        }

        public void run() {
            String input;
            
            try {
                inStream = socket.getInputStream();
                in = new BufferedReader(new InputStreamReader(inStream));
                out = new PrintWriter(socket.getOutputStream(), true);
                clientIP = socket.getInetAddress();
                
                timer = new Timer(timerDelay, forceTimer);
                secTimer = new Timer(secTimerDelay, secClock);
                
                while (!allReady) {
                    input = in.readLine();
                    if (input == null) {
                        return;
                    }
                    if(input.equals("GameStarted")){
                        allReady = true;
                        break;
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
                    if (allReady) {
                        for (ClientHandler clienti : clientHandler) {
                            clienti.out.println("StartGame");
                            //this ClientHandler is starting the game, but all the others are waiting for a line "GameStarted". I guess all the clients should send "GameStarted" except the client bound to this ClientHandler.
                        }
                        in.readLine();//throw out extra "GameStarted"
                        //this.out.println("StartGame");
                        //start the game
                        //timer.start();
                    }
                }
                //in.close();
                //out.close();
                //socket.close();
                //UDPsoc = new DatagramSocket(UDPport, clientIP);
                //game has started
                if(!timer.isRunning()){
                    timer.start();
                }
                if(!secTimer.isRunning()){
                    secTimer.start();
                }
                while(allReady){
                    //UDPsoc.receive(datagramIn);
                    //in.readLine();//ignore input for now
                    System.out.println(this.playerNum + " is in the game loop.");
                    System.out.println(inStream.read());
                    elapsedSeconds += 10;
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