package com.example.wasfah;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.Serializable;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

     private Context mcontext;
     private List<RecipeInfo> mData;
     private String name;
     private String recipeId;

    public RecyclerViewAdapter(Context mcontext, List<RecipeInfo> mData,String userName) {
        this.mcontext = mcontext;
        this.mData = mData;
        this.name=userName;
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
        view=mInflater.inflate(R.layout.carrdview_item,parent,false);


        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        if (name !=null)
        {
           holder.profile.setVisibility(View.VISIBLE);
           holder.category.setVisibility(View.GONE);
        }
        else{

            holder.profile.setVisibility(View.GONE);
            holder.category.setVisibility(View.VISIBLE);
            holder.tv_title_cat.setText(mData.get(position).getTitle());
            holder.date.setText(mData.get(position).getTimestamp());
            holder.name.setText(mData.get(position).getName());
            Picasso.get().load(mData.get(position).getImg()).into(holder.img2);
        }
        holder.tv_title.setText(mData.get(position).getTitle());
//        Glide.with(mcontext).load(mData.get(position).getImg()).into(holder.img);
        Picasso.get().load(mData.get(position).getImg()).into(holder.img);
        // set Click lisner
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inten = new Intent(mcontext, recepe.class);
                inten.putExtra("title",mData.get(position).getTitle());
                inten.putExtra("category",mData.get(position).getCategory());
                inten.putExtra("ingredients", (Serializable) mData.get(position).getIngredients());
                inten.putExtra("steps", (Serializable) mData.get(position).getSteps());
                inten.putExtra("img",mData.get(position).getImg());
                if (name !=null)
                {inten.putExtra("userName",name);}
                else {
                    inten.putExtra("userName", mData.get(position).getName());

                }
                inten.putExtra("publishedByUser",mData.get(position).isPublishedByUser());
                inten.putExtra("recipeId",mData.get(position).getRecipeId());
                inten.putExtra("timestamp",mData.get(position).getTimestamp());
                mcontext.startActivity(inten);

            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tv_title,tv_title_cat;
        ImageView img,img2;
        CardView cardView;
        LinearLayout profile,category;
        TextView name,date;
        public MyViewHolder(View itemView)
        {
            super((itemView));

            tv_title=(TextView) itemView.findViewById(R.id.tv);
            img=(ImageView) itemView.findViewById(R.id.img);
            img2=(ImageView) itemView.findViewById(R.id.img2);
            cardView=(CardView) itemView.findViewById(R.id.cardview);
            tv_title_cat=(TextView) itemView.findViewById(R.id.tv2);
            name=(TextView) itemView.findViewById(R.id.name);
            date=(TextView) itemView.findViewById(R.id.date);
            profile=(LinearLayout) itemView.findViewById(R.id.profile);
            category=(LinearLayout) itemView.findViewById(R.id.category);
        }
    }
}
