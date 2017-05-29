package bierbest.communication;

import javassist.bytecode.stackmap.TypeData;

import javax.net.ssl.SSLServerSocketFactory;
import javax.persistence.EntityManagerFactory;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;


public class BierBestServer extends Thread {
    private static final Logger LOGGER = Logger.getLogger(TypeData.ClassName.class.getName());
    private static int port;
    private ServerSocket serverSocket;
    private EntityManagerFactory sessionFactory;

    public BierBestServer(int port, EntityManagerFactory sessionFactory) {
        this.port = port;
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void run() {
        this.setName("BierBestServerThread");
        this.startServer();
    }

    public void close() {
        try {
            if (!serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            LOGGER.log(Level.INFO, "cannot stop server");
        }
    }

    private void startServer() {
        RequestHandlingService requestHandlingService = new RequestHandlingService(sessionFactory);
        LOGGER.log(Level.INFO, "starting server on port " + port);

        try (
                ServerSocket serverSocket = SSLServerSocketFactory.getDefault().createServerSocket(port);
        ) {
            Socket clientSocket;
            this.serverSocket = serverSocket;
            while ((clientSocket = serverSocket.accept()) != null) {
                BierBestServerThreadForClient srv = new BierBestServerThreadForClient("scktForClient", clientSocket, requestHandlingService);
                srv.start();
            }

        } catch (Exception e) {
            if (e.getMessage().equals("socket closed")) {
                LOGGER.log(Level.INFO, "server stopped");
            } else {
                LOGGER.log(Level.INFO, "server error");
            }
        }
    }
}
