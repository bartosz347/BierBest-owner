package bierbest.communication.payloads;

import bierbest.client.ClientModel;

import java.io.Serializable;

public class ClientData extends Payload implements Serializable {
    private static final long serialVersionUID = 103L;
    public ClientModel client;

    public ClientData() {
    }

    public ClientData(ClientModel client) {
        this.client = client;
    }
}
