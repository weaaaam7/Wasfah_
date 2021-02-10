package com.example.wasfah;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private FirebaseDatabase root;
    private DatabaseReference ref;
    private FirebaseAuth mAuth;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView BN = findViewById(R.id.bottomNavigationView);
        BN.setBackground(null);


        //loading the default fragment
        loadFragment(new home());

        //getting bottom navigation view and attaching the listener

        BN.setOnNavigationItemSelectedListener(this);

        mAuth = FirebaseAuth.getInstance();
        root = FirebaseDatabase.getInstance();
        ref = root.getReference("Users");
    }


    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new home())
                    .commit();
            //return true;
        }
        return false;
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
                fragment = new Search();
                break;



        }

        return loadFragment(fragment);
    }
}