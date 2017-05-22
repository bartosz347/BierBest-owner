package BierBest.communication;

import java.io.Serializable;

public class Message implements Serializable {

    private String username = "user";
    private String password = "pass";
    public Payload.PayloadType payloadType = Payload.PayloadType.TEST;
    public Payload payload = new TestPayload();

}

