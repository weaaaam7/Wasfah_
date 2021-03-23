package com.example.wasfah.services;

import com.example.wasfah.model.NotificationBody;
import com.example.wasfah.model.NotificationResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AppService {
    @POST("noty.php")
    Call<NotificationResponse> sendPushNotification(@Body NotificationBody notificationBody);


}
