package com.example.wasfah.services;

import com.example.wasfah.model.NotificationBody;
import com.example.wasfah.model.NotificationResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface APIInterface {


    @POST("androidpush/noty.php")
    Call<NotificationResponse> sendNotification(@Body NotificationBody body);


}