package com.example.trynfc;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface CategoryInterface {

    String CATEGORY = "http://gym.etouchsol.net/api/";

    @GET("categories")
    Call<List<Category>> getCategories();


}
