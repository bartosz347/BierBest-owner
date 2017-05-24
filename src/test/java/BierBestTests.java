import BierBest.MainApp;
import BierBest.client.ClientModel;
import BierBest.communication.Request;
import BierBest.communication.RequestHandlingService;
import BierBest.communication.Response;
import BierBest.communication.payloads.ClientData;
import BierBest.communication.payloads.PayloadType;
import BierBest.order.BeerInfo;
import BierBest.order.OrderModel;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Date;

import static org.junit.Assert.*;

public class BierBestTests {

    private static EntityManagerFactory sessionFactory;

    @BeforeClass
    public static void startSession() {
        sessionFactory = Persistence.createEntityManagerFactory( "BierBest-owner" );

        // Add some test data
        try {
            EntityManager entityManager = sessionFactory.createEntityManager();
            entityManager.getTransaction().begin();

            ClientModel client1 = new ClientModel();
            client1.setFirstName("Kate");
            client1.setLastName("Kowalski");
            client1.setUsername("k_kowalski");
            client1.setCity("London");
            client1.setPhoneNumber("+44111333666");
            client1.setRegistrationDate(new Date());
            entityManager.persist(client1);

            ClientModel client2 = new ClientModel();
            client2.setFirstName("Andrew");
            client2.setLastName("Stephens");
            client2.setUsername("a_stephens");
            client2.setPhoneNumber("+74999333666");
            client2.setCity("New York");
            client2.setRegistrationDate(new Date());
            entityManager.persist(client2);


            OrderModel order = new OrderModel();
            order.setStatusClientSide("new");
            order.setBeerInfo(new BeerInfo() {{
                setName("pamperifko");
                setPriceString("20.10");
                setURL("https://untappd/najlepszePiwo");
                setImgURL("https://scontent.fwaw3-1.fna.fbcdn.net/v/t1.0-9/14316725_1206873812697643_1428947998854593240_n.jpg?oh=8a110acf4e63b59177aaeffe46dacc4d&oe=59B6016B");
            }});
            order.setDate(new Date());
            order.setClient(client1);
            entityManager.persist(order);

            order = new OrderModel();
            order.setStatusClientSide("new");
            order.setBeerInfo(new BeerInfo() {{
                setName("some_beer");
                setURL("https://untappd/some_beer");
                setImgURL("http://www.anagram.pl/wp-content/uploads/krolweskie.jpg");
                setPriceString("12.30");
            }});
            order.setDate(new Date());
            order.setStatusShopSide("rejected");

            order.setClient(client2);
            entityManager.persist(order);

            entityManager.getTransaction().commit();
            entityManager.close();

        }
        catch (Exception e) {
            System.out.println(e);
        }

    }

    @AfterClass
    public static void closeSession() {
        if(sessionFactory != null && sessionFactory.isOpen())
            sessionFactory.close();
    }



    @Test
    public void GivenUniqueUsernameWhenCheckRequestedThenSuccessResponseCodeReceived() {
        RequestHandlingService handlingService = new RequestHandlingService(sessionFactory);

        Request req = new Request("a_nowak","", PayloadType.CHECK_USERNAME, null);
        BierBest.communication.Response resp;
        resp = handlingService.handleRequest(req);

        assertTrue(resp instanceof Response);
        assertEquals(resp.payloadType, PayloadType.RESPONSE_CODE);
        assertEquals(((Response)resp).getResponseCode(),  resp.SUCCESS);
    }

    @Test
    public void GivenDuplicateUsernameWhenCheckRequestedThenInvalidResponseCodeReceived() {
        RequestHandlingService handlingService = new RequestHandlingService(sessionFactory);

        Request req = new Request("k_kowalski","", PayloadType.CHECK_USERNAME, null);
        BierBest.communication.Response resp;
        resp = handlingService.handleRequest(req);

        assertTrue(resp instanceof Response);
        assertEquals(resp.payloadType, PayloadType.RESPONSE_CODE);
        assertEquals(((Response)resp).getResponseCode(),  resp.INVALID);
    }

    //Given[ExplainYourInput]When[WhatIsDone]Then[ExpectedResult]
    @Test
    public void GivenClientDataWhenAddRequestedThenDataIsSaved() {
        RequestHandlingService handlingService = new RequestHandlingService(sessionFactory);

        ClientData clientData = new ClientData();
        clientData.client = new ClientModel();
        clientData.client.setFirstName("a_nowak");
        clientData.client.setLastName("Nowak");
        clientData.client.setCity("Poznań");
        clientData.client.setAddress("Słoneczna 14 m. 4");
        clientData.client.setPhoneNumber("+48111222333");
        clientData.client.setEmail("test@test.com");
        clientData.client.setRegistrationDate(new Date());

        Request req = new Request("a_nowak","secret_password", PayloadType.ADD_CLIENT, clientData);
        Response resp;
        resp = handlingService.handleRequest(req);


        ClientModel fetchedClient = null;
        EntityManager entityManager = MainApp.sessionFactory.createEntityManager();
        entityManager.getTransaction().begin();
        fetchedClient = entityManager.createQuery( "from client where username = :username", ClientModel.class )
                .setParameter("username",clientData.client.getUsername())
                .getSingleResult();

        entityManager.getTransaction().commit();
        entityManager.close();

        assertNotNull(fetchedClient);
        assertEquals(clientData.client, fetchedClient);
        assertEquals(resp.payloadType,  PayloadType.RESPONSE_CODE);
        assertEquals(resp.getResponseCode(), resp.SUCCESS);
    }

    @Test
    public void GivenValidClientCredentialsWhenClientDataRequestedThenDataIsReturned() {
        RequestHandlingService handlingService = new RequestHandlingService(sessionFactory);

        ClientModel referenceClient = new ClientModel();
        referenceClient = new ClientModel();
        referenceClient.setFirstName("a_nowak");
        referenceClient.setLastName("Nowak");
        referenceClient.setCity("Poznań");
        referenceClient.setAddress("Słoneczna 14 m. 4");
        referenceClient.setPhoneNumber("+48111222333");
        referenceClient.setEmail("test@test.com");
        referenceClient.setRegistrationDate(new Date());

        Request req = new Request("a_nowak","secret_password", PayloadType.GET_CLIENT_DATA, null);
        Response resp;
        resp = handlingService.handleRequest(req);

        assertNotNull(req.payload);
        assertTrue(req.payload instanceof ClientData);
        assertEquals(referenceClient, ((ClientData)req.payload).client);
        assertEquals(resp.payloadType, PayloadType.RESPONSE_CODE);
        assertEquals(((Response) resp).getResponseCode(), resp.SUCCESS);
    }
}


