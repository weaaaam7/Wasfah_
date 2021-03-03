package com.example.wasfah;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wasfah.model.IngredientModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class recepe extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private TextView title_tv;
    private ImageView image, back;
    private TextView tv_ingrediants;
    private TextView tv_category;
    private TextView tv_steps;
    private TextView tv_timestamp;
    private String str = "";
    private EditText comment;
    private Button addComment, dots;
    private FirebaseAuth fAuth;
    private FirebaseUser user;
    private FirebaseDatabase db;
    RecyclerView RvComment;
    commentAdapter adapter;
    List<Comment> listComment;
    String recpieId;
    boolean publishedByUser = true;

    // favorite list
    private Button fav_r;
    boolean faved = false;
    DatabaseReference favList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recepe);

        //get Intent
        Intent intent = getIntent();
        String img = intent.getExtras().getString("img");
        String tilte = intent.getExtras().getString("title");
        String category = intent.getExtras().getString("category");
        String userName = intent.getExtras().getString("userName");
        String timestamp = intent.getExtras().getString("timestamp");
        boolean isProfile = intent.getExtras().getBoolean("isProfile");
        recpieId = intent.getExtras().getString("recipeId");
        publishedByUser = intent.getExtras().getBoolean("publishedByUser");
        List<IngredientModel> ingredients = (List<IngredientModel>) intent.getSerializableExtra("ingredients");
        List<Steps> steps = (List<Steps>) intent.getSerializableExtra("steps");

        //Ininilize
        tv_ingrediants=(TextView) findViewById(R.id.Ingred_val);
        tv_steps=(TextView) findViewById(R.id.Steps_val);
        title_tv=(TextView) findViewById(R.id.tv);
        tv_category=(TextView) findViewById(R.id.categ);
        image=(ImageView) findViewById(R.id.img2);
        comment=(EditText) findViewById(R.id.editText);
        addComment=(Button) findViewById(R.id.button);
        RvComment=(RecyclerView) findViewById(R.id.commentRec);
        tv_timestamp=(TextView) findViewById(R.id.date);
        dots=(Button) findViewById(R.id.bU1);
        back=(ImageView) findViewById(R.id.back);
        fav_r = (Button) findViewById(R.id.fav_r);

        //hide and display 3 dots

        if (publishedByUser) {
            dots.setVisibility(View.VISIBLE);

        } else {
            dots.setVisibility(View.INVISIBLE);
        }


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        //Firebase

        fAuth=FirebaseAuth.getInstance();
        user=fAuth.getCurrentUser();
        db=FirebaseDatabase.getInstance();
        String uid=user.getUid();

        // fav

        favList = db.getReference("FavoriteList");
        favList.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(recpieId).hasChild(uid))
                {
                    fav_r.setBackgroundResource(R.drawable.ic_favorite_on);
                } else {
                    fav_r.setBackgroundResource(R.drawable.ic_favorite_off);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        fav_r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                faved = true;
                    favList.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (faved) {
                                if (snapshot.child(recpieId).hasChild(uid)) {
                                    favList.child(recpieId).child(uid).removeValue();
                                    faved = false;
                                } else {
                                    favList.child(recpieId).child(uid).setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            showMessage("Added to your favorite list");
                                            faved = false;
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            showMessage("fail to add to your favorite list: " + e.getMessage());
                                        }
                                    });

                                    faved = false;
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

            }
        });

        addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addComment.setVisibility(View.INVISIBLE);
                DatabaseReference commentRef = db.getReference("Recipes").child(recpieId).child("comment").push();
                String comment_content = comment.getText().toString();
                String uName=userName;
                String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                Comment comment1 = new Comment(comment_content, uid, uName,date);

                commentRef.setValue(comment1).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        showMessage("comment Added");
                        comment.setText("");
                        comment.setHint("Add comment");
                        addComment.setVisibility(View.VISIBLE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showMessage("fail to add comment : " + e.getMessage());
                    }
                });

            }
        });


        //initi Recycler view
        initRvComment();

        //add values
        title_tv.setText(tilte);
        tv_timestamp.setText(timestamp);

        tv_category.setText(category);
        Picasso.get().load(img).into(image);
        for (int i = 0; i < ingredients.size(); i++) {
            str += (i + 1) + "- " + ingredients.get(i).getName();
            if (i < ingredients.size() - 1) {
                str += "\n\n";
            }

        }
        tv_ingrediants.setText(str);
        str = "";
        if (steps!=null)
            for (int i = 0; i < steps.size(); i++) {
                str += (i + 1) + "- " + steps.get(i).getDescription();
                if (i < steps.size() - 1) {
                    str += "\n\n";
                }
            }
        tv_steps.setText(str);


    }

    //show popup

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.edit_recepie_menu);
        popup.show();

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.edit:
                //Toast.makeText(this,"Edit recepe is clicked",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), EditRecipeActivity.class);
                intent.putExtra("rec",this.recpieId);
                startActivity(intent);
                return true;


            case R.id.delete:

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Are you sure you want to delete recipe?")
                        .setCancelable(false)

                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                DatabaseReference dRec = FirebaseDatabase.getInstance().getReference("Recipes").child(recpieId);
                                dRec.removeValue();
                                Toast.makeText(recepe.this, "Recipe is deleted", Toast.LENGTH_SHORT).show();
                                finish();
                            }

                        })

                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });


                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.yellow2));
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.yellow2));

                return true;

            case R.id.fav:
             /*   //Firebase
                fAuth=FirebaseAuth.getInstance();
                user=fAuth.getCurrentUser();
                db=FirebaseDatabase.getInstance();
                String uid=user.getUid();
                // fav
                favList = db.getReference("FavoriteList").child(uid).push();

                if (!faved) {
                    favList.setValue(recpieId).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            showMessage("Added to your favourite list");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            showMessage("fail to add it to your favourite list : " + e.getMessage());
                        }
                    });
                } */

                return true;

            default:
                return false;
        }

    }

    private void initRvComment() {
        RvComment.setLayoutManager(new LinearLayoutManager(this));
        DatabaseReference commentRef = db.getReference("Recipes").child(recpieId).child("comment");
        commentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listComment = new ArrayList<>();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Comment comment = snap.getValue(Comment.class);
                    listComment.add(comment);
                }

                adapter = new commentAdapter(getApplicationContext(), listComment);
                RvComment.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

}