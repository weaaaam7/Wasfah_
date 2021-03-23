package com.example.wasfah.services;

import com.google.api.core.ApiService;

public class ApiUtils {
    private ApiUtils(){
    }
    public static final String BASE_URL ="https://pauhero.com/androidpush/";
    public static AppService getApiService(){
        return  RetrofitClient.getClient(BASE_URL).create(AppService.class);
    }
}
