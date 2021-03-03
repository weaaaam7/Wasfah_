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
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.io.Serializable;

public class myadapter extends FirebaseRecyclerAdapter<RecipeModel, myadapter.myviewholder> {
    public myadapter(@NonNull FirebaseRecyclerOptions<RecipeModel> options, Context c, String a) {
        super(options);
        this.currentUser = a;
        this.mcontext = c;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    @NonNull
    @Override
    public RecipeModel getItem(int position) {
        return super.getItem(getItemCount() - 1 - position);
    }

    private String currentUser;
    Context mcontext;

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull RecipeModel model) {
        holder.name.setText(model.getTitle());

        if (model.getPicUri() != null && !model.getPicUri().isEmpty())
            Glide.with(holder.img.getContext()).load(model.getPicUri()).into(holder.img);


        holder.root.setOnClickListener(view -> {

            Log.e("TAG-=-", "onClick: " + model.getImg());
            Log.e("TAG-=-", "onClick: " + model.isPublishedByUser());

            Log.e("TAG_title", model.getTitle());
            Log.e("TAG_category", model.getCategory());
            Log.e("TAG_ingredients", model.getIngredients().toString());
//                Log.e("TAG_steps", model.getSteps().toString());
            Log.e("TAG_img", model.getPicUri());
            Log.e("TAG_userName", currentUser);
            Log.e("TAG_publishedByUser", model.isPublishedByUser() + "");
            Log.e("TAG_recipeId", model.getRecipeId());
            Log.e("TAG_timestamp", model.getTimestamp());
            Log.e("TAG_isProfile", model.isProfile() + "");

            Intent inten = new Intent(mcontext, recepe.class);
            inten.putExtra("title", model.getTitle());
            inten.putExtra("category", model.getCategory());
            inten.putExtra("ingredients", (Serializable) model.getIngredients());
                inten.putExtra("steps", (Serializable) model.getSteps());
            inten.putExtra("img", model.getPicUri());
            inten.putExtra("userName", currentUser);
            inten.putExtra("publishedByUser", model.isPublishedByUser());
            inten.putExtra("recipeId", model.getRecipeId());
            inten.putExtra("timestamp", model.getTimestamp());
            inten.putExtra("isProfile", model.isProfile());
            inten.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            holder.root.getContext().startActivity(inten);

        });

    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.carrdview_item, parent, false);
        return new myviewholder(view);
    }


    class myviewholder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView name;
        LinearLayout root;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            name = (TextView) itemView.findViewById(R.id.tv);
            root = (LinearLayout) itemView.findViewById(R.id.profile);


        }
    }
}
