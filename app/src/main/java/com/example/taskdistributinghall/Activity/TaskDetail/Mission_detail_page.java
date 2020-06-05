package com.example.taskdistributinghall.Activity.TaskDetail;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.taskdistributinghall.DBControl.DBControl;
import com.example.taskdistributinghall.Model.Task;
import com.example.taskdistributinghall.Model.User;
import com.example.taskdistributinghall.R;
import com.example.taskdistributinghall.Resume_page;

public class Mission_detail_page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission_detail_page);
        Toolbar toolbar=findViewById(R.id.mission_detail_page_toolbar);
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

                Thread thread=  new Thread(new Runnable() {
            @Override
            public void run() {
                Intent intent=getIntent();
                int taskID=intent.getIntExtra("id",0);
                SharedPreferences sp=getApplicationContext().getSharedPreferences("my_info", Context.MODE_PRIVATE);
                String phone=sp.getString("phone","");//我的手机号码
                String publisher=intent.getStringExtra("publisher");
                if(publisher.equals(phone)){
                    ((Button)findViewById(R.id.accept_task_btn)).setVisibility(View.GONE);
                }
                Task task= DBControl.searchTaskByID(taskID);
                User user=DBControl.searchPublisher(taskID);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((TextView)findViewById(R.id.mission_title_text)).setText(task.title);
                        ((TextView)findViewById(R.id.detail_page_mission_date_text)).setText("发布日期:"+task.date.substring(0,16));
                        String type=task.type;
                        if(type.equals("errand"))
                            type="跑腿";
                        else if(type.equals("study"))
                            type="学习";
                        else type="合作";

                        if(user!=null) {
                            ((ImageView) findViewById(R.id.profile_photo)).setImageBitmap(user.headPortrait);
                            ((TextView) findViewById(R.id.name_text)).setText("昵称:" + user.name);
                            ((TextView) findViewById(R.id.dept_text)).setText("学院:" + user.dept);
                            ((TextView) findViewById(R.id.address_text)).setText("常住位置:" + user.address);
                            ((TextView) findViewById(R.id.phone_text)).setText("联系电话:" + user.phone);
                        }


                        ((TextView)findViewById(R.id.detail_page_mission_type_text)).setText("任务类型:"+type);
                        ((TextView)findViewById(R.id.detail_page_detail_text)).setText(task.content);
                        ((ImageView)findViewById(R.id.mission_show_picture)).setImageBitmap(task.taskPhoto);
                        ((TextView)findViewById(R.id.mission_detail_page_bounty_text)).setText("悬赏金额:"+String.valueOf(task.rewards)+"元");

                    }
                });
    }
        });
         thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Button button=findViewById(R.id.accept_task_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=getIntent();
                int taskID=intent.getIntExtra("id",0);
                Intent intentForResume=new Intent();
                intentForResume.putExtra("id",taskID);
                intentForResume.setClass(Mission_detail_page.this, Resume_page.class);
                startActivity(intentForResume);
                finish();
            }
        });

    }
}
