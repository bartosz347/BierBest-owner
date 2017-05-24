package BierBest.communication;

import BierBest.client.ClientModel;
import BierBest.communication.payloads.CommunicationCheck;
import BierBest.communication.payloads.PayloadType;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class RequestHandlingService {

    public RequestHandlingService(EntityManagerFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private EntityManagerFactory sessionFactory;

    public Response handleRequest(Request incomingRequest) {
        Response responseMessage = null;
        switch (incomingRequest.payloadType) {
            case COMMUNICATION_CHECK:
                System.out.println(((CommunicationCheck) incomingRequest.payload).testData);
                break;
            case CHECK_USERNAME:
                if(checkProposedUsername(incomingRequest.getUsername()))
                    responseMessage = new Response(PayloadType.RESPONSE_CODE, Response.SUCCESS);
                else
                    responseMessage = new Response(PayloadType.RESPONSE_CODE, Response.INVALID);
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
}
