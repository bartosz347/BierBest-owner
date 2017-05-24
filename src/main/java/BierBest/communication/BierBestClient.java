package BierBest.communication;

import BierBest.communication.payloads.CommunicationCheck;
import BierBest.communication.payloads.Payload;
import BierBest.communication.payloads.Response;

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

            List<Message> messages = new ArrayList<>();

            messages.add(new Message("","", Payload.PayloadType.COMMUNICATION_CHECK,new CommunicationCheck("test")));
            messages.add(new Message("test_user","", Payload.PayloadType.CHECK_USERNAME, null));
            messages.add(new Message("a_stephens","", Payload.PayloadType.CHECK_USERNAME, null));

            for (Message m : messages) {
                outToServer.writeObject(m);
            }

            Message incomingMessage;
            while ((incomingMessage = (Message) inFromServer.readObject()) != null) {
                if(incomingMessage.payloadType == Payload.PayloadType.RESPONSE)
                    System.out.println(((Response)incomingMessage.payload).getCode());
            }


            client.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
