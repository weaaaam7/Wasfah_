package com.example.wasfah;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

///**
// * A simple {@link Fragment} subclass.
// * Use the {@link Fav#newInstance} factory method to
// * create an instance of this fragment.
// */


public class Fav extends Fragment {

    private View RootView;
    private RecyclerView recyclerView;
    List<RecipeInfo> recipeList;


    //Firebase
    DatabaseReference favRef;
    FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String recipeID;

    public Fav(){}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        RootView = inflater.inflate(R.layout.fragment_fav, container, false);
        recipeList = new ArrayList<>();

        recyclerView = (RecyclerView) RootView.findViewById(R.id.rec_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mAuth = FirebaseAuth.getInstance();
        recipeID = mAuth.getCurrentUser().getUid();
        favRef = database.getReference().child("FavoriteList").child(recipeID);

        return RootView;

    }



    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<RecipeInfo>().setQuery(favRef, RecipeInfo.class).build();
        FirebaseRecyclerAdapter<RecipeInfo, favViewHolder> adapter = new FirebaseRecyclerAdapter<RecipeInfo, favViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull favViewHolder favViewHolder, int i, @NonNull RecipeInfo recipeInfo) {
                String recipesId = getRef(i).getKey();
                favRef.child(recipesId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(snapshot.hasChild("picUri")) {
                            String recipeImage = snapshot.child("picUri").getValue().toString();
                            String recipeTitle = snapshot.child("title").getValue().toString();

                            Picasso.get().load(recipeImage).into(favViewHolder.image);
                            favViewHolder.title.setText(recipeTitle);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @NonNull
            @Override
            public favViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.carrdview_item, parent, false);
                favViewHolder viewHolder = new favViewHolder(view);
                return viewHolder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    public static class favViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;

        public favViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.img);
            title = itemView.findViewById(R.id.tv);
        }
    }


}