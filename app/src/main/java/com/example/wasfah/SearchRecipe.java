package com.example.wasfah;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wasfah.model.IngredientModel;
import com.example.wasfah.model.MyAdapter2;
import com.example.wasfah.model.RecipeModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;

public class SearchRecipe extends AppCompatActivity {
    RecyclerView recview;
    MyAdapter2 adapter;
    SearchView searchView;
    ImageView search;
    ImageView backSearch;
    private static String TAG = "SearchRecipe";

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Users");
    DatabaseReference recipeRef = database.getReference("Recipes");
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String userID = user.getUid();
    String userName;
    private ArrayList<RecipeModel> recipeModels = new ArrayList<>();
    @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_recipe);
//       ImageView search=findViewById(R.id.fileName);


        recview = (RecyclerView) findViewById(R.id.recylerview);
        GridLayoutManager mLayoutManager = new GridLayoutManager(this, 2);
//        mLayoutManager.setReverseLayout(true);

        recview.setLayoutManager(mLayoutManager);
        searchView = findViewById(R.id.fileName);
        backSearch = findViewById(R.id.back);

        adapter = new MyAdapter2(getApplicationContext());
        recview.setAdapter(adapter);

        Log.e(TAG, "onCreate: uid " + userID);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userName = snapshot.child(userID).child("name").getValue(String.class);
                adapter.setCurrentUser(userName);
                Log.e(TAG, "onDataChange: userName " + userName);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        allData();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                processSearch(query.trim());
                return false;

            }

            @Override
            public boolean onQueryTextChange(String newText) {

                Log.e(TAG, "onQueryTextChange: " + newText);
                if (!newText.isEmpty() && !newText.trim().equals("") && newText.trim() != null)
                    processSearch(newText.trim());
                else
                    adapter.setRecipeModels(recipeModels);


                return true;
            }
        });

        backSearch.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), MainActivity.class)));

    }


    private void processSearch(String s) {

        Log.e(TAG, "processSearch: key*" + s + "*");

        ArrayList<RecipeModel> models = new ArrayList<>();

        Log.e(TAG, "processSearch: @@ " + recipeModels.size());

        for (RecipeModel model :
                recipeModels) {

            for (IngredientModel ingredientModel : model.getIngredients()) {
                if (model.getTitle().contains(s) || ingredientModel.getName().contains(s))
                    models.add(model);
            }

        }

        Collections.sort(models, (t1, t2) -> Long.compare(t2.getLikes(), t1.getLikes()));


        Collections.sort(models, (t1, t2) -> {

            try {
                long l1 = format.parse(t1.getTimestamp()).getTime();
                long l2 = format.parse(t2.getTimestamp()).getTime();
                Log.e(TAG, "processSearch: t" );
                return Long.compare(l2, l1);
            } catch (ParseException e) {
                Log.e(TAG, "processSearch: t" +e.getMessage());
                e.printStackTrace();
            }
            return 0;
        });


        adapter.setRecipeModels(models);


    }

    private void allData() {
        FirebaseDatabase.getInstance().getReference()
                .child("Recipes").orderByChild("likes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot m : snapshot.getChildren()) {

                    recipeModels.add(m.getValue(RecipeModel.class));

                }

                Collections.reverse(recipeModels);



                Collections.sort(recipeModels, (t1, t2) -> {

                    try {
                        long l1 = format.parse(t1.getTimestamp()).getTime();
                        long l2 = format.parse(t2.getTimestamp()).getTime();
                        Log.e(TAG, "processSearch: t" );
                        return Long.compare(l2, l1);
                    } catch (ParseException e) {
                        Log.e(TAG, "processSearch: t" +e.getMessage());
                        e.printStackTrace();
                    }
                    return 0;
                });


                adapter.setRecipeModels(recipeModels);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}