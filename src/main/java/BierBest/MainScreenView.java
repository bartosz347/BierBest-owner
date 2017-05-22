package BierBest;

import BierBest.order.OrderDetailsDisplayViewModel;
import BierBest.order.OrderViewModel;
import BierBest.order.OrdersTableViewModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class MainScreenView implements Initializable {

    private OrdersTableViewModel ordersTableViewModel = new OrdersTableViewModel();
    private OrderDetailsDisplayViewModel orderDetailsDisplayViewModel = new OrderDetailsDisplayViewModel();

    ObservableList<String> shopSideStatuses = FXCollections.<String>observableArrayList(
            "", "rejected", "price offered", "sent");

    @FXML
    private TableView<OrderViewModel> ordersTable;

    @FXML
    private TableColumn<OrderViewModel, String> clientNameColumn;
    @FXML
    private TableColumn<OrderViewModel, Integer> orderIdColumn;
    @FXML
    private TableColumn<OrderViewModel, String> orderDateColumn;
    @FXML
    private TableColumn<OrderViewModel, String> orderStatusClientColumn;
    @FXML
    private TableColumn<OrderViewModel, String> orderStatusShopColumn;


    @FXML
    private Label clientNameLabel;
    @FXML
    private Label clientCityLabel;
    @FXML
    private Label clientRegistrationDateLabel;
    @FXML
    private Label clientPhoneNumberLabel;
    @FXML
    private Label clientSideStatusLabel;

    @FXML
    private ImageView beerImageView;
    @FXML
    private Label beerNameLabel;
    @FXML
    private Hyperlink beerURLHyperlink;
    @FXML
    private TextField beerPriceField;
    @FXML
    private ChoiceBox<String> beerStatusBox;

    private Image img;

    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        beerStatusBox.setItems(shopSideStatuses);

        orderIdColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        clientNameColumn.setCellValueFactory(cellData -> cellData.getValue().getClientViewModel().nameProperty());
        orderDateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        orderStatusClientColumn.setCellValueFactory(cellData -> cellData.getValue().statusClientSideProperty());
        orderStatusShopColumn.setCellValueFactory(cellData -> cellData.getValue().statusShopSideProperty());

        ordersTable.setItems(ordersTableViewModel.getOrdersData());
  /*      ordersTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> this.orderDetailsDisplayViewModel.load(newValue)
        );*/

        ordersTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<OrderViewModel>() {
           @Override
           public void changed(ObservableValue<? extends OrderViewModel> observable, OrderViewModel oldValue, OrderViewModel newValue) {
               orderDetailsDisplayViewModel.load(newValue);
               refresh();
           }
       }
        );

        //clientNameLabel.textProperty().bindBidirectional(clientViewModel.nameProperty());

        refresh();



    }

    private void refresh() {

        // TODO is binding every time needed ?
        clientNameLabel.textProperty().bind(orderDetailsDisplayViewModel.getOrderViewModel().getClientViewModel().nameProperty());
        clientCityLabel.textProperty().bind(orderDetailsDisplayViewModel.getOrderViewModel().getClientViewModel().cityProperty());
        clientRegistrationDateLabel.textProperty().bind(orderDetailsDisplayViewModel.getOrderViewModel().getClientViewModel().registrationDateProperty());
        clientPhoneNumberLabel.textProperty().bind(orderDetailsDisplayViewModel.getOrderViewModel().getClientViewModel().phoneNumberProperty());

        clientSideStatusLabel.textProperty().bind(orderDetailsDisplayViewModel.getOrderViewModel().statusClientSideProperty());

        if(!orderDetailsDisplayViewModel.getOrderViewModel().getBeerImgURL().isEmpty()) {
            img = new Image(orderDetailsDisplayViewModel.getOrderViewModel().getBeerImgURL());
            beerImageView.imageProperty().setValue(img);
        }
        beerNameLabel.textProperty().setValue(orderDetailsDisplayViewModel.getOrderViewModel().getBeerName());
        beerURLHyperlink.textProperty().setValue(orderDetailsDisplayViewModel.getOrderViewModel().getBeerURL());
        beerPriceField.textProperty().setValue(orderDetailsDisplayViewModel.getOrderViewModel().getBeerPrice());
        beerStatusBox.setValue(orderDetailsDisplayViewModel.getOrderViewModel().getStatusShopSide());

    }

    @FXML
    private void handleUpdateOrder(ActionEvent event) {
        orderDetailsDisplayViewModel.updateOrder(beerStatusBox.getValue());
        refresh();
    }





}
