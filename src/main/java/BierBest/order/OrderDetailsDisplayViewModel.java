package BierBest.order;

public class OrderDetailsDisplayViewModel {



    OrderViewModel orderViewModel = new OrderViewModel();

    public OrderViewModel getOrderViewModel() {
        return orderViewModel;
    }


    public void load(OrderViewModel newValue) {
        this.orderViewModel = newValue;
    }
}
