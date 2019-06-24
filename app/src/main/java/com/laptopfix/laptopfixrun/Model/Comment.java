package com.laptopfix.laptopfixrun.Model;

public class Comment {

    private String User;
    private String Comment;
    private String Date;
    private int ca;

    public Comment() {
    }

    public Comment(String user, String comment, String date, int ca) {
        User = user;
        Comment = comment;
        Date = date;
        this.ca = ca;
    }

    public int getCa() {
        return ca;
    }

    public void setCa(int ca) {
        this.ca = ca;
    }

    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
