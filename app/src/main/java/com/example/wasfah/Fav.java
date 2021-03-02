package com.example.wasfah;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

///**
// * A simple {@link Fragment} subclass.
// * Use the {@link Fav#newInstance} factory method to
// * create an instance of this fragment.
// */


public class Fav extends Fragment {

    List<RecipeInfo> recipeList;
    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    private String name;
    private String email;

    //Firebase
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Users");
    DatabaseReference recipeRef = database.getReference("Recipes");
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String userID = user.getUid();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View RootView = inflater.inflate(R.layout.fragment_profile, container, false);

        recipeList = new ArrayList<>();

        if (user != null) {
//            myRef.child(userID).addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    // This method is called once with the initial value and again
//                    // whenever data at this location is updated.
//
//                    name = dataSnapshot.child("name").getValue(String.class);
//                    email= dataSnapshot.child("email").getValue(String.class);
////                        img =dataSnapshot.child("uimage").getValue(String.class);
////                        if (img !=null){
////                            Picasso.get().load(img).into(imageView);
////                        }
//
////                    if (name != null) {
////                        nameTv.setText(name);
////                    }
//
//                }
//
//                @Override
//                public void onCancelled(DatabaseError error) {
//                    // Failed to read value
////                    Log.w(TAG, "Failed to read value.", error.toException());
//                }
//            });

//            recipeRef.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    getUpdate(dataSnapshot);
//                    RecyclerView rs=(RecyclerView) RootView.findViewById(R.id.rv);
//                    if (recipeList.size()>0)
//                    {
//                        RecyclerViewAdapter recAdap = new RecyclerViewAdapter(getContext(),recipeList,name);
//                        rs.setLayoutManager(new GridLayoutManager(getContext(),3));
//                        rs.setAdapter(recAdap);
//                    }
//                    else {
//                        rs.setVisibility(View.GONE);
//                        TextView tv_noRecipe=(TextView) RootView.findViewById(R.id.tv_noRecipe);
//                        tv_noRecipe.setVisibility(View.VISIBLE);
//
//
//                    }
//
//                }
//
//                @Override
//                public void onCancelled(DatabaseError error) {
//                    // Failed to read value
////                    Log.w(TAG, "Failed to read value.", error.toException());
//                }
//            });
        }

        return RootView;

    }


    private void getUpdate(@NonNull DataSnapshot snapshot) {
        if (recipeList != null && recipeList.size() > 0) {
            recipeList.clear();
        }

        for (DataSnapshot ds : snapshot.getChildren()) {
            String email2 = snapshot.child(ds.getKey()).child("createdBy").getValue(String.class);
            if (email2 != null && email != null && email2.equalsIgnoreCase(email)) {
                List<Ingredients> ingredients=new ArrayList<>();
                List<Steps> steps=new ArrayList<>();
                for (DataSnapshot ds2: ds.child("ingredients").getChildren())
                {
                    Ingredients ingredients1=new Ingredients(ds2.child("name").getValue(String.class),ds2.child("quantity").getValue(long.class),ds2.child("unitOfMeasure").getValue(String.class));
                    ingredients.add(ingredients1);
                }

                for (DataSnapshot ds2: ds.child("preparationSteps").getChildren())
                {
                    Steps s=new Steps(ds2.child("description").getValue(String.class));
                    steps.add(s);
                }

                RecipeInfo recipe = new RecipeInfo(ds.child("title").getValue(String.class), ds.child("category").getValue(String.class), ds.child("picUri").getValue(String.class),ingredients,steps,ds.child("recipeId").getValue(String.class),ds.child("timestamp").getValue(String.class));
                recipeList.add(recipe);
            }

        }
    }
}