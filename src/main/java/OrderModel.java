import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity (name = "product_order")
public class OrderModel {
    //protected ClientModel client;

    @Id
    @GeneratedValue
    protected Integer id;

    // TODO
    @Column(name = "status")
    @NotNull
    protected String status;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
