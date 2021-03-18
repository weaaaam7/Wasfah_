package com.example.wasfah.Repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.wasfah.api.APIClient;
import com.example.wasfah.api.TranslationApiService;
import com.example.wasfah.model.TranslateText;
import com.example.wasfah.model.TranslationResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TranslationRepository {

    private final TranslationApiService translationApiService ;

    public TranslationRepository() {
        translationApiService = APIClient.getClient().create(TranslationApiService.class);
    }

    public LiveData<TranslationResponse> translateText(TranslateText translateText) {

        MutableLiveData<TranslationResponse> translatedText = new MutableLiveData<>();

        Call<TranslationResponse> userListCall = translationApiService.translateText(translateText);

        userListCall.enqueue(new Callback<TranslationResponse>() {
            @Override
            public void onResponse(@NonNull Call<TranslationResponse> call, @NonNull Response<TranslationResponse> response) {
                if (response.isSuccessful())
                    translatedText.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<TranslationResponse> call, @NonNull Throwable t) {
                Log.e("Failed", t.getMessage());
                translatedText.setValue(null);
            }

        });

        return translatedText;
    }
}
