package BierBest.order;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class OrdersTableViewModel {

    private ObservableList<OrderViewModel> ordersData = FXCollections.observableArrayList();
    private OrdersLoaderService ordersLoaderService = new OrdersLoaderService();


    public OrdersTableViewModel() {
        loadWithRejected();
    }

    public void loadWithRejected() {
        this.ordersData = ordersLoaderService.loadData(true);
}

    public void loadWithoutRejected() {
        this.ordersData = ordersLoaderService.loadData(false);
    }


    public ObservableList<OrderViewModel> getOrdersData() {
        return ordersData;
    }
}
