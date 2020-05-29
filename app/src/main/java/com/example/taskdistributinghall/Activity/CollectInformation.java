package com.example.taskdistributinghall.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.taskdistributinghall.R;

public class CollectInformation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_information1);
        Intent collectionIntent=getIntent();
        Button ok_btn=findViewById(R.id.ok_btn);
        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone=collectionIntent.getStringExtra("phone");
                String password=collectionIntent.getStringExtra("password");

                Intent intent=new Intent();
                intent.setClass(CollectInformation.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
