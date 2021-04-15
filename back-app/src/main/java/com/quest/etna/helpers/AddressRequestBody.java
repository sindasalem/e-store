package com.quest.etna.helpers;

public class AddressRequestBody {
    private String street;
    private String city;
    private String postalCode;
    private String country;
    private Integer user_id;

    public AddressRequestBody(String street, String city, String postalCode, String country, Integer user_id) {
        this.street = street;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
        this.user_id = user_id;
    }

    public AddressRequestBody() {
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "AddressRequestBody{" +
                "street='" + street + '\'' +
                ", city='" + city + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", country='" + country + '\'' +
                ", user_id=" + user_id +
                '}';
    }
}
