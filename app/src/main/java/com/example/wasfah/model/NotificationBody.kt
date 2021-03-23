package com.example.wasfah.model


import com.google.gson.annotations.SerializedName

data class NotificationBody(
    @SerializedName("body")
    val body: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("myKey")
    val myKey: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("title")
    val title: String
)