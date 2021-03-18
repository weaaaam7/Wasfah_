package com.example.wasfah.api;
import com.example.wasfah.model.TranslateText;
import com.example.wasfah.model.TranslationResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface TranslationApiService {
    @POST("v2")
    Call<TranslationResponse> translateText(@Body TranslateText translateText);
}
