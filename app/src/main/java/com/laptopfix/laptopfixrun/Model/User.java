package com.laptopfix.laptopfixrun.Model;

public class User {

    private String email;
    private String password;
    private int status;
    private int idTypeUser;

    public User() {
    }

    public User(String email, String password, int status, int idTypeUser) {
        this.email = email;
        this.password = password;
        this.status = status;
        this.idTypeUser = idTypeUser;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getIdTypeUser() {
        return idTypeUser;
    }

    public void setIdTypeUser(int idTypeUser) {
        this.idTypeUser = idTypeUser;
    }
}