package BierBest.communication;

import BierBest.client.ClientModel;
import BierBest.communication.payloads.ClientData;
import BierBest.communication.payloads.MessageAction;
import BierBest.communication.payloads.OrderData;
import BierBest.order.BeerInfo;
import BierBest.order.OrderModel;

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

public class BierBestClientSimulator {

    public static final int PORT = 4488;
    public static final String SERVER_ADDRESS = "127.0.0.1";

    public static void main(String[] args) {
        try {
            BierBestClientSimulator bierBestClientSimulator = new BierBestClientSimulator();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }

    public BierBestClientSimulator() throws NoSuchAlgorithmException, KeyManagementException {
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
                Socket client = sslContext.getSocketFactory().createSocket(SERVER_ADDRESS, PORT);
                ObjectOutputStream outToServer = new ObjectOutputStream(client.getOutputStream());
                ObjectInputStream inFromServer = new ObjectInputStream(client.getInputStream());

        ) {
            System.out.println("connected to " + client.getRemoteSocketAddress());

            List<Request> requests = getSampleData();

            for (Request r : requests) {
                outToServer.writeObject(r);
            }


/*           Response incomingResponse;
            while ((incomingResponse = (Response) inFromServer.readObject()) != null) {
               // ignoring responses
            }*/

            Thread.sleep(7000);
            client.close();
        } catch (Exception e) {
            System.out.println(e);
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
        beerInfo.setPriceString("33.16");
        beerInfo.setURL("https://example.com/pamperifko");
        beerInfo.setImgURL("https://scontent.fwaw3-1.fna.fbcdn.net/v/t1.0-9/14316725_1206873812697643_1428947998854593240_n.jpg?oh=8a110acf4e63b59177aaeffe46dacc4d&oe=59B6016B");
        order.setBeerInfo(beerInfo);
        order.setDate(new Date());
        order.setClient(client1);

        requests.add(new Request("a_kowalski","pass1",MessageAction.ADD_CLIENT, new ClientData(client1)));
        requests.add(new Request("a_kowalski","pass1",MessageAction.ADD_ORDER, new OrderData(order)));



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
        beerInfo.setPriceString("13.80");
        beerInfo.setURL("https://example.com/Kormoran");
        beerInfo.setImgURL("http://2.bp.blogspot.com/-_j6bGKjBBrs/TgebGWRx1dI/AAAAAAAAAu4/ZAH6ZycPPyk/s1600/Kormoran+Jasne+Mocno+Chmielone.jpg");
        order.setBeerInfo(beerInfo);
        order.setDate(new Date());
        order.setClient(client1);

        requests.add(new Request("j_nowak","pass2",MessageAction.ADD_CLIENT, new ClientData(client1)));
        requests.add(new Request("j_nowak","pass2",MessageAction.ADD_ORDER, new OrderData(order)));

        return requests;
    }
}
