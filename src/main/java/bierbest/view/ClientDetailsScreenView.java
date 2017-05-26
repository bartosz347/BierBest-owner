package bierbest.view;

import bierbest.client.ClientViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javassist.bytecode.stackmap.TypeData;

import java.net.URL;
import java.text.ParseException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientDetailsScreenView implements Initializable {

    private static final Logger LOGGER = Logger.getLogger(TypeData.ClassName.class.getName());
    private ClientViewModel clientViewModel;
    private Stage ownStage;

    @FXML
    private Label clientRegistrationDate;

    @FXML
    private TextField clientPhonenumber;

    @FXML
    private TextField clientName;

    @FXML
    private TextField clientUsername;

    @FXML
    private TextField clientCity;

    @FXML
    private TextField clientAddress;

    @FXML
    private TextField clientEmail;


    @FXML
    void saveClientDetails(ActionEvent event) {
        try {
            clientViewModel.saveDataToClientModel();
        } catch (ParseException e) {
            LOGGER.log(Level.INFO, "parsing exception while saving client details");
        }

        ownStage.close();
    }

    @FXML
    void resetClientDetailsForm(ActionEvent event) {
        clientViewModel.loadDataFromClientModel();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        clientEmail.setEditable(false);

    }

    private void updateFields() {
        clientName.textProperty().bindBidirectional(clientViewModel.nameProperty());
        clientUsername.textProperty().bindBidirectional(clientViewModel.usernameProperty());
        clientPhonenumber.textProperty().bindBidirectional(clientViewModel.phoneNumberProperty());
        clientCity.textProperty().bindBidirectional(clientViewModel.cityProperty());
        clientAddress.textProperty().bindBidirectional(clientViewModel.addressProperty());
        clientRegistrationDate.textProperty().bindBidirectional(clientViewModel.registrationDateProperty());
        clientEmail.textProperty().bindBidirectional(clientViewModel.emailProperty());
    }

    public void setClientViewModel(ClientViewModel clientViewModel) {
        this.clientViewModel = clientViewModel;
        updateFields();
    }

    public void setOwnStage(Stage ownStage) {
        this.ownStage = ownStage;
    }
}
