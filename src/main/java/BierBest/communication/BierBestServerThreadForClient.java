package BierBest.communication;

import javassist.bytecode.stackmap.TypeData;

import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BierBestServerThreadForClient extends Thread {

    private RequestHandlingService requestHandlingService;
    private static final Logger LOGGER = Logger.getLogger( TypeData.ClassName.class.getName() );
    private Socket clientSocket;

    public BierBestServerThreadForClient(String name, Socket clientSocket, RequestHandlingService requestHandlingService) {
        super(name);
        this.clientSocket = clientSocket;
        this.requestHandlingService = requestHandlingService;
    }

    @Override
    public void run() {
        LOGGER.log(Level.INFO, "starting new thread for a client from "+clientSocket.getInetAddress());

        try (
                ObjectOutputStream outToClient = new ObjectOutputStream(clientSocket.getOutputStream());
                ObjectInputStream inFromClient = new ObjectInputStream(clientSocket.getInputStream());
        ) {
            Request incomingRequest;
            Response responseToSend;
            while ((incomingRequest = (Request) inFromClient.readObject()) != null) {
                responseToSend = requestHandlingService.handleRequest(incomingRequest);
                if (responseToSend != null)
                    outToClient.writeObject(responseToSend);
            }

        } catch (EOFException eof) {
            LOGGER.log( Level.INFO, "connection ended by client" );
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
