package com.laptopfix.laptopfixrun.Model;

public class MatchDate {

    private DateHome dateHome;
    private Technical technical;

    public MatchDate() {
    }

    public MatchDate(DateHome dateHome, Technical technical) {
        this.dateHome = dateHome;
        this.technical = technical;
    }

    public DateHome getDateHome() {
        return dateHome;
    }

    public void setDateHome(DateHome dateHome) {
        this.dateHome = dateHome;
    }

    public Technical getTechnical() {
        return technical;
    }

    public void setTechnical(Technical technical) {
        this.technical = technical;
    }
}