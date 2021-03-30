package com.example.wasfah;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
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
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wasfah.translation.TranslationViewModel;
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
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class recepe extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
private TranslationViewModel translationViewModel;
    private TextView title_tv;
    private ImageView image,back;
    private TextView tv_ingrediants,Ingred_label;
    private TextView tv_category;
    private TextView tv_steps,Steps_label;
    private TextView tv_timestamp;
    private String ingeredientsStr="";
    private String stepsStr="";
    private EditText comment;
    private Button addComment,dots,deleteComment;
    private TextView translateBtn;
    private FirebaseAuth fAuth;
    private FirebaseUser user;
    private FirebaseDatabase db;
    RecyclerView RvComment;
    commentAdapter adapter;
    List<Comment> listComment;
    String recpieId;
    boolean publishedByUser=true;
    boolean commentedByUser=true;
    List<Ingredients> ingredients;
    List<Steps> steps;
    // favorite list
    private Button fav_r;
    boolean faved = false;
    DatabaseReference favList;
    private String deletedComment= null;



    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recepe);
translationViewModel = ViewModelProviders.of(this).get(TranslationViewModel.class);
        //get Intent
        Intent intent = getIntent();
        String img = intent.getExtras().getString("img");
        String tilte = intent.getExtras().getString("title");
        String category = intent.getExtras().getString("category");
        String userName = intent.getExtras().getString("userName");
        String timestamp = intent.getExtras().getString("timestamp");
        boolean isProfile = intent.getExtras().getBoolean("isProfile");
        recpieId = intent.getExtras().getString("recipeId");
        publishedByUser=intent.getExtras().getBoolean("publishedByUser");
        commentedByUser=intent.getExtras().getBoolean("commentedByUser");
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
        tv_timestamp=(TextView) findViewById(R.id.date);
        dots=(Button) findViewById(R.id.bU1);
        back=(ImageView) findViewById(R.id.back);
        translateBtn=(TextView) findViewById(R.id.translateBtn);
       // deleteComment=(Button)findViewById(R.id.delButton);;
        Ingred_label=(TextView) findViewById(R.id.Ingred_label);
        Steps_label=(TextView) findViewById(R.id.Steps_label);
        fav_r = (Button) findViewById(R.id.fav_r);

        //hide and display 3 dots

        if (publishedByUser){
            dots.setVisibility(View.VISIBLE);

        }
        else{
            dots.setVisibility(View.INVISIBLE);
        }




        back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
            });
        translateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("ddddd", "dddddd");
                //translationViewModel.translateIngerediants(stepsStr);
                subscribeObserver();
                subscribeObserverIngredients();
                subscribeObserverSteps();

            }
        });
