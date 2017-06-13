package bierbest.communication.payloads;

import bierbest.order.OrderModel;

import java.io.Serializable;

public class OrderData extends Payload implements Serializable {
    private static final long serialVersionUID = 102L;
    public OrderModel order;

    public OrderData(OrderModel order) {
        this.order = order;
    }
}
