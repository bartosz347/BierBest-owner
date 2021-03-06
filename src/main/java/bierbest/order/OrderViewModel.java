package bierbest.order;

import bierbest.client.ClientViewModel;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.text.DateFormat;

public class OrderViewModel {

    public OrderModel getOrder() {
        return order;
    }

    private OrderModel order = new OrderModel();
    private ClientViewModel clientViewModel;


    public OrderViewModel(OrderModel order) {
        this.order = order;
        this.clientViewModel = new ClientViewModel();
        loadDataFromOrderModel();
    }

    public OrderViewModel() {
        this.clientViewModel = new ClientViewModel();
    }

    public void loadDataFromOrderModel() {
        clientViewModel = new ClientViewModel(order.getClient());
        setId(order.getId());
        setDate(DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.MEDIUM).format(order.getDate()));
        setStatusClientSide(order.getStatusClientSide());
        setStatusShopSide(order.getStatusShopSide());

        setBeerName(order.getBeerInfo().getName());
        setBeerPrice(order.getBeerInfo().getPriceString());
        setQuantity(order.getQuantity());
        setBeerImgURL(order.getBeerInfo().getImgURL());
    }


    private IntegerProperty id = new SimpleIntegerProperty(this, "id", 0);
    private StringProperty date = new SimpleStringProperty(this, "date", "");
    private StringProperty statusClientSide = new SimpleStringProperty(this, "statusClientSide", "");
    private StringProperty statusShopSide = new SimpleStringProperty(this, "statusShopSide", "");

    private StringProperty beerName = new SimpleStringProperty(this, "beerName", "");
    private StringProperty beerPrice = new SimpleStringProperty(this, "beerPrice", "");
    private StringProperty beerImgURL = new SimpleStringProperty(this, "beerImgURL", "");
    private IntegerProperty quantity = new SimpleIntegerProperty(this, "quantity", 0);


    public ClientViewModel getClientViewModel() {
        return clientViewModel;
    }

    public void setClientViewModel(ClientViewModel clientViewModel) {
        this.clientViewModel = clientViewModel;
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getDate() {
        return date.get();
    }

    public StringProperty dateProperty() {
        return date;
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public String getStatusClientSide() {
        return statusClientSide.get();
    }

    public StringProperty statusClientSideProperty() {
        return statusClientSide;
    }

    public void setStatusClientSide(String statusClientSide) {
        this.statusClientSide.set(statusClientSide);
    }

    public String getStatusShopSide() {
        return statusShopSide.get();
    }

    public StringProperty statusShopSideProperty() {
        return statusShopSide;
    }

    public void setStatusShopSide(String statusShopSide) {
        this.statusShopSide.set(statusShopSide);
    }

    public String getBeerName() {
        return beerName.get();
    }

    public StringProperty beerNameProperty() {
        return beerName;
    }

    public void setBeerName(String beerName) {
        this.beerName.set(beerName);
    }

    public String getBeerPrice() {
        return beerPrice.get();
    }

    public StringProperty beerPriceProperty() {
        return beerPrice;
    }

    public void setBeerPrice(String beerPrice) {
        this.beerPrice.set(beerPrice);
    }

    public int getQuantity() {
        return quantity.get();
    }

    public IntegerProperty quantityProperty() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }

    public String getBeerImgURL() {
        return beerImgURL.get();
    }

    public StringProperty beerImgURLProperty() {
        return beerImgURL;
    }

    public void setBeerImgURL(String beerImgURL) {
        this.beerImgURL.set(beerImgURL);
    }
}
