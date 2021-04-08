package com.example.wasfah;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.CaseMap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import static android.app.ProgressDialog.show;


public class editprofile extends AppCompatActivity {

    Button btnupdate, deletAcc;
    //added these
    private DatabaseReference ref;
    DatabaseReference dbreference, reference;
    StorageReference storageReference;
    Uri filepath;
    Bitmap bitmap;
    String UserID = "", oldPassword = "", oldEmail = "";
    EditText profileFirstName, profileLastName, profileEmail, profilePassword, profileConfirmPass;
    ImageView backProfile;
    String email, name, password;
    private static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,16}$";
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);
    FirebaseAuth fAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);
        if (Pref.getValue(getApplicationContext(),"language_checked", "false").equalsIgnoreCase("true"))
        {
            setApplicationLocale("ar");
        }
        else
        {
            setApplicationLocale("en");
        }
        profileFirstName=(EditText) findViewById(R.id.EditFirstName);
        profileLastName=(EditText) findViewById(R.id.EditLastName);
        profileEmail=(EditText) findViewById(R.id.EditEmail);
        profilePassword=(EditText) findViewById(R.id.EditPass);
        profileConfirmPass=(EditText) findViewById(R.id.EditConfirmPass);
        btnupdate=(Button)findViewById(R.id.SaveProfile);
        backProfile = findViewById(R.id.back);
        deletAcc = (Button)findViewById(R.id.DeleteAcc);




        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        UserID=user.getUid();
        UserID=user.getUid();
        ref = FirebaseDatabase.getInstance().getReference("Users").child(UserID);

        dbreference=FirebaseDatabase.getInstance().getReference().child("Users");
        storageReference= FirebaseStorage.getInstance().getReference();

        reference=FirebaseDatabase.getInstance().getReference().child("Users").child(UserID);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    email = dataSnapshot.child("email").getValue(String.class);
                    name = dataSnapshot.child("name").getValue(String.class);
                    password = dataSnapshot.child("password").getValue(String.class);

                    String[] parts = name.split(" ");
                    String part1 = parts[0];
                    String part2 = parts[1];

                    profileEmail.setText(email);
                    profileFirstName.setText(part1);
                    profileLastName.setText(part2);
                    profilePassword.setText(password);
                    profileConfirmPass.setText(password);
                } else {
//                    startActivity(new Intent(editprofile.this, LoginActivity.class));
//                    finish();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        backProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatetofirebase();
            }
        });
        // delete Account
//        deletAcc.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(editprofile.this);
//                builder.setMessage("Are you sure you want to delete your account?")
//                        .setCancelable(false)
//
//                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                               DatabaseReference dRec = FirebaseDatabase.getInstance().getReference("Recipes");
//                                dRec.addValueEventListener(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                        for (DataSnapshot ds : snapshot.getChildren()) {
//                                            // delete user's comments
//                                            DatabaseReference comment = dRec.child(ds.getKey()).child("comment");
//                                            comment.addListenerForSingleValueEvent(new ValueEventListener() {
//                                                @Override
//                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                                    for (DataSnapshot ds : snapshot.getChildren()) {
//                                                        String uid_comp = snapshot.child(ds.getKey()).child("uid").getValue(String.class);
//                                                        if (uid_comp != null && UserID != null && uid_comp.equalsIgnoreCase(UserID)) {
//                                                            comment.child(ds.getKey()).removeValue();
//                                                        }
//                                                    }
//                                                }
//
//                                                @Override
//                                                public void onCancelled(@NonNull DatabaseError error) {
//
//                                                }
//                                            });
//                                            // delete user's recipes
//                                           String email2 = snapshot.child(ds.getKey()).child("createdBy").getValue(String.class);
//                                            if (email2 != null && email != null && email2.equalsIgnoreCase(email)) {
//                                                dRec.child(ds.getKey()).removeValue();
//                                            }
//                                        }
//                                    }
//                                    @Override
//                                    public void onCancelled(@NonNull DatabaseError error) {
//
//                                    }
//                                });
//
//                                AuthCredential credential = EmailAuthProvider.getCredential(email, password);
//
//                                // Prompt the user to re-provide their sign-in credentials
//                                if (user != null) {
//                                    user.reauthenticate(credential)
//                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                @Override
//                                                public void onComplete(@NonNull Task<Void> task) {
//                                                    user.delete()
//                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                                @Override
//                                                                public void onComplete(@NonNull Task<Void> task) {
//                                                                    if (task.isSuccessful()) {
//                                                                        reference.removeValue();
//                                                                        Log.d("TAG", "User account deleted.");
//                                                                        startActivity(new Intent(editprofile.this, LoginActivity.class));
//                                                                    }
//                                                                }
//                                                            });
//                                                }
//                                            });
//                                }
//                            }
//                        })
//                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                dialogInterface.cancel();
//                            }
//                        });
//
//                AlertDialog alertDialog = builder.create();
//                alertDialog.show();
//                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.yellow2));
//                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.yellow2));
//            }
//        }); // end
//    }
        // delete Account
        deletAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(editprofile.this);
                builder.setMessage("Are you sure you want to delete your account?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ProgressDialog pd = new ProgressDialog(editprofile.this);
                                pd.setTitle("Deleting Account");
                                pd.setMessage("Please wait as we delete your Account");
                                ref.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            finalizeDelete(pd);
                                        }
                                    }
                                });

                                pd.show();

