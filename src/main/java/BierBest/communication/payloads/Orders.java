package BierBest.communication.payloads;

import BierBest.order.OrderModel;

import java.io.Serializable;
import java.util.List;

public class Orders extends Payload implements Serializable {
    public List<OrderModel> orders;

    public Orders(List<OrderModel> orders) {
        this.orders = orders;
    }
}
