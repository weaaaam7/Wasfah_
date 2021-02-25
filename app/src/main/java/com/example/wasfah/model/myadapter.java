package com.example.wasfah.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wasfah.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

public class myadapter extends FirebaseRecyclerAdapter<RecipeModel,myadapter.myviewholder>
{
    public myadapter(@NonNull FirebaseRecyclerOptions<RecipeModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull RecipeModel model)
    {
        holder.name.setText(model.getTitle());
        //Glide.with(holder.img.getContext()).load(model.getPicUri()).into(holder.img);
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.carrdview_item,parent,false);
        return new myviewholder(view);
    }


    class myviewholder extends RecyclerView.ViewHolder
    {
        //CircleImageView img;
        TextView name;
        public myviewholder(@NonNull View itemView)
        {
            super(itemView);
           // img=itemView.findViewById(R.id.img);
            name=(TextView)itemView.findViewById(R.id.tv);
        }
    }
}
