package bierbest.communication.payloads;

import bierbest.order.OrderModel;

import java.io.Serializable;

public class OrderData extends Payload implements Serializable {
    public OrderModel order;

    public OrderData(OrderModel order) {
        this.order = order;
    }
}
