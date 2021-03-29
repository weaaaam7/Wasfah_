package com.example.wasfah;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.wasfah.database.AuthenticationManager;
import com.example.wasfah.model.IngredientModel;
import com.example.wasfah.model.RecipeModel;
import com.example.wasfah.model.StepModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class PublishRecipeActivity extends AppCompatActivity {

    public static final int GALLERY_ACT_REQ_CODE = 2;
    public static final int MAX_INGR_ITEMS =  15;
    public static final int MAX_STEPS_COUNT = 20;

    private Button publishButton;
    private static DatabaseReference db = FirebaseDatabase.getInstance("https://wasfah-126bf-default-rtdb.firebaseio.com").getReference().child("Recipes");
    private ImageView picture;
    private String currentModelPic;
    private StorageReference storRef = FirebaseStorage.getInstance().getReference();
    private Uri picURI;
    private Button buttonAdd;
    private Button addStepButton;
    private List<IngredientModel> ingredientsList = new ArrayList<>();
    private List<StepModel> stepsList = new ArrayList<>();
    private ImageView backHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_recipe);

        publishButton = findViewById(R.id.uploadBut);
        picture = findViewById(R.id.uploadedP);
        backHome = findViewById(R.id.back);
        buttonAdd = findViewById(R.id.add);
        this.addStepButton = findViewById(R.id.add_step_bt);

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
                    Toast.makeText(PublishRecipeActivity.this, "Please select a picture", Toast.LENGTH_SHORT).show();

                }



            }
        });

        backHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addIngredientRow(v);
            }
        });

        this.addStepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addStepRow(v);
            }
        });
        this.setCategoriesSpinnerItems();
        this.setIngredientsListModel(this.ingredientsList);
        this.setStepsListModel(this.stepsList);
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
        IngredientListAdapterP adapter = new IngredientListAdapterP(this,
                R.layout.row_add, models);
        ingredientsListView .setAdapter(adapter);
    }
    public void addIngredientRow(View view)
    {
        this.addBlankIngredientToListView(new IngredientModel());
    }
    private void addBlankIngredientToListView(IngredientModel model)
    {
        if(this.ingredientsList.size() >= MAX_INGR_ITEMS)
        {
            Toast.makeText(this, "The maximum number for ingredient is " + MAX_INGR_ITEMS, Toast.LENGTH_LONG).show();
            return;
        }else {
            this.ingredientsList.remove(model);
        }
        this.ingredientsList.add(model);
        this.setIngredientsListModel(ingredientsList);
    }

    private void setStepsListModel(List<StepModel> models)
    {
        ListView stepsListView = (ListView) findViewById(R.id.steps_list_view);

        StepListAdapterP adapter = new StepListAdapterP(this,
                R.layout.steps_row, models);
        stepsListView.setAdapter(adapter);
    }

    public void addStepRow(View view)
    {
        this.addBlankStepToListView(new StepModel());
    }

    private void addBlankStepToListView(StepModel model)
    {
        int order = StepsOrderUtil.getNextStepOrder(this.stepsList);
        if (order > MAX_STEPS_COUNT) {
            Toast.makeText(this, "The maximum number for steps is " + MAX_STEPS_COUNT, Toast.LENGTH_LONG).show();
            return;
        }
        model.setOrder(order);
        this.stepsList.add(model);
        this.setStepsListModel(this.stepsList);
    }
    @Override
    protected void onActivityResult ( int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_ACT_REQ_CODE && resultCode == RESULT_OK && data != null) {
            picURI = data.getData();
            picture.setImageURI(picURI);
//            this.currentModelPic = picURI.toString();
        }

    }


    private void uploadToFirebase(Uri uri) {

        StorageReference fileRef = storRef.child(System.currentTimeMillis()+"."+getFileExtension(uri));
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                currentModelPic = uri.toString();
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        currentModelPic= uri.toString();
                        publishRecipe();

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PublishRecipeActivity.this,"Uploading Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    public void publishRecipe()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userID = user.getUid();
        AuthenticationManager.CURRENT_USER_EMAIL = user.getEmail();
        RecipeModel model = getRecipe();
        model.setCreatedBy(AuthenticationManager.CURRENT_USER_EMAIL);
        model.setPicUri(currentModelPic);
        model.setCurrentUserId(userID);
        model.setTimestamp();
        if(this.validateModel(model)) {
            db.child(model.getRecipeId()).setValue(model)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(PublishRecipeActivity.this,"Recipe Published Successfully..", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
            Intent i = new Intent(this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
    }

    private RecipeModel getRecipe()
    {
        RecipeModel model = new RecipeModel();
        Spinner catSpinner = findViewById(R.id.cats_spinner);
        EditText titleEdit = findViewById(R.id.recipe_title);


        String category = (String)catSpinner.getSelectedItem();
        String title = getTextOrEmpty(titleEdit);
        model.setCategory(category);
        model.setTitle(title);
        model.setIngredients(this.ingredientsList);
        model.setPreparationSteps(this.stepsList);
        return model;
    }


    private boolean validateModel(RecipeModel model)
    {
        boolean isValid = true;

        if(!validateTitle(model.getTitle()))
        {
            Toast.makeText(this,"Title must only contain letters and spaces", Toast.LENGTH_LONG).show();
            isValid = false;
        }
        else if(!validateCategory(model.getCategory()))
        {
            Toast.makeText(this,"Please select a category", Toast.LENGTH_LONG).show();
            isValid = false;
        }
        else if(!this.isIngredientListValid(model.getIngredients()))
        {
            isValid = false;
        }
        else if(!this.isStepsListValid(model.getPreparationSteps()))
        {
            isValid = false;
        }
        return isValid;
    }
    private boolean validateTitle(String title)
    {

        if(title == null || title.length() <= 0)
        {
            return false;
        }
        return Pattern.matches("[\\u0621-\\u064A\\s]*$",title) || Pattern.matches("[a-zA-Z\\s]*$",title);

    }

    private boolean validateCategory(String text)
    {
        if(text == null || text.length() <= 0)
        {
            return false;
        }
        return Pattern.matches("[a-zA-Z]*$",text);
    }
    private String getTextOrEmpty(EditText edit)
    {
        return edit.getText() != null ? edit.getText().toString() : "";
    }

    private boolean isNotEmptyAndOnlyCharacters(String text)
    {
        boolean isValid = true;
        if(text == null || text.length() <= 0)
        {
            isValid = false;
        }
        else if(!Pattern.matches("/^[a-zA-Z\\s]*$/",text))
        {
            isValid = false;
        }
        return isValid;
    }

    private boolean isIngredientListValid(List<IngredientModel> models)
    {
        boolean isValid = true;
        if(models == null || models.size() <= 0)
        {
            Toast.makeText(this, "Please add ingredients", Toast.LENGTH_LONG).show();
            isValid = false;

        }

        if(models.size() > MAX_INGR_ITEMS) {
            Toast.makeText(this, "The maximum number for ingredients is " + MAX_INGR_ITEMS, Toast.LENGTH_LONG).show();
            isValid = false;
        } else {
            for(IngredientModel model: models)
            {
               /* if(!this.isNotEmptyAndOnlyCharacters(model.getUnitOfMeasure())) {
                    Toast.makeText(this, "Please select a unit of measure", Toast.LENGTH_LONG).show();
                    isValid = false;
                    break;
                } */
                if (!validateTitle(model.getName())) {
                    Toast.makeText(this, "Please enter a proper ingredient name that only contain letters", Toast.LENGTH_LONG).show();
                    isValid = false;
                    break;
                }
                if(model.getQuantity() <= 0)
                {
                    Toast.makeText(this, "Please enter a proper quantity", Toast.LENGTH_LONG).show();
                    isValid = false;
                    break;
                }
            }
        }
        return isValid;
    }

    private boolean isStepsListValid(List<StepModel> models)
    {
        boolean isValid = true;
        if(models == null || models.size() <= 0)
        {
            Toast.makeText(this, "Please add steps", Toast.LENGTH_LONG).show();
            isValid = false;

        } else {
            for(StepModel model: models)
            {
                if(!validateTitle(model.getDescription()))
                {
                    Toast.makeText(this, "Please enter steps", Toast.LENGTH_LONG).show();
                    isValid = false;
                    break;
                } else if (model.getOrder() <= 0) {
                    Toast.makeText(this, "There are no steps", Toast.LENGTH_LONG).show();
                    isValid = false;
                    break;
                }
            }
        }
        return isValid;
    }
}