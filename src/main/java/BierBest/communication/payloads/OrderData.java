package BierBest.communication.payloads;

import BierBest.order.OrderModel;

import java.io.Serializable;

public class OrderData extends Payload implements Serializable {
    public OrderModel order;

    public OrderData(OrderModel order) {
        this.order = order;
    }
}
