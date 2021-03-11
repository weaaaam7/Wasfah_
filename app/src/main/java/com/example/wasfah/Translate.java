package com.example.wasfah;

import android.os.AsyncTask;

import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

class Translate extends AsyncTask<String, Void, String> {


    public static com.google.cloud.translate.Translate.TranslateOption TranslateOption;

    protected String doInBackground(String... params) {

        String text = params[0]; //text to translate
        com.google.cloud.translate.Translate translate = TranslateOptions.getDefaultInstance().getService();
        Translation translation =
                translate.translate(
                        text,
                        com.google.cloud.translate.Translate.TranslateOption.sourceLanguage("en"),
                        com.google.cloud.translate.Translate.TranslateOption.targetLanguage("ru"));
        return translation.getTranslatedText();
    }

    //this method will run after doInBackground execution
    protected void onPostExecute(String result) {
        System.out.printf("Translation: %s%n", result);
    }


    public Translation Translate(String originalText, com.google.cloud.translate.Translate.TranslateOption arabic, com.google.cloud.translate.Translate.TranslateOption english) {
        return null;
    }
}