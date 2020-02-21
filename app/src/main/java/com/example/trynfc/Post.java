package com.example.trynfc;

import com.google.gson.annotations.SerializedName;

public class Post {


    private String customer_id;

    public Post(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    //    private int userId;
//
//    private int id;
//
//    private String title;
//
//    @SerializedName("body")
//    private String text;
//
//    public int getUserId() {
//        return userId;
//    }
//
//    public int getId() {
//        return id;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public String getText() {
//        return text;
//    }
}
