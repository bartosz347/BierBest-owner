package BierBest.communication;

import BierBest.MainApp;
import javassist.bytecode.stackmap.TypeData;

import javax.net.ssl.SSLServerSocketFactory;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;


public class BierBestServer extends Thread {

    // PKCS#12 file containing certificate and private key
    // setup here
        //System.setProperty("javax.net.ssl.keyStore", "...");
        //System.setProperty("javax.net.ssl.keyStorePassword", "...");
    // or in VM options
        //-Djavax.net.ssl.keyStorePassword="..." -Djavax.net.ssl.keyStore="..."

    private static final Logger LOGGER = Logger.getLogger( TypeData.ClassName.class.getName() );
    public static final int PORT = 4488;


    @Override
    public void run() {
        this.setName("BierBestServerThread");
        this.startServer();
    }

    private void startServer() {
        RequestHandlingService requestHandlingService = new RequestHandlingService(MainApp.sessionFactory);
        LOGGER.log( Level.INFO, "starting server" );

        try (
                //ServerSocket serverSocket = new ServerSocket(PORT); // without SSL
                ServerSocket serverSocket = SSLServerSocketFactory.getDefault().createServerSocket(PORT);
        ) {
            Socket clientSocket;
            while ((clientSocket = serverSocket.accept()) != null) {
                BierBestServerThreadForClient srv = new BierBestServerThreadForClient("scktForClient", clientSocket, requestHandlingService);
                srv.start();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
