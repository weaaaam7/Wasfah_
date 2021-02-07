package com.example.wasfah;
// weeeeem
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.lang.String;
import java.util.regex.Pattern;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.wasfah.database.AuthenticationManager;
import com.example.wasfah.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SignupActivity extends AppCompatActivity {

    private EditText firstName, lastName, email, password, confPass;
    private Button registerBtn;
    private TextView logBtn;
    private FirebaseAuth fAuth;
    private String checkEmail, checkPass, checkConf, checkFName, checkLName, fullName;
    private FirebaseDatabase root;
    private DatabaseReference ref;

    private static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,16}$";
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        fAuth = FirebaseAuth.getInstance();
        firstName = findViewById(R.id.fname);
        lastName = findViewById(R.id.lastName);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        registerBtn = (Button) findViewById(R.id.Rbutton);
        confPass = findViewById(R.id.confirmP);
        logBtn = findViewById(R.id.Acreate);

        root = FirebaseDatabase.getInstance();
        ref = root.getReference("Users");

        registerBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                checkEmail = email.getText().toString().trim();
                checkPass = password.getText().toString().trim();
                checkConf = confPass.getText().toString().trim();
                checkFName = firstName.getText().toString().trim();
                checkLName = lastName.getText().toString().trim();


                if (TextUtils.isEmpty(checkEmail) || TextUtils.isEmpty(checkFName) || TextUtils.isEmpty(checkLName) || TextUtils.isEmpty(checkPass) || TextUtils.isEmpty(checkPass)) {
                    Toast.makeText(SignupActivity.this, "All fields are required!", Toast.LENGTH_LONG).show();
                    return;
                }

                if (!checkFName.matches("^[a-zA-Z]*$")) {
                    firstName.setError("Invalid first name.");
                    firstName.setText("");
                    return;
                }

                if (!checkLName.matches("^[a-zA-Z]*$")) {
                    lastName.setError("Invalid last name.");
                    lastName.setText("");
                    return;
                }

                if(!android.util.Patterns.EMAIL_ADDRESS.matcher(checkEmail).matches()) {
                    email.setError("Invalid email.");
                    email.setText("");
                    return;
                }


                if (!(PASSWORD_PATTERN.matcher(checkPass).matches())) {
                    password.setError("Password must contain a capital letter, a small letter, a digit, one of [@#$%^&+=] and of length 8-16.");
                    password.setText("");
                    return;
                }

                if (!checkPass.equals(checkConf)) {
                    confPass.setError("Passwords are not matched.");
                    confPass.setText("");
                    return;
                }

                fullName = checkFName + " " + checkLName;

                final UserModel helper = new UserModel(fullName, checkEmail, checkPass);

                fAuth.createUserWithEmailAndPassword(checkEmail, checkPass).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(SignupActivity.this, "Sign up failed, Please Try Again!", Toast.LENGTH_SHORT).show();

                        } else {
                            AuthenticationManager.CURRENT_USER_EMAIL = checkEmail;
                            startActivity(new Intent(SignupActivity.this, PublishRecipeActivity.class));
                        }

                        FirebaseUser user = fAuth.getCurrentUser();
                        String userI = user.getUid();
                        ref.child(userI).setValue(helper);

                    }
                });


            }
        });

        logBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

    }

}


