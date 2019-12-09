package com.laptopfix.laptopfixrun.Model;

public class FiscalData {

    private String customer;
    private String name;
    private String address;
    private String phone;
    private String rfc;
    private String cfdi;
    private String email;

    public FiscalData() {
    }

    public FiscalData(String customer, String name, String address, String phone, String rfc, String cfdi, String email) {
        this.customer = customer;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.rfc = rfc;
        this.cfdi = cfdi;
        this.email = email;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public String getCfdi() {
        return cfdi;
    }

    public void setCfdi(String cfdi) {
        this.cfdi = cfdi;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}