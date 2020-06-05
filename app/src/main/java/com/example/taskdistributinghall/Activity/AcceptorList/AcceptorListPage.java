package com.example.taskdistributinghall.Activity.AcceptorList;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.taskdistributinghall.DBControl.DBControl;
import com.example.taskdistributinghall.Fragment.Mission.AcceptedPageRecyclerViewAdapter;
import com.example.taskdistributinghall.Model.User;
import com.example.taskdistributinghall.R;

import java.sql.SQLException;
import java.util.List;

public class AcceptorListPage extends AppCompatActivity {

    List<User> users;
    AcceptorListAdapter adapter;
    RecyclerView recyclerView;
    int taskID;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accept_people_list);

        Intent intent = getIntent();
        taskID = intent.getIntExtra("id", 0);
        Thread thread =new Thread(new Runnable() {
            @Override
            public void run() {
                users= DBControl.searchRequestingUser(taskID);
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        recyclerView=findViewById(R.id.acceptor_recycle_list);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
        adapter=new AcceptorListAdapter(getApplicationContext(),users,taskID);
        adapter.setButtonClickListener(new AcceptorListAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(User user) {

                showDialog(user);

            }
        });

        adapter.setClickItemListener(new AcceptorListAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(User user) {
                Intent intent = new Intent();
                intent.putExtra("phone",user.phone);
                intent.putExtra("id",taskID);
                intent.setClass(AcceptorListPage.this, AcceptorInfo.class);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }










    protected   void showDialog( User user){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("提示");

        builder.setMessage("请仔细查看相关请求人的个人信息，是否确认选择他来完成你的任务？");
        builder.setIcon(R.mipmap.ic_launcher);

        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SharedPreferences sp=getApplicationContext().getSharedPreferences("my_info", Context.MODE_PRIVATE);
                        String phone=sp.getString("phone","");
                        try {
                            DBControl.modifyAcceptor(taskID,user.phone);
                            DBControl.deletePendingTask(taskID);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

                AcceptorListPage.this.finish();
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

       AlertDialog dialog= builder.create();
       dialog.show();
        Button btn_pos=dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        btn_pos.setTextColor(Color.GREEN);
        Button btn_neg=dialog.getButton((DialogInterface.BUTTON_NEGATIVE));
        btn_neg.setTextColor(Color.GREEN);
    }

}


