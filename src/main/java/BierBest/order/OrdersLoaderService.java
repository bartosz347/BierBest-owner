package BierBest.order;

import BierBest.MainApp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.persistence.EntityManager;
import java.util.List;

public class OrdersLoaderService {
    public ObservableList<OrderViewModel> loadData(boolean loadRejected) {
        ObservableList<OrderViewModel> ordersData = FXCollections.observableArrayList();
        EntityManager entityManager = MainApp.sessionFactory.createEntityManager();
        entityManager.getTransaction().begin();
        List<OrderModel> result;
        if(loadRejected)
           result = entityManager.createQuery( "from product_order", OrderModel.class ).getResultList();
        else
            result = entityManager.createQuery( "from product_order WHERE status_shop_side <> 'rejected' OR status_shop_side IS NULL", OrderModel.class ).getResultList();
        for ( OrderModel order : result ) {
            ordersData.add(new OrderViewModel(order));
        }
        entityManager.getTransaction().commit();
        entityManager.close();

        return ordersData;
    }

}
