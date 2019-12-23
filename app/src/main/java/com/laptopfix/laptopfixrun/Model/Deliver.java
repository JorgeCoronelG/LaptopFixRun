package com.laptopfix.laptopfixrun.Model;

public class Deliver {

    private int idDeliver;
    private DateHome dateHome;
    private String dateDel;
    private String descDel;
    private String costDel;

    public Deliver() {
    }

    public Deliver(int idDeliver, DateHome dateHome, String dateDel, String descDel, String costDel) {
        this.idDeliver = idDeliver;
        this.dateHome = dateHome;
        this.dateDel = dateDel;
        this.descDel = descDel;
        this.costDel = costDel;
    }

    public int getIdDeliver() {
        return idDeliver;
    }

    public void setIdDeliver(int idDeliver) {
        this.idDeliver = idDeliver;
    }

    public DateHome getDateHome() {
        return dateHome;
    }

    public void setDateHome(DateHome dateHome) {
        this.dateHome = dateHome;
    }

    public String getDateDel() {
        return dateDel;
    }

    public void setDateDel(String dateDel) {
        this.dateDel = dateDel;
    }

    public String getDescDel() {
        return descDel;
    }

    public void setDescDel(String descDel) {
        this.descDel = descDel;
    }

    public String getCostDel() {
        return costDel;
    }

    public void setCostDel(String costDel) {
        this.costDel = costDel;
    }

}