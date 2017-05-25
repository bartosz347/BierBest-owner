import BierBest.client.ClientModel;
import BierBest.communication.Request;
import BierBest.communication.RequestHandlingService;
import BierBest.communication.Response;
import BierBest.communication.payloads.ClientData;
import BierBest.communication.payloads.MessageAction;
import BierBest.communication.payloads.OrderData;
import BierBest.communication.payloads.Orders;
import BierBest.order.BeerInfo;
import BierBest.order.OrderModel;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Date;
import java.util.List;

import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
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

            // Client1
            ClientModel client1 = new ClientModel();
            client1.setFirstName("Kate");
            client1.setLastName("Kowalski");
            client1.setUsername("k_kowalski");
            client1.setCity("London");
            client1.setPhoneNumber("+44111333666");
            client1.setRegistrationDate(new Date());
            entityManager.persist(client1);

            // Client2
            ClientModel client2 = new ClientModel();
            client2.setFirstName("Andrew");
            client2.setLastName("Stephens");
            client2.setUsername("a_stephens");
            client2.setHash("9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08".toUpperCase());
            client2.setPhoneNumber("+74999333666");
            client2.setCity("New York");
            client2.setRegistrationDate(new Date());
            entityManager.persist(client2);

            // Order1
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

            // Order2 for client2
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

            // Order3 for client2
            order = new OrderModel();
            order.setStatusClientSide("new");
            order.setBeerInfo(new BeerInfo() {{
                setName("another_beer");
                setURL("https://untappd/some_beer");
                setImgURL("http://www.anagram.pl/wp-content/uploads/krolweskie.jpg");
                setPriceString("19.30");
            }});
            order.setDate(new Date());
            order.setStatusShopSide("");
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

        Request req = new Request("a_nowak","", MessageAction.CHECK_USERNAME, null);
        BierBest.communication.Response resp;
        resp = handlingService.handleRequest(req);

        assertTrue(resp instanceof Response);
        assertEquals(resp.messageAction, MessageAction.CHECK_USERNAME);
        assertEquals(resp.getResponseCode(), resp.SUCCESS);
    }

    @Test
    public void GivenDuplicateUsernameWhenCheckRequestedThenInvalidResponseCodeReceived() {
        RequestHandlingService handlingService = new RequestHandlingService(sessionFactory);

        Request req = new Request("k_kowalski","", MessageAction.CHECK_USERNAME, null);
        BierBest.communication.Response resp;
        resp = handlingService.handleRequest(req);

        assertTrue(resp instanceof Response);
        assertEquals(resp.messageAction, MessageAction.CHECK_USERNAME);
        assertEquals(resp.getResponseCode(), resp.INVALID);
    }

    @Test
    public void GivenClientDataWhenAddRequestedThenDataIsSaved() {
        RequestHandlingService handlingService = new RequestHandlingService(sessionFactory);

        ClientData clientData = new ClientData();
        clientData.client = new ClientModel();
        clientData.client.setFirstName("Andrzej");
        clientData.client.setLastName("Nowak");
        clientData.client.setUsername("a_nowak");
        clientData.client.setCity("Poznań");
        clientData.client.setAddress("Słoneczna 14 m. 4");
        clientData.client.setPhoneNumber("+48111222333");
        clientData.client.setEmail("test@test.com");
        //clientData.client.setRegistrationDate(new Date()); TODO timestamp-date comparison mismatch

        Request req = new Request("a_nowak","secret_password", MessageAction.ADD_CLIENT, clientData);
        Response resp;
        resp = handlingService.handleRequest(req);


        ClientModel fetchedClient = null;
        EntityManager entityManager = sessionFactory.createEntityManager();
        entityManager.getTransaction().begin();
        fetchedClient = entityManager.createQuery( "from client where username = :username", ClientModel.class )
                .setParameter("username",clientData.client.getUsername())
                .getSingleResult();

        entityManager.getTransaction().commit();
        entityManager.close();

        assertNotNull(fetchedClient);
        Assert.assertThat(clientData.client, samePropertyValuesAs(fetchedClient));
        assertEquals(resp.messageAction,  MessageAction.ADD_CLIENT);
        assertEquals(resp.getResponseCode(), resp.SUCCESS);
    }

    @Test
    public void GivenValidClientCredentialsWhenClientDataRequestedThenDataIsReturned() {
        RequestHandlingService handlingService = new RequestHandlingService(sessionFactory);

        ClientModel referenceClient;
        referenceClient = new ClientModel();
        referenceClient.setFirstName("Andrzej");
        referenceClient.setLastName("Nowak");
        referenceClient.setUsername("a_nowak");
        referenceClient.setCity("Poznań");
        referenceClient.setAddress("Słoneczna 14 m. 4");
        referenceClient.setPhoneNumber("+48111222333");
        referenceClient.setEmail("test@test.com");
        //referenceClient.setRegistrationDate(new Date()); TODO timestamp-date comparison mismatch

        Request req = new Request("a_nowak","secret_password", MessageAction.GET_CLIENT_DATA, null);
        Response resp;
        resp = handlingService.handleRequest(req);

        assertNotNull(resp.payload);
        assertTrue(resp.payload instanceof ClientData);
        Assert.assertThat(referenceClient, samePropertyValuesAs(((ClientData) resp.payload).client));
        assertEquals(resp.messageAction, MessageAction.GET_CLIENT_DATA);
        assertEquals(resp.getResponseCode(), resp.SUCCESS);
    }

    @Test
    public void GivenInvalidClientCredentialsWhenClientDataRequestedThenAccesDeniedCodeIsReturned() {
        RequestHandlingService handlingService = new RequestHandlingService(sessionFactory);

        Request req = new Request("a_nowak","BAD_PASSWORD", MessageAction.GET_CLIENT_DATA, null);
        Response resp;
        resp = handlingService.handleRequest(req);

        assertNull(resp.payload);
        assertEquals(resp.messageAction, MessageAction.GET_CLIENT_DATA);
        assertEquals(resp.getResponseCode(), resp.DENIED);
    }

    @Test
    public void GivenValidClientCredentialsWhenAddingNewOrderThenOrderIsSaved() {
        RequestHandlingService handlingService = new RequestHandlingService(sessionFactory);

        OrderModel newOrder = new OrderModel();
        newOrder.setStatusClientSide("new");
        BeerInfo beerInfo = new BeerInfo() {{
            setName("some beer");
            setPriceString("12.30");
            setURL("http://example.com/");
            setImgURL("http://example.com/img.jpg");
        }};
        newOrder.setBeerInfo(beerInfo);

        Request req = new Request("a_nowak", "secret_password", MessageAction.ADD_ORDER, new OrderData(newOrder));
        Response resp;
        resp = handlingService.handleRequest(req);

        EntityManager entityManager = sessionFactory.createEntityManager();
        entityManager.getTransaction().begin();
        OrderModel fetchedOrder;
        fetchedOrder = entityManager.createQuery("select p from product_order p JOIN client c ON c.id = p.client.id WHERE c.username = :a", OrderModel.class).setParameter("a", "a_nowak").getSingleResult();


        entityManager.getTransaction().commit();
        entityManager.close();

        assertNull(resp.payload);
        assertEquals(newOrder.getBeerInfo(), beerInfo);
        assertEquals("a_nowak", fetchedOrder.getClient().getUsername());
        assertEquals(resp.messageAction,  MessageAction.ADD_ORDER);
        assertEquals(resp.getResponseCode(), resp.SUCCESS);
    }

    @Test
    public void GivenInvalidClientCredentialsWhenAddingNewOrderThenAccessDeniedCodeIsReturned() {
        RequestHandlingService handlingService = new RequestHandlingService(sessionFactory);

        OrderModel newOrder = new OrderModel();
        newOrder.setStatusClientSide("new");
        BeerInfo beerInfo = new BeerInfo() {{
            setName("some beer");
            setPriceString("12.30");
            setURL("http://example.com/");
            setImgURL("http://example.com/img.jpg");
        }};
        newOrder.setBeerInfo(beerInfo);

        Request req = new Request("a_nowak", "BAD_PASSWORD", MessageAction.ADD_ORDER, new OrderData(newOrder));
        Response resp;
        resp = handlingService.handleRequest(req);

        assertNull(resp.payload);
        assertEquals(resp.messageAction, MessageAction.ADD_ORDER);
        assertEquals(resp.getResponseCode(), resp.DENIED);
    }

    @Test
    public void GivenValidClientCredentialsWhenFetchingOrdersThenOrdersAreReturned() {
        RequestHandlingService handlingService = new RequestHandlingService(sessionFactory);

        ClientModel referenceClient;

        Request req = new Request("a_stephens","test", MessageAction.GET_CLIENT_ORDERS, null);
        Response resp;
        resp = handlingService.handleRequest(req);

        List<OrderModel> orders = ((Orders)resp.payload).orders;

        assertNotNull(resp.payload);
        assertTrue(resp.payload instanceof Orders);
        assertEquals(2, orders.size());
        assertEquals("some_beer", orders.get(0).getBeerInfo().getName());
        assertEquals("another_beer", orders.get(1).getBeerInfo().getName());
        assertEquals(resp.messageAction, MessageAction.GET_CLIENT_ORDERS);
        assertEquals(resp.getResponseCode(), resp.SUCCESS);
    }

    @Test
    public void GivenInvalidClientCredentialsWhenFetchingOrdersThenAccessDeniedCodeReturned() {
        RequestHandlingService handlingService = new RequestHandlingService(sessionFactory);

        Request req = new Request("a_stephens","bad_password", MessageAction.GET_CLIENT_ORDERS, null);
        Response resp;
        resp = handlingService.handleRequest(req);

        assertNull(resp.payload);
        assertEquals(resp.messageAction, MessageAction.GET_CLIENT_ORDERS);
        assertEquals(resp.getResponseCode(), resp.DENIED);

    }

    @Test
    public void GivenValidClientCredentialsAndNewStatusWhenUpdatingOrderThenNewStatusIsSaved() {
        RequestHandlingService handlingService = new RequestHandlingService(sessionFactory);

        OrderModel order = new OrderModel();
        order.setStatusClientSide("accepted");
        order.setBeerInfo(new BeerInfo() {{
            setName("another_beer");
            setURL("https://untappd/some_beer");
            setImgURL("http://www.anagram.pl/wp-content/uploads/krolweskie.jpg");
            setPriceString("19.30");
        }});
        order.setDate(new Date());
        order.setStatusShopSide("");

        // we need to manually fetch order id before running the test
        EntityManager entityManager = sessionFactory.createEntityManager();
        OrderModel fetchedOrder;
        fetchedOrder = entityManager.createQuery("select p from product_order p JOIN client c ON c.id = p.client.id" +
                " WHERE c.username = :a AND p.beerInfo.name = :b", OrderModel.class)
                .setParameter("a", "a_stephens")
                .setParameter("b",order.getBeerInfo().getName())
                .getSingleResult();
        entityManager.close();

        order.setId(fetchedOrder.getId());

        Request req = new Request("a_stephens","test", MessageAction.UPDATE_ORDER_STATUS, new OrderData(order));
        Response resp;
        resp = handlingService.handleRequest(req);

        entityManager = sessionFactory.createEntityManager();
        entityManager.getTransaction().begin();
        fetchedOrder = null;
        fetchedOrder = entityManager.createQuery("select p from product_order p JOIN client c ON c.id = p.client.id" +
                " WHERE c.username = :a AND p.beerInfo.name = :b", OrderModel.class)
                .setParameter("a", "a_stephens")
                .setParameter("b",order.getBeerInfo().getName())
                .getSingleResult();

        entityManager.getTransaction().commit();
        entityManager.close();

        assertNull(resp.payload);
        assertNotNull(fetchedOrder);
        assertEquals(order.getStatusClientSide(), fetchedOrder.getStatusClientSide());
        assertEquals(resp.messageAction,  MessageAction.UPDATE_ORDER_STATUS);
        assertEquals(resp.getResponseCode(), resp.SUCCESS);
    }

    @Test
    public void GivenInvalidClientCredentialsAndNewStatusWhenUpdatingOrderThenAccessDeniedCodeIsReturned() {
        RequestHandlingService handlingService = new RequestHandlingService(sessionFactory);

        OrderModel order = new OrderModel();
        order.setStatusClientSide("accepted");
        order.setBeerInfo(new BeerInfo() {{
            setName("another_beer");
            setURL("https://untappd/some_beer");
            setImgURL("http://www.anagram.pl/wp-content/uploads/krolweskie.jpg");
            setPriceString("19.30");
        }});
        order.setDate(new Date());
        order.setStatusShopSide("");

        Request req = new Request("a_stephens","bad_password", MessageAction.UPDATE_ORDER_STATUS, new OrderData(order));
        Response resp;
        resp = handlingService.handleRequest(req);

        EntityManager entityManager = sessionFactory.createEntityManager();
        entityManager.getTransaction().begin();
        OrderModel fetchedOrder;
        fetchedOrder = entityManager.createQuery("select p from product_order p JOIN client c ON c.id = p.client.id" +
                " WHERE c.username = :a AND p.beerInfo.name = :b", OrderModel.class)
                .setParameter("a", "a_stephens")
                .setParameter("b",order.getBeerInfo().getName())
                .getSingleResult();

        entityManager.getTransaction().commit();
        entityManager.close();

        assertNull(resp.payload);
        assertNotEquals(order.getStatusClientSide(), fetchedOrder.getStatusClientSide());
        assertEquals(resp.messageAction, MessageAction.UPDATE_ORDER_STATUS);
        assertEquals(resp.getResponseCode(), resp.DENIED);
    }


}


