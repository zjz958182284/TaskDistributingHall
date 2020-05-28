package com.example.taskdistributinghall;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.taskdistributinghall.DBControl.DBControl;

import java.sql.SQLException;

public class Login extends AppCompatActivity {
    public static final int popup_toast=1;

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
                    /*
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
                                    Looper.prepare();
                                   Toast.makeText(Login.this,"登陆成功",
                                            Toast.LENGTH_SHORT).show();
                                   Looper.loop();
                                    Intent intent=new Intent();
                                    intent.setClass(Login.this,MainActivity.class);
                                    startActivity(intent);
                                }
                                else if(loginState==0){
                                    Looper.prepare();
                                    Toast.makeText(Login.this,"不存在此用户请注册账号",
                                            Toast.LENGTH_SHORT).show();
                                    Looper.loop();
                                }
                                else {
                                    Looper.prepare();
                                    Toast.makeText(Login.this,"密码错误",
                                            Toast.LENGTH_SHORT).show();
                                    Looper.loop();
                                    etPassWord.setText("");
                                }
                            } catch (SQLException ex) {
                                Looper.prepare();
                                Toast.makeText(Login.this,"数据库连接失败请重新登录",
                                        Toast.LENGTH_SHORT).show();
                                Looper.loop();
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
                registerIntent.setClass(Login.this,SignUp.class);
                startActivity(registerIntent);
            }
        });
    }
}
