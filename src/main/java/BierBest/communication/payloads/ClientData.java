package BierBest.communication.payloads;

import BierBest.client.ClientModel;

import java.io.Serializable;

public class ClientData extends Payload implements Serializable {
    public ClientModel client;
}