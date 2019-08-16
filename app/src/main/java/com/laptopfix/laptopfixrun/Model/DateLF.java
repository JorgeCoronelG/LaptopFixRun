package com.laptopfix.laptopfixrun.Model;

public class DateLF {

    private int id;
    private Customer customer;
    private String date;
    private String hour;
    private String desProblem;

    public DateLF() {
    }

    public DateLF(int id, Customer customer, String date, String hour, String desProblem) {
        this.id = id;
        this.customer = customer;
        this.date = date;
        this.hour = hour;
        this.desProblem = desProblem;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getDesProblem() {
        return desProblem;
    }

    public void setDesProblem(String desProblem) {
        this.desProblem = desProblem;
    }
}