import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.InputStream;
import java.io.OutputStream;
//import java.net.DatagramPacket;
//import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Timer;

public class SimpleGameServer {
    private static ServerSocket ss;
    //private static DatagramSocket UDPsoc;
    private static final int PORT = 4446;
    //private static final int UDPport = 4447;
    private static ArrayList<ClientHandler> clientHandler = new ArrayList<>(6);
    private static boolean allReady = false;
    
    private static javax.swing.Timer timer , secTimer;
    private static int timerDelay = 32, secTimerDelay = 1000;
    private static volatile int elapsedSeconds = 0;
    
    //private static DatagramPacket datagramOut;
    private static byte[] outByte = new byte[1], inByte = new byte[1];
    
    
        static ActionListener forceTimer = new ActionListener() {
            public void actionPerformed(ActionEvent e){
                for(ClientHandler clienti : clientHandler){//for all the players
                    //send all the locations of things
                    for (ClientHandler clientj : clientHandler) {//for all the ships
                        try{
                            outByte[0] = (byte) clientj.xLoc;
                            clienti.outStream.write(outByte);
                            clienti.outStream.flush();
                            outByte[0] = (byte) clientj.yLoc;
                            clienti.outStream.write(outByte);
                            clienti.outStream.flush();
                        }catch (IOException ex){
                            System.out.println(ex);
                        }
                    }
                    /*
                    for (ClientHandler clientj : clientHandler) {
                        try {
                            outByte[0] = (byte) clienti.xLoc;
                            clienti.outStream.write(outByte);
                            clienti.outStream.flush();
                            outByte[0] = (byte) clienti.yLoc;
                            clienti.outStream.write(outByte);
                            clienti.outStream.flush();
                            outByte[0] = (byte) clientj.xLoc;
                            clienti.outStream.write(outByte);
                            clienti.outStream.flush();
                            outByte[0] = (byte) clientj.yLoc;
                            clienti.outStream.write(outByte);
                            clienti.outStream.flush();
                        } catch (IOException ex) {
                            System.out.println(ex);
                        }
                    }*/
                }
            }
        };
        
        static ActionListener secClock = new ActionListener() {
            public void actionPerformed(ActionEvent e){
                //put 60 - elapsedSeconds as a byte in the datagram
                //outByte[0] = new Integer(60 - elapsedSeconds).byteValue();
                //for(ClientHandler clienti : clientHandler){
                //    clienti.out.println(new Integer(60 - elapsedSeconds).toString());
                //}
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
        private OutputStream outStream;
        private BufferedReader in;
        private PrintWriter out;
        private boolean ready;
        private int gameInput;
        
        private int xLoc = 100, yLoc = 100;

        public ClientHandler(Socket socket, int numPlayers) throws SocketException {
            this.socket = socket;
            this.socket.setTcpNoDelay(true);
            playerNum = numPlayers;
            ready = false;
            if(playerNum == 2){
                xLoc = 200;
                yLoc = 200;
            }
            //datagramIn = new DatagramPacket(inByte, inByte.length);
        }

        public void run() {
            String input;
            
            try {
                inStream = socket.getInputStream();
                in = new BufferedReader(new InputStreamReader(inStream));
                outStream = socket.getOutputStream();
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
                            clienti.out.flush();
                        }
                        allReady = allReady && clienti.ready;
                    }
                    if (allReady) {
                        for (ClientHandler clienti : clientHandler) {
                            clienti.out.println("StartGame");
                            clienti.out.flush();
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
                    //System.out.println(this.playerNum + " is in the game loop.");
                    gameInput = inStream.read();
                    System.out.println(gameInput);
                    switch(gameInput){
                        case 0://a
                            xLoc--;
                            break;
                        case 1://s
                            yLoc++;
                            break;
                        case 2://w
                            yLoc--;
                            break;
                        case 3://d
                            xLoc++;
                            break;
                        default:
                            break;
                    }
                    //elapsedSeconds += 10;
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
