package BierBest.order;

import BierBest.DataOperationsService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class OrdersTableViewModel {

    private ObservableList<OrderViewModel> ordersData = FXCollections.observableArrayList();
    private DataOperationsService dataOperationsService;


    public OrdersTableViewModel(DataOperationsService dataOperationsService) {
        this.dataOperationsService = dataOperationsService;
        loadWithRejected();
    }

    public void loadWithRejected() {
        this.ordersData = dataOperationsService.getOrders(true);
}

    public void loadWithoutRejected() {
        this.ordersData = dataOperationsService.getOrders(false);
    }


    public ObservableList<OrderViewModel> getOrdersData() {
        return ordersData;
    }
}
