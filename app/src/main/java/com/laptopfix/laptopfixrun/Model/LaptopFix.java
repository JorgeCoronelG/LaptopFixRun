package com.laptopfix.laptopfixrun.Model;

public class LaptopFix {

    private String idLaptop;
    private String email;
    private User user;

    public LaptopFix(String idLaptop, String email, User user) {
        this.idLaptop = idLaptop;
        this.email = email;
        this.user = user;
    }

    public LaptopFix() {
    }

    public String getIdLaptop() {
        return idLaptop;
    }

    public void setIdLaptop(String idLaptop) {
        this.idLaptop = idLaptop;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
