package com.example.covid19tracker.Api;

import com.example.covid19tracker.Model.CovidData;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {
    @GET("summary")
    Call<CovidData> getCovidData();
}
