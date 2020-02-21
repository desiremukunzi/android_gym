package com.example.trynfc;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;


public interface ColporateInterface {

    String COLPORATE = "http://gym.etouchsol.net/api/";

    @GET("entities")
    Call<List<Entity>> getEntities();

    @FormUrlEncoded
    @POST("committed")
    Call<String> postCommited(
            @Field("payment") String payment
    );

    @GET("corporateEntitie")
    Call<List<Colporate>> getColpolates(@QueryMap Map<String, String> parameters);


    @FormUrlEncoded
    @POST("corporate")
    Call<String> postCorporate(
            @Field("payment") String payment
    );
}
