package com.example.wasfah;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class recepe extends AppCompatActivity {

    private TextView title_tv;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recepe);

        Intent intent = getIntent();
        String tilte = intent.getExtras().getString("title");
        int img = intent.getExtras().getInt("img");

        title_tv=(TextView) findViewById(R.id.tv);
        image=(ImageView) findViewById(R.id.img2);

        title_tv.setText(tilte);
        image.setImageResource(img);




    }
}