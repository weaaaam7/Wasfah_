package com.example.wasfah;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class commentAdapter extends RecyclerView.Adapter<commentAdapter.CommentVewHolder> {

    private Context mContext;
    private List<Comment> mData;
    private List<Comment> mDatabk;
    private boolean publishedByUser;
    View view;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String userID = user.getUid();
    public commentAdapter(Context mContext, List<Comment> mData, List<Comment> mDatabk,boolean publishedByUser) {
        this.mContext = mContext;
        this.mData = mData;
        this.mDatabk=mDatabk;
        this.publishedByUser=publishedByUser;
    }

    @NonNull
    @Override
    public CommentVewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view=mInflater.inflate(R.layout.row_comment,parent,false);

        return new CommentVewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentVewHolder holder, int position) {
        holder.name.setText(mData.get(position).getUname() + " - "+mData.get(position).getDate());
        holder.comment.setText(mData.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public List<Object> getData() {
        return null;
    }

    public void deleteComment(int position,String recpieId){
        DatabaseReference dRec = FirebaseDatabase.getInstance().getReference("Recipes").child(recpieId).child("comment");
        dRec.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (userID.equalsIgnoreCase(mData.get(position).getUid())||publishedByUser)
                    {
                        dRec.child(mData.get(position).getTimestamp()).removeValue();
                    }
                    else if (!publishedByUser|| !userID.equalsIgnoreCase(mData.get(position).getUid())) {
                        Toast.makeText(mContext, "you are not authorized to delete this comment", Toast.LENGTH_SHORT).show();

                    }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public class CommentVewHolder extends RecyclerView.ViewHolder {

        TextView name,comment;
        Button deleteComment;
        public CommentVewHolder(@NonNull View itemView) {
            super(itemView);
            name =(TextView) itemView.findViewById(R.id.textView5);
            comment =(TextView) itemView.findViewById(R.id.textView6);
        }
    }
}

