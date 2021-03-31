package com.example.wasfah;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wasfah.model.Like;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecyclerViewAdapter<M extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private String TAG = "RecyclerViewAdapter";
    private Context mcontext;
    private List<RecipeInfo> mData;
    private String name;
    private String currentUser;
    private String recipeId;
    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference recipeRef = database.getReference("Recipes");

    public RecyclerViewAdapter(Context mcontext, List<RecipeInfo> mData, String currentUser) {
        this.mcontext = mcontext;
        this.mData = mData;
        this.currentUser = currentUser;
    }

    public RecyclerViewAdapter(Context mcontext, List<RecipeInfo> mData) {
        this.mcontext = mcontext;
        this.mData = mData;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mcontext);
        view = mInflater.inflate(R.layout.carrdview_item, parent, false);


        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        if (mData.get(position).isProfile()) {
            holder.profile.setVisibility(View.VISIBLE);
            holder.category.setVisibility(View.GONE);
        } else {

            holder.profile.setVisibility(View.GONE);
            holder.category.setVisibility(View.VISIBLE);
            holder.tv_title_cat.setText(mData.get(position).getTitle());
            holder.date.setText(mData.get(position).getTimestamp());
            holder.name.setText(mData.get(position).getName());
            Picasso.get().load(mData.get(position).getImg()).into(holder.img2);
        }
        holder.tv_title.setText(mData.get(position).getTitle());
        Glide.with(mcontext).load(mData.get(position).getImg()).into(holder.img);
        Picasso.get().load(mData.get(position).getImg()).into(holder.img);
        holder.name.setText(mData.get(position).getName());
        // set Click lisner
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inten = new Intent(mcontext, recepe.class);
                inten.putExtra("title", mData.get(position).getTitle());
                inten.putExtra("category", mData.get(position).getCategory());
                inten.putExtra("ingredients",( (  (Serializable) mData.get(position).getIngredients())));
                inten.putExtra("steps", (Serializable) mData.get(position).getSteps());
                inten.putExtra("img", mData.get(position).getImg());
                inten.putExtra("userName", currentUser);
                inten.putExtra("publishedByUser", mData.get(position).isPublishedByUser());
                inten.putExtra("recipeId", mData.get(position).getRecipeId());
                inten.putExtra("timestamp", mData.get(position).getTimestamp());
                inten.putExtra("isProfile", mData.get(position).isProfile());


                mcontext.startActivity(inten);

            }
        });



        recipeRef.child(mData.get(position).getRecipeId()).child("likes").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {



                Log.e(TAG, "onSuccess: " + dataSnapshot.getValue());

                if (dataSnapshot.getChildren() != null) {
               holder.likes.setText(""+mData.get(position).getLikes());
                }
                else {
                    holder.likes.setText("0");
                }
            }
        });
        recipeRef.child(mData.get(position).getRecipeId()).child("dislikes").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {



                Log.e(TAG, "onSuccess: " + dataSnapshot.getValue());

                if (dataSnapshot.getChildren() != null) {
                    holder.dislikes.setText(""+mData.get(position).getDislikes());
                }
                else {
                    holder.dislikes.setText("0");
                }

            }
        });
        //like and dislike
        final Like[] like = new Like[1];

        recipeRef.child(mData.get(position).getRecipeId()).child("likeList").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
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

        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (like[0] == null) {
                    like[0] = new Like(userId, true);

                    recipeRef.child(mData.get(position).getRecipeId()).child("likeList").child(like[0].getUid()).setValue(like[0])
                            .addOnSuccessListener(aVoid -> {
                                final Map<String, Object> map = new HashMap<>();
                                map.put("likes", mData.get(position).getLikes() + 1);

                                recipeRef.child(mData.get(position).getRecipeId()).updateChildren(map)
                                        .addOnSuccessListener(aVoid1 -> {
                                            holder.like.setImageResource(R.drawable.ic_like_used);
                                            holder.likes.setText(mData.get(position).getName() );

                                        });
                            });
                } else {
                    if (like[0].isLike())
                        recipeRef.child(mData.get(position).getRecipeId()).child("likeList").child(like[0].getUid()).removeValue()
                                .addOnSuccessListener(aVoid -> {
                                    final Map<String, Object> map = new HashMap<>();
                                    map.put("likes", mData.get(position).getLikes() - 1);

                                    recipeRef.child(mData.get(position).getRecipeId()).updateChildren(map)
                                            .addOnSuccessListener(aVoid1 -> {
                                                holder.like.setImageResource(R.drawable.ic_like);

                                            });
                                });
                    else {
                        like[0].setLike(true);
                        recipeRef.child(mData.get(position).getRecipeId()).child("likeList").child(like[0].getUid()).setValue(like[0])
                                .addOnSuccessListener(aVoid -> {
                                    final Map<String, Object> map = new HashMap<>();
                                    map.put("dislikes", mData.get(position).getDislikes() - 1);
                                    map.put("likes", mData.get(position).getLikes() + 1);

                                    recipeRef.child(mData.get(position).getRecipeId()).updateChildren(map)
                                            .addOnSuccessListener(aVoid1 -> {
                                                holder.dislike.setImageResource(R.drawable.ic_dislike);
                                                holder.like.setImageResource(R.drawable.ic_like_used);

                                            });
                                });
                    }
                }

            }
        });

        holder.dislike.setOnClickListener(view -> {


            if (like[0] == null) {
                like[0] = new Like(userId, false);

                recipeRef.child(mData.get(position).getRecipeId()).child("likeList").child(like[0].getUid()).setValue(like[0])
                        .addOnSuccessListener(aVoid -> {
                            final Map<String, Object> map = new HashMap<>();
                            map.put("dislikes", mData.get(position).getDislikes() + 1);

                            recipeRef.child(mData.get(position).getRecipeId()).updateChildren(map)
                                    .addOnSuccessListener(aVoid1 -> {
                                        holder.dislike.setImageResource(R.drawable.ic_dislike_used);

                                    });
                        });
            } else {
                if (!like[0].isLike())
                    recipeRef.child(mData.get(position).getRecipeId()).child("likeList").child(like[0].getUid()).removeValue()
                            .addOnSuccessListener(aVoid -> {
                                final Map<String, Object> map = new HashMap<>();
                                map.put("dislikes", mData.get(position).getDislikes() - 1);

                                recipeRef.child(mData.get(position).getRecipeId()).updateChildren(map)
                                        .addOnSuccessListener(aVoid1 -> {
                                            holder.dislike.setImageResource(R.drawable.ic_dislike);

                                        });
                            });
                else {
                    like[0].setLike(false);
                    recipeRef.child(mData.get(position).getRecipeId()).child("likeList").child(like[0].getUid()).setValue(like[0])
                            .addOnSuccessListener(aVoid -> {
                                final Map<String, Object> map = new HashMap<>();
                                map.put("dislikes", mData.get(position).getDislikes() + 1);
                                map.put("likes", mData.get(position).getLikes() - 1);

                                recipeRef.child(mData.get(position).getRecipeId()).updateChildren(map)
                                        .addOnSuccessListener(aVoid1 -> {
                                            holder.like.setImageResource(R.drawable.ic_like);
                                            holder.dislike.setImageResource(R.drawable.ic_dislike_used);

                                        });
                            });

                }            }

        });


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView like;
        ImageView dislike;
        TextView tv_title, tv_title_cat;
        ImageView img, img2;//,like, dislike;
        RelativeLayout cardView;
        LinearLayout profile, category;
        TextView name, date , likes,dislikes;


        public MyViewHolder(View itemView) {
            super((itemView));

            tv_title = (TextView) itemView.findViewById(R.id.tv);
            img = (ImageView) itemView.findViewById(R.id.img);
            img2 = (ImageView) itemView.findViewById(R.id.img2);
      like = (ImageView) itemView.findViewById(R.id.img_like);
            dislike = (ImageView) itemView.findViewById(R.id.img_dislike);
            cardView = (RelativeLayout) itemView.findViewById(R.id.cardview);
            tv_title_cat = (TextView) itemView.findViewById(R.id.tv2);
            name = (TextView) itemView.findViewById(R.id.name);
            date = (TextView) itemView.findViewById(R.id.date);
            profile = (LinearLayout) itemView.findViewById(R.id.profile);
            category = (LinearLayout) itemView.findViewById(R.id.category);
            likes = (TextView) itemView.findViewById(R.id.likes);
            dislikes = (TextView) itemView.findViewById(R.id.dislikes);
        }
    }

}
