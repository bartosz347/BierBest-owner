package bierbest.client;

import bierbest.view.UpdateModelsTask;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javassist.bytecode.stackmap.TypeData;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import static bierbest.MainApp.sessionFactory;


public class ClientViewModel {
    private static final Logger LOGGER = Logger.getLogger(TypeData.ClassName.class.getName());

    private ClientModel client;

    private StringProperty name = new SimpleStringProperty(this, "name", "");
    private StringProperty registrationDate = new SimpleStringProperty(this, "registrationDate", "");
    private StringProperty city = new SimpleStringProperty(this, "city", "");
    private StringProperty phoneNumber = new SimpleStringProperty(this, "phoneNumber", "");

    private StringProperty username = new SimpleStringProperty(this, "username", "");
    private StringProperty email = new SimpleStringProperty(this, "email", "");
    private StringProperty address = new SimpleStringProperty(this, "address", "");

    public ClientViewModel() {

    }


    public ClientViewModel(ClientModel client) {
        this.client = client;
        loadDataFromClientModel();
    }

    public void loadDataFromClientModel() {
        setName(client.getFirstName() + " " + client.getLastName());
        setRegistrationDate(
                DateFormat.getDateInstance().format(client.getRegistrationDate())
        );
        setCity(client.getCity());
        setPhoneNumber(client.getPhoneNumber());
        setUsername(client.getUsername());
        setEmail(client.getEmail());
        setAddress(client.getAddress());
    }

    public void saveDataToClientModel() throws ParseException {
        StringTokenizer st = new StringTokenizer(this.getName());
        if (st.countTokens() > 2) {
            throw new ParseException("invalid name", 0);
        }
        client.setFirstName(st.nextToken());
        client.setLastName(st.nextToken());
        DateFormat regDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        client.setRegistrationDate(regDateFormat.parse(this.getRegistrationDate()));
        client.setCity(this.getCity());
        client.setPhoneNumber(this.getPhoneNumber());
        client.setUsername(this.getUsername()); // username changing not allowed, blocked in edit dialog
        client.setEmail(this.getEmail());
        client.setAddress(this.getAddress());

        final UpdateModelsTask updateModelsTask = new UpdateModelsTask(sessionFactory, client);
        updateModelsTask.setOnSucceeded(ev -> LOGGER.log(Level.INFO, "client updated successfully"));
        Thread backgroundThread = new Thread(updateModelsTask);
        backgroundThread.setDaemon(true);
        backgroundThread.start();
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

    public String getAddress() {
        return address.get();
    }

    public StringProperty addressProperty() {
        return address;
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public String getEmail() {
        return email.get();
    }

    public StringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }
}
