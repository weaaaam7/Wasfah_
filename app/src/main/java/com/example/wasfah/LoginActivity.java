package com.example.wasfah;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.opengl.ETC1;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wasfah.database.AuthenticationManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

public class LoginActivity extends AppCompatActivity {

    //Variables
    EditText mEmail, mPassword;
    Button mLogin;
    TextView mCreateAccount, mReset;
    FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        fAuth = FirebaseAuth.getInstance();
        mLogin = findViewById(R.id.login);
        mCreateAccount = findViewById(R.id.signup);
        mReset = findViewById(R.id.reset);

        //Underline "Click Here!" text view.
        String resetText = "Click Here!";
        SpannableString sReset = new SpannableString(resetText);
        UnderlineSpan unReset = new UnderlineSpan();
        sReset.setSpan(unReset, 0,11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mReset.setText(sReset);

        //Underline "Sign up!" text view.
        String signupText = "Sign up!";
        SpannableString sSignup = new SpannableString(signupText);
        UnderlineSpan unSignup = new UnderlineSpan();
        sSignup.setSpan(unSignup, 0,8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mCreateAccount.setText(sSignup);



        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password)) {
                    mEmail.setError("Email is required");
                    mPassword.setError("Password is required");
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Email is required");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    mPassword.setError("Password is required");
                    return;
                }


                //Authenticate the data
                fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                            AuthenticationManager.CURRENT_USER_EMAIL=email;
                            startPublishActivity();
                        } else {
                            Toast.makeText(LoginActivity.this, "Login Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });



            }
        });

        mCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSignupActivity();
            }
        });

        mReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startResetActivity();
            }
        });

    }

        public void startSignupActivity()
        {
            Intent i = new Intent(this, SignupActivity.class);
            //no need to combe back to login
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }

        public void startPublishActivity()
        {
            Intent i = new Intent(this, MainActivity.class);
            //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }

    public void startResetActivity()
    {
        Intent i = new Intent(this, ResetPasswordActivity.class);
        //no need to combe back to login
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    }