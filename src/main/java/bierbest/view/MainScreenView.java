package bierbest.view;

import bierbest.MainApp;
import bierbest.order.OrderDetailsDisplayViewModel;
import bierbest.order.OrderViewModel;
import bierbest.order.OrdersTableViewModel;
import javafx.application.Platform;
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

    public MainApp mainApp;

    private OrdersTableViewModel ordersTableViewModel = new OrdersTableViewModel(this);
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
    private Label clientEmailLabel;
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
    private Label quantityLabel;
    @FXML
    private TextField beerPriceField;
    @FXML
    private ChoiceBox<String> beerStatusBox;

    @FXML
    private CheckMenuItem showRejectedCheck;

    @FXML
    private Button refreshButton;

    private Image img;

    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        beerStatusBox.setItems(shopSideStatuses);

        ordersTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                handleShowClientDetails();
            }
        });

        orderIdColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        clientNameColumn.setCellValueFactory(cellData -> cellData.getValue().getClientViewModel().nameProperty());
        orderDateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        orderStatusClientColumn.setCellValueFactory(cellData -> cellData.getValue().statusClientSideProperty());
        orderStatusShopColumn.setCellValueFactory(cellData -> cellData.getValue().statusShopSideProperty());

        ordersTableViewModel.load(true);
        ordersTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        orderDetailsDisplayViewModel.load(newValue);
                        refresh();
                    }
                }
        );

        refresh();
    }

    private void refresh() {
        // Binding to change dynamically while editing client details from dialog
        clientNameLabel.textProperty().bind(orderDetailsDisplayViewModel.getOrderViewModel().getClientViewModel().nameProperty());
        clientEmailLabel.textProperty().bind(orderDetailsDisplayViewModel.getOrderViewModel().getClientViewModel().emailProperty());
        clientRegistrationDateLabel.textProperty().bind(orderDetailsDisplayViewModel.getOrderViewModel().getClientViewModel().registrationDateProperty());
        clientPhoneNumberLabel.textProperty().bind(orderDetailsDisplayViewModel.getOrderViewModel().getClientViewModel().phoneNumberProperty());

        clientSideStatusLabel.textProperty().bind(orderDetailsDisplayViewModel.getOrderViewModel().statusClientSideProperty());

        new Thread(() -> {
            img = new Image("bierbest/images/placeholder.png");
            String url = orderDetailsDisplayViewModel.getOrderViewModel().getBeerImgURL();
            if (url != null && !url.isEmpty()) {
                img = new Image(url);
            }
            Platform.runLater(() -> beerImageView.imageProperty().setValue(img));
        }).start();

        beerNameLabel.textProperty().setValue(orderDetailsDisplayViewModel.getOrderViewModel().getBeerName());
        quantityLabel.textProperty().setValue(orderDetailsDisplayViewModel.getOrderViewModel().quantityProperty().getValue().toString());
        beerPriceField.textProperty().setValue(orderDetailsDisplayViewModel.getOrderViewModel().getBeerPrice());
        beerStatusBox.setValue(orderDetailsDisplayViewModel.getOrderViewModel().getStatusShopSide());
    }

    @FXML
    private void handleUpdateOrder(ActionEvent event) {
        orderDetailsDisplayViewModel.updateOrder(beerStatusBox.getValue(), beerPriceField.getText());
        refresh();
    }

    @FXML
    private void handleShowClientDetails() {
        mainApp.showClientDetails(orderDetailsDisplayViewModel.getOrderViewModel().getClientViewModel(), false);

    }

    @FXML
    private void handleEditClientDetails() {
        mainApp.showClientDetails(orderDetailsDisplayViewModel.getOrderViewModel().getClientViewModel(), true);
    }

    @FXML
    private void handleUpdateShowRejected(ActionEvent event) {
        ordersTable.getSelectionModel().clearSelection();
        ordersTableViewModel.load(showRejectedCheck.isSelected());

    }

    @FXML
    private void handleRefreshOrdersList() {
        refreshButton.setDisable(true);
        ordersTable.getSelectionModel().clearSelection();
        ordersTableViewModel.load(showRejectedCheck.isSelected());
    }

    public void setTableItems(ObservableList<OrderViewModel> list) {
        ordersTable.setItems(list);
        refresh();
    }

    public void updateOrdersList() {
        handleRefreshOrdersList();
    }

    public void unlockRefresh() {
        refreshButton.setDisable(false);
    }


}
