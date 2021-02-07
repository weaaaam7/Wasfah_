package com.example.wasfah;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

     private Context mcontext;
     private List<RecipeInfo> mData;

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


        holder.tv_title.setText(mData.get(position).getTitle());
        holder.img.setImageResource(mData.get(position).getImg());

        // set Click lisner
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inten = new Intent(mcontext, recepe.class);
                inten.putExtra("title",mData.get(position).getTitle());
                inten.putExtra("img",mData.get(position).getImg());
                mcontext.startActivity(inten);

            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tv_title;
        ImageView img;
        CardView cardView;
        public MyViewHolder(View itemView)
        {
            super((itemView));

            tv_title=(TextView) itemView.findViewById(R.id.tv);
            img=(ImageView) itemView.findViewById(R.id.img);
            cardView=(CardView) itemView.findViewById(R.id.cardview);
        }
    }
}
