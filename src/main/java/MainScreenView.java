import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class MainScreenView implements Initializable {

    private ClientViewModel clientViewModel = new ClientViewModel();

    @FXML
    private Label clientNameLabel;
    @FXML
    private Label clientCityLabel;


    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //clientNameLabel.textProperty().bindBidirectional(clientViewModel.nameProperty());
        clientNameLabel.textProperty().bind(clientViewModel.nameProperty());
        clientCityLabel.textProperty().bind(clientViewModel.cityProperty());
    }
}
