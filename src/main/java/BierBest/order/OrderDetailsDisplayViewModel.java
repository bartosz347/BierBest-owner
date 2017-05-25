package BierBest.order;

import BierBest.DataOperationsService;

public class OrderDetailsDisplayViewModel {

    OrderViewModel orderViewModel;
    private DataOperationsService dataOperationsService;

    public OrderDetailsDisplayViewModel(DataOperationsService dataOperationsService) {
        this.dataOperationsService = dataOperationsService;
        this.orderViewModel = new OrderViewModel(dataOperationsService);
    }

    public OrderViewModel getOrderViewModel() {
        return orderViewModel;
    }

    public void updateOrder(String newStatus, String newPrice) {
        orderViewModel.getOrder().setStatusShopSide(newStatus);
        orderViewModel.getOrder().getBeerInfo().setPriceString(newPrice);

        dataOperationsService.updateOrder(orderViewModel.getOrder());

        orderViewModel.loadDataFromOrderModel();
    }


    public void load(OrderViewModel newValue) {
        this.orderViewModel = newValue;
    }
}
