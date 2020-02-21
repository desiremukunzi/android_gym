package com.example.trynfc;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface LoginInterfaceRec {

    String LOGINURL = "http://gym.etouchsol.net/api/";
    @FormUrlEncoded
    @POST("loginReceptionist")
    Call<String> getUserLogin(
            @Field("id") String id
    );
}
