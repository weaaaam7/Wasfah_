package com.example.wasfah;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

public class editprofile extends AppCompatActivity {

    ProgressDialog pd;
    ImageView uimage;
    Button btnupdate;
    DatabaseReference dbreference, reference;
    StorageReference storageReference;
    Uri filepath;
    Bitmap bitmap;
    String UserID = "", oldPassword = "", oldEmail = "";
    EditText profileFirstName, profileLastName, profileEmail, profilePassword, profileConfirmPass;
    ImageView backProfile;
    private static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,16}$";
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);
    FirebaseAuth fAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        uimage=(ImageView)findViewById(R.id.ProfileImage);
        profileFirstName=(EditText) findViewById(R.id.EditFirstName);
        profileLastName=(EditText) findViewById(R.id.EditLastName);
        profileEmail=(EditText) findViewById(R.id.EditEmail);
        profilePassword=(EditText) findViewById(R.id.EditPass);
        profileConfirmPass=(EditText) findViewById(R.id.EditConfirmPass);
        btnupdate=(Button)findViewById(R.id.SaveProfile);
        backProfile = findViewById(R.id.back);
        user = FirebaseAuth.getInstance().getCurrentUser();
        UserID = user.getUid();
        fAuth = FirebaseAuth.getInstance();

        dbreference = FirebaseDatabase.getInstance().getReference().child("Users");
        storageReference = FirebaseStorage.getInstance().getReference();

        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(UserID);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //todo
                String email = dataSnapshot.child("email").getValue().toString();
                String name = dataSnapshot.child("name").getValue().toString();
                String password = dataSnapshot.child("password").getValue().toString();

                oldEmail = email;
                oldPassword = password;


                String[] parts = name.split(" ");
                String part1 = parts[0];
                String part2 = (parts.length > 1) ? parts[1] : "";


                profileEmail.setText(email);
                profileFirstName.setText(part1);
                profileLastName.setText(part2);
                profilePassword.setText(password);
                profileConfirmPass.setText(password);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        backProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(getApplicationContext(), ProfileView.class));
                finish();
            }
        });

        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatetofirebase();
            }
        });

        uimage.setOnClickListener(new View.OnClickListener() {
            }

       /* uimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dexter.withContext(getApplicationContext())
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                Intent intent = new Intent();
                                intent.setType("image/*");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(intent, "Please Select File"), 101);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();

            }
        });


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK) {
            filepath = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(filepath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                uimage.setImageBitmap(bitmap);
            } catch (Exception ex) {
                Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }


    public void updatetofirebase() {

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


        pd = new ProgressDialog(this);
        pd.setTitle("File Uploader");
        pd.show();

        final StorageReference uploader = storageReference.child("profileimages/" + "img" + System.currentTimeMillis());

        if (filepath != null) {
            uploader.putFile(filepath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            uploader.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    final Map<String, Object> map = new HashMap<>();
                                    map.put("uimage", uri.toString());
                                    map.put("name", profileFirstName.getText().toString() + " " + profileLastName.getText().toString());
                                    map.put("email", profileEmail.getText().toString());
                                    map.put("password", profilePassword.getText().toString());

                                    updateEmailAndPassword(map);
                                }
                            });
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            float percent = (100 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                            pd.setMessage("Uploaded :" + (int) percent + "%");
                        }
                    });
        } else {
            final Map<String, Object> map = new HashMap<>();
//        map.put("uimage",uri.toString());
            map.put("name", profileFirstName.getText().toString() + " " + profileLastName.getText().toString());
            map.put("email", profileEmail.getText().toString());
            map.put("password", profilePassword.getText().toString());

//            saveData(map);
            updateEmailAndPassword(map);
        }
    }

    public void saveData(Map<String, Object> map) {

        dbreference.child(UserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                    dbreference.child(UserID).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            onFinish();
                        }
                    });
                else
                    dbreference.child(UserID).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            onFinish();
                        }
                    });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


    }

    private void onFinish() {
        pd.dismiss();
        Toast.makeText(getApplicationContext(), "Updated Successfully", Toast.LENGTH_LONG).show();
        finish();

    }

    private void error() {
        pd.dismiss();
        Toast.makeText(getApplicationContext(), "Updated email or password failed \n try again", Toast.LENGTH_LONG).show();
        finish();

    }

    private void updateEmailAndPassword(Map<String, Object> map) {
        if (!oldEmail.equals(profileEmail.getText().toString())) {
            user.updateEmail(profileEmail.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    if (!oldPassword.equals(profilePassword.getText().toString())) {
                        user.updatePassword(profilePassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                saveData(map);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                error();
                            }
                        });
                    }
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            error();
                        }
                    });
        } else if (!oldPassword.equals(profilePassword.getText().toString())) {
            user.updatePassword(profilePassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    saveData(map);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    error();
                }
            });
        } else
            saveData(map);

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