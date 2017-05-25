package BierBest.communication.payloads;

import java.io.Serializable;

public class CommunicationCheck extends Payload implements Serializable {
    public String testData;

    public CommunicationCheck(String testData) {
        this.testData = testData;
    }

}
