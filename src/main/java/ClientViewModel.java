import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ClientViewModel {

    private ClientsLoaderService clientsLoaderService = new ClientsLoaderService();

    private ClientModel client;

    private StringProperty name = new SimpleStringProperty(this,"name","");
    private StringProperty city = new SimpleStringProperty(this,"city","");

    public ClientViewModel() {
        this.client = clientsLoaderService.loadData();
        setName(client.getFirstName() + " " + client.getLastName());
        setCity(client.getCity());
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getCity() {
        return city.get();
    }

    public StringProperty cityProperty() {
        return city;
    }

    public void setCity(String city) {
        this.city.set(city);
    }
}
