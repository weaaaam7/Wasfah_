package com.example.wasfah;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import model.Messages;


public class ChatActivity extends AppCompatActivity {


    private String messageSenderId,messageRecieverId,messagepid,saveCurrentdate,saveCurrenttime,productRandomKey,downloadImageUrl,ppp;
    private FirebaseRecyclerOptions<Messages> options;
    private FirebaseRecyclerAdapter<Messages,MessagesHolder> adapter;
    private RecyclerView recyclerView;
    private TextView inputmessage;
    private ImageButton send_message_btn,image_btn;
    private ImageView bottom,imagechat;
    private CircleImageView im;
    private Uri imageUri;
    private static int gallerypick=1;
    private String check="";
    private ProgressDialog loadingbar;
    private DatabaseReference rootRef,Myproductref,Myhomestatusref,reference;
    private String checkifmessageisempty="";
    private StorageReference productImagesRef;
    private Button vd;
    //private String fromname,fromname2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        loadingbar = new ProgressDialog(this);
        Myproductref = FirebaseDatabase.getInstance().getReference().child("MyMessages");
        Myhomestatusref = FirebaseDatabase.getInstance().getReference().child("Myhomestatus");
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentdate=currentDate.format(calendar.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrenttime=currentTime.format(calendar.getTime());
        productRandomKey=saveCurrentdate+"  "+saveCurrenttime;

        messageSenderId = getIntent().getStringExtra("senderId");
        // productId = getIntent().getStringExtra("senderName");
        messageRecieverId = getIntent().getStringExtra("recieverId");
        messagepid = getIntent().getStringExtra("productId");
        rootRef = FirebaseDatabase.getInstance().getReference();

        send_message_btn = (ImageButton)findViewById(R.id.send_message_btn);

        bottom = (ImageView)findViewById(R.id.down);
        bottom.setVisibility(View.INVISIBLE);
        imagechat = (ImageView)findViewById(R.id.backchat);
        inputmessage = (TextView)findViewById(R.id.input_message) ;


        //  recieverMessage = (TextView)findViewById(R.id.recieverMessage) ;
        // senderMessage = (TextView)findViewById(R.id.senderMessage) ;
        imagechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!checkifmessageisempty.equals(""))
                {
                    scroll();
                }}
        });


        send_message_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });


        reference = FirebaseDatabase.getInstance().getReference().child("Messages");

        recyclerView = findViewById(R.id.private_message);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        options=new FirebaseRecyclerOptions.Builder<Messages>().setQuery(reference.child(messageRecieverId).child(messagepid).child(messageSenderId),Messages.class).build();

        adapter = new FirebaseRecyclerAdapter<Messages, MessagesHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull final MessagesHolder messagesHolder, int i, @NonNull final Messages messages) {

                final String me = FirebaseAuth.getInstance().getCurrentUser().getUid();

                final String fromUserId = messages.getFrom();
                final String fromMessageType = messages.getType();

                if(fromMessageType.equals("text"))
                {
                    messagesHolder.recieverMessage.setVisibility(View.INVISIBLE);
                    messagesHolder.senderMessage.setVisibility(View.INVISIBLE);
                    messagesHolder.mim.setVisibility(View.INVISIBLE);
                    messagesHolder.yim.setVisibility(View.INVISIBLE);
                }
                else
                {
                    messagesHolder.recieverMessage.setVisibility(View.INVISIBLE);
                    messagesHolder.senderMessage.setVisibility(View.INVISIBLE);
                    messagesHolder.mim.setVisibility(View.INVISIBLE);
                    messagesHolder.yim.setVisibility(View.INVISIBLE);
                }

                if(!fromUserId.equals(me))
                {

                    messagesHolder.recieverMessage.setVisibility(View.VISIBLE);
                        messagesHolder.recieverMessage.setText(messages.getMessage());
                        checkifmessageisempty = messages.getMessage();

                        //  messagesHolder.senderMessage.setVisibility(View.INVISIBLE);


                }
                else {


                        reference.child(messageSenderId).child(messagepid).child(messageRecieverId).child(messages.getPushId())
                                .addValueEventListener(new ValueEventListener() {

                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        if (dataSnapshot.exists()) {
                                            messagesHolder.stts.setVisibility(View.VISIBLE);
                                            Messages Messages = dataSnapshot.getValue(Messages.class);
                                            String st = Messages.getStatus();
                                            if (st.equals("seen")) {
                                                messagesHolder.stts.setText("seen");
                                            } else {
                                                messagesHolder.stts.setText("delivered");
                                            }

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                        messagesHolder.senderMessage.setVisibility(View.VISIBLE);
                        messagesHolder.senderMessage.setText(messages.getMessage());
                        // messagesHolder.recieverMessage.setVisibility(View.INVISIBLE);
                        checkifmessageisempty = messages.getMessage();




                }



                messagesHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if(fromUserId.equals(me))
                            new AlertDialog.Builder(ChatActivity.this)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setTitle("Are You Sure")
                                    .setMessage("Do you want to delete this Message?")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int i) {
                                            reference.child(messageSenderId).child(messagepid).child(messageRecieverId).child(messages.getPushId())
                                                    .removeValue();
                                            reference.child(messageRecieverId).child(messagepid).child(messageSenderId).child(messages.getPushId())
                                                    .removeValue();
                                        }
                                    })
                                    .setNegativeButton("No",null)
                                    .show();
                        return true;
                    }
                });

                HashMap<String,Object> productMap2 = new HashMap<>();
                productMap2.put("status","seen");
                reference.child(messageRecieverId).child(messagepid).child(messageSenderId).child(messages.getPushId()).updateChildren(productMap2);
                HashMap<String,Object> productMap = new HashMap<>();
                productMap.put("status","seen");
                Myproductref.child(messageRecieverId).child(messagepid).updateChildren(productMap);
                HashMap<String,Object> productMap3 = new HashMap<>();
                productMap3.put("homestatus","seen");
                Myhomestatusref.child(messageRecieverId).updateChildren(productMap3);

            }


            @NonNull
            @Override
            public MessagesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_chatbar_layout,parent,false);
                return new MessagesHolder(v);

            }
        };
        adapter.startListening();

        recyclerView.setAdapter(adapter);

    }

    private void scroll() {
        recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
    }



    private void sendMessage() {

        String messageText = inputmessage.getText().toString();
        if(TextUtils.isEmpty(messageText)&&check.equals(""))
        {
            Toast.makeText(this, "Type Message", Toast.LENGTH_SHORT).show();
        }

        else
        {
            downloadImageUrl="";
            String messageSenderRef = "Messages/" +messageSenderId+"/"+  messagepid+"/"+messageRecieverId;
            String messageRecieverRef = "Messages/" +messageRecieverId+"/"+  messagepid+"/"+messageSenderId;
            DatabaseReference userMessageKeyRef = rootRef.child("Messages").child(messageSenderId).child(messagepid).child(messageRecieverId).push();
            String messagePushId = userMessageKeyRef.getKey();
            Map messageTextBody = new HashMap();
            messageTextBody.put("message",messageText);
            messageTextBody.put("type","text");
            messageTextBody.put("from",messageRecieverId);
            messageTextBody.put("status","unseen");
            messageTextBody.put("pid",messagepid);
            messageTextBody.put("time",productRandomKey);

            messageTextBody.put("pushId",messagePushId);
            messageTextBody.put("image",downloadImageUrl);

            Map messageBodyDetails = new HashMap();
            messageBodyDetails.put(messageSenderRef + "/" +messagePushId , messageTextBody);
            messageBodyDetails.put(messageRecieverRef + "/" +messagePushId , messageTextBody);
            rootRef.updateChildren(messageBodyDetails).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful())
                    {
                        recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
                    }
                    else
                    {
                        Toast.makeText(ChatActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                    inputmessage.setText("");

                }
            });

            HashMap<String,Object> productMap = new HashMap<>();
            productMap.put("pid",messagepid);
            productMap.put("from",messageRecieverId);
            productMap.put("time",productRandomKey);

            //productMap.put("fromname",fromname);
            productMap.put("status","unseen");
            Myproductref.child(messageSenderId).child(messagepid).updateChildren(productMap);

            HashMap<String,Object> productMap3 = new HashMap<>();
            productMap3.put("homestatus","unseen");
            Myhomestatusref.child(messageSenderId).updateChildren(productMap3);

            HashMap<String,Object> productMap2 = new HashMap<>();
            productMap2.put("pid",messagepid);
            productMap2.put("from",messageSenderId);
            productMap2.put("time",productRandomKey);
            // productMap2.put("fromname",fromname2);
            Myproductref.child(messageRecieverId).child(messagepid).updateChildren(productMap2);

        }
    }




}