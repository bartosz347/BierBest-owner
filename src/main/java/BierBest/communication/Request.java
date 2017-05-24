package BierBest.communication;

import BierBest.communication.payloads.Payload;
import BierBest.communication.payloads.PayloadType;

import java.io.Serializable;

public class Request implements Serializable {
    public PayloadType payloadType;
    public Payload payload;

    private String username = "";
    private String password = "";

    public Request(String username, String password, PayloadType payloadType, Payload payload) {
        this.username = username;
        this.password = password;
        this.payloadType = payloadType;
        this.payload = payload;
    }

    public Request(PayloadType payloadType, Payload payload) {
        this.payloadType = payloadType;
        this.payload = payload;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
