package com.example.wasfah;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class WishlistHolder extends RecyclerView.ViewHolder {
   ImageView dot;
    TextView textproductName,textproductPrice,time;
    ImageView imageView;

    public WishlistHolder(@NonNull View itemView) {
        super(itemView);
        textproductName = itemView.findViewById(R.id.productName2);
        textproductPrice = itemView.findViewById(R.id.productPrice2);
        time = itemView.findViewById(R.id.time3);
        dot = (ImageView)itemView.findViewById(R.id.dot);
        imageView = (ImageView)itemView.findViewById(R.id.lay);


    }
}
