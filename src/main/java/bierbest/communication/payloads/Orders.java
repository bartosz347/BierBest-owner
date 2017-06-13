package bierbest.communication.payloads;

import bierbest.order.OrderModel;

import java.io.Serializable;
import java.util.List;

public class Orders extends Payload implements Serializable {
    private static final long serialVersionUID = 106L;
    public List<OrderModel> orders;

    public Orders(List<OrderModel> orders) {
        this.orders = orders;
    }
}
