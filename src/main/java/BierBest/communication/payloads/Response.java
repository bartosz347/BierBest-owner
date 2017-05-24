package BierBest.communication.payloads;

import java.io.Serializable;

public class Response extends Payload implements Serializable {
    int code;

    public Response(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
