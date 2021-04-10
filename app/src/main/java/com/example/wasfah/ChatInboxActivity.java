package com.example.wasfah;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wasfah.model.RecipeModel;
import com.example.wasfah.model.UserModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import model.Messages;
import model.MyMessages;



public class ChatInboxActivity extends AppCompatActivity {

    DatabaseReference reference,reference2;

    private FirebaseRecyclerOptions<Messages> options;
    private FirebaseRecyclerAdapter<Messages,WishlistHolder> adapter;
    private RecyclerView recyclerView;
    // private ImageView productImage;
    private String name ,time;
    private ImageView image;

    @Override
    public void onBackPressed() {

        finish();
        Intent intent = new Intent(ChatInboxActivity.this, ProfileView.class);
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_inbox);

        image = (ImageView)findViewById(R.id.backchatin);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(ChatInboxActivity.this, ProfileView.class);
                startActivity(intent);
            }
        });
        // productImage=(ImageView) findViewById(R.id.productImage2);
        reference = FirebaseDatabase.getInstance().getReference().child("MyMessages");
        reference2 = FirebaseDatabase.getInstance().getReference().child("Messages");
        recyclerView = findViewById(R.id.chatinboxrecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        options=new FirebaseRecyclerOptions.Builder<Messages>().setQuery(reference
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()),Messages.class).build();
        adapter = new FirebaseRecyclerAdapter<Messages, WishlistHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final WishlistHolder wishlistHolder, int i, @NonNull final Messages messages) {
                DatabaseReference productsref3 = FirebaseDatabase.getInstance().getReference().child("MyMessages");

                productsref3.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(messages.getPid())
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if(dataSnapshot.exists())
                                {

                                    MyMessages myMessages= dataSnapshot.getValue(MyMessages.class);
                                    time= myMessages.getTime();
                                    String status=myMessages.getStatus();
                                    if(status.equals("unseen"))
                                    {

                                        wishlistHolder.dot.setVisibility(View.VISIBLE);
                                        wishlistHolder.imageView.setBackgroundResource(R.drawable.custom_button);
                                    }
                                    else
                                    {
                                        wishlistHolder.dot.setVisibility(View.INVISIBLE);
                                    }

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                DatabaseReference productsref2 = FirebaseDatabase.getInstance().getReference().child("Users");
                productsref2.child(messages.getFrom()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists())
                        {
                            UserModel users = dataSnapshot.getValue(UserModel.class);
                            name = users.getName();

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                DatabaseReference productsref = FirebaseDatabase.getInstance().getReference().child("Recipes");
                productsref.child(messages.getPid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists())
                        {
                            RecipeModel products = dataSnapshot.getValue(RecipeModel.class);
                            wishlistHolder.textproductPrice.setText(products.getTitle());
                            wishlistHolder.textproductName.setText(name);
                            wishlistHolder.time.setText(time);


                        }
                        else
                        {
                            wishlistHolder.textproductName.setText("User has removed");
                            wishlistHolder.textproductPrice.setText("The Item");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                wishlistHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        new AlertDialog.Builder(ChatInboxActivity.this)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setTitle("Are You Sure")
                                .setMessage("Do you want to delete this Message?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int i) {
                                        reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(messages.getPid())
                                                .removeValue();
                                        reference2.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(messages.getPid()).child(messages.getFrom())
                                                .removeValue();
                                    }
                                })
                                .setNegativeButton("No",null)
                                .show();

                        return true;
                    }
                });


                wishlistHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(ChatInboxActivity.this,ChatActivity.class);
                        intent.putExtra("senderId",messages.getFrom());
                        intent.putExtra("recieverId",FirebaseAuth.getInstance().getCurrentUser().getUid());
                        intent.putExtra("productId",messages.getPid());
                        startActivity(intent);
                    }
                });


                //  getProductDetails(wish.getPid());
                //Picasso.get().load(wish.getImage()).into(wishlistHolder.imageView);
            }


            @NonNull
            @Override
            public WishlistHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.wish_layout,parent,false);
                return new WishlistHolder(v);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
}
