package BierBest.communication;

import javassist.bytecode.stackmap.TypeData;

import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BierBestServerThreadForClient extends Thread {

    private static final Logger LOGGER = Logger.getLogger( TypeData.ClassName.class.getName() );
    private Socket clientSocket;

    public BierBestServerThreadForClient(String name, Socket clientSocket) {
        super(name);
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        LOGGER.log(Level.INFO, "starting new thread for a client from "+clientSocket.getInetAddress());

        try (
                ObjectOutputStream outToClient = new ObjectOutputStream(clientSocket.getOutputStream());
                ObjectInputStream inFromClient = new ObjectInputStream(clientSocket.getInputStream());
        ) {
            Message incomingMessage;
            Message messageToSend;
            while ((incomingMessage = (Message) inFromClient.readObject()) != null) {
                messageToSend = RequestHandlingService.getInstance().handleMessage(incomingMessage);
                if (messageToSend != null)
                    outToClient.writeObject(messageToSend);
            }

        } catch (EOFException eof) {
            System.out.printf("connection ended by client");
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
