package BierBest.client;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.text.DateFormat;

public class ClientViewModel {

    // private ClientsLoaderService clientsLoaderService = new ClientsLoaderService();

    private ClientModel client;

    private StringProperty name = new SimpleStringProperty(this,"name","");
    private StringProperty registrationDate = new SimpleStringProperty(this,"registrationDate","");
    private StringProperty city =  new SimpleStringProperty(this,"city","");
    private StringProperty phoneNumber =  new SimpleStringProperty(this,"phoneNumber","");

    private StringProperty username = new SimpleStringProperty(this,"username","");

    public ClientViewModel() {

    }


    public ClientViewModel(ClientModel client) {
        this.client = client;
        setName(client.getFirstName() + " " + client.getLastName());
        setRegistrationDate(
                DateFormat.getDateInstance().format(client.getRegistrationDate())
        );
        setCity(client.getCity());
        setPhoneNumber(client.getPhoneNumber());
        setUsername(client.username);
    }


    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getRegistrationDate() {
        return registrationDate.get();
    }

    public StringProperty registrationDateProperty() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate.set(registrationDate);
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

    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    public StringProperty phoneNumberProperty() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber.set(phoneNumber);
    }

    public String getUsername() {
        return username.get();
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public void setUsername(String username) {
        this.username.set(username);
    }
}
