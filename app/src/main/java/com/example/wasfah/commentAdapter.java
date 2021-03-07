package com.example.wasfah;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class commentAdapter extends RecyclerView.Adapter<commentAdapter.CommentVewHolder> {

    private Context mContext;
    private List<Comment> mData;

    public commentAdapter(Context mContext, List<Comment> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public CommentVewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
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

    public class CommentVewHolder extends RecyclerView.ViewHolder {

        TextView name,comment;
        public CommentVewHolder(@NonNull View itemView) {
            super(itemView);
            name =(TextView) itemView.findViewById(R.id.textView5);
            comment =(TextView) itemView.findViewById(R.id.textView6);
        }
    }
}
