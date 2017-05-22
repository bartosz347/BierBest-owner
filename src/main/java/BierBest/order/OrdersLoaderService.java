package BierBest.order;

import BierBest.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.persistence.EntityManager;
import java.util.List;

public class OrdersLoaderService {
    public ObservableList<OrderViewModel> loadData() {
        ObservableList<OrderViewModel> ordersData = FXCollections.observableArrayList();
        EntityManager entityManager = Main.sessionFactory.createEntityManager();
        entityManager.getTransaction().begin();
        List<OrderModel> result = entityManager.createQuery( "from product_order", OrderModel.class ).getResultList();
        for ( OrderModel order : result ) {
            ordersData.add(new OrderViewModel(order));
        }
        entityManager.getTransaction().commit();
        entityManager.close();

        return ordersData;
    }

}
