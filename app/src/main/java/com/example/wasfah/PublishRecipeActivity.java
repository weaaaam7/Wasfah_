package com.example.wasfah;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class PublishRecipeActivity extends AppCompatActivity {

    private Button uploadButton;
    private ImageView picture;
    private ProgressBar bar;
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Recipes");
    private StorageReference storRef = FirebaseStorage.getInstance().getReference();
    private Uri picURI;
    private LinearLayout layoutList;
    private Button buttonAdd;
    private Button buttonSubmitList;
    List<String> QList = new ArrayList<>();
    //ArrayList<RecipeHelperClass> cricketersList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_recipe);

        uploadButton = findViewById(R.id.uploadBut);
        picture = findViewById(R.id.uploadedP);
        bar = findViewById(R.id.pB);
        layoutList = findViewById(R.id.layout_list);
        buttonAdd = findViewById(R.id.add);
        //buttonSubmitList = findViewById(R.id.button_submit_list);

        buttonAdd.setOnClickListener((View.OnClickListener) this);
        buttonSubmitList.setOnClickListener((View.OnClickListener) this);


        QList.add("1");
        QList.add("2");
        QList.add("3");
        QList.add("4");


        bar.setVisibility(View.VISIBLE);

        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, 2);
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (picURI != null) {
                    uploadToFirebase(picURI);
                } else {
                    Toast.makeText(PublishRecipeActivity.this, "Please Select Image", Toast.LENGTH_SHORT);
                }

            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addView();
            }
        });

    }

    private void addView() {
        final View ingView = getLayoutInflater().inflate(R.layout.row_add,null,false);

        EditText editText = (EditText)ingView.findViewById(R.id.edit_cricketer_name);
        AppCompatSpinner spinnerTeam = (AppCompatSpinner)ingView.findViewById(R.id.spinner_team);
        ImageView imageClose = (ImageView)ingView.findViewById(R.id.image_remove);

        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,QList);
        spinnerTeam.setAdapter(arrayAdapter);

        imageClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeView(ingView);
            }
        });

        layoutList.addView(ingView);
    }

    private void removeView(View view) {
        layoutList.removeView(view);
    }

    @Override
    protected void onActivityResult ( int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            picURI = data.getData();
            picture.setImageURI(picURI);
        }
    }

    private void uploadToFirebase(Uri uri) {
        StorageReference fileRef = storRef.child(System.currentTimeMillis()+"."+getFileExtension(uri));
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        RecipeHelperClass recU = new RecipeHelperClass(uri.toString());
                        String ID = ref.push().getKey();
                        ref.child(ID).setValue(recU);
                        bar.setVisibility(View.INVISIBLE);
                        Toast.makeText(PublishRecipeActivity.this,"Uploaded Successfully", Toast.LENGTH_SHORT);
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                bar.setVisibility(View.VISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                bar.setVisibility(View.INVISIBLE);
                Toast.makeText(PublishRecipeActivity.this,"Uploading Failed", Toast.LENGTH_SHORT);
            }
        });
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }
}