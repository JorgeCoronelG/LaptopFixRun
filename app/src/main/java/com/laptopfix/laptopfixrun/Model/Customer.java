package com.laptopfix.laptopfixrun.Model;

public class Customer {

    private int idCus;
    private String name;
    private String number;
    private User user;

    public Customer() {
    }

    public Customer(int idCus, String name, String number, User user) {
        this.idCus = idCus;
        this.name = name;
        this.number = number;
        this.user = user;
    }

    public int getIdCus() {
        return idCus;
    }

    public void setIdCus(int idCus) {
        this.idCus = idCus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}