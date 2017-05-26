package BierBest.communication;

import BierBest.client.ClientModel;
import BierBest.communication.payloads.*;
import BierBest.order.OrderModel;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

public class RequestHandlingService {

    public RequestHandlingService(EntityManagerFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private EntityManagerFactory sessionFactory;

    public Response handleRequest(Request incomingRequest) {
        Response responseMessage = null;
        int responseCode = Response.NOT_SET;
        switch (incomingRequest.messageAction) {
            case COMMUNICATION_CHECK:
                System.out.println(((CommunicationCheck) incomingRequest.payload).testData);
                new Response(MessageAction.COMMUNICATION_CHECK, new CommunicationCheck("pong"), Response.SUCCESS);
                break;
            case CHECK_USERNAME:
                if (checkProposedUsername(incomingRequest.getUsername())) {
                    responseMessage = new Response(MessageAction.CHECK_USERNAME, Response.SUCCESS);
                } else {
                    responseMessage = new Response(MessageAction.CHECK_USERNAME, Response.INVALID);
                }
                break;
            case ADD_CLIENT:
                responseCode = addClient(((ClientData) incomingRequest.payload).client, incomingRequest.getPassword());
                responseMessage = new Response(MessageAction.ADD_CLIENT, responseCode);
                break;
            case GET_CLIENT_DATA:
                ClientModel cl = getClient(incomingRequest.getUsername(), incomingRequest.getPassword());
                if (cl != null) {
                    responseMessage = new Response(MessageAction.GET_CLIENT_DATA, new ClientData(cl), Response.SUCCESS);
                } else {
                    responseMessage = new Response(MessageAction.GET_CLIENT_DATA, null, Response.DENIED);
                }
                break;
            case ADD_ORDER:
                responseCode = addOrder(((OrderData) incomingRequest.payload).order, incomingRequest.getUsername(), incomingRequest.getPassword());
                responseMessage = new Response(MessageAction.ADD_ORDER, responseCode);
                break;
            case GET_CLIENT_ORDERS:
                List<OrderModel> orders = getOrders(incomingRequest.getUsername(), incomingRequest.getPassword());
                if (orders != null) {
                    responseMessage = new Response(MessageAction.GET_CLIENT_ORDERS, new Orders(orders), Response.SUCCESS);
                } else {
                    responseMessage = new Response(MessageAction.GET_CLIENT_ORDERS, null, Response.DENIED);
                }
                break;
            case UPDATE_ORDER_STATUS:
                responseCode = updateOrderStatus(incomingRequest.getUsername(), incomingRequest.getPassword(), ((OrderData) incomingRequest.payload).order);
                responseMessage = new Response(MessageAction.UPDATE_ORDER_STATUS, responseCode);
                break;
            default:
                throw new RuntimeException("unsupported payload type");
        }

        return responseMessage;
    }

    private int updateOrderStatus(String username, String password, OrderModel order) {
        if (!checkAuthorization(username, password)) {
            return Response.DENIED;
        }

        if (order.isIdNull() || order.getId() < 0) {
            return Response.FAILED;
        }

        EntityManager entityManager = sessionFactory.createEntityManager();
        entityManager.getTransaction().begin();
        OrderModel fetchedOrder;
        fetchedOrder = entityManager.createQuery("select p from product_order p JOIN client c ON c.id = p.client.id" +
                " WHERE c.username = :username AND p.id = :id", OrderModel.class)
                .setParameter("username", username)
                .setParameter("id", order.getId())
                .getSingleResult();

        fetchedOrder.setStatusClientSide(order.getStatusClientSide());

        entityManager.merge(fetchedOrder);

        entityManager.getTransaction().commit();
        entityManager.close();

        return Response.SUCCESS;
    }

    private int addOrder(OrderModel order, String username, String password) {
        ClientModel client = getClient(username, password);
        if (client == null) {
            return Response.DENIED;
        }

        EntityManager entityManager = sessionFactory.createEntityManager();
        entityManager.getTransaction().begin();

        order.setClient(client);
        order.setStatusClientSide("new");
        order.setStatusShopSide("");
        order.getBeerInfo().setPriceString("0.00");
        order.setDate(new Date());

        entityManager.persist(order);

        entityManager.getTransaction().commit();
        entityManager.close();

        return Response.SUCCESS;
    }

    private boolean checkProposedUsername(String username) {
        EntityManager entityManager = sessionFactory.createEntityManager();
        entityManager.getTransaction().begin();
        int resNo = entityManager
                .createQuery("from client where username = :username", ClientModel.class)
                .setParameter("username", username)
                .getResultList().size();

        entityManager.getTransaction().commit();
        entityManager.close();

        return resNo == 0;
    }

    private int addClient(ClientModel client, String password) {
        EntityManager entityManager = sessionFactory.createEntityManager();
        if (!checkProposedUsername(client.getUsername())) {
            return Response.FAILED;
        }
        entityManager.getTransaction().begin();


        client.setHash(getHash(password));
        entityManager.persist(client);

        entityManager.getTransaction().commit();
        entityManager.close();

        client.setHash("");
        return Response.SUCCESS;
    }


    private ClientModel getClient(String username, String password) {
        EntityManager entityManager = sessionFactory.createEntityManager();
        entityManager.getTransaction().begin();
        ClientModel fetchedClient;
        try {
            fetchedClient = entityManager.createQuery("from client where username = :username AND hash = :hash", ClientModel.class)
                    .setParameter("username", username)
                    .setParameter("hash", getHash(password))
                    .getSingleResult();
        } catch (NoResultException e) {
            fetchedClient = null;
        }
        entityManager.getTransaction().commit();
        entityManager.close();

        return fetchedClient;
    }

    private boolean checkAuthorization(String username, String password) {
        EntityManager entityManager = sessionFactory.createEntityManager();
        entityManager.getTransaction().begin();
        ClientModel fetchedClient;
        try {
            fetchedClient = entityManager.createQuery("from client where username = :username AND hash = :hash", ClientModel.class)
                    .setParameter("username", username)
                    .setParameter("hash", getHash(password))
                    .getSingleResult();
        } catch (NoResultException e) {
            return false;
        } finally {
            entityManager.getTransaction().commit();
            entityManager.close();
        }

        if (fetchedClient.getUsername().equals(username)) {
            return true;
        }

        return false;
    }

    private List<OrderModel> getOrders(String username, String password) {
        if (!checkAuthorization(username, password)) {
            return null;
        }
        EntityManager entityManager = sessionFactory.createEntityManager();
        entityManager.getTransaction().begin();
        List<OrderModel> fetchedOrders = null;
        fetchedOrders = entityManager.createQuery("select p from product_order p JOIN client c ON c.id = p.client.id" +
                " WHERE c.username = :user", OrderModel.class).setParameter("user", username).getResultList();

        entityManager.getTransaction().commit();
        entityManager.close();

        return fetchedOrders;
    }

    private String getHash(String password) {
        String hash = "";
        try {
            hash = DatatypeConverter.printHexBinary(
                    MessageDigest.getInstance("SHA-256").digest(password.getBytes("UTF-8")));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return hash;
    }
}