//                               DatabaseReference dRec = FirebaseDatabase.getInstance().getReference("Recipes");
//                                dRec.addValueEventListener(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                        for (DataSnapshot ds : snapshot.getChildren()) {
//                                            // delete user's comments
//                                            DatabaseReference comment = dRec.child(ds.getKey()).child("comment");
//                                            comment.addListenerForSingleValueEvent(new ValueEventListener() {
//                                                @Override
//                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                                    for (DataSnapshot ds : snapshot.getChildren()) {
//                                                        String uid_comp = snapshot.child(ds.getKey()).child("uid").getValue(String.class);
//                                                        if (uid_comp != null && UserID != null && uid_comp.equalsIgnoreCase(UserID)) {
//                                                            comment.child(ds.getKey()).removeValue();
//                                                        }
//                                                    }
//                                                }
//
//                                                @Override
//                                                public void onCancelled(@NonNull DatabaseError error) {
//
//                                                }
//                                            });
//                                            // delete user's recipes
//                                           String email2 = snapshot.child(ds.getKey()).child("createdBy").getValue(String.class);
//                                            if (email2 != null && email != null && email2.equalsIgnoreCase(email)) {
//                                                dRec.child(ds.getKey()).removeValue();
//                                            }
//                                        }
//                                    }
//                                    @Override
//                                    public void onCancelled(@NonNull DatabaseError error) {
//
//                                    }
//                                });
//
//                                AuthCredential credential = EmailAuthProvider.getCredential(email, password);
//
//                                // Prompt the user to re-provide their sign-in credentials
//                                if (user != null) {
//                                    user.reauthenticate(credential)
//                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                @Override
//                                                public void onComplete(@NonNull Task<Void> task) {
//                                                    user.delete()
//                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                                @Override
//                                                                public void onComplete(@NonNull Task<Void> task) {
//                                                                    if (task.isSuccessful()) {
//                                                                        reference.removeValue();
//                                                                        Log.d("TAG", "User account deleted.");
//                                                                        startActivity(new Intent(editprofile.this, LoginActivity.class));
//                                                                    }
//                                                                }
//                                                            });
//                                                }
//                                            });
//                                }
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.yellow2));
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.yellow2));
            }
        }); // end
    }

    private void finalizeDelete(ProgressDialog pd) {
        pd.setMessage("Finishing Up");
        pd.setTitle("Almost");
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        assert currentUser != null;
        currentUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    pd.dismiss();
                    Log.d("DELETE", "onComplete: TASK DONE");
                    startActivity(new Intent(editprofile.this,SplashScreen.class));
                    finish();
                }else {
                    pd.dismiss();
                    Log.d("DELETE", "onComplete: TASK NOT DONE"+task.getException().getMessage());

                }
            }
        });
    }
    public void updatetofirebase()
    {

        String checkEmail = profileEmail.getText().toString().trim();
        String checkPass = profilePassword.getText().toString().trim();
        String checkConf = profileConfirmPass.getText().toString().trim();
        String checkFName = profileFirstName.getText().toString().trim();
        String checkLName = profileLastName.getText().toString().trim();


        if (TextUtils.isEmpty(checkEmail) || TextUtils.isEmpty(checkFName) || TextUtils.isEmpty(checkLName) || TextUtils.isEmpty(checkPass) || TextUtils.isEmpty(checkPass)) {
            Toast.makeText(editprofile.this, "All fields are required!", Toast.LENGTH_LONG).show();
            return;
        }

        /*if (!checkFName.matches("^[a-zA-Z]*$")) {
            profileFirstName.setError("Invalid first name.");
            profileFirstName.setText("");
            return;
        }

        if (!checkLName.matches("^[a-zA-Z]*$")) {
            profileLastName.setError("Invalid last name.");
            profileLastName.setText("");
            return;
        }
*/
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(checkEmail).matches()) {
            profileEmail.setError("Invalid email.");
            profileEmail.setText("");
            return;
        }


        if (!(PASSWORD_PATTERN.matcher(checkPass).matches())) {
            profilePassword.setError("Password must contain a capital letter, a small letter, a digit, one of [@#$%^&+=] and of length 8-16.");
            profilePassword.setText("");
            return;
        }

        if (!checkPass.equals(checkConf)) {
            profileConfirmPass.setError("Passwords are not matched.");
            profileConfirmPass.setText("");
            return;
        }

        String fname=profileFirstName.getText().toString();
        String lname=profileLastName.getText().toString();
        String Full_name= fname+" "+lname;

        final Map<String,Object> map=new HashMap<>();
        // map.put("uimage",uri.toString());
        map.put("name",Full_name);
        map.put("email",profileEmail.getText().toString());
        map.put("password",profilePassword.getText().toString());

        dbreference.child(UserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    dbreference.child(UserID).updateChildren(map);
                    Toast.makeText(getApplicationContext(),"Updated Successfully",Toast.LENGTH_LONG).show();
                }
                else
                    dbreference.child(UserID).setValue(map);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }
    public void setApplicationLocale(String locale) {
        Resources resources = getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(new Locale(locale.toLowerCase()));
        } else {
            config.locale = new Locale(locale.toLowerCase());
        }
        resources.updateConfiguration(config, dm);
    }

}