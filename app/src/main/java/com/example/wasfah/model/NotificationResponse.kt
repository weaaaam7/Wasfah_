package com.example.wasfah.model


import com.google.gson.annotations.SerializedName

data class NotificationResponse(
    @SerializedName("myKey")
    val myKey: String
)