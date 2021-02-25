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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.wasfah.database.AuthenticationManager;
//import com.example.wasfah.database.RecipeFirebaseManager;
import com.example.wasfah.model.IngredientModel;
import com.example.wasfah.model.RecipeModel;
import com.example.wasfah.model.StepModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class EditRecipeActivity extends AppCompatActivity  {

    public static final int GALLERY_ACT_REQ_CODE = 2;
    //RecipeFirebaseManager recipeFirebaseManager = new RecipeFirebaseManager();
    private String currentModelPic;
    private StorageReference storRef = FirebaseStorage.getInstance().getReference();
    private Uri picURI;

    private static final String TAG = "MainActivity";

    private ImageView picture;
    private EditText titleEdit;
    private Spinner catSpinner;

    private List<IngredientModel> ingredientsList = new ArrayList<>();
    private List<StepModel> stepsList = new ArrayList<>();
    private List<Ingredients> ingredients;
    private List<Steps> steps;
    private ListView ingredientsListView;
    private ListView stepsListView;

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

        //Firebase
        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();
        db = FirebaseDatabase.getInstance();
        //recipeId = user.getUid();
        databaseReference1 = FirebaseDatabase.getInstance().getReference("Recipes").child("recipeId");
//        databaseReference2 = databaseReference1.child("ingredients");
//        databaseReference3 = databaseReference1.child("preparationSteps");

//        //Retrieve data
//        Intent intent = getIntent();
//        String img = intent.getExtras().getString("img");
//        String title = intent.getExtras().getString("title");
//        String category = intent.getExtras().getString("category");
//
//        Picasso.get().load(img).into(picture);
//        titleEdit.setText(title);
//        catSpinner.setSelection(getIndex_SpinnerItem(catSpinner, category));


        //Assign
        picture = (ImageView) findViewById(R.id.uploadedP);
        titleEdit = (EditText) findViewById(R.id.recipe_title);
        catSpinner = (Spinner) findViewById(R.id.cats_spinner);
        ingredientsListView = (ListView) findViewById(R.id.ingredients_list_view);
        stepsListView = (ListView) findViewById(R.id.steps_list_view);
        addIngrButton = (Button) findViewById(R.id.add_ing_but);
        addStepButton = (Button) findViewById(R.id.add_step_bt);
        saveButton = (Button) findViewById(R.id.saveBut);
        cancelButton = (Button) findViewById(R.id.cancelBut);


        //Set image, title and category.
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String img = snapshot.child("image").getValue(String.class);
                String title = snapshot.child("title").getValue(String.class);
                String category = snapshot.child("category").getValue(String.class);

                Picasso.get().load(img).into(picture);
                titleEdit.setText(title);
                catSpinner.setSelection(getIndex_SpinnerItem(catSpinner, category));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        //Retrieve ingredients and steps.
//        databaseReference1.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot ds: dataSnapshot.getChildren()) {
//                    String key = (String) ds.getKey();
//                    DatabaseReference keyReference = databaseReference1.child(key);
//
//                    if (key == "ingredients") {
//                        keyReference.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(DataSnapshot dataSnapshot) {
//                                //ingredientsList.clear();
//                                String name = dataSnapshot.child("name").getValue(String.class);
//                                Long quan = dataSnapshot.child("quantity").getValue(long.class);
//                                String unit = dataSnapshot.child("unitOfMeasure").getValue(String.class);
//                                Ingredients ing = new Ingredients(name, quan, unit);
//                                ingredients.add(ing);
//                            }
//
//                            @Override
//                            public void onCancelled(DatabaseError databaseError) {
//                                Log.d(TAG, "Read failed");
//                            }
//                        }); // [End of keyReference]
//
//                        if (key=="preparationSteps") {
//                            keyReference.addValueEventListener(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(DataSnapshot dataSnapshot) {
//                                    //steps.clear();
//                                    String desc = dataSnapshot.child("description").getValue(String.class);
//                                    Steps ste = new Steps(desc);
//                                    steps.add(ste);
//                                }
//
//                                @Override
//                                public void onCancelled(DatabaseError databaseError) {
//                                    Log.d(TAG, "Read failed");
//                                }
//                            }); // [End of keyReference]
//
//                        }
//
//                    } // END of for Loop
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.d(TAG, "Read failed");
//            }
//        });

        updateToFirebase();


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
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (picURI != null) {
//                    uploadToFirebase(picURI);
//                }
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }

        });


    }


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


    public void updateToFirebase() {

        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_ACT_REQ_CODE);
            }
        });

        this.setCategoriesSpinnerItems();
        this.setIngredientsListModel(this.ingredientsList);
        this.setStepsListModel(this.stepsList);

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
        ListView ingredientsListView = (ListView) findViewById(R.id.ingredients_list_view);
        IngredientsListAdapter adapter = new IngredientsListAdapter(this,
                R.layout.row_add, models);

        ingredientsListView .setAdapter(adapter);
    }

    public void addStepRow(View view)
    {

        this.addBlankStepToListView(new StepModel());
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

    private void addBlankStepToListView(StepModel model)
    {
        int order = StepsOrderUtil.getNextStepOrder(this.stepsList);
        model.setOrder(order);
        this.stepsList.add(model);
        this.setStepsListModel(this.stepsList);
    }











//    private void uploadToFirebase(Uri uri) {
//
//        StorageReference fileRef = storRef.child(System.currentTimeMillis()+"."+getFileExtension(uri));
//        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                currentModelPic = uri.toString();
//                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//                        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>(){
//                            @Override
//                            public void onSuccess(Uri downloadUrl) {
//                                currentModelPic= downloadUrl.toString();
//                                publishRecipe();
//
//                            }
//                        });
//
//                    }
//                });
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(EditRecipeActivity.this,"Uploading Failed", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

//    private String getFileExtension(Uri uri) {
//        ContentResolver cr = getContentResolver();
//        MimeTypeMap mime = MimeTypeMap.getSingleton();
//        return mime.getExtensionFromMimeType(cr.getType(uri));
//    }
//
//
//    public void publishRecipe()
//    {
//        RecipeModel model = getRecipe();
//        model.setCreatedBy(AuthenticationManager.CURRENT_USER_EMAIL);
//        model.setPicUri(currentModelPic);
//        model.setTimestamp();
//        if(this.validateModel(model)) {
//            recipeFirebaseManager.SaveRecipe(model, this);
//            Intent i = new Intent(this, MainActivity.class);
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(i);
//        }
//    }
//
//    private RecipeModel getRecipe()
//    {
//        RecipeModel model = new RecipeModel();
//        Spinner catSpinner = findViewById(R.id.cats_spinner);
//        EditText titleEdit = findViewById(R.id.recipe_title);
//
//
//        String category = (String)catSpinner.getSelectedItem();
//        String title = getTextOrEmpty(titleEdit);
//        model.setCategory(category);
//        model.setTitle(title);
//        model.setIngredients(this.ingredientsList);
//        model.setPreparationSteps(this.stepsList);
//        return model;
//    }
//
//    //the validation.
//    private boolean validateModel(RecipeModel model)
//    {
//        boolean isValid = true;
//        //validations
//        if(!isNotEmptyAndOnlyCharacters(model.getTitle()))
//        {
//            Toast.makeText(this,"Title is not valid", Toast.LENGTH_LONG).show();
//            isValid = false;
//        }
//        else if(!isNotEmptyAndOnlyCharacters(model.getCategory()))
//        {
//            Toast.makeText(this,"Category is not valid", Toast.LENGTH_LONG).show();
//            isValid = false;
//        }
//        else if(model.getPicUri() == null || model.getPicUri().length() <= 0)
//        {
//            Toast.makeText(this,"Picture is not valid", Toast.LENGTH_LONG).show();
//            isValid = false;
//        }
//        else if(!this.isIngredientListValid(model.getIngredients()))
//        {
//            Toast.makeText(this,"Ingredients are not valid", Toast.LENGTH_LONG).show();
//            isValid = false;
//        }
//        else if(!this.isStepsListValid(model.getPreparationSteps()))
//        {
//            Toast.makeText(this,"Prepartion steps are not valid", Toast.LENGTH_LONG).show();
//            isValid = false;
//        }
//        return isValid;
//    }
//
//    private String getTextOrEmpty(EditText edit)
//    {
//        return edit.getText() != null ? edit.getText().toString() : "";
//    }
//
//
//    private boolean isNotEmptyAndOnlyCharacters(String text)
//    {
//        boolean isValid = true;
//        if(text == null || text.length() <= 0)
//        {
//            isValid = false;
//        }
//        else if(!Pattern.matches("([A-Za-z])+",text))
//        {
//            isValid = false;
//        }
//        return isValid;
//    }
//
//    private boolean isIngredientListValid(List<IngredientModel> models)
//    {
//        boolean isValid = true;
//        if(models == null || models.size() <= 0)
//        {
//            isValid = false;
//
//        }
//        else
//        {
//            for(IngredientModel model: models)
//            {
//                if(!this.isNotEmptyAndOnlyCharacters(model.getUnitOfMeasure())
//                        || !this.isNotEmptyAndOnlyCharacters(model.getName())
//                        || model.getQuantity() <= 0)
//                {
//                    isValid = false;
//                    break;
//                }
//            }
//        }
//        return isValid;
//    }
//    private boolean isStepsListValid(List<StepModel> models)
//    {
//        boolean isValid = true;
//        if(models == null || models.size() <= 0)
//        {
//            isValid = false;
//
//        }
//        else
//        {
//            for(StepModel model: models)
//            {
//                if(!this.isNotEmptyAndOnlyCharacters(model.getDescription())
//                        ||  model.getOrder() <= 0)
//                {
//                    isValid = false;
//                    break;
//                }
//            }
//        }
//        return isValid;
//    }



}