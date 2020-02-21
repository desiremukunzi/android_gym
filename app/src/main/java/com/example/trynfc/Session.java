package com.example.trynfc;

public class Session {
    private String category;
    private String sport;
    private String membership;
    private String amount;
    private String telephone;

    public Session(String category, String sport, String membership, String amount, String telephone) {
        this.category = category;
        this.sport = sport;
        this.membership = membership;
        this.amount = amount;
        this.telephone = telephone;
    }

    public Session() {
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public String getMembership() {
        return membership;
    }

    public void setMembership(String membership) {
        this.membership = membership;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Override
    public String toString() {
        return "Session{" +
                "category='" + category + '\'' +
                ", sport='" + sport + '\'' +
                ", membership='" + membership + '\'' +
                ", amount='" + amount + '\'' +
                ", telephone='" + telephone ;
    }
}
