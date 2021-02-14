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
import android.os.PatternMatcher;
import android.text.Editable;
import android.view.LayoutInflater;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.wasfah.database.AuthenticationManager;
import com.example.wasfah.database.RecipeFirebaseManager;
import com.example.wasfah.model.IngredientModel;
import com.example.wasfah.model.RecipeModel;
import com.example.wasfah.model.StepModel;
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
import java.util.regex.Pattern;

public class PublishRecipeActivity extends AppCompatActivity {

    public static final int GALLERY_ACT_REQ_CODE = 2;
    RecipeFirebaseManager recipeFirebaseManager = new RecipeFirebaseManager();
    private Button publishButton;

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
                    Toast.makeText(PublishRecipeActivity.this, "Please Select Image", Toast.LENGTH_SHORT);

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
        IngredientsListAdapter adapter = new IngredientsListAdapter(this,
                R.layout.row_add, models);

        ingredientsListView .setAdapter(adapter);
    }
    public void addIngredientRow(View view)
    {
        this.addBlankIngredientToListView(new IngredientModel());
    }
    private void addBlankIngredientToListView(IngredientModel model)
    {
        this.ingredientsList.add(model);
        this.setIngredientsListModel(this.ingredientsList);
    }

    private void setStepsListModel(List<StepModel> models)
    {
        ListView stepsListView = (ListView) findViewById(R.id.steps_list_view);

        StepsListAdapter adapter = new StepsListAdapter(this,
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
                        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>(){
                            @Override
                            public void onSuccess(Uri downloadUrl) {
                                currentModelPic= downloadUrl.toString();
                                publishRecipe();
                            }
                        });
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


    public void publishRecipe()
    {
        RecipeModel model = getRecipe();
        model.setCreatedBy(AuthenticationManager.CURRENT_USER_EMAIL);
        model.setPicUri(currentModelPic);
        if(this.validateModel(model)) {
            recipeFirebaseManager.SaveRecipe(model, this);
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

    //the validation.
    private boolean validateModel(RecipeModel model)
    {
        boolean isValid = true;
        //validations
        if(!isNotEmptyAndOnlyCharacters(model.getTitle()))
        {
            Toast.makeText(this,"Title is not valid", Toast.LENGTH_LONG).show();
            isValid = false;
        }
        else if(!isNotEmptyAndOnlyCharacters(model.getCategory()))
        {
            Toast.makeText(this,"Category is not valid", Toast.LENGTH_LONG).show();
            isValid = false;
        }
       /* else if(model.getPicUri() == null || model.getPicUri().length() <= 0)
        {
            Toast.makeText(this,"Picture is not valid", Toast.LENGTH_LONG).show();
            isValid = false;
        }*/
        else if(!this.isIngredientListValid(model.getIngredients()))
        {
            Toast.makeText(this,"Ingredients are not valid", Toast.LENGTH_LONG).show();
            isValid = false;
        }
        else if(!this.isStepsListValid(model.getPreparationSteps()))
        {
            Toast.makeText(this,"Prepartion steps are not valid", Toast.LENGTH_LONG).show();
            isValid = false;
        }
        return isValid;
    }

    private String getTextOrEmpty(EditText edit)
    {
        return edit.getText() != null ? edit.getText().toString() : "";
    }

    /**
     * checks if the text is not null or empty and only contains characters
     * no special characters or numbers.
     * @param text
     * @return
     */
    private boolean isNotEmptyAndOnlyCharacters(String text)
    {
        boolean isValid = true;
        if(text == null || text.length() <= 0)
        {
            isValid = false;
        }
        else if(!Pattern.matches("([A-Za-z])+",text))
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
            isValid = false;

        }
        else
        {
            for(IngredientModel model: models)
            {
                if(!this.isNotEmptyAndOnlyCharacters(model.getUnitOfMeasure())
                        || !this.isNotEmptyAndOnlyCharacters(model.getName())
                        || model.getQuantity() <= 0)
                {
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
            isValid = false;

        }
        else
        {
            for(StepModel model: models)
            {
                if(!this.isNotEmptyAndOnlyCharacters(model.getDescription())
                        ||  model.getOrder() <= 0)
                {
                    isValid = false;
                    break;
                }
            }
        }
        return isValid;
    }
}