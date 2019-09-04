package com.laptopfix.laptopfixrun.Model;

public class DateHome {

    private String id;
    private int service;
    private String address;
    private String problem;
    private String date;
    private String hour;
    private Customer customer;

    public DateHome() {
    }

    public DateHome(String id, int service, String address, String problem, String date, String hour, Customer customer) {
        this.id = id;
        this.service = service;
        this.address = address;
        this.problem = problem;
        this.date = date;
        this.hour = hour;
        this.customer = customer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getService() {
        return service;
    }

    public void setService(int service) {
        this.service = service;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
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

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}