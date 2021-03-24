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

import java.io.Serializable;
import java.util.ArrayList;

public class MyAdapter2 extends RecyclerView.Adapter<MyAdapter2.MyViewHolder> {

    private String TAG="MyAdapter2";

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


    }

    @Override
    public int getItemCount() {
        return recipeModels.size();
    }



    static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView name;
        LinearLayout root;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            name = (TextView) itemView.findViewById(R.id.tv);
            root = (LinearLayout) itemView.findViewById(R.id.profile);


        }
    }
}
