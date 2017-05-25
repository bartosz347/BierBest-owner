package BierBest;

import BierBest.client.ClientModel;
import BierBest.order.OrderModel;
import javafx.concurrent.Task;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class UpdateModelsTask extends Task {
    private EntityManagerFactory sessionFactory;
    private OrderModel order;
    private ClientModel client;

    public UpdateModelsTask(EntityManagerFactory sessionFactory, OrderModel order) {
        this.sessionFactory = sessionFactory;
        this.order = order;
    }

    public UpdateModelsTask(EntityManagerFactory sessionFactory, ClientModel client) {
        this.sessionFactory = sessionFactory;
        this.client = client;
    }

    @Override
    protected Object call() throws Exception {
        if(client != null)
            updateClient();
        if(order != null)
            updateOrder();
        else
            throw new Exception("nothing to do");

        return null;
    }


    public void updateOrder() {
        EntityManager entityManager = sessionFactory.createEntityManager();
        entityManager.getTransaction().begin();

        entityManager.merge(order);

        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public void updateClient() {
        EntityManager entityManager = sessionFactory.createEntityManager();
        entityManager.getTransaction().begin();

        entityManager.merge(client);

        entityManager.getTransaction().commit();
        entityManager.close();
    }
}

