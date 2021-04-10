package com.example.wasfah;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MessagesHolder extends RecyclerView.ViewHolder {
    TextView senderMessage,recieverMessage,stts;
    ImageView mim,yim;
    public MessagesHolder(@NonNull View itemView) {
        super(itemView);
        senderMessage = itemView.findViewById(R.id.senderMessage);
        recieverMessage = itemView.findViewById(R.id.recieverMessage);
       mim= itemView.findViewById(R.id.mim);
       yim = itemView.findViewById(R.id.yim);
        stts = (TextView)itemView.findViewById(R.id.status);
    }
}
