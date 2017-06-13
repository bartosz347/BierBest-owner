package bierbest.communication;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

public class BierBestObjectInputStream extends ObjectInputStream {
    public BierBestObjectInputStream(InputStream in) throws IOException {
        super(in);
    }
    @Override
    protected java.io.ObjectStreamClass readClassDescriptor()
            throws IOException, ClassNotFoundException {
        ObjectStreamClass desc = super.readClassDescriptor();
        switch (desc.getName()) {
            case "bierbest.model.Request":
                return ObjectStreamClass.lookup(bierbest.communication.Request.class);
            case "bierbest.model.OrderModel":
                return ObjectStreamClass.lookup(bierbest.order.OrderModel.class);
            case "bierbest.model.ClientModel":
                return ObjectStreamClass.lookup(bierbest.client.ClientModel.class);
            case "bierbest.model.BeerInfo":
                return ObjectStreamClass.lookup(bierbest.order.BeerInfo.class);
            case "bierbest.model.payloads.ClientData":
                return ObjectStreamClass.lookup(bierbest.communication.payloads.ClientData.class);
            case "bierbest.model.payloads.OrderData":
                return ObjectStreamClass.lookup(bierbest.communication.payloads.OrderData.class);
            case "bierbest.model.payloads.MessageAction":
                return ObjectStreamClass.lookup(bierbest.communication.payloads.MessageAction.class);
            case "bierbest.model.payloads.Payload":
                return ObjectStreamClass.lookup(bierbest.communication.payloads.Payload.class);
        }
        return desc;
    }
}
