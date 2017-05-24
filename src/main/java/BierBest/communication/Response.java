package BierBest.communication;

import BierBest.communication.payloads.Payload;
import BierBest.communication.payloads.PayloadType;

import java.io.Serializable;

public class Response implements Serializable {
    public static final int SUCCESS = 1;
    public static final int INVALID = 0;
    public static final int FAILED = -1;
    public static final int DENIED = -2;

    public PayloadType payloadType;
    public Payload payload;
    private int responseCode;

    public Response(PayloadType payloadType, int responseCode) {
        this.payloadType = payloadType;
        this.responseCode = responseCode;
    }

    public int getResponseCode() {
        return responseCode;
    }
}
