package BierBest;

import BierBest.client.ClientViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.text.ParseException;
import java.util.ResourceBundle;

public class ClientDetailsScreenView implements Initializable {

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
    void saveClientDetails(ActionEvent event) {
        // todo verify username
        // todo save data in DB
        try {
            clientViewModel.saveDataToClientModel();
        } catch (ParseException e) {
            System.out.println(e);
        }

        ownStage.close();
    }

    @FXML
    void resetClientDetailsForm(ActionEvent event) {
        clientViewModel.loadDataFromClientModel();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    private void updateFields() {
        clientName.textProperty().bindBidirectional(clientViewModel.nameProperty());
        clientUsername.textProperty().bindBidirectional(clientViewModel.usernameProperty());
        clientPhonenumber.textProperty().bindBidirectional(clientViewModel.phoneNumberProperty());
        clientCity.textProperty().bindBidirectional(clientViewModel.cityProperty());
        clientAddress.textProperty().bindBidirectional(clientViewModel.addressProperty());
        clientRegistrationDate.textProperty().bindBidirectional(clientViewModel.addressProperty());
    }

    public void setClientViewModel(ClientViewModel clientViewModel) {
        this.clientViewModel = clientViewModel;
        updateFields();
    }

    public void setOwnStage(Stage ownStage) {
        this.ownStage = ownStage;
    }
}
