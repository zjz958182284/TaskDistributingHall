package com.example.taskdistributinghall;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.taskdistributinghall.DBControl.DBControl;

import java.sql.SQLException;

public class Resume_page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_page);
        Toolbar toolbar=findViewById(R.id.resume_page_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button button=findViewById(R.id.resume_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String resumeContent=((EditText)findViewById(R.id.mission_description_edit)).getText().toString();
                        Intent intent=getIntent();
                        int taskID=intent.getIntExtra("id",0);
                        SharedPreferences sp=getApplicationContext().getSharedPreferences("my_info", Context.MODE_PRIVATE);
                        String phone=sp.getString("phone","");
                        try {
                            if(!resumeContent.equals("")) {
                                DBControl.addRequestingTask(phone, taskID, resumeContent);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        button.setVisibility(View.GONE);
                                        Toast.makeText(Resume_page.this
                                                , "您已成功申请该任务，请等待对方审核",
                                                Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });
                            }
                        } catch (SQLException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    button.setVisibility(View.GONE);
                                    Toast.makeText(Resume_page.this
                                            ,"您已成功申请过该任务请耐心等待对方回应",
                                            Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });
                        }

                    }
                }).start();
            }
        });
    }
}
