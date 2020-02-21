package com.example.trynfc;

import android.widget.EditText;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface MemberShipInterface {
    String MEMBERSHIP = "http://gym.etouchsol.net/api/";


    @GET("memberships")
    Call<List<MemberShip>> getMemberships(@QueryMap Map<String, String> parameters);

    @FormUrlEncoded
    @POST("session")
    Call<String> getSession(
            @Field("phone") String phone,
            @Field("category_id") String category_id,
            @Field("sport_id") String sport_id,
            @Field("membership_id") String membership_id,
            @Field("id") String id
    );
}
