package com.upgrad.FoodOrderingApp.service.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "address")
@NamedQueries({
        @NamedQuery(
                name = "getAllAddressesOfCustomer",
                query= "select address from AddressEntity address where address.customer.uuid=:uuid"
        ),
        @NamedQuery(
                name = "addressByUUID",
                query = "select address from AddressEntity address where address.uuid=:addressId"
        ),
})
public class AddressEntity implements Serializable {

    public AddressEntity(@Size(max = 200) @NotNull String uuid, @Size(max = 255) String flatBuildingNumber, @Size(max = 255) String locality, @Size(max = 30) String city, @Size(max = 30) String pincode, StateEntity state) {
        this.uuid = uuid;
        this.flatBuildingNumber = flatBuildingNumber;
        this.locality = locality;
        this.city = city;
        this.pincode = pincode;
        this.state = state;
    }

    public AddressEntity() {

    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "uuid", unique = true)
    @Size(max = 200)
    @NotNull
    private String uuid;

    @Column(name = "flat_buil_number")
    @Size(max = 255)
    private String flatBuildingNumber;

    @Column(name = "locality")
    @Size(max = 255)
    private String locality;

    @Column(name = "city")
    @Size(max = 30)
    private String city;

    @Size(max = 30)
    @Column(name = "pincode")
    private String pincode;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "state_id")
    private StateEntity state;

    @Column(name = "active")
    private Integer active;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinTable(name = "customer_address", joinColumns = @JoinColumn(name = "address_id"), inverseJoinColumns = @JoinColumn(name = "customer_id"))
    private CustomerEntity customer;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFlatBuildingNumber() {
        return flatBuildingNumber;
    }

    public void setFlatBuilNo(String flatBuildingNumber) {
        this.flatBuildingNumber = flatBuildingNumber;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public StateEntity getState() {
        return state;
    }

    public void setState(StateEntity state) {
        this.state = state;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public CustomerEntity getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerEntity customer) {
        this.customer = customer;
    }

}
