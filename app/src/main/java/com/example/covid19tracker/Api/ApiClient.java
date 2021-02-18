package com.example.covid19tracker.Api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "https://api.covid19api.com/";
    private static Retrofit retrofit = null;
    private static Retrofit getClient(){
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static ApiInterface getService(){
        Retrofit retrofit = getClient();
        ApiInterface apiService = retrofit.create(ApiInterface.class);
        return apiService;
    }
}
