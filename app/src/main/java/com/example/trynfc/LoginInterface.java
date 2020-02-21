package com.example.trynfc;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface LoginInterface {

    String LOGINURL = "http://gym.etouchsol.net/api/";
    @FormUrlEncoded
    @POST("loginController")
    Call<String> getUserLogin(
            @Field("email") String username,
            @Field("password") String password,
            @Field("sport") String sport
    );
}
