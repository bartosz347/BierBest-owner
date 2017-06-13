package bierbest.order;

import bierbest.client.ClientModel;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity(name = "product_order")
public class OrderModel implements Serializable {
    private static final long serialVersionUID = 107L;

    @Id
    @GeneratedValue
    protected Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id", nullable = false)
    protected ClientModel client;

    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    protected Date date;

    @Column(name = "status_client_side")
    @NotNull
    protected String statusClientSide;

    @Column(name = "status_shop_side")
    @NotNull
    protected String statusShopSide;

    @Embedded
    protected BeerInfo beerInfo;

    @Column(name = "quantity")
    protected Integer quantity;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isIdNull() {
        return id == null;
    }

    public ClientModel getClient() {
        return client;
    }

    public void setClient(ClientModel client) {
        this.client = client;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStatusClientSide() {
        return statusClientSide;
    }

    public void setStatusClientSide(String statusClientSide) {
        this.statusClientSide = statusClientSide;
    }

    public String getStatusShopSide() {
        return statusShopSide;
    }

    public void setStatusShopSide(String statusShopSide) {
        this.statusShopSide = statusShopSide;
    }

    public BeerInfo getBeerInfo() {
        return beerInfo;
    }

    public void setBeerInfo(BeerInfo beerInfo) {
        this.beerInfo = beerInfo;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
