package com.upgrad.FoodOrderingApp.service.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customer")
@NamedQueries({
        @NamedQuery(name = "customerByContactNumber", query = "select c from CustomerEntity c where c.contactNumber = :contactNumber"),
        @NamedQuery(name = "customerByUUID", query = "select c from CustomerEntity c where c.uuid = :uuid"),
})
public class CustomerEntity implements Serializable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "UUID")
    @Size(max = 64)
    private String uuid;

    @Column(name = "FIRSTNAME")
    @NotNull
    @Size(max = 200)
    private String firstName;

    @Column(name = "LASTNAME")
    @NotNull
    @Size(max = 200)
    private String lastName;

    @Column(name = "EMAIL")
    @NotNull
    @Size(max = 200)
    private String emailAddress;

    //@ToStringExclude
    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "CONTACT_NUMBER")
    @NotNull
    @Size(max = 50)
    private String contactNumber;

    @Column(name = "SALT")
    @NotNull
    @Size(max = 200)
    //@ToStringExclude
    private String salt;
    @OneToMany
    @JoinTable(name = "customer_address", joinColumns = @JoinColumn(name = "customer_id"),
            inverseJoinColumns = @JoinColumn(name = "address_id"))
    private List<AddressEntity> addresses = new ArrayList<>();

    public List<AddressEntity> getAddresses() {
        return addresses;
    }


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

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    @Override
    public boolean equals(Object obj) {
        return new EqualsBuilder().append(this, obj).isEquals();
    }


    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this).hashCode();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }


}