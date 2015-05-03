import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.OutputStream;
import java.net.Socket;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
//import java.net.DatagramPacket;
//import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

public class SimpleGameClient extends Frame {
    private static final int PORT = 4446;
    //private static final int UDPport = 4447;
    private BufferedReader in;
    private PrintWriter out;
    private OutputStream outStream;
    private JFrame frame = new JFrame("SimpleGameClient");
    private Graphics dbg;
    private Image dbImage;
    private static final int width = 600, height = 600;
    
    //private JTextField textField = new JTextField(40);
    //private JTextArea messageArea = new JTextArea(8, 40);
    
    private boolean ready = false;
    private int numPlayers = 0;
    private ArrayList<String> playerStatus = new ArrayList<>(6);
    private String line = "";
    private String tempPlayerNum = "0";
    private String tempReady = "false";
    private String[] lineParts;
    private boolean gameStarted = false;
    //private DatagramSocket UDPSoc;
    //private DatagramPacket clientPacket, serverPacket;
    private InetAddress serverIP;
    private final byte[] aByte = new byte[] {0x00};
    private final byte[] sByte = new byte[] {0x01};
    private final byte[] wByte = new byte[] {0x02};
    private final byte[] dByte = new byte[] {0x03};
    private byte[] inByte = new byte[1];
    

    public SimpleGameClient() {
        super("Simple Game");
        setSize(600, 600);
        setLocation(0, 0);
        //setUndecorated(true);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        setVisible(true);
        //textField.setEditable(false);
        //messageArea.setEditable(true);
        //frame.getContentPane().add(textField, "North");
        //frame.getContentPane().add(new JScrollPane(messageArea), "Center");
        //frame.pack();
// Add Listeners
        addKeyListener(new keyListener());
        //textField.addActionListener(new ActionListener() {
        //    /**
        //     * Responds to pressing the enter key in the textfield by sending
        //     * the contents of the text field to the server. Then clear the text
        //     * area in preparation for the next message.
        //     */
        //    public void actionPerformed(ActionEvent e) {
        //        out.println(textField.getText());
        //        textField.setText("");
        //    }
        //});
        //serverPacket = new DatagramPacket(inByte, inByte.length);
    }

    /**
     * Prompt for and return the address of the server.
     */
    private String getServerAddress() {
        return JOptionPane.showInputDialog(
                frame,
                "Enter IP Address of the Server:",
                JOptionPane.QUESTION_MESSAGE);
    }

    /**
     * Prompt for and return the desired screen name.
     */
    private String getPlayerName() {
        return JOptionPane.showInputDialog(
                frame,
                "Choose a screen name:",
                "Screen name selection",
                JOptionPane.PLAIN_MESSAGE);
    }

    private void run() throws IOException {
// Make connection and initialize streams
        String serverAddress = getServerAddress();
        Socket socket = new Socket(serverAddress, PORT);
        serverIP = socket.getInetAddress();
        
        in = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));
        outStream = socket.getOutputStream();
        out = new PrintWriter(outStream, true);
        frame.setVisible(false);
        int i = 0;
        int numberOfPlayersReady = 0;
        while (!gameStarted) {
            line = in.readLine();
            if(line == null){
                break;
            }
            if(line.equals("StartGame")){
                gameStarted = true;
                break;
            }
            numPlayers = new Integer(line);
            playerStatus = new ArrayList<>(numPlayers);
            numberOfPlayersReady = 0;
            for(i = 0; i < numPlayers; i++){
                playerStatus.add(in.readLine());
                lineParts = playerStatus.get(i).split(" ");
                if(lineParts.length < 2){
                    break;
                }
                tempPlayerNum = lineParts[0];
                tempReady = lineParts[1];
                if(tempReady.equalsIgnoreCase("true")) {
                    numberOfPlayersReady++;
                }
            }
            
            if(/*numPlayers > 1 &&*/ numberOfPlayersReady == numPlayers) {
                gameStarted = true;
            }
            repaint();
        }
        //in.close();
        //out.close();
        //socket.close();
        //UDPSoc = new DatagramSocket(UDPport, serverIP);
        while(gameStarted){
            //UDPSoc.receive(serverPacket);
            line = in.readLine();
            repaint();
        }
// Process all messages from server, according to the protocol.
  /*      while (true) {
            String line = in.readLine();
            if (line.startsWith("SUBMITNAME")) {
                out.println(getName());
            } else if (line.startsWith("NAMEACCEPTED")) {
                textField.setEditable(true);
            } else if (line.startsWith("MESSAGE")) {
                messageArea.append(line.substring(8) + "\n");
            }
        }*/
    }

    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.BLACK);
        if (!gameStarted) {
            for(int i = 0; i < playerStatus.size();i++){
                lineParts = playerStatus.get(i).split(" ");
                tempPlayerNum = lineParts[0];
                tempReady = lineParts[1];
                g.drawString(playerStatus.get(i), 10, 150 + 15 * new Integer(tempPlayerNum));
            }
        }
        else {
            //g.drawString(new Byte(serverPacket.getData()[0]).toString(), width/2, width/2);
            g.drawString(line, width/2, width/2);
        }
    }
    public void update(Graphics g) {
        if (dbImage == null) {
            dbImage = createImage(width, height);
            dbg = dbImage.getGraphics();
        }
        dbg.setColor(Color.WHITE);
        dbg.fillRect(0, 0, width, height);
        paint(dbg);
        g.drawImage(dbImage, 0, 0, this);
    }
    
    public static void main(String[] args) throws Exception {
        SimpleGameClient client = new SimpleGameClient();
        client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.frame.setVisible(true);
        client.run();
    }
    
    private class keyListener extends KeyAdapter {

        char c;

        public void keyPressed(KeyEvent e) {
            //assume that only 1 key is pressed at a time
            //TODO: optionally make it work with multiple keys pressed
            //      as a player would expect ex. 'w' and 'd' makes angle = PI/4
            c = e.getKeyChar();
            if(!gameStarted) {
                if (c == 'r') {
                    //try {
                        ready = !ready;
                        if (ready) {
                            out.println("R");
                        } else {
                            out.println("r");
                        }

                    //} catch (IOException ex) {
                     //   System.out.println(ex);
                    //}
                }
            }
            else {
                    try {
                        if (c == 'a') {
                            outStream.write(aByte);
                            //clientPacket = new DatagramPacket(aByte, aByte.length, UDPSoc.getInetAddress(), UDPport);
                            //UDPSoc.send(clientPacket);
                        }
                        else if (c == 's') {
                            outStream.write(sByte);
                            //clientPacket = new DatagramPacket(sByte, sByte.length, UDPSoc.getInetAddress(), UDPport);
                            //UDPSoc.send(clientPacket);
                        }
                        else if (c == 'w') {
                            outStream.write(wByte);
                            //clientPacket = new DatagramPacket(wByte, wByte.length, UDPSoc.getInetAddress(), UDPport);
                            //UDPSoc.send(clientPacket);
                        }
                        else if (c == 'd') {
                            outStream.write(dByte);
                            //clientPacket = new DatagramPacket(dByte, dByte.length, UDPSoc.getInetAddress(), UDPport);
                            //UDPSoc.send(clientPacket);
                        }
                    } catch (IOException ex) {
                        System.out.println(ex);
                    }
            }
        }
    }
}
