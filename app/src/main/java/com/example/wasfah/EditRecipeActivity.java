package com.example.wasfah;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import com.example.wasfah.model.ModelRecipe;
import com.example.wasfah.model.RecipeModel;
import com.example.wasfah.model.StepModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class EditRecipeActivity extends AppCompatActivity  {

    public static final int GALLERY_ACT_REQ_CODE = 2;

    private String currentModelPic;
    private StorageReference storRef = FirebaseStorage.getInstance().getReference();
    private Uri picURI;
    private static final String TAG = "EditRecipeActiviy";

    private ImageView picture;
    private EditText titleEdit;
    private Spinner catSpinner;
    private ModelRecipe recipeModel;
    private List<IngredientModel> ingredientsList = new ArrayList<>();
    private List<StepModel> stepsList = new ArrayList<>();

    private ListView ingredientsListView;
    private ListView stepsListView;
    private ImageView backHome;
    private Button addIngrButton;
    private Button addStepButton;
    private Button saveButton;
    private Button cancelButton;


    private String recipeId;
    private FirebaseAuth fAuth;
    private FirebaseUser user;
    private FirebaseDatabase db;
    private DatabaseReference databaseReference1;
    private DatabaseReference databaseReference2;
    private DatabaseReference databaseReference3;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe);

        //Assign
        picture = (ImageView) findViewById(R.id.uploadedP_er);
        titleEdit = (EditText) findViewById(R.id.recipe_title_er);
        catSpinner = (Spinner) findViewById(R.id.cats_spinner_er);
        ingredientsListView = (ListView) findViewById(R.id.ingredients_list_view_er);
        stepsListView = (ListView) findViewById(R.id.steps_list_view_er);
        addIngrButton = (Button) findViewById(R.id.add_ing_but_er);
        addStepButton = (Button) findViewById(R.id.add_step_bt_er);
        saveButton = (Button) findViewById(R.id.saveBut_er);
        cancelButton = (Button) findViewById(R.id.cancelBut_er);
        backHome = findViewById(R.id.back_er);

        this.setCategoriesSpinnerItems();

        //Firebase
        db = FirebaseDatabase.getInstance();
        recipeId = getIntent().getStringExtra("rec");
        db.getReference("Recipes").child(recipeId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    HashMap vals = (HashMap) task.getResult().getValue();
                    recipeModel = new ModelRecipe() ;
                    recipeModel.setRecipeId((String) vals.get("recipeId"));
                    recipeModel.setTitle((String) vals.get("title"));
                    recipeModel.setPicUri((String) vals.get("picUri"));
                    currentModelPic = recipeModel.getPicUri();
                    recipeModel.setCategory((String) vals.get("category"));
                    recipeModel.setCreatedBy((String) vals.get("createdBy"));
                    recipeModel.setCurrentUserId((String)vals.get("currentUserId"));
                    recipeModel.setPreparationSteps((List<StepModel>) vals.get("preparationSteps"));
                    recipeModel.setIngredients((List<IngredientModel>) vals.get("ingredients"));
                    Picasso.get().load(recipeModel.getPicUri()).into(picture);
                    currentModelPic = recipeModel.getPicUri();
                    titleEdit.setText(recipeModel.getTitle());
                    String[] data = EditRecipeActivity.this.getResources().getStringArray(R.array.ingredient_categories);
                    updateSpinner(catSpinner,recipeModel.getCategory(),data);
                    ingredientsList = recipeModel.getIngredients();
                    stepsList = recipeModel.getPreparationSteps();
                    setIngredientsListModel(ingredientsList);
                    setStepsListModel(stepsList);
                }
            }
        });

        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_ACT_REQ_CODE);
            }
        });

        addIngrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addIngredientRow(v);
            }
        });

        addStepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addStepRow(v);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EditRecipeActivity.this, "Recipe had not been edited.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadToFirebase(picURI);
            }

        });

        backHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        this.setCategoriesSpinnerItems();
        this.setIngredientsListModel(this.ingredientsList);
        this.setStepsListModel(this.stepsList);

    }

    private void uploadToFirebase(Uri uri) {
        String picUri = "";
        if(uri == null)
        {

            editRecipe();
        }
        else
        {
            picUri = System.currentTimeMillis()+"."+ getFileExtension(uri);
            StorageReference fileRef = storRef.child(picUri);
            fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                currentModelPic = uri.toString();
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            if (uri != null) {
                                currentModelPic = uri.toString();
                                editRecipe();
                            }

                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditRecipeActivity.this,"Uploading Failed", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private void editRecipe() {
        ModelRecipe model = getRecipe();
        model.setCreatedBy(recipeModel.getCreatedBy());
        model.setCurrentUserId(recipeModel.getCurrentUserId());
        model.setPicUri(currentModelPic);
        model.setTimestamp();
        model.setDislikes(0);
        model.setLikes(0);

        if(this.validateModel(model)) {
            db.getReference("Recipes").child(recipeModel.getRecipeId()).setValue(model)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(EditRecipeActivity.this,"Recipe Edited Successfully..", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
            Intent i = new Intent(this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
    }

    private ModelRecipe getRecipe()
    {
        ModelRecipe model = new ModelRecipe();
        Spinner catSpinner = findViewById(R.id.cats_spinner_er);
        EditText titleEdit = findViewById(R.id.recipe_title_er);


        String category = (String)catSpinner.getSelectedItem();

        String title = getTextOrEmpty(titleEdit);
        model.setRecipeId(recipeModel.getRecipeId());
        model.setCategory(category);
        model.setTitle(title);
        model.setIngredients(this.ingredientsList);
        model.setPreparationSteps(this.stepsList);
        return model;
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void updateSpinner(Spinner spinner, String selectedValue, String[] dataSource)
    {

        if(selectedValue== null)
        {
            return;
        }
        for(int i = 0; i < dataSource.length; i++)
        {
            if(dataSource[i].equals(selectedValue))
            {
                spinner.setSelection(i);
                return;
            }
        }
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

    private int getIndex_SpinnerItem(Spinner catSpinner, String category) {
        int index = 0;
        for(int i=0;i<catSpinner.getCount(); i++){
            if(catSpinner.getItemAtPosition(i).equals(category)){
                index=i;
                break;
            }
        }
        return index;
    }


    private void setCategoriesSpinnerItems()
    {
        //Spinner spinner = (Spinner) findViewById(R.id.cats_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.ingredient_categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        catSpinner.setAdapter(adapter);

    }

    public void addIngredientRow(View view)
    {
        this.addBlankIngredientToListView(new IngredientModel());
    }

    private void setIngredientsListModel(List<IngredientModel> models)
    {
        ListView ingredientsListView = (ListView) findViewById(R.id.ingredients_list_view_er);
        IngredientsListAdapter adapter = new IngredientsListAdapter(this,
                R.layout.row_add, models);
        ingredientsListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    public void addStepRow(View view)
    {

        this.addBlankStepToListView(new StepModel());
    }

    private void addBlankIngredientToListView(IngredientModel model)
    {
        if(this.ingredientsList.size()  > PublishRecipeActivity.MAX_INGR_ITEMS)
        {
            String msg = "The maximum number for ingredients is = "
                    + PublishRecipeActivity.MAX_INGR_ITEMS;
            Toast.makeText(this,  msg, Toast.LENGTH_LONG).show();
            return;
        }
        this.ingredientsList.add(model);
        this.setIngredientsListModel(this.ingredientsList);
    }

    private void setStepsListModel(List<StepModel> models)
    {
        ListView stepsListView = (ListView) findViewById(R.id.steps_list_view_er);

        StepsListAdapter adapter = new StepsListAdapter(this,
                R.layout.steps_row, models);
        stepsListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void addBlankStepToListView(StepModel model)
    {
        int order = StepsOrderUtil.getNextStepOrder(this.stepsList);
        if (order > PublishRecipeActivity.MAX_STEPS_COUNT) {
            String msg = "The maximum number for steps is " + PublishRecipeActivity.MAX_STEPS_COUNT;
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
            return;
        }
        model.setOrder(order);
        this.stepsList.add(model);
        this.setStepsListModel(this.stepsList);
    }


    private boolean validateModel(ModelRecipe model)
    {
        boolean isValid = true;
        //validations
        if(!validateTitle(model.getTitle()))
        {
            Toast.makeText(this,"Title must only contain letters and spaces!", Toast.LENGTH_LONG).show();
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

        if(text == null || text.length() <= 0)
        {
            return false;
        }
        return Pattern.matches("[a-zA-Z\\s]*$/",text);

    }

    private boolean isIngredientListValid(List<IngredientModel> models)
    {
        boolean isValid = true;
        if(models == null || models.size() <= 0)
        {
            Toast.makeText(this, "Please add ingredients", Toast.LENGTH_LONG).show();
            isValid = false;

        }

        if(models.size() > PublishRecipeActivity.MAX_STEPS_COUNT) {
            Toast.makeText(this, "The maximum number for ingredients is "
                    + PublishRecipeActivity.MAX_STEPS_COUNT, Toast.LENGTH_LONG).show();
            isValid = false;
        } else {
            for(int i =0; i < models.size(); i++)
            {
                IngredientModel model = IngridientModelConverter.getIngredientModel(models.get(i));
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
            for(int i =0; i < models.size(); i++)
            {
                StepModel model = StepModelConverter.getStepModel(models.get(i));
                if(!validateTitle(model.getDescription()))
                {
                    Toast.makeText(this, "Please enter steps that only contain letters and spaces", Toast.LENGTH_LONG).show();
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