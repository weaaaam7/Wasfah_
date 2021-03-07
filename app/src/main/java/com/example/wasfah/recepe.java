package com.example.wasfah;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import com.google.cloud.translate.*;
import com.example.wasfah.HomeFragments.HealthyFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.common.model.RemoteModelManager;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.TranslateRemoteModel;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class recepe extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private TextView title_tv;
    private ImageView image,back;
    private TextView tv_ingrediants;
    private TextView tv_category;
    private TextView tv_steps;
    private TextView tv_timestamp;
    private String str="";
    private EditText comment;
    private Button addComment,dots;
    private FirebaseAuth fAuth;
    private FirebaseUser user;
    private FirebaseDatabase db;
    RecyclerView RvComment;
    commentAdapter adapter;
    List<Comment> listComment;
    String recpieId;
    boolean publishedByUser=true;
    String tilte;
    boolean flag=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recepe);
        if (Pref.getValue(getApplicationContext(),"language_checked", "false").equalsIgnoreCase("true"))
        {
            setApplicationLocale("ar");
        }
        else
        {
           setApplicationLocale("en");
        }
        //get Intent
        Intent intent = getIntent();
        String img = intent.getExtras().getString("img");
        tilte = intent.getExtras().getString("title");
        String category = intent.getExtras().getString("category");
        String userName = intent.getExtras().getString("userName");
        String timestamp = intent.getExtras().getString("timestamp");
        boolean isProfile = intent.getExtras().getBoolean("isProfile");
        recpieId = intent.getExtras().getString("recipeId");
        publishedByUser=intent.getExtras().getBoolean("publishedByUser");
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
        dots=(Button) findViewById(R.id.b1);
        back=(ImageView) findViewById(R.id.back);

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

        //Firebase

        String language_checked=Pref.getValue(this,"test", "false");

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
                String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                Comment comment1= new Comment(comment_content,uid,uName,date);

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
        tv_timestamp.setText(timestamp);

        tv_category.setText(category);
        Picasso.get().load(img).into(image);
        for (int i=0;i<ingredients.size();i++){
            str+=(i+1)+"- "+ingredients.get(i).getFullName();
            if(i<ingredients.size()-1) {
                str += "\n\n";
            }

        }
        tv_ingrediants.setText(str);
        str="";
        for (int i=0;i<steps.size();i++){
            str+=(i+1)+"- "+steps.get(i).getDescription();
            if(i<steps.size()-1) {
                str += "\n\n";
            }
        }
        tv_steps.setText(str);



//
//        TranslatorOptions options =
//                new TranslatorOptions.Builder()
//                        .setSourceLanguage(TranslateLanguage.ENGLISH)
//                        .setTargetLanguage(TranslateLanguage.ARABIC)
//                        .build();
//        final Translator englishGermanTranslator =
//                Translation.getClient(options);
//        DownloadConditions conditions = new DownloadConditions.Builder()
//                .requireWifi()
//                .build();
//        englishGermanTranslator.downloadModelIfNeeded(conditions)
//                .addOnSuccessListener(
//                        (OnSuccessListener) v -> {
//
//                                englishGermanTranslator.translate(tilte)
//                                        .addOnSuccessListener(
//                                                (OnSuccessListener) translatedText -> {
//                                                    title_tv.setText(translatedText.toString());
//                                                })
//                                        .addOnFailureListener(
//                                                new OnFailureListener() {
//                                                    @Override
//                                                    public void onFailure(@NonNull Exception e) {
//                                                        // Error.
//                                                        // ...
//                                                    }
//                                                });
//
//                        })
//                .addOnFailureListener(
//                        new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                // Model couldn’t be downloaded or other internal error.
//                                // ...
//                            }
//                        });
//
//
//        Translate translate = TranslateOptions.getDefaultInstance().getService();
//        Translation translation = translate.translate(tilte);
//        title_tv.setText(translation.getTranslatedText());
//        String language_checked=Pref.getValue(this,"test", "false");

//        Translate2 translate = new Translate2();
//        translate.execute(tilte);
//        title_tv.setText(translate.getResult());

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
    public void setApplicationLocale(String locale) {
        Resources resources = getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(new Locale(locale.toLowerCase()));
        } else {
            config.locale = new Locale(locale.toLowerCase());
        }
        resources.updateConfiguration(config, dm);
    }
    private void showMessage(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
    }

}
