package com.example.taskdistributinghall.Activity.Launch;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.taskdistributinghall.R;

public class SignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
        Button register_btn=findViewById(R.id.register_btn1);
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone=((EditText)findViewById(R.id.phone_edit1)).getText().toString();
                String password=((EditText)findViewById(R.id.password_edit1)).getText().toString();
                String confirmPassword=((EditText)findViewById(R.id.password_edit2)).getText().toString();
                boolean isEmpty=(password.equals("") || phone.equals(""));
                if(isEmpty){
                    Toast.makeText(SignUp.this,"请输入完整用户名和密码",Toast.LENGTH_SHORT).show();
                }
                else if(password.equals(confirmPassword)){
                Intent intent=new Intent();
                intent.setClass(SignUp.this, CollectInformation.class);
                intent.putExtra("phone",phone);  //与CollectInformationActivity交互数据
                intent.putExtra("password",password);
                startActivity(intent);
                SignUp.this.finish();
                }
                else {
                    Toast.makeText(SignUp.this,"密码与确认密码不相符请重新输入密码",Toast.LENGTH_SHORT).show();
                    ((EditText)findViewById(R.id.password_edit1)).setText("");
                    ((EditText)findViewById(R.id.password_edit2)).setText("");
                }
            }
        });
    }


}
