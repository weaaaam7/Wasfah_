package com.example.wasfah.model;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wasfah.R;
import com.example.wasfah.recepe;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;

public class MyAdapter2 extends RecyclerView.Adapter<MyAdapter2.MyViewHolder> {

    private String TAG="MyAdapter2";
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference recipeRef = database.getReference("Recipes");
    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();


    public MyAdapter2( Context c) {
        this.mcontext = c;
    }

    private ArrayList<RecipeModel> recipeModels=new ArrayList<>();

    private String currentUser;
    private Context mcontext;

    public void setRecipeModels(ArrayList<RecipeModel> d) {
//        this.recipeModels.clear();
        Log.e(TAG, "setRecipeModels: d "+d.size() );
        this.recipeModels = d;
        Log.e(TAG, "setRecipeModels: a "+recipeModels.size() );
        notifyDataSetChanged();
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        RecipeModel model=recipeModels.get(position);

        holder.name.setText(model.getTitle());



           holder.likes.setText(""+model.getLikes());


            holder.dislikes.setText(""+model.getDislikes());

        if (model.getPicUri() != null && !model.getPicUri().isEmpty())
            Glide.with(holder.img.getContext()).load(model.getPicUri()).into(holder.img);


        holder.root.setOnClickListener(view -> {




            Intent inten = new Intent(mcontext, recepe.class);
            inten.putExtra("title", model.getTitle());
            inten.putExtra("category", model.getCategory());
            inten.putExtra("ingredients", (Serializable) model.getIngredients());
            inten.putExtra("steps", (Serializable) model.getSteps());
            inten.putExtra("img", model.getPicUri());
            inten.putExtra("userName", currentUser+" ");
            inten.putExtra("publishedByUser", model.isPublishedByUser());
            inten.putExtra("recipeId", model.getRecipeId());
            inten.putExtra("timestamp", model.getTimestamp());
            inten.putExtra("isProfile", model.isProfile());
//            inten.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            holder.root.getContext().startActivity(inten);


        }
        );
        recipeRef.child(model.getRecipeId()).child("likes").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {



                Log.e(TAG, "onSuccess: " + dataSnapshot.getValue());

                if (dataSnapshot.getChildren() != null) {
                    holder.likes.setText(""+model.getLikes());
                }
                else {
                    holder.likes.setText("0");
                }
            }
        });
        recipeRef.child(model.getRecipeId()).child("dislikes").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {



                Log.e(TAG, "onSuccess: " + dataSnapshot.getValue());

                if (dataSnapshot.getChildren() != null) {
                    holder.dislikes.setText(""+model.getDislikes());
                }
                else {
                    holder.dislikes.setText("0");
                }

            }
        });
        //like and dislike
        final Like[] like = new Like[1];

        recipeRef.child(model.getRecipeId()).child("likeList").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {



                Log.e(TAG, "onSuccess: " + dataSnapshot.getValue());

                if (dataSnapshot.getChildren() != null) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Like like1 = new Like(
                                ds.child("uid").getValue(String.class),
                                ds.child("like").getValue(Boolean.class)
                        );

                        if (like1.getUid().equals(userId)) {
                            like[0] = like1;
                            if (like1.isLike()) {
                                holder.dislike.setImageResource(R.drawable.ic_dislike);
                                holder.like.setImageResource(R.drawable.ic_like_used);

                            } else {
                                holder.dislike.setImageResource(R.drawable.ic_dislike_used);
                                holder.like.setImageResource(R.drawable.ic_like);
                            }
                            return;
                        }

                    }
                }

            }
        });






    }


    @Override
    public int getItemCount() {
        return recipeModels.size();
    }



    static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView img,like,dislike;
        TextView name ,likes ,dislikes;
        LinearLayout root;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            like = itemView.findViewById(R.id.img_like);
            dislike = itemView.findViewById(R.id.img_dislike);
            name = (TextView) itemView.findViewById(R.id.tv);
            root = (LinearLayout) itemView.findViewById(R.id.profile);
            likes = (TextView) itemView.findViewById(R.id.likes);
          dislikes = (TextView) itemView.findViewById(R.id.dislikes);



        }
    }
}
