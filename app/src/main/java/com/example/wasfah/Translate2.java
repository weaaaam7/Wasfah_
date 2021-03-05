package com.example.wasfah;

import android.os.AsyncTask;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

class Translate2 extends AsyncTask<String, Void, String> {


    protected String doInBackground(String... params) {

        String text = params[0]; //text to translate
        Translate translate = TranslateOptions.getDefaultInstance().getService();
        Translation translation =
                translate.translate(
                        text,
                        Translate.TranslateOption.sourceLanguage("en"),
                        Translate.TranslateOption.targetLanguage("ru"));
        return translation.getTranslatedText();
    }

    //this method will run after doInBackground execution
    protected void onPostExecute(String result) {
        System.out.printf("Translation: %s%n", result);
    }


}