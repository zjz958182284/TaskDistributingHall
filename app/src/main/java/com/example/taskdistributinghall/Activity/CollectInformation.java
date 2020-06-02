package com.example.taskdistributinghall.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.taskdistributinghall.DBControl.DBControl;
import com.example.taskdistributinghall.R;

import java.sql.SQLException;

public class CollectInformation extends AppCompatActivity {
      String gender=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_information1);
        Intent collectionIntent=getIntent();
        Button ok_btn=findViewById(R.id.ok_btn);
        RadioGroup radioGroup=findViewById(R.id.gender_radio_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.men)
                    gender="男";
                else gender="女";
            }
        });
        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(gender==null){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(CollectInformation.this,"请选择性别",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                String phone=collectionIntent.getStringExtra("phone");
                String password=collectionIntent.getStringExtra("password");//获取signup的数据
                String name=((EditText)findViewById(R.id.name_edit)).getText().toString();
                String address=((EditText)findViewById(R.id.address_edit)).getText().toString();
                String grade=((EditText)findViewById(R.id.grade_edit)).getText().toString();
                String dept=((EditText)findViewById(R.id.dept_edit)).getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            DBControl.addUser(phone,password);
                            DBControl.updateUser(null,phone,name,gender,dept,grade,address);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(CollectInformation.this,"注册成功",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                            Intent intent=new Intent();
                            intent.setClass(CollectInformation.this, MainActivity.class);
                            startActivity(intent); //加载主页面
                            CollectInformation.this.finish();

                        } catch (SQLException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(CollectInformation.this," 数据库连接失败请重新登录",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }).start();
            }
        });
    }
}
