package BierBest;

import BierBest.order.OrderViewModel;
import BierBest.order.OrdersLoaderService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class OrdersTableViewModel {

    private ObservableList<OrderViewModel> ordersData = FXCollections.observableArrayList();
    private OrdersLoaderService ordersLoaderService = new OrdersLoaderService();


    public OrdersTableViewModel() {
        this.ordersData = ordersLoaderService.loadData();
    }


    public ObservableList<OrderViewModel> getOrdersData() {
        return ordersData;
    }
}
