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

import com.example.wasfah.model.IngredientModel;
import com.example.wasfah.model.RecipeModel;
import com.example.wasfah.model.StepModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

    private String currentModelPic;
    private StorageReference storRef = FirebaseStorage.getInstance().getReference();
    private Uri picURI;

    private static final String TAG = "MainActivity";

    private ImageView picture;
    private EditText titleEdit;
    private Spinner catSpinner;
    private RecipeModel recipeModel;
    private List<IngredientModel> ingredientsList = new ArrayList<>();
    private List<StepModel> stepsList = new ArrayList<>();

    private ListView ingredientsListView;
    private ListView stepsListView;

    private Button addIngrButton;
    private Button addStepButton;
    private Button saveButton;
    private Button cancelButton;


    private String recipeId;

    private FirebaseDatabase db;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe);
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
                    recipeModel = (RecipeModel)(task.getResult().getValue());
                    Picasso.get().load(recipeModel.getPicUri()).into(picture);
                    titleEdit.setText(recipeModel.getTitle());
                    String[] data = EditRecipeActivity.this.getResources().getStringArray(R.array.ingredient_categories);
                    updateSpinner(catSpinner,recipeModel.getCategory(),data);
                    ingredientsList = recipeModel.getIngredients();
                    stepsList = recipeModel.getPreparationSteps();
                }
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
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }

        });


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





}