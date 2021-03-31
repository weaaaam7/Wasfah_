package com.example.wasfah;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.wasfah.database.AuthenticationManager;
import com.example.wasfah.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

public class SplashScreen extends AppCompatActivity {

    //Variables
    Animation topAnim;
    ImageView logo;
    Handler handler;
    Runnable runnable;
    private String CHANNEL_ID ="101";
    private String username ="";
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splashscreen);
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser()!=null){
            username =mAuth.getCurrentUser().getDisplayName();

        }
        Log.d("TOPIC", "onCreate: "+username);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser!=null){
            createNotificationChannel();
            getToken();
            subscribeToTopic1();
        }
//        subscribeToTopic2();

        //Animation
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        logo = findViewById(R.id.wasfahlogo);
        logo.setAnimation(topAnim);

        handler = new Handler();
        handler.postDelayed(() -> {
            Intent i = new Intent(SplashScreen.this, LoginActivity.class);
            //no need to come back to login
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }, 2000);


    }

    private void getToken(){
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                Log.e("Token", "My token:  "+instanceIdResult.getToken());

            }
        }) ;

    }

    //create a notification channel
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String name = getString(R.string.firebasenotif_channel);
            String  descriptionText = getString(R.string.channel_description);
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(descriptionText);
            // Register the channel with the system
            NotificationManager notificationManager =(NotificationManager)
                    getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
    }

    //subscribe to a topic
    private void subscribeToTopic1(){
        FirebaseMessaging.getInstance().subscribeToTopic(mAuth.getCurrentUser().getUid()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d("USER", "onComplete: "+mAuth.getCurrentUser().getUid());
                //Toast.makeText(SplashScreen.this, "Subscribed successfully", Toast.LENGTH_SHORT).show();

            }
        });
    }
//    private void subscribeToTopic2(){
//        FirebaseMessaging.getInstance().subscribeToTopic("comments").addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                Toast.makeText(SplashScreen.this, "Subscribed successfully", Toast.LENGTH_SHORT).show();
//
//            }
//        });
//    }
}

