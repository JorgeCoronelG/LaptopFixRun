package com.laptopfix.laptopfixrun.Model;

public class Comment {

    private int idComment;
    private String comment;
    private int score;
    private String dateComment;
    private Customer customer;
    private Technical technical;

    public Comment() {
    }

    public Comment(int idComment, String comment, int score, String dateComment, Customer customer, Technical technical) {
        this.idComment = idComment;
        this.comment = comment;
        this.score = score;
        this.dateComment = dateComment;
        this.customer = customer;
        this.technical = technical;
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

    public Technical getTechnical() {
        return technical;
    }

    public void setTechnical(Technical technical) {
        this.technical = technical;
    }

}