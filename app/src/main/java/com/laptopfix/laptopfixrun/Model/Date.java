package com.laptopfix.laptopfixrun.Model;

public class Date {

    private int idDate;
    private Customer customer;
    private String date;
    private String hour;
    private String residenceCus;
    private String desProblem;

    public Date() {
    }

    public Date(int idDate, Customer customer, String date, String hour, String residenceCus, String desProblem) {
        this.idDate = idDate;
        this.customer = customer;
        this.date = date;
        this.hour = hour;
        this.residenceCus = residenceCus;
        this.desProblem = desProblem;
    }

    public int getIdDate() {
        return idDate;
    }

    public void setIdDate(int idDate) {
        this.idDate = idDate;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getResidenceCus() {
        return residenceCus;
    }

    public void setResidenceCus(String residenceCus) {
        this.residenceCus = residenceCus;
    }

    public String getDesProblem() {
        return desProblem;
    }

    public void setDesProblem(String desProblem) {
        this.desProblem = desProblem;
    }
}