package com.laptopfix.laptopfixrun.Model;

public class DateHome {

    private String id;
    private int service;
    private String address;
    private String problem;
    private String date;
    private String hour;
    private Customer customer;
    private int payment; //1 - Efectivo, 2 - PayPal
    private int bill; //0 - Sin factura, 1 - Con factura
    private int status;//0 - Sin aceptar, 1 - Aceptado, 2 - En reparación, 3 - Reparado y esperando pago, 4 - Pagado y finalización
    private Technical technical;

    public DateHome() {
    }

    public DateHome(String id) {
        this.id = id;
    }

    public DateHome(String id, int service, String address, String problem, String date, String hour, Customer customer, int payment, int bill, int status, Technical technical) {
        this.id = id;
        this.service = service;
        this.address = address;
        this.problem = problem;
        this.date = date;
        this.hour = hour;
        this.customer = customer;
        this.payment = payment;
        this.bill = bill;
        this.status = status;
        this.technical = technical;
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

    public int getPayment() {
        return payment;
    }

    public void setPayment(int payment) {
        this.payment = payment;
    }

    public int getBill() {
        return bill;
    }

    public void setBill(int bill) {
        this.bill = bill;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Technical getTechnical() {
        return technical;
    }

    public void setTechnical(Technical technical) {
        this.technical = technical;
    }

}