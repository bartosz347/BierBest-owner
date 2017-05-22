package BierBest.order;

import javax.persistence.EntityManager;

import static BierBest.MainApp.sessionFactory;

public class OrderDetailsDisplayViewModel {

    OrderViewModel orderViewModel = new OrderViewModel();

    public OrderViewModel getOrderViewModel() {
        return orderViewModel;
    }

    public void updateOrder(String newStatus) {
        EntityManager entityManager = sessionFactory.createEntityManager();
        entityManager.getTransaction().begin();

        orderViewModel.getOrder().setStatusShopSide(newStatus);
        entityManager.merge(orderViewModel.getOrder());

        entityManager.getTransaction().commit();
        entityManager.close();
        orderViewModel.loadDataFromOrderModel();
    }


    public void load(OrderViewModel newValue) {
        this.orderViewModel = newValue;
    }
}
