package com.example.wasfah;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
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
import java.util.HashMap;
import java.util.Map;

import static android.app.ProgressDialog.show;

public class editprofile extends AppCompatActivity {

    ImageView uimage;
    Button btnupdate;
    DatabaseReference dbreference;
    StorageReference storageReference;
    Uri filepath;
    Bitmap bitmap;
    String UserID="";
    EditText profileFirstName,profileLastName,profileEmail,profilePassword,profileConfirmPass;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);

        uimage=(ImageView)findViewById(R.id.ProfileImage);
        profileFirstName=(EditText) findViewById(R.id.EditFirstName);
        profileLastName=(EditText) findViewById(R.id.EditLastName);
        profileEmail=(EditText) findViewById(R.id.EditEmail);
        profilePassword=(EditText) findViewById(R.id.EditPass);
        profileConfirmPass=(EditText) findViewById(R.id.EditConfirmPass);
        btnupdate=(Button)findViewById(R.id.SaveProfile);

        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        UserID=user.getUid();

        dbreference=FirebaseDatabase.getInstance().getReference().child("Users");
        storageReference= FirebaseStorage.getInstance().getReference();

<<<<<<< Updated upstream
=======
        reference=FirebaseDatabase.getInstance().getReference().child("Users").child("JJn5mHzyKeh49dcZKvUyxvHkbci2");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String email=dataSnapshot.child("email").getValue().toString();
                String name=dataSnapshot.child("name").getValue().toString();
                String password=dataSnapshot.child("password").getValue().toString();


                String[] parts = name.split(" ");
                String part1 = parts[0];
                String part2 = parts[1];


                profileEmail.setText(email);
                profileFirstName.setText(part1);
                profileLastName.setText(part2);
                profilePassword.setText(password);
                profileConfirmPass.setText(password);
                profilePassword.setTransformationMethod(null);
                profileConfirmPass.setTransformationMethod(null);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        backProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ProfileView.class));
            }
        });




>>>>>>> Stashed changes
        uimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dexter.withContext(getApplicationContext())
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                Intent intent=new Intent();
                                intent.setType("image/*");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(intent,"Please Select File"),101);
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

        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatetofirebase();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==101 && resultCode==RESULT_OK)
        {
            filepath=data.getData();
            try {
                InputStream inputStream=getContentResolver().openInputStream(filepath);
                bitmap= BitmapFactory.decodeStream(inputStream);
                uimage.setImageBitmap(bitmap);
            }catch (Exception ex)
            {
                Toast.makeText(getApplicationContext(),ex.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }


    public void updatetofirebase()
    {
        final ProgressDialog pd=new ProgressDialog(this);
        pd.setTitle("File Uploader");
        pd.show();

        final StorageReference uploader=storageReference.child("profileimages/"+"img"+System.currentTimeMillis());
        uploader.putFile(filepath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        uploader.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                final Map<String,Object> map=new HashMap<>();
                                map.put("uimage",uri.toString());
                                map.put("name",profileFirstName.getText().toString());
                                map.put("email",profileEmail.getText().toString());
                                map.put("password",profilePassword.getText().toString());

                                dbreference.child(UserID).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.exists())
                                            dbreference.child(UserID).updateChildren(map);
                                        else
                                            dbreference.child(UserID).setValue(map);
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                    }
                                });

                                pd.dismiss();
                                Toast.makeText(getApplicationContext(),"Updated Successfully",Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        float percent=(100*snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                        pd.setMessage("Uploaded :"+(int)percent+"%");
                    }
                });

    }

}