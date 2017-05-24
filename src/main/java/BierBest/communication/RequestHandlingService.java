package BierBest.communication;

import BierBest.client.ClientModel;
import BierBest.communication.payloads.ClientData;
import BierBest.communication.payloads.CommunicationCheck;
import BierBest.communication.payloads.MessageAction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
                break;
            case CHECK_USERNAME:
                if(checkProposedUsername(incomingRequest.getUsername()))
                    responseMessage = new Response(MessageAction.RESPONSE_CODE, Response.SUCCESS);
                else
                    responseMessage = new Response(MessageAction.RESPONSE_CODE, Response.INVALID);
                break;
            case ADD_CLIENT:
                responseCode = addClient(((ClientData)incomingRequest.payload).client,incomingRequest.getPassword());
                responseMessage = new Response(MessageAction.RESPONSE_CODE, responseCode);
                break;
            case GET_CLIENT_DATA:
                // TODO refactor
                // TODO better enum values for MessageAction, universal for request and response ?
                ClientModel cl = getClient(incomingRequest.getUsername(), incomingRequest.getPassword());
                if(cl != null)
                    responseCode = Response.SUCCESS;
                else
                    responseCode = Response.FAILED;
                responseMessage = new Response(MessageAction.GET_CLIENT_DATA, new ClientData(cl), responseCode);
                break;
            default:
                throw new RuntimeException("unsupported payload type");
        }

        return responseMessage;
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
        entityManager.getTransaction().begin();


        client.setHash(getHash(password));
        entityManager.persist(client);

        entityManager.getTransaction().commit();
        entityManager.close();

        client.setHash("");
        return Response.SUCCESS;
    }


    private ClientModel getClient(String username, String password) {
        ClientModel client = null;
        EntityManager entityManager = sessionFactory.createEntityManager();
        entityManager.getTransaction().begin();
        ClientModel fetchedClient = entityManager.createQuery( "from client where username = :username AND hash = :hash", ClientModel.class )
                .setParameter("username", username)
                .setParameter("hash", getHash(password))
                .getSingleResult();
        entityManager.getTransaction().commit();
        entityManager.close();


        return fetchedClient;
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
