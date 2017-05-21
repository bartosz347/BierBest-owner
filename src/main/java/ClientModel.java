import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity (name = "client")
public class ClientModel {
    //protected List<OrderModel> Orders = new ArrayList<OrderModel>();

    @Id
    @GeneratedValue
    protected Long id;

    @Column(name = "first_name")
    @NotNull
    @Size(
            max = 30,
            message = "maximum first name length is 30 characters"
    )
    protected String firstName;

    @Column(name = "last_name")
    @NotNull
    @Size(
            max = 30,
            message = "maximum last name length is 30 characters"
    )
    protected String lastName;

    @NotNull
    @Size(
            max = 40,
            message = "maximum city name length is 40 characters"
    )
    protected String city;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
