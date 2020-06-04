package com.example.taskdistributinghall.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.taskdistributinghall.Activity.MainPage.MainActivity;
import com.example.taskdistributinghall.DBControl.DBControl;
import com.example.taskdistributinghall.R;

import java.sql.SQLException;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button login_btn=findViewById(R.id.login_btn);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etUser=findViewById(R.id.phone_edit);
                EditText etPassWord=findViewById(R.id.password_edit);
                    /**
                     *  验证登录是否成功
                     */

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String phone=etUser.getText().toString();
                            String password= etPassWord.getText().toString();
                            try {
                                int loginState=DBControl.validateAccount(phone,password);
                                if(loginState==1){
                                    DBControl.updateIP(phone);
                                   runOnUiThread(new Runnable() {
                                       @Override
                                       public void run() {
                                           /**
                                            * 保存用户数据到本地SP文件
                                            */
                                           SharedPreferences sp=getApplicationContext().getSharedPreferences("my_info",Context.MODE_PRIVATE);
                                           SharedPreferences.Editor editor=sp.edit();
                                           editor.putString("phone",phone);
                                           editor.putString("password",password);
                                           editor.apply();

                                           Toast.makeText(Login.this,"登陆成功",
                                                   Toast.LENGTH_SHORT).show();
                                           Intent intent=new Intent();
                                           intent.setClass(Login.this, MainActivity.class);
                                           startActivity(intent);
                                           Login.this.finish();
                                       }
                                   });
                                }
                                else if(loginState==0){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(Login.this,"不存在此用户请注册账号",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                                else {
                                   runOnUiThread(new Runnable() {
                                       @Override
                                       public void run() {
                                           Toast.makeText(Login.this,"密码错误",
                                                   Toast.LENGTH_SHORT).show();
                                       }
                                   });
                                }
                            } catch (SQLException ex) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(Login.this," 数据库连接失败请重新登录",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }).start();
            }
        });

        TextView create_btn=findViewById(R.id.create_text);
        create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent=new Intent();
                registerIntent.setClass(Login.this, SignUp.class);
                startActivity(registerIntent);
            }
        });
    }
}
