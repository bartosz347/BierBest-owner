package BierBest.communication;

import javax.net.ssl.SSLServerSocketFactory;
import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class BierBestServer extends Thread {

    // PKCS#12 file containing certificate and private key
    // setup here
    //System.setProperty("javax.net.ssl.keyStore", "...");
    //System.setProperty("javax.net.ssl.keyStorePassword", "...");
    // or in VM options
    //-Djavax.net.ssl.keyStorePassword="..." -Djavax.net.ssl.keyStore="..."

    public static final int PORT = 4488;

    RequestHandlingService requestHandler = new RequestHandlingService();

    public static void main(String[] args) {
        BierBestServer server = new BierBestServer();
        server.start();

    }

    @Override
    public void run() {
        this.setName("BierBestServerThread");
        this.startServer();
    }


    private void startServer() {
        System.out.println("starting server");

        try (
            //ServerSocket serverSocket = new ServerSocket(PORT); // without SSL
            ServerSocket serverSocket = SSLServerSocketFactory.getDefault().createServerSocket(PORT);

            Socket clientSocket = serverSocket.accept();

            ObjectOutputStream outToClient = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream inFromClient = new ObjectInputStream(clientSocket.getInputStream());
        ) {
            Message incomingMessage;
            Message messageToSend;
            while ((incomingMessage = (Message) inFromClient.readObject()) != null) {
                messageToSend = requestHandler.handleMessage(incomingMessage);
                if(messageToSend != null)
                    outToClient.writeObject(messageToSend);
            }

        } catch (EOFException eof) {
            System.out.printf("connection ended by client");
        }
          catch (Exception e) {
            System.out.println(e);
        }
    }
}
