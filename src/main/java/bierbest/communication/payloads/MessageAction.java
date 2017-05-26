package bierbest.communication.payloads;

import java.io.Serializable;

public enum MessageAction implements Serializable {
    CHECK_USERNAME,
    ADD_CLIENT,
    GET_CLIENT_DATA,
    ADD_ORDER,
    GET_CLIENT_ORDERS,
    UPDATE_CLIENT_DATA,
    UPDATE_ORDER_STATUS,
    COMMUNICATION_CHECK
}
