package BierBest.client;

import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity (name = "client")
public class ClientModel {

    @Id
    @GeneratedValue
    protected Long id;

    @NaturalId
    @NotNull
    @Column(name = "username", unique = true)
    protected String username;

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

    @Column(name="address")
    protected String address;

    @NotNull
    @Column(name = "phone_number")
    protected String phoneNumber;

    @NotNull
    @Column(name = "registration_date")
    @Temporal(TemporalType.TIMESTAMP)
    protected Date registrationDate;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }
}
