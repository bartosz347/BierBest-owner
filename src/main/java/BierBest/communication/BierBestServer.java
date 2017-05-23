package BierBest.communication;

import javax.net.ssl.SSLServerSocketFactory;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class BierBestServer {

    public static final int PORT = 4488;

    public static void main(String[] args) {

        // PKCS#12 file containing certificate and private key
        // setup here
            //System.setProperty("javax.net.ssl.keyStore", "...");
            //System.setProperty("javax.net.ssl.keyStorePassword", "...");
        // or in VM options
            //-Djavax.net.ssl.keyStorePassword="..." -Djavax.net.ssl.keyStore="..."

        System.out.println("starting server");

        try {
            //ServerSocket serverSocket = new ServerSocket(PORT); // without SSL
            ServerSocket serverSocket = SSLServerSocketFactory.getDefault().createServerSocket(PORT);

            Socket clientSocket = serverSocket.accept();

            ObjectOutputStream outToClient = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream inFromClient = new ObjectInputStream(clientSocket.getInputStream());

            Message incoming = (Message) inFromClient.readObject();
            switch (incoming.payloadType) {
                case TEST:
                    System.out.println(((TestPayload) incoming.payload).testData);
                    break;
                default:
                    throw new RuntimeException("unsupported payload type");
            }

            clientSocket.close();
            serverSocket.close();


        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
