package BierBest;

import BierBest.client.ClientModel;
import BierBest.order.OrderModel;
import BierBest.order.OrderViewModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

public class DataOperationsService {
    private EntityManagerFactory sessionFactory;

    public DataOperationsService(EntityManagerFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public ObservableList<OrderViewModel> getOrders(boolean loadRejected) {
        ObservableList<OrderViewModel> ordersData = FXCollections.observableArrayList();
        EntityManager entityManager = sessionFactory.createEntityManager();
        entityManager.getTransaction().begin();
        List<OrderModel> result;
        if(loadRejected)
           result = entityManager.createQuery( "from product_order", OrderModel.class ).getResultList();
        else
            result = entityManager.createQuery( "from product_order p WHERE p.statusShopSide <> 'rejected' OR p.statusShopSide IS NULL", OrderModel.class ).getResultList();
        for ( OrderModel order : result ) {
            ordersData.add(new OrderViewModel(order,this));
        }
        entityManager.getTransaction().commit();
        entityManager.close();

        return ordersData;
    }

    public void updateOrder(OrderModel order) {
        EntityManager entityManager = sessionFactory.createEntityManager();
        entityManager.getTransaction().begin();

        entityManager.merge(order);

        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public void updateClient(ClientModel client) {
        EntityManager entityManager = sessionFactory.createEntityManager();
        entityManager.getTransaction().begin();

        entityManager.merge(client);

        entityManager.getTransaction().commit();
        entityManager.close();
    }

}
