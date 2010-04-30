package com.gisgraphy.domain.geoloc.service.geoloc.street;

public class Address {

    private String streetNumber;
    private String StreetName;
    private String city;
    private String state;
    private String ZipCode;
    
    public String getStreetNumber() {
        return streetNumber;
    }
    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }
    public String getStreetName() {
        return StreetName;
    }
    public void setStreetName(String streetName) {
        StreetName = streetName;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }
    public String getZipCode() {
        return ZipCode;
    }
    public void setZipCode(String zipCode) {
        ZipCode = zipCode;
    }
    
    
}
