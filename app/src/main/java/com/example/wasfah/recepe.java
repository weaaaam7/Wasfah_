package com.example.wasfah;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class recepe extends AppCompatActivity {

    private TextView title_tv;
    private ImageView image;
    private TextView tv_ingrediants;
    private TextView tv_category;
    private TextView tv_steps;
    private String str="";
    private EditText comment;
    private Button addComment;
    private FirebaseAuth fAuth;
    private FirebaseUser user;
    private FirebaseDatabase db;
    RecyclerView RvComment;
    commentAdapter adapter;
    List<Comment> listComment;
    String recpieId;


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
        recpieId = intent.getExtras().getString("recipeId");
        List<Ingredients> ingredients= (List<Ingredients>) intent.getSerializableExtra("ingredients");
        List<Steps> steps= (List<Steps>) intent.getSerializableExtra("steps");

        //Ininilize
        tv_ingrediants=(TextView) findViewById(R.id.Ingred_val);
        tv_steps=(TextView) findViewById(R.id.Steps_val);
        title_tv=(TextView) findViewById(R.id.tv);
        tv_category=(TextView) findViewById(R.id.categ);
        image=(ImageView) findViewById(R.id.img2);
        comment=(EditText) findViewById(R.id.editText);
        addComment=(Button) findViewById(R.id.button);
        RvComment=(RecyclerView) findViewById(R.id.commentRec);


        //Firebase

        fAuth=FirebaseAuth.getInstance();
        user=fAuth.getCurrentUser();
        db=FirebaseDatabase.getInstance();

        addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addComment.setVisibility(View.INVISIBLE);
                DatabaseReference commentRef=db.getReference("Recipes").child(recpieId).child("comment").push();
                String comment_content=comment.getText().toString();
                String uid=user.getUid();
                String uName=userName;
                Comment comment1= new Comment(comment_content,uid,uName);

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
                        showMessage("fail to add comment : "+e.getMessage());
                    }
                });

            }
        });

        //initi Recycler view
        initRvComment();

        //add values
        title_tv.setText(tilte);
        tv_category.setText(category);
        Picasso.get().load(img).into(image);
        for (int i=0;i<ingredients.size();i++){
            str+=ingredients.get(i).getFullName();
            if(i<ingredients.size()-1) {
                str += "\n\n";
            }

        }
        tv_ingrediants.setText(str);
        str="";
        for (int i=0;i<steps.size();i++){
            str+=steps.get(i).getDescription();
            if(i<steps.size()-1) {
                str += "\n\n";
            }
        }
        tv_steps.setText(str);






    }

    private void initRvComment() {
        RvComment.setLayoutManager(new LinearLayoutManager(this));
        DatabaseReference commentRef=db.getReference("Recipes").child(recpieId).child("comment");
       commentRef.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               listComment=new ArrayList<>();
               for (DataSnapshot snap:snapshot.getChildren()){
                   Comment comment=snap.getValue(Comment.class);
                   listComment.add(comment);
               }

               adapter = new commentAdapter(getApplicationContext(),listComment);
               RvComment.setAdapter(adapter);
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });
    }

    private void showMessage(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
    }
}