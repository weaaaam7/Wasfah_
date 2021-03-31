package com.example.wasfah;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.Locale;

public class profile extends Fragment {



    private TextView nameTv;
    private Button logout;
    private Button edit;
   private String email;
    List<RecipeInfo> recipieList;
    String name11;
    String img;
    private String recipeId;
    private ImageView imageView;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Users");
    DatabaseReference recipeRef = database.getReference("Recipes");
//    FirebaseStorage storage=FirebaseStorage.getInstance();
//    StorageReference storageRef = storage.getReferenceFromUrl("gs://wasfah-126bf.appspot.com");
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String userID = user.getUid();

    public profile() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View RootView = inflater.inflate(R.layout.fragment_profile, container, false);
        if (Pref.getValue(getContext(),"language_checked", "false").equalsIgnoreCase("true"))
        {
           setApplicationLocale("ar");
        }
        else
        {
          setApplicationLocale("en");
        }
recipieList=new ArrayList<>();
      nameTv=(TextView) RootView.findViewById(R.id.name);
      logout=(Button) RootView.findViewById(R.id.logout);
        edit=(Button) RootView.findViewById(R.id.edit);
//        imageView=(ImageView) RootView.findViewById(R.id.imageView);
        if (user != null) {
            // Read from the database
            myRef.child(userID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.

                        name11 = dataSnapshot.child("name").getValue(String.class);
                        email= dataSnapshot.child("email").getValue(String.class);
//                        img =dataSnapshot.child("uimage").getValue(String.class);
//                        if (img !=null){
//                            Picasso.get().load(img).into(imageView);
//                        }

                        if (name11 != null) {
                            nameTv.setText(name11);
                        }

                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
//                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });

            recipeRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    getUpdate(dataSnapshot);
                    RecyclerView rs=(RecyclerView) RootView.findViewById(R.id.rv);
                    if (recipieList.size()>0)
                    {
                        RecyclerViewAdapter<RecyclerView.ViewHolder> recAdap = new RecyclerViewAdapter<RecyclerView.ViewHolder>(getContext(),recipieList,name11);
                        rs.setLayoutManager(new GridLayoutManager(getContext(),3));
                        rs.setAdapter(recAdap);
                    }
                    else {
                        rs.setVisibility(View.GONE);
                        TextView tv_noRecipe=(TextView) RootView.findViewById(R.id.tv_noRecipe);



                    }

                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
//                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });






        }
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);

            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), editprofile.class);
                startActivity(intent);

            }
        });

        // Inflate the layout for this fragment
        // layout
        return RootView;

    }

    private void getUpdate(@NonNull DataSnapshot snapshot) {
        if (recipieList != null && recipieList.size() > 0) {
            recipieList.clear();
        }

        for (DataSnapshot ds : snapshot.getChildren()) {
            String email2 = snapshot.child(ds.getKey()).child("createdBy").getValue(String.class);
            if (email2 != null && email != null && email2.equalsIgnoreCase(email)) {
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

                RecipeInfo recipe = new RecipeInfo(ds.child("title").getValue(String.class), ds.child("category").getValue(String.class), ds.child("picUri").getValue(String.class),ingredients,steps,ds.child("recipeId").getValue(String.class),ds.child("timestamp").getValue(String.class));
                recipieList.add(recipe);
            }

        }
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