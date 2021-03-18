package com.example.wasfah;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private FirebaseDatabase root;
    private DatabaseReference ref;
    private FirebaseAuth mAuth;
    private String name;
    private FloatingActionButton publishFB;
   // private TranslationViewModel translationViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView BN = findViewById(R.id.bottomNavigationView);
        BN.setBackground(null);
        if (Pref.getValue(getApplicationContext(), "language_checked", "false").equalsIgnoreCase("true")) {
            setApplicationLocale("ar");
        } else {
            setApplicationLocale("en");
        }

        //loading the default fragment
        loadFragment(new home());

//        Log.d("dddddd", "dddddddddddd :");
//        translationViewModel = ViewModelProviders.of(this).get(TranslationViewModel.class);
//        subscribeUsersObserver();

        //getting bottom navigation view and attaching the listener.

        BN.setOnNavigationItemSelectedListener(this);

        mAuth = FirebaseAuth.getInstance();
        root = FirebaseDatabase.getInstance();
        ref = root.getReference("Users");
        publishFB = findViewById(R.id.publish_fb);
        publishFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPublishRecipeActivity();
            }
        });

    }


//    private void subscribeUsersObserver() {
//        Log.d("dddddd", "ddddd :");
//        translationViewModel.translateText("hello").observe(this, translationResponse -> {
//                    String test = translationResponse.getData().getTranslations().get(0).getTranslatedText();
//                    Log.d("dddddd", "ddddd :" + test);
//                }
//        );
//
//        translationViewModel.translateText("hello");
//
//    }

    private void startPublishRecipeActivity() {
        Intent i = new Intent(this, PublishRecipeActivity.class);
        startActivity(i);
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    public void restartActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {

            case R.id.mHome:
                fragment = new home();
                break;

            case R.id.mprofile:
                fragment = new profile();
                break;


            case R.id.mFav:
                fragment = new Fav();
                break;

            case R.id.mSearch:
                Intent intent1 = new Intent(this, SearchRecipe.class);
                this.startActivity(intent1);
                break;
        }

        return loadFragment(fragment);
    }

    public void setApplicationLocale(String locale) {
        Resources resources = getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(new Locale(locale.toLowerCase()));
        } else {
            config.locale = new Locale(locale.toLowerCase());
        }
        resources.updateConfiguration(config, dm);
    }
}