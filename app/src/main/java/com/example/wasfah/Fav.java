package com.example.wasfah;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wasfah.Ingredients;
import com.example.wasfah.Pref;
import com.example.wasfah.R;
import com.example.wasfah.RecipeInfo;
import com.example.wasfah.RecyclerViewAdapter;
import com.example.wasfah.Steps;
import com.example.wasfah.home;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import static android.text.TextUtils.isEmpty;


public class Fav extends Fragment  {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Users");
    DatabaseReference recipeRef = database.getReference("Recipes");
    DatabaseReference favRef = database.getReference("FavoriteList");
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String userID = user.getUid();
    String name,email;
    String user_id;
    List<RecipeInfo> recipieList;
    DataSnapshot userDataSnap;
    String currentUser;
    boolean isPublishedByUser=false;
    public Fav() {
    }

    private FloatingActionButton filter;
    private RecyclerView rs;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View RootView = inflater.inflate(R.layout.fragment_fav, container, false);
        recipieList=new ArrayList<>();
        rs= RootView.findViewById(R.id.rv);
        recAdap = new RecyclerViewAdapter(getContext(),recipieList,currentUser);
        rs.setLayoutManager(new GridLayoutManager(getContext(),1));
        rs.setAdapter(recAdap);

//        filter = (FloatingActionButton) RootView.findViewById(R.id.filter);
//        filter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                PopupMenu popup = new PopupMenu(getActivity(), view);
//                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(MenuItem item) {
//                        switch (item.getItemId()) {
//                            case R.id.newest:
//                                Collections.sort(recipieList, RecipeInfo.newest);
//                                recAdap.notifyDataSetChanged();
//
//                                return true;
//                            case R.id.alphaSort:
//                                sortList();
//
//                                return true;
//                            default:
//                                return false;
//                        }
//                    }
//                });
//                MenuInflater inflater = popup.getMenuInflater();
//                inflater.inflate(R.menu.sort_menu, popup.getMenu());
//                popup.show();
//            }
//        });
        if (user != null) {
            // Read from the database
            if (Pref.getValue(getContext(),"language_checked", "false").equalsIgnoreCase("true"))
            {
                setApplicationLocale("ar");
            }
            else
            {
                setApplicationLocale("en");
            }
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    currentUser=dataSnapshot.child(userID).child("name").getValue(String.class);
                    userDataSnap=dataSnapshot;
                    favRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            getFilterFavourite(snapshot,userDataSnap);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    recipeRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            getAllRecipes(dataSnapshot,userDataSnap);
                            rs=(RecyclerView) RootView.findViewById(R.id.rv);
                            if (recipieList.size()>0)
                            {
                                recAdap = new RecyclerViewAdapter(getContext(),recipieList,currentUser);
                                rs.setLayoutManager(new GridLayoutManager(getContext(),1));
                                rs.setAdapter(recAdap);
                                Collections.sort(recipieList, RecipeInfo.newest);
                                recAdap.notifyDataSetChanged();
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
        }
        return RootView;

    }
    RecyclerViewAdapter recAdap;

