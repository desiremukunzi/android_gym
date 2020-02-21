package com.example.trynfc;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface SportInterface {

    String SPORT = "http://gym.etouchsol.net/api/";


    @GET("sports")
    Call<List<Sport>> getSports(@QueryMap Map<String, String> parameters);

    @GET("corporateSport")
    Call<List<Sport>> getColpSports(@QueryMap Map<String, String> parameters);
}
