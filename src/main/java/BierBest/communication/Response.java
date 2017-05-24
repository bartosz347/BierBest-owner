package BierBest.communication;

import BierBest.communication.payloads.Payload;
import BierBest.communication.payloads.MessageAction;

import java.io.Serializable;

public class Response implements Serializable {
    public static final int SUCCESS = 1;
    public static final int INVALID = 0;
    public static final int FAILED = -1;
    public static final int DENIED = -2;
    public static final int NOT_SET = 999;

    public MessageAction messageAction;
    public Payload payload;
    private int responseCode;

    public Response(MessageAction messageAction, int responseCode) {
        this.messageAction = messageAction;
        this.responseCode = responseCode;
    }

    public Response(MessageAction messageAction, Payload payload, int responseCode) {
        this.messageAction = messageAction;
        this.payload = payload;
        this.responseCode = responseCode;
    }

    public int getResponseCode() {
        return responseCode;
    }
}
