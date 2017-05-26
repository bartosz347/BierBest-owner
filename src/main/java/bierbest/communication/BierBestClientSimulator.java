package bierbest.communication;

import bierbest.client.ClientModel;
import bierbest.communication.payloads.ClientData;
import bierbest.communication.payloads.MessageAction;
import bierbest.communication.payloads.OrderData;
import bierbest.order.BeerInfo;
import bierbest.order.OrderModel;
import javassist.bytecode.stackmap.TypeData;

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
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BierBestClientSimulator extends Thread {

    private static final Logger LOGGER = Logger.getLogger(TypeData.ClassName.class.getName());
    public static final int PORT = 4488;
    public static final String SERVER_ADDRESS = "127.0.0.1";

    public static void main(String[] args) {
            BierBestClientSimulator bierBestClientSimulator = new BierBestClientSimulator();
            bierBestClientSimulator.start();
    }

    @Override
    public void run() {
        LOGGER.log(Level.INFO, "starting client");

        // Dumb trust manager that does not verify if we trust certificate's CA - temporary
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

        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(null, trustAllCertificates, null);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        try (
                //Socket client = new Socket(SERVER_ADDRESS, PORT); // without SSL
                Socket client = sslContext.getSocketFactory().createSocket(SERVER_ADDRESS, PORT);
                ObjectOutputStream outToServer = new ObjectOutputStream(client.getOutputStream());
                ObjectInputStream inFromServer = new ObjectInputStream(client.getInputStream());

        ) {
            LOGGER.log(Level.INFO, "client connected to " + client.getRemoteSocketAddress());

            List<Request> requests = getSampleData();

            for (Request r : requests) {
                outToServer.writeObject(r);
            }

            Thread.sleep(7000);
            client.close();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "client thread error");
        }
    }

    private List<Request> getSampleData() {
        List<Request> requests = new ArrayList<>();

        ClientModel client1 = new ClientModel();
        client1.setFirstName("Andrzej");
        client1.setLastName("Kowalski");
        client1.setUsername("a_kowalski");
        client1.setCity("Warszawa");
        client1.setAddress("Puławska 156 m. 7");
        client1.setEmail("a.kowal@test.com");
        client1.setPhoneNumber("+48111333666");
        client1.setRegistrationDate(new Date());

        OrderModel order = new OrderModel();
        order.setStatusClientSide("new");
        BeerInfo beerInfo = new BeerInfo();
        beerInfo.setName("Pamperifko");
        beerInfo.setURL("https://example.com/pamperifko");
        beerInfo.setImgURL("https://scontent.fwaw3-1.fna.fbcdn.net/v/t1.0-9/14316725_1206873812697643_1428947998854593240_n.jpg?oh=8a110acf4e63b59177aaeffe46dacc4d&oe=59B6016B");
        order.setBeerInfo(beerInfo);
        order.setDate(new Date());
        order.setClient(client1);

        requests.add(new Request("a_kowalski", "pass1", MessageAction.ADD_CLIENT, new ClientData(client1)));
        requests.add(new Request("a_kowalski", "pass1", MessageAction.ADD_ORDER, new OrderData(order)));


        client1 = new ClientModel();
        client1.setFirstName("Jan");
        client1.setLastName("Nowak");
        client1.setUsername("j_nowak");
        client1.setCity("Poznań");
        client1.setAddress("Ursynowska 24 m. 1");
        client1.setEmail("j.nowak@test.com");
        client1.setPhoneNumber("+48444888999");
        client1.setRegistrationDate(new Date());

        order = new OrderModel();
        order.setStatusClientSide("new");
        beerInfo = new BeerInfo();
        beerInfo.setName("Kormoran");
        beerInfo.setURL("https://example.com/Kormoran");
        beerInfo.setImgURL("http://2.bp.blogspot.com/-_j6bGKjBBrs/TgebGWRx1dI/AAAAAAAAAu4/ZAH6ZycPPyk/s1600/Kormoran+Jasne+Mocno+Chmielone.jpg");
        order.setBeerInfo(beerInfo);
        order.setDate(new Date());
        order.setClient(client1);

        requests.add(new Request("j_nowak", "pass2", MessageAction.ADD_CLIENT, new ClientData(client1)));
        requests.add(new Request("j_nowak", "pass2", MessageAction.ADD_ORDER, new OrderData(order)));

        return requests;
    }
}
