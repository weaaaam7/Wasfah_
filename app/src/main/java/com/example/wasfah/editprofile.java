package com.example.wasfah;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import static android.app.ProgressDialog.show;

public class editprofile extends AppCompatActivity {





    EditText profileFirstName,profileLastName,profileEmail,profilePassword,profileConfirmPass;
    ImageView ProfileImageView;

    // Global variables to hold user data inside this activity
    String _UserName , _FirstName , _LastName , _Email , _Password;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);

        Toast.makeText(editprofile.this, "Firebase connection success", Toast.LENGTH_LONG).show();


        reference = FirebaseDatabase.getInstance().getReference("users");


        profileFirstName = findViewById(R.id.EditFirstName);
        profileLastName = findViewById(R.id.EditLastName);
        profileEmail = findViewById(R.id.EditEmail);
        profilePassword = findViewById(R.id.EditPass);
        profileConfirmPass = findViewById(R.id.EditConfirmPass);
        ProfileImageView = findViewById(R.id.ProfileImage);

        ProfileImageView.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Toast.makeText(editprofile.this, "Profile Imaged Clicked", Toast.LENGTH_SHORT).show();
            }
        });

       /* profileFirstName.setText();
        profileLastName.setText();
        profileEmail.setText();
        profilePassword.setText();
        profileConfirmPass.setText();*/
    }

    public void update(View view){
        if(isFirstNameChanged() || isLastNameChanged() || isEmailChanged() || isPasswordChanged()){
            Toast.makeText(this, "Data has been updated", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(this, "Data cannot be updated!", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isPasswordChanged() {
        if(!_Password.equals(profilePassword.getEditableText().toString())){
            reference.child(_UserName).child("password").setValue(profilePassword.getEditableText().toString());
            _Password = profilePassword.getEditableText().toString();
            return true;
        }
        else{
            return false;
        }
    }

    private boolean isEmailChanged() {
        if(!_Email.equals(profileEmail.getEditableText().toString())){
            reference.child(_UserName).child("email").setValue(profileEmail.getEditableText().toString());
            _Email = profileEmail.getEditableText().toString();
            return true;
        }
        else{
            return false;
        }
    }

    private boolean isLastNameChanged() {
        if(!_LastName.equals(profileLastName.getEditableText().toString())){
            reference.child(_UserName).child("name").setValue(profileLastName.getEditableText().toString());
            _LastName = profileLastName.getEditableText().toString();
            return true;
        }
        else{
            return false;
        }
    }

    private boolean isFirstNameChanged() {
        if(!_FirstName.equals(profileFirstName.getEditableText().toString())){
            reference.child(_UserName).child("name").setValue(profileFirstName.getEditableText().toString());
            _FirstName = profileFirstName.getEditableText().toString();
            return true;
        }
        else{
            return false;
        }
    }
}