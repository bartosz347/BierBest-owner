package bierbest.communication;

import bierbest.communication.payloads.MessageAction;
import bierbest.communication.payloads.Payload;

import java.io.Serializable;

public class Request implements Serializable {
    public MessageAction messageAction;
    public Payload payload;

    private String username = "";
    private String password = "";

    public Request(String username, String password, MessageAction messageAction, Payload payload) {
        this.username = username;
        this.password = password;
        this.messageAction = messageAction;
        this.payload = payload;
    }

    public Request(MessageAction messageAction, Payload payload) {
        this.messageAction = messageAction;
        this.payload = payload;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
