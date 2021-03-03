package com.example.wasfah.HomeFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wasfah.R;
import com.example.wasfah.RecipeInfo;
import com.example.wasfah.RecyclerViewAdapter;
import com.example.wasfah.Steps;
import com.example.wasfah.model.IngredientModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ItalianFragment extends Fragment {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Users");
    DatabaseReference recipeRef = database.getReference("Recipes");
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String userID = user.getUid();
    String name,email,username;
    List<RecipeInfo> recipieList;
    DataSnapshot userDataSnap;
    String currentUser;
    boolean isPublishedByUser=false;
    public ItalianFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View RootView = inflater.inflate(R.layout.fragment_italian, container, false);
        recipieList=new ArrayList<>();
        if (user != null) {
            // Read from the database

            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    currentUser=dataSnapshot.child(userID).child("name").getValue(String.class);
                    userDataSnap=dataSnapshot;
                    recipeRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            getAllRecipes(dataSnapshot,userDataSnap);
                            RecyclerView rs=(RecyclerView) RootView.findViewById(R.id.rv);
                            if (recipieList.size()>0)
                            {
                                RecyclerViewAdapter recAdap = new RecyclerViewAdapter(getContext(),recipieList,currentUser);
                                rs.setLayoutManager(new GridLayoutManager(getContext(),1));
                                rs.setAdapter(recAdap);
                            }
                            else {
                                rs.setVisibility(View.GONE);
                                TextView tv_noRecipe=(TextView) RootView.findViewById(R.id.tv_noRecipe);
                                tv_noRecipe.setVisibility(View.VISIBLE);


                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
//                    Log.w(TAG, "Failed to read value.", error.toException());
                        }
                    });

                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
//                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });





            // Inflate the layout for this fragment

        }
        return RootView;

    }
    private void getAllRecipes(@NonNull DataSnapshot snapshot, @NonNull DataSnapshot userSnapShot) {
        if (recipieList != null && recipieList.size() > 0) {
            recipieList.clear();
        }



        for (DataSnapshot ds : snapshot.getChildren()) {
            String Category = snapshot.child(ds.getKey()).child("category").getValue(String.class);
            String Email = snapshot.child(ds.getKey()).child("createdBy").getValue(String.class);
            for (DataSnapshot snap:userSnapShot.getChildren()){

                email= snap.child("email").getValue(String.class);
                if(email!=null && Email!=null && email.equalsIgnoreCase(Email))
                {
                    name=snap.child("name").getValue(String.class);
                }

            }


            if (Category != null &&Category.equalsIgnoreCase("Italian")) {
                List<IngredientModel> ingredients=new ArrayList<>();
                List<Steps> steps=new ArrayList<>();
                for (DataSnapshot ds2: ds.child("ingredients").getChildren())
                {
                    IngredientModel ingredients1=new IngredientModel(ds2.child("name").getValue(String.class),ds2.child("quantity").getValue(long.class),ds2.child("unitOfMeasure").getValue(String.class));
                    ingredients.add(ingredients1);
                }

                for (DataSnapshot ds2: ds.child("preparationSteps").getChildren())
                {
                    Steps s=new Steps(ds2.child("description").getValue(String.class));
                    steps.add(s);
                }

                if (currentUser!=null && name!=null && currentUser.equalsIgnoreCase(name))
                {
                    isPublishedByUser=true;
                }
                else {
                    isPublishedByUser=false;
                }
                RecipeInfo recipe = new RecipeInfo(ds.child("title").getValue(String.class), ds.child("category").getValue(String.class), ds.child("picUri").getValue(String.class), ingredients, steps, ds.child("recipeId").getValue(String.class), ds.child("timestamp").getValue(String.class), name,isPublishedByUser,false);
                recipieList.add(recipe);

            }

        }
    }
}