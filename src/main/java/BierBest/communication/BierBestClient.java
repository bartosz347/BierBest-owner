package BierBest.communication;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

public class BierBestClient {

    public static final int PORT = 4488;
    public static final String SERVER_ADDRESS = "127.0.0.1";

    public static void main(String[] args) throws NoSuchAlgorithmException {

        System.out.println("starting client");

        try {

            // Dumb trust manager that does not verify if we trust certificate's CA
            final TrustManager[] trustAllCertificates = new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(final X509Certificate[] chain, final String authType) {
                }

                @Override
                public void checkServerTrusted(final X509Certificate[] chain, final String authType) {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            }};

            SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(null, trustAllCertificates, null);


            //Socket client = new Socket(SERVER_ADDRESS, PORT); // without SSL
            //Socket client = SSLSocketFactory.getDefault().createSocket(SERVER_ADDRESS, PORT);
            Socket client = sslContext.getSocketFactory().createSocket(SERVER_ADDRESS, PORT);

            System.out.println("connected to " + client.getRemoteSocketAddress());
            ObjectOutputStream outToServer = null;
            try {
                outToServer = new ObjectOutputStream(client.getOutputStream());
            } catch (Exception e1) {

            }
            ObjectInputStream inFromClient = new ObjectInputStream(client.getInputStream());

            outToServer.writeObject(new Message());

            client.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
