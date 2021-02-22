package com.example.wasfah;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.wasfah.model.RecipeModel;
import com.example.wasfah.model.myadapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class SearchRecipe extends AppCompatActivity
{
    RecyclerView recview;
    myadapter adapter;
    SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("");

        recview=(RecyclerView)findViewById(R.id.recylerview);
        recview.setLayoutManager(new LinearLayoutManager(this));
        searchView=findViewById(R.id.fileName);

        FirebaseRecyclerOptions<RecipeModel> options =
                new FirebaseRecyclerOptions.Builder<RecipeModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Recipes"), RecipeModel.class)
                        .build();

        adapter=new myadapter(options);
        recview.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                processsearch(query);
                return false;

            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }



    private void processsearch(String s)
    {
        FirebaseRecyclerOptions<RecipeModel> options =
                new FirebaseRecyclerOptions.Builder<RecipeModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Recipes").orderByChild("category").startAt(s).endAt(s+"\uf8ff"), RecipeModel.class)
                        .build();

        adapter=new myadapter(options);
        adapter.startListening();
        recview.setAdapter(adapter);

    }
}