    private void getAllRecipes(@NonNull DataSnapshot snapshot,@NonNull DataSnapshot userSnapShot) {
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


            if (Category != null ) {
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

                if (currentUser!=null && name!=null && currentUser.equalsIgnoreCase(name))
                {
                    isPublishedByUser=true;
                }
                else {
                    isPublishedByUser=false;
                }
                RecipeInfo recipe = new RecipeInfo(ds.child("title").getValue(String.class), ds.child("category").getValue(String.class), ds.child("picUri").getValue(String.class), ingredients, steps, ds.child("recipeId").getValue(String.class), ds.child("timestamp").getValue(String.class), name,isPublishedByUser,false);
                HashSet<String> hashSet = new HashSet<String>();
                hashSet.addAll(stringReceipeList);
                stringReceipeList.clear();
                stringReceipeList.addAll(hashSet);
                for(int i=0;i<stringReceipeList.size();i++){
                    if(stringReceipeList.get(i).equalsIgnoreCase(ds.child("recipeId").getValue(String.class))){
                        recipieList.add(recipe);
                    }
                }
            }

        }

//        Collections.sort(recipieList, RecipeInfo.newest);
//        Collections.sort(recipieList, RecipeInfo.alphabetically);
    }

    public static String substringAfter(final String str, final String separator) {
        if (isEmpty(str)) {
            return str;
        }
        if (separator == null) {
            return "";
        }
        final int pos = str.indexOf(separator);
        if (pos == 0) {
            return str;
        }
        return str.substring(pos + separator.length());
    }

    private ArrayList<String> stringReceipeList = new ArrayList<>();

    private void getFilterFavourite(@NonNull DataSnapshot snapshot,@NonNull DataSnapshot userSnapShot) {

        for (DataSnapshot ds : snapshot.getChildren()) {

            String Category = snapshot.child(ds.getKey()).child("category").getValue(String.class);
            String Email = snapshot.child(ds.getKey()).child("createdBy").getValue(String.class);
            for (DataSnapshot snap : userSnapShot.getChildren()) {

                email = snap.child("email").getValue(String.class);
                if (email != null && Email != null && email.equalsIgnoreCase(Email)) {
                    name = snap.child("name").getValue(String.class);
                }

            }
            String getFavoriteKey = String.valueOf(snapshot.child(ds.getKey()));
            Log.d("======>1", getFavoriteKey);
            for (DataSnapshot snap : userSnapShot.getChildren()) {
                Log.d("=====>2", snap.getKey().toString());
                if (getFavoriteKey.contains(snap.getKey().toString())) {

                    String tempAfter = substringAfter(String.valueOf(snapshot.child(ds.getKey())), "key =");
                    String[] split = tempAfter.split(", value =");
                    String finalReceipeID = split[0];
                    stringReceipeList.add(finalReceipeID.replace(" ", ""));
                    Log.d("=========>3", finalReceipeID);
                }
            }


//            if (Category != null ) {
//                List<Ingredients> ingredients=new ArrayList<>();
//                List<Steps> steps=new ArrayList<>();
//                for (DataSnapshot ds2: ds.child("ingredients").getChildren())
//                {
//                    Ingredients ingredients1=new Ingredients(ds2.child("name").getValue(String.class),ds2.child("quantity").getValue(long.class),ds2.child("unitOfMeasure").getValue(String.class));
//                    ingredients.add(ingredients1);
//                }
//
//                for (DataSnapshot ds2: ds.child("preparationSteps").getChildren())
//                {
//                    Steps s=new Steps(ds2.child("description").getValue(String.class));
//                    steps.add(s);
//                }
//
//                if (currentUser!=null && name!=null && currentUser.equalsIgnoreCase(name))
//                {
//                    isPublishedByUser=true;
//                }
//                else {
//                    isPublishedByUser=false;
//                }
//                RecipeInfo recipe = new RecipeInfo(ds.child("title").getValue(String.class), ds.child("category").getValue(String.class), ds.child("picUri").getValue(String.class), ingredients, steps, ds.child("recipeId").getValue(String.class), ds.child("timestamp").getValue(String.class), name,isPublishedByUser,false);
//
//                Log.d("======>", String.valueOf(stringReceipeList.size()));
//                for(int i=0;i<stringReceipeList.size();i++){
//                    Log.d("====>4",stringReceipeList.get(i));
//                    Log.d("====>5",ds.child("recipeId").getValue(String.class));
//                    if(stringReceipeList.get(i).equalsIgnoreCase(ds.child("recipeId").getValue(String.class))){
//                        recipieList.add(recipe);
//                    }
//                }

//
//            }
//
//        }

//        Collections.sort(recipieList, RecipeInfo.newest);
//        Collections.sort(recipieList, RecipeInfo.alphabetically);
        }
    }


    public void sortList(){
        Collections.sort(recipieList, new Comparator<RecipeInfo>() {
            @Override
            public int compare(RecipeInfo recipeInfo, RecipeInfo t1) {
                return recipeInfo.getTitle().compareTo(t1.getTitle());
            }
        });
        recAdap.notifyDataSetChanged();
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