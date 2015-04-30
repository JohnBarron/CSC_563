import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class SimpleGameServer {
    private static final int PORT = 4446;
    private static ArrayList<ClientHandler> clientHandler = new ArrayList<>(3);
    private static boolean allReady = false;
    
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
        private BufferedReader in;
        private PrintWriter out;
        private boolean ready;

        public ClientHandler(Socket socket, int numPlayers) {
            this.socket = socket;
            playerNum = numPlayers;
            ready = false;
        }

        public void run() {
            String input;
            
            try {
                in = new BufferedReader(new InputStreamReader(
                        socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                
                while (!allReady) {
                    input = in.readLine();
                    if (input == null) {
                        return;
                    }
                    ready = input.equals("R");
                    allReady = false;
                    for (ClientHandler clienti : clientHandler) {
                        clienti.out.println(clientHandler.size());
                        for (ClientHandler clientj : clientHandler) {
                            clienti.out.println(clientj.playerNum + " " + clientj.ready);
                        }
                        allReady = allReady && clienti.ready;
                    }
                    if (allReady) {
                        //start the game
                        gameLoop();
                    }
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
        private void gameLoop(){
            
        }
    }
}