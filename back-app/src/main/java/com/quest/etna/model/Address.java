package com.quest.etna.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Objects;

@Entity
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Integer id;

    @Column(nullable = false, length = 100)
    @NotNull
    private String street;

    @Column(nullable = false, length = 30)
    @NotNull
    private String postalCode;

    @Column(nullable = false, length = 50)
    @NotNull
    private String city;

    @Column(nullable = false, length = 50)
    @NotNull
    private String country;

    @JsonIgnore
    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "user_id")
    private User user;

    @CreationTimestamp
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date creationDate;

    @UpdateTimestamp
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date updatedDate;

    public Address(@NotNull String street, @NotNull String postalCode, @NotNull String city, @NotNull String country, @NotNull User user) {
        this.street = street;
        this.postalCode = postalCode;
        this.city = city;
        this.country = country;
        setUser(user);
    }

    public Address() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Address)) return false;
        Address address = (Address) o;
        return id.equals(address.id) && street.equals(address.street) && postalCode.equals(address.postalCode) && city.equals(address.city) && country.equals(address.country) && user.equals(address.user) && creationDate.equals(address.creationDate) && updatedDate.equals(address.updatedDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, street, postalCode, city, country, user, creationDate, updatedDate);
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", street='" + street + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", user=" + user +
                ", creationDate=" + creationDate +
                ", updatedDate=" + updatedDate +
                '}';
    }
}
