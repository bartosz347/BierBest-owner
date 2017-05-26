package BierBest;

import BierBest.order.OrderModel;
import BierBest.order.OrderViewModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

public class GetOrdersTask extends Task {
    private EntityManagerFactory sessionFactory;
    private boolean loadRejected;

    public GetOrdersTask(EntityManagerFactory sessionFactory, boolean loadRejected) {
        this.sessionFactory = sessionFactory;
        this.loadRejected = loadRejected;
    }

    @Override
    protected ObservableList<OrderViewModel> call() throws Exception {
        ObservableList<OrderViewModel> ordersData = FXCollections.observableArrayList();
        EntityManager entityManager = sessionFactory.createEntityManager();
        entityManager.getTransaction().begin();
        List<OrderModel> result;
        if (loadRejected) {
            result = entityManager.createQuery("from product_order", OrderModel.class).getResultList();
        } else {
            result = entityManager.createQuery("from product_order p WHERE p.statusShopSide <> 'rejected' OR p.statusShopSide IS NULL", OrderModel.class).getResultList();
        }
        for (OrderModel order : result) {
            ordersData.add(new OrderViewModel(order));
        }
        entityManager.getTransaction().commit();
        entityManager.close();

        return ordersData;
    }
}
