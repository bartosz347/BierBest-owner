package BierBest.communication;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class BierBestClient {

    public static final int PORT = 4488;
    public static final String SERVER_ADDRESS = "127.0.0.1";

    public static void main(String[] args) {
        System.out.printf("starting client");
        try {
            Socket client = new Socket(SERVER_ADDRESS, PORT);

            System.out.println("connected to " + client.getRemoteSocketAddress());
            ObjectOutputStream outToServer = new ObjectOutputStream(client.getOutputStream());
            ObjectInputStream inFromClient = new ObjectInputStream(client.getInputStream());

            outToServer.writeObject(new Message());

            client.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
