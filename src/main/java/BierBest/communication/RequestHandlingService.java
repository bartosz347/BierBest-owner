package BierBest.communication;

import BierBest.MainApp;
import BierBest.client.ClientModel;
import BierBest.communication.payloads.CommunicationCheck;
import BierBest.communication.payloads.Payload;
import BierBest.communication.payloads.Response;

import javax.persistence.EntityManager;

public class RequestHandlingService {
    private static RequestHandlingService ourInstance = new RequestHandlingService();

    public static RequestHandlingService getInstance() {
        return ourInstance;
    }

    private RequestHandlingService() {
    }


    public Message handleMessage(Message incomingMessage) {
        Message responseMessage = null;
        switch (incomingMessage.payloadType) {
            case COMMUNICATION_CHECK:
                System.out.println(((CommunicationCheck) incomingMessage.payload).testData);
                break;
            case CHECK_USERNAME:
                if(checkProposedUsername(incomingMessage.getUsername()))
                    responseMessage = new Message(Payload.PayloadType.RESPONSE, new Response(1));
                else
                    responseMessage = new Message(Payload.PayloadType.RESPONSE, new Response(0));
                break;
            default:
                throw new RuntimeException("unsupported payload type");
        }

        return responseMessage;
    }


    private boolean checkProposedUsername(String username) {
        EntityManager entityManager = MainApp.sessionFactory.createEntityManager();
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
