package com.example.wasfah.translation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wasfah.Repository.TranslationRepository;
import com.example.wasfah.model.TranslateText;
import com.example.wasfah.model.TranslationResponse;

public class TranslationViewModel extends ViewModel {

    public LiveData<TranslationResponse> viewModelList = new MutableLiveData<>();


    public void translateText(String text) {
        TranslationRepository translationRepository = new TranslationRepository();
        //Target: the language to translate to
        //q: the text to translate
        TranslateText translateText = new TranslateText(text, "ar");
        viewModelList = translationRepository.translateText(translateText);

        //return viewModelList;
    }

}
