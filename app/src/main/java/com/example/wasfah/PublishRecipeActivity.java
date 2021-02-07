package com.example.wasfah;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.ReceiverCallNotAllowedException;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.wasfah.database.AuthenticationManager;
import com.example.wasfah.database.RecipeFirebaseManager;
import com.example.wasfah.model.IngredientModel;
import com.example.wasfah.model.RecipeModel;
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

    public static final int GALLERY_ACT_REQ_CODE = 2;
    RecipeFirebaseManager recipeFirebaseManager = new RecipeFirebaseManager();
    private Button publishButton;
    private ImageView picture;
    private String currentModelPic;
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Recipes");
    private StorageReference storRef = FirebaseStorage.getInstance().getReference();

    private Uri picURI;
    private Button buttonAdd;
    private List<IngredientModel> ingredientsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_recipe);

        publishButton = findViewById(R.id.uploadBut);
        picture = findViewById(R.id.uploadedP);

        buttonAdd = findViewById(R.id.add);

        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_ACT_REQ_CODE);
            }
        });

        publishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (picURI != null) {
                    uploadToFirebase(picURI);

                } else {
                    Toast.makeText(PublishRecipeActivity.this, "Please Select Image", Toast.LENGTH_SHORT);

                }

                publishRecipe(v);

            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addIngredientRow(v);
            }
        });
        this.setCategoriesSpinnerItems();
        this.setIngredientsListModel(this.ingredientsList);
    }

    private void setCategoriesSpinnerItems()
    {
        Spinner spinner = (Spinner) findViewById(R.id.cats_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.ingredient_categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

    }

    private void setIngredientsListModel(List<IngredientModel> models)
    {
        ListView ingredientsListView = (ListView) findViewById(R.id.ingredients_list_view);
        IngredientsListAdapter adapter = new IngredientsListAdapter(this,
                R.layout.row_add, models);

        ingredientsListView .setAdapter(adapter);
    }
    public void addIngredientRow(View view)
    {
        this.addBlankIngredientsListView(new IngredientModel());
    }



    @Override
    protected void onActivityResult ( int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_ACT_REQ_CODE && resultCode == RESULT_OK && data != null) {
            picURI = data.getData();
            picture.setImageURI(picURI);
            this.currentModelPic = picURI.toString();
        }

    }

    private void addBlankIngredientsListView(IngredientModel model)
    {
        this.ingredientsList.add(model);
        this.setIngredientsListModel(this.ingredientsList);
    }

    private void uploadToFirebase(Uri uri) {

      StorageReference fileRef = storRef.child(System.currentTimeMillis()+"."+getFileExtension(uri));
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                currentModelPic = uri.toString();
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Toast.makeText(PublishRecipeActivity.this,"Uploaded Successfully", Toast.LENGTH_SHORT);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PublishRecipeActivity.this,"Uploading Failed", Toast.LENGTH_SHORT);
            }
        });
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }


    public void publishRecipe(View view)
    {
        RecipeModel model = getRecipe();
        model.setCreatedBy(AuthenticationManager.CURRENT_USER_EMAIL);
        model.setPicUri(currentModelPic);
        recipeFirebaseManager.SaveRecipe(model,this);
    }

    private RecipeModel getRecipe()
    {
        RecipeModel model = new RecipeModel();
        Spinner catSpinner = findViewById(R.id.cats_spinner);
        EditText titleEdit = findViewById(R.id.recipe_title);
        EditText descEdit = findViewById(R.id.recipe_steps);

        String category = (String)catSpinner.getSelectedItem();
        String title = titleEdit.getText().toString();
        String steps = descEdit.getText().toString();

        model.setCategory(category);
        model.setTitle(title);
        model.setPreparationSteps(steps);
        model.setIngredients(this.ingredientsList);

        return model;
    }
}