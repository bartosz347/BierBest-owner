package BierBest.communication;

import BierBest.communication.payloads.Payload;

import java.io.Serializable;

public class Message implements Serializable {

    public Payload.PayloadType payloadType;
    public Payload payload;

    private String username = "";
    private String password = "";

    public Message(String username, String password, Payload.PayloadType payloadType, Payload payload) {
        this.username = username;
        this.password = password;
        this.payloadType = payloadType;
        this.payload = payload;
    }

    public Message(Payload.PayloadType payloadType, Payload payload) {
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

