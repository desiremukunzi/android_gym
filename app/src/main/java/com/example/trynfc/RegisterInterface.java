package com.example.trynfc;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RegisterInterface {

    String REGIURL = "https://gym.etouchsol.net/api/0788999990/";
    @FormUrlEncoded
    @POST("session")
    Call<String> getUserRegi(
            @Field("receptionist_id") String receptionist_id,
            @Field("category_id") String category_id,
            @Field("sport_id") String sport_id,
            @Field("membership_id") String membership_id
    );

}