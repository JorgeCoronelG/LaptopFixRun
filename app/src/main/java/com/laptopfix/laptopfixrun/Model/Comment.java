package com.laptopfix.laptopfixrun.Model;

public class Comment {

    private int idComment;
    private String comment;
    private int score;
    private String dateComment;
    private Customer customer;

    public Comment() {
    }

    public Comment(int idComment, String comment, int score, String dateComment, Customer customer) {
        this.idComment = idComment;
        this.comment = comment;
        this.score = score;
        this.dateComment = dateComment;
        this.customer = customer;
    }

    public int getIdComment() {
        return idComment;
    }

    public void setIdComment(int idComment) {
        this.idComment = idComment;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getDateComment() {
        return dateComment;
    }

    public void setDateComment(String dateComment) {
        this.dateComment = dateComment;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

}