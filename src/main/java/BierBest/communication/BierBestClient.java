package BierBest.communication;

import BierBest.communication.payloads.CommunicationCheck;
import BierBest.communication.payloads.MessageAction;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

public class BierBestClient {

    public static final int PORT = 4488;
    public static final String SERVER_ADDRESS = "127.0.0.1";

    public static void main(String[] args) throws NoSuchAlgorithmException, KeyManagementException {

        System.out.println("starting client");

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

        try (
            //Socket client = new Socket(SERVER_ADDRESS, PORT); // without SSL
            //Socket client = SSLSocketFactory.getDefault().createSocket(SERVER_ADDRESS, PORT);
            Socket client = sslContext.getSocketFactory().createSocket(SERVER_ADDRESS, PORT);
            ObjectOutputStream outToServer = new ObjectOutputStream(client.getOutputStream());
            ObjectInputStream inFromServer = new ObjectInputStream(client.getInputStream());

        ) {
            System.out.println("connected to " + client.getRemoteSocketAddress());

            List<Request> requests = new ArrayList<>();

            requests.add(new Request("","", MessageAction.COMMUNICATION_CHECK,new CommunicationCheck("test")));
            requests.add(new Request("test_user","", MessageAction.CHECK_USERNAME, null));
            requests.add(new Request("a_stephens","", MessageAction.CHECK_USERNAME, null));

            for (Request r : requests) {
                outToServer.writeObject(r);
            }

/*          TODO FIX
            Response incomingResponse;
            while ((incomingResponse = (Response) inFromServer.readObject()) != null) {
                if(incomingResponse.messageAction == MessageAction.RESPONSE)
                    System.out.println(((Response)incomingMessage.payload).getCode());
            }
      */


            client.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