//        if (commentedByUser){
//            deleteComment.setVisibility(View.VISIBLE);
//
//        }
//        else{
//            deleteComment.setVisibility(View.INVISIBLE);
//        }
//        deleteComment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DatabaseReference dComment = FirebaseDatabase.getInstance().getReference("Recipes").child(recpieId).child("comment");
//                dComment.removeValue();
//                Toast.makeText(recepe.this, "Comment is deleted", Toast.LENGTH_SHORT).show();
//            }
//        });

        //Firebase


            fAuth=FirebaseAuth.getInstance();
        user=fAuth.getCurrentUser();
        db=FirebaseDatabase.getInstance();
        String uid = user.getUid();

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
                                showMessage("Removed your favorite list");
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
                String timestamp=String.valueOf(System.currentTimeMillis());
                DatabaseReference commentRef=db.getReference("Recipes").child(recpieId).child("comment");
                HashMap<String,Object> hashmap=new HashMap<>();
                hashmap.put("timestamp",timestamp);
                hashmap.put("content",comment.getText().toString());
                hashmap.put("uid",user.getUid());
                hashmap.put("uname",userName);
                hashmap.put("date",new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date()));

                commentRef.child(timestamp).setValue(hashmap).addOnSuccessListener(new OnSuccessListener<Void>() {
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
        tv_timestamp.setText(timestamp);

        tv_category.setText(category);
        Picasso.get().load(img).into(image);
        for (int i=0;i<ingredients.size();i++) {
            ingeredientsStr += (i + 1) + "- " + ingredients.get(i).getFullName()+".";
            if (i < ingredients.size() - 1) {
                ingeredientsStr += "\n\n";
            }
        }
//        Set<Ingredients> ingredientsSet= new HashSet<>();
//
//        for (int i=0;i<ingredients.size();i++){
//            ingredientsSet.add(ingredients.get(i));
//        }
//        JSONObject multiple = new JSONObject();
//        // create a new Gson object
//        Gson gson = new Gson();
//
//        // convert your set to json
//        String jsonUsersSet = gson.toJson(ingredients);
//
//        // print your generated json
//        System.out.println("jsonUsersSet: " + jsonUsersSet);
//
//        System.out.println("Multiple ingredients: " + multiple);
//

        tv_ingrediants.setText(ingeredientsStr);

        for (int i=0;i<steps.size();i++){
            stepsStr+=(i+1)+"- "+steps.get(i).getDescription();
            if(i<steps.size()-1) {
                stepsStr += "\n\n";
            }
        }
        tv_steps.setText(stepsStr);






    }

    private String getTranslatedText() {
        List<String> translatedArray = new ArrayList<>();
        translatedArray.add(String.valueOf(title_tv.getText()));
        translatedArray.add(String.valueOf(tv_category.getText()));
        translatedArray.add(String.valueOf(Ingred_label.getText()));
        translatedArray.add(String.valueOf(Steps_label.getText()));

        Log.d("ndlnjc","jfrhkjb"+String.valueOf(Steps_label.getText()));

        return convertListToString(translatedArray);

    }

    private String convertListToString(List<String> translatedArray){
        String listString = "";
        for (String s : translatedArray)
        {
            listString += s + "#";
        }

        return listString;
    }
    //show popup

    public void showPopup(View v){
        PopupMenu popup = new PopupMenu(this,v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.edit_recepie_menu);
        popup.show();

    }

    @Override
    public boolean onMenuItemClick(MenuItem item){

        switch (item.getItemId())
        {
            case R.id.edit:
                //Toast.makeText(this,"Edit recepe is clicked",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), EditRecipeActivity.class);
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

                return true;


            default:return false;
        }

    }

    public void initRvComment() {
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

               adapter = new commentAdapter(getApplicationContext(),listComment,listComment,publishedByUser);

               RvComment.setAdapter(adapter);


               new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
                   @Override
                   public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                       return false;
                   }

                   @Override
                   public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                     adapter.deleteComment(viewHolder.getAdapterPosition(),recpieId);
                     adapter.notifyDataSetChanged();
                       Toast.makeText(recepe.this, "Comment is deleted", Toast.LENGTH_SHORT).show();
                   }

                   @Override
                   public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                       new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                               .addBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.red))
                               .addActionIcon(R.drawable.ic_baseline_delete_24)
                               .create()
                               .decorate();

                       super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                   }
               }).attachToRecyclerView(RvComment);


           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });
    }


    private void showMessage(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
    }
    private void subscribeObserver() {
        translationViewModel.translateText(getTranslatedText());
        translationViewModel.translateText(getTranslatedText()).observe(this, translationResponse -> {
                    String test = translationResponse.getData().getTranslations().get(0).getTranslatedText();
                    Log.d("ddddds", "dddddd" + test);
                    List<String> items = Arrays.asList(test.split("\\s*#\\s*"));
                    title_tv.setText(items.get(0));
                    tv_category.setText(items.get(1));
                    Ingred_label.setText(items.get(2));
                    Steps_label.setText(items.get(3));

                }
        );
    }
    private void subscribeObserverIngredients() {


//        for (int i = 0; i < ingredients.size(); i++) {
//            ingeredientsStr += (i + 1) + "- " + ingredients.get(i).getFullName();
//            if (i < ingredients.size() - 1) {
//               // ingeredientsStr += "\n\n";
//            }
            translationViewModel.translateIngredients(ingeredientsStr);
            translationViewModel.translateIngredients(ingeredientsStr).observe(this, translationResponse -> {
                        String test = translationResponse.getData().getTranslations().get(0).getTranslatedText() + "\n \n";
                        Log.d("ddddds222", "dddddd" + test);

                        String [] text = test.split(".");
                        for(int i=0; i<= text.length; i++){
                            test+=text[i]+"\n";
                            tv_ingrediants.setText(test);
                        }


                    }
            );


    }

    private void subscribeObserverSteps() {
        translationViewModel.translateSteps(stepsStr);
        translationViewModel.translateSteps(stepsStr).observe(this, translationResponse -> {
                    String test = translationResponse.getData().getTranslations().get(0).getTranslatedText();
                    Log.d("ddddd123", "dddddd" + test);

                    tv_steps.setText(test);
                }



        ); }

    }
