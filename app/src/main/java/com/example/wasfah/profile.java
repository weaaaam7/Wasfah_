package com.example.wasfah;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class profile extends Fragment {



    private TextView nameTv;
    private Button logout;
    private Button edit;
   private String email;
    List<RecipeInfo> recipieList;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Users");
    DatabaseReference recipeRef = database.getReference("Recipes");
//    FirebaseStorage storage=FirebaseStorage.getInstance();
//    StorageReference storageRef = storage.getReferenceFromUrl("gs://wasfah-126bf.appspot.com");
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String userID = user.getUid();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View RootView = inflater.inflate(R.layout.fragment_profile, container, false);
recipieList=new ArrayList<>();
      nameTv=(TextView) RootView.findViewById(R.id.name);
      logout=(Button) RootView.findViewById(R.id.logout);
        edit=(Button) RootView.findViewById(R.id.edit);
        if (user != null) {
            // Read from the database
            myRef.child(userID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.

                        String name11 = dataSnapshot.child("name").getValue(String.class);
                        email= dataSnapshot.child("email").getValue(String.class);
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

            recipeRef.child(userID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    getUpdate(dataSnapshot);
                    RecyclerView rs=(RecyclerView) RootView.findViewById(R.id.rv);
                    if (recipieList.size()>0)
                    {
                        RecyclerViewAdapter recAdap = new RecyclerViewAdapter(getContext(),recipieList);
                        rs.setLayoutManager(new GridLayoutManager(getContext(),3));
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
        return RootView;

    }

    private void getUpdate(@NonNull DataSnapshot snapshot) {
        if(recipieList!=null && recipieList.size() >0) {
            recipieList.clear();
        }

       String email2=snapshot.child("createdBy").getValue(String.class);
            if (email2!=null&&email!=null&&email2.equalsIgnoreCase(email)){


                RecipeInfo recipe = new RecipeInfo(snapshot.child("title").getValue(String.class),snapshot.child("category").getValue(String.class),snapshot.child("picUri").getValue(String.class));
                recipieList.add(recipe);
            }

    }


}