package com.example.wasfah;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wasfah.model.RecipeModel;
import com.example.wasfah.model.myadapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SearchRecipe extends AppCompatActivity {
    RecyclerView recview;
    myadapter adapter;
    SearchView searchView;
    ImageView search;
    ImageView backSearch;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Users");
    DatabaseReference recipeRef = database.getReference("Recipes");
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String userID = user.getUid();
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_recipe);
//       ImageView search=findViewById(R.id.fileName);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userName = snapshot.child(userID).child("name").getValue(String.class);
                adapter.setCurrentUser(userName);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

                recview = (RecyclerView) findViewById(R.id.recylerview);
        GridLayoutManager mLayoutManager = new GridLayoutManager(this,2);
//        mLayoutManager.setReverseLayout(true);

        recview.setLayoutManager(mLayoutManager);
        searchView = findViewById(R.id.fileName);
        backSearch = findViewById(R.id.back);

        FirebaseRecyclerOptions<RecipeModel> options =
                new FirebaseRecyclerOptions.Builder<RecipeModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Recipes").orderByChild("likes"), RecipeModel.class)
                        .build();
//        currentUser=dataSnapshot.child(userID).child("name").getValue(String.class);

        adapter = new myadapter(options,getApplicationContext(),userName);
        recview.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                processsearch(query);
                return false;

            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (!newText.isEmpty())
                    processsearch(newText);

                return true;
            }
        });
        backSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
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


    private void processsearch(String s) {
        FirebaseRecyclerOptions<RecipeModel> options =
                new FirebaseRecyclerOptions.Builder<RecipeModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("Recipes").orderByChild("title").startAt(s).endAt(s + "\uf8ff"), RecipeModel.class)
                        .build();

        adapter = new myadapter(options,getApplicationContext(),userName);
        adapter.startListening();
        recview.setAdapter(adapter);


    }
}