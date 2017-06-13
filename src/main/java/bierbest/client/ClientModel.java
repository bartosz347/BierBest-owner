package bierbest.client;

import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Entity(name = "client")
public class ClientModel implements Serializable {
    private static final long serialVersionUID = 108L;

    public ClientModel() {

    }

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

    @Column(name = "address")
    protected String address;

    @NotNull
    @Column(name = "phone_number")
    protected String phoneNumber;

    @NotNull
    @Column(name = "email")
    protected String email;

    @NotNull
    @Column(name = "registration_date", columnDefinition = "DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    protected Date registrationDate;

    @Column(name = "hash", updatable = false)
    @Type(type = "bierbest.client.HashStringUserType")
    protected String hash = "";


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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
