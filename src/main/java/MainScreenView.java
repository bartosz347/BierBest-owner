import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;

public class MainScreenView implements Initializable {

    private ClientViewModel clientViewModel = new ClientViewModel();
    private OrdersTableViewModel ordersTableViewModel = new OrdersTableViewModel();

    @FXML
    private TableView<OrderViewModel> ordersTable;
    @FXML
    private TableColumn<OrderViewModel, Integer> orderIdColumn;
    @FXML
    private TableColumn<OrderViewModel, String> orderStatusColumn;

    @FXML
    private Label clientNameLabel;
    @FXML
    private Label clientCityLabel;


    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        orderIdColumn.setCellValueFactory(orderIdColumn -> orderIdColumn.getValue().idProperty().asObject());
        orderStatusColumn.setCellValueFactory(cellData -> cellData.getValue().statusProperty());

        ordersTable.setItems(ordersTableViewModel.getOrdersData());

        //clientNameLabel.textProperty().bindBidirectional(clientViewModel.nameProperty());
        clientNameLabel.textProperty().bind(clientViewModel.nameProperty());
        clientCityLabel.textProperty().bind(clientViewModel.cityProperty());
    }





}
