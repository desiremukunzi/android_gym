package com.example.trynfc;

import com.google.gson.annotations.SerializedName;

public class Postcopy {

    private Integer id;

    private int customer_id;

    private int controller_id;

    private int membership_id;

    private int category_id;

    private int payment_id;

    public Postcopy(Integer id, int customer_id, int controller_id, int membership_id, int category_id, int payment_id) {
        this.id = id;
        this.customer_id = customer_id;
        this.controller_id = controller_id;
        this.membership_id = membership_id;
        this.category_id = category_id;
        this.payment_id = payment_id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public int getController_id() {
        return controller_id;
    }

    public void setController_id(int controller_id) {
        this.controller_id = controller_id;
    }

    public int getMembership_id() {
        return membership_id;
    }

    public void setMembership_id(int membership_id) {
        this.membership_id = membership_id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public int getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(int payment_id) {
        this.payment_id = payment_id;
    }
}
