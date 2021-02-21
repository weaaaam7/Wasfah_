package com.example.wasfah;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.opengl.ETC1;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.wasfah.database.RecipeFirebaseManager;
import com.example.wasfah.model.IngredientModel;
import com.example.wasfah.model.StepModel;
import com.example.wasfah.model.Steps;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.example.wasfah.PublishRecipeActivity.GALLERY_ACT_REQ_CODE;

public class EditRecipeActivity extends AppCompatActivity {

    public static final int GALLERY_ACT_REQ_CODE = 2;
    RecipeFirebaseManager recipeFirebaseManager = new RecipeFirebaseManager();
    private ImageView picture;
    private EditText titleEdit;
    private Spinner spinner;
    private List<IngredientModel> ingredientsList = new ArrayList<>();
    private List<StepModel> stepsList = new ArrayList<>();
    private ListView ingredientsListView;
    private ListView stepsListView;
    private Button addIngrButton;
    private Button addStepButton;
    private Button saveButton;
    private Button cancelButton;
    String recpieId;
    FirebaseAuth fAuth;
    FirebaseUser user;
    FirebaseDatabase db;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe);

        //Hooks.
        picture = findViewById(R.id.uploadedP);
        titleEdit = findViewById(R.id.recipe_title);
        spinner = (Spinner) findViewById(R.id.cats_spinner);
        ingredientsListView = (ListView) findViewById(R.id.ingredients_list_view);
        stepsListView = (ListView) findViewById(R.id.steps_list_view);
        addIngrButton = findViewById(R.id.add_ing_but);
        addStepButton = findViewById(R.id.add_step_bt);
        saveButton = findViewById(R.id.saveBut);
        cancelButton = findViewById(R.id.cancelBut);

        //Show all recipe's data.
        Intent intent = getIntent();
        String pic = intent.getStringExtra("img");
        String title = intent.getStringExtra("title");
        String category = intent.getStringExtra("category");
        List<Ingredients> ingredients= (List<Ingredients>) intent.getSerializableExtra("ingredients");
        List<Steps> steps= (List<Steps>) intent.getSerializableExtra("steps");
        recpieId = intent.getStringExtra("recipeId");

        //Firebase
        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();
        db = FirebaseDatabase.getInstance();

        //Edit picture
        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_ACT_REQ_CODE);
            }
        });

        //Edit title.
        titleEdit.setText(title);

        //Edit categories.
        this.setCategoriesSpinnerItems();

        //Edit ingrediants.
        this.setIngredientsListModel(this.ingredientsList);

        //Edit steps.
        this.setStepsListModel(this.stepsList);


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


    private void setCategoriesSpinnerItems()
    {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.ingredient_categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

    }

    private void setIngredientsListModel(List<IngredientModel> models)
    {
        IngredientsListAdapter adapter = new IngredientsListAdapter(this,
                R.layout.row_add, models);

        ingredientsListView.setAdapter(adapter);
    }

    private void setStepsListModel(List<StepModel> models)
    {
        StepsListAdapter adapter = new StepsListAdapter(this,
                R.layout.steps_row, models);
        stepsListView.setAdapter(adapter);
    }



}