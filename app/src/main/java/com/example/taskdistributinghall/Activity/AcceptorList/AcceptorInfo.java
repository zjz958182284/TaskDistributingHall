package com.example.taskdistributinghall.Activity.AcceptorList;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.taskdistributinghall.DBControl.DBControl;
import com.example.taskdistributinghall.Model.User;
import com.example.taskdistributinghall.R;

import java.sql.SQLException;

public class AcceptorInfo extends AppCompatActivity {
    private int taskID;
    private String phone;
    String resume;
    User user;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accptor_personal_info);
        Intent intent=getIntent();
        taskID=intent.getIntExtra("id",0);
        phone= intent.getStringExtra("phone");
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    user=DBControl.searchUserByPhone(phone);
                   resume= DBControl.searchResume(phone,taskID);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ((TextView)findViewById(R.id.grade_text)).setText("年级:"+user.grade);
                            ((TextView)findViewById(R.id.dept_text)).setText("学院:"+user.dept);
                            ((TextView)findViewById(R.id.phone_text)).setText("联系方式:"+user.phone);
                            ((TextView)findViewById(R.id.location_text)).setText("常住位置:"+user.address);
                            ((TextView)findViewById(R.id.resume_text)).setText(resume);
                        }
                    });
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}
