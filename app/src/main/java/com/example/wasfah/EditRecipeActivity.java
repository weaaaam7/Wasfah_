package com.example.wasfah;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.wasfah.model.IngredientModel;
import com.example.wasfah.model.StepModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class EditRecipeActivity extends AppCompatActivity {

    public static final int GALLERY_ACT_REQ_CODE = 2;
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
        recipeId = user.getUid();
        databaseReference1 = db.getReference("Recipes").child(recipeId);
        databaseReference2 = databaseReference1.child("ingredients");
        databaseReference3 = databaseReference1.child("preparationSteps");

        //Retrieve data
        Intent intent = getIntent();
        String img = intent.getExtras().getString("img");
        String title = intent.getExtras().getString("title");
        String category = intent.getExtras().getString("category");
//        List<Ingredients> ingredients= (List<Ingredients>) intent.getSerializableExtra("ingredients");
//        List<Steps> steps= (List<Steps>) intent.getSerializableExtra("steps");


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
                Picasso.get().load(img).into(picture);
                titleEdit.setText(title);
                catSpinner.setSelection(getIndex_SpinnerItem(catSpinner, category));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Retrieve ingredients and steps.
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    String key = (String) ds.getKey();
                    DatabaseReference keyReference = databaseReference1.child(key);

                    if (key == "ingredients") {
                        keyReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                ingredientsList.clear();
                                String name = dataSnapshot.child("name").getValue(String.class);
                                Long quan = dataSnapshot.child("quantity").getValue(long.class);
                                String unit = dataSnapshot.child("unitOfMeasure").getValue(String.class);
                                Ingredients ing = new Ingredients(name, quan, unit);
                                ingredients.add(ing);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.d(TAG, "Read failed");
                            }
                        }); // [End of keyReference]

                        if (key=="preparationSteps") {
                            keyReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    steps.clear();
                                    String desc = dataSnapshot.child("description").getValue(String.class);
                                    Steps ste = new Steps(desc);
                                    steps.add(ste);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Log.d(TAG, "Read failed");
                                }
                            }); // [End of keyReference]

                        }

                    } // END of for Loop
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "Read failed");
            }
        });


        //ing2
//        databaseReference2.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                int i = 1;
//                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
//                    ingredientsList[i] = dataSnapshot.getValue(IngredientModel.class);
//
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

//        //Retrieve steps.
//        databaseReference3.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });


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

        this.updateToFirebase();

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
        Spinner spinner = (Spinner) findViewById(R.id.cats_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.ingredient_categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

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