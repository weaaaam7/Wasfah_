package com.example.wasfah;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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

public class profile extends Fragment {



    private TextView nameTv;
    private Button logout;
    private Button edit;

    List<RecipeInfo> recipe;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View RootView = inflater.inflate(R.layout.fragment_profile, container, false);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users");

        recipe=new ArrayList<>();
        recipe.add((new RecipeInfo("Dounat", R.drawable.b)));
        recipe.add((new RecipeInfo("Salmon", R.drawable.images)));
        recipe.add((new RecipeInfo("Dounat", R.drawable.b)));
        recipe.add((new RecipeInfo("Salmon", R.drawable.images)));
        recipe.add((new RecipeInfo("Dounat", R.drawable.b)));
        recipe.add((new RecipeInfo("Salmon", R.drawable.images)));
        recipe.add((new RecipeInfo("Dounat", R.drawable.b)));
        recipe.add((new RecipeInfo("Salmon", R.drawable.images)));
        recipe.add((new RecipeInfo("Dounat", R.drawable.b)));
        recipe.add((new RecipeInfo("Salmon", R.drawable.images)));

        RecyclerView rs=(RecyclerView) RootView.findViewById(R.id.rv);
        RecyclerViewAdapter recAdap = new RecyclerViewAdapter(getContext(),recipe);
        rs.setLayoutManager(new GridLayoutManager(getContext(),3));
        rs.setAdapter(recAdap);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userID = user.getUid();
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

//                Intent intent = new Intent(getContext(), SignupActivity.class);
//                startActivity(intent);

            }
        });

        // Inflate the layout for this fragment
        return RootView;

    }




}