package com.quest.etna.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Integer id;

    @Column(nullable = false, unique = true)
    @NotNull
    private String username;

    @Column(nullable = false)
    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Enumerated(value = EnumType.STRING)
    private UserRole role = UserRole.ROLE_USER;

    @CreationTimestamp
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date creationDate;

    @UpdateTimestamp
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date updatedDate;
    @JsonIgnore
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL, targetEntity = Order.class)
    private Set<Order> orders;

    @OneToMany(cascade = CascadeType.ALL, targetEntity = Address.class, mappedBy = "user")
    private Set<Address> addresses;

//    public User(String username, String password, UserRole role) {
//        this.username = username;
//        this.password = password;
//        this.role = role;
//    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User() {
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }

    public User(@NotNull String username, @NotNull String password, @NotNull UserRole role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public Set<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(Set<Address> addresses) {
        this.addresses = addresses;
    }

    public void addAddress(Address address) {
        this.addresses.add(address);
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
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id) && username.equals(user.username) && password.equals(user.password) && role == user.role && creationDate.equals(user.creationDate) && updatedDate.equals(user.updatedDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, role, creationDate, updatedDate);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", creationDate=" + creationDate +
                ", updatedDate=" + updatedDate +
                '}';
    }
//
//    public String toString() {
//        return "id_" + id + "_u_" + username + "_p_" + password + "_r_" + role + "_cd_" + creationDate + "_ud_" + updatedDate;
//    }
//
//    public boolean equals(Object o) {
//        if (o == this) return true;
//        if (!(o instanceof User)) return false;
//        return id.equals(((User) o).getId());
//    }
//
//    public int hashCode() {
//        return id.intValue();
//    }
}
