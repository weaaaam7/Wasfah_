package com.example.wasfah;

import android.os.AsyncTask;
<<<<<<< HEAD
import android.widget.Toast;
=======
>>>>>>> origin/master

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

class Translate2 extends AsyncTask<String, Void, String> {

<<<<<<< HEAD
String result;
     public String doInBackground(String... params) {
=======

    protected String doInBackground(String... params) {
>>>>>>> origin/master

        String text = params[0]; //text to translate
        Translate translate = TranslateOptions.getDefaultInstance().getService();
        Translation translation =
                translate.translate(
                        text,
                        Translate.TranslateOption.sourceLanguage("en"),
<<<<<<< HEAD
                        Translate.TranslateOption.targetLanguage("ar"));
=======
                        Translate.TranslateOption.targetLanguage("ru"));
>>>>>>> origin/master
        return translation.getTranslatedText();
    }

    //this method will run after doInBackground execution
    protected void onPostExecute(String result) {
<<<<<<< HEAD
             this.result = result;
    }

    public String getResult() {
        return result;
    }
=======
        System.out.printf("Translation: %s%n", result);
    }


>>>>>>> origin/master
}