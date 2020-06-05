package com.example.taskdistributinghall.Activity.TaskDetail;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.taskdistributinghall.Activity.AcceptorList.AcceptorListPage;
import com.example.taskdistributinghall.Activity.ChatPage.Chat;
import com.example.taskdistributinghall.DBControl.DBControl;
import com.example.taskdistributinghall.Model.Task;
import com.example.taskdistributinghall.Model.User;
import com.example.taskdistributinghall.R;

import java.sql.SQLException;

public class AcceptedTaskDetailPage extends AppCompatActivity {

    Button bountyButton;
    Button cancelButton;
    Button chatButton;
    Task task;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accepted_task_detail);
        bountyButton = findViewById(R.id.bounty_got_btn);
        cancelButton = findViewById(R.id.cancel_btn);
        chatButton=findViewById(R.id.chat_btn);

        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);

        Thread thread=
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        task = DBControl.searchTaskByID(id);
                    }
                });

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        init();

    }

    private void initButton() {
        bountyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        showDialogForBountyGot();
                    }
        });


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogForCancelTask();
            }
        });

        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        User user = DBControl.searchPublisher(task.id);
                        Intent intent = new Intent();
                        intent.putExtra("phone", user.phone);
                        intent.setClass(AcceptedTaskDetailPage.this, Chat.class);
                        startActivity(intent);
                    }
                }).start();

            }
        });
    }

    private void init() {
        ((TextView) findViewById(R.id.accepted_task_detail_page_title_text)).setText(task.title);
        ((TextView) findViewById(R.id.accepted_task_detail_page_date_text)).setText("发布日期:" + task.date.substring(0, 16));
        ((TextView) findViewById(R.id.accepted_task_detail_page_bounty_text)).setText("酬劳金额:" + task.rewards + "元");
        String type=task.type;
        if(type.equals("errand"))
            type="跑腿";
        else if(type.equals("study"))
            type="学习";
        else type="合作";
        ((TextView) findViewById(R.id.accepted_task_detail_page_type_text)).setText("任务类型:" + type);
        ((TextView) findViewById(R.id.accepted_task_detail_page_detail_text)).setText(task.content);
        ((ImageView) findViewById(R.id.accepted_task_detail_page_image_view)).setImageBitmap(task.taskPhoto);
        initButton();
    }

    protected   void showDialogForBountyGot(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("提示");

        builder.setMessage("确认收到酬金后该任务可随时被对方取消，请确定自己已经收到酬金");
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
                            DBControl.updateTaskState(task.id, Task.taskState.completed);

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

                AcceptedTaskDetailPage.this.finish();
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
        btn_pos.setTextColor(Color.BLUE);
        Button btn_neg=dialog.getButton((DialogInterface.BUTTON_NEGATIVE));
        btn_neg.setTextColor(Color.BLUE);
    }

    protected   void showDialogForCancelTask(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("提示");

        builder.setMessage("请确认是否无法再继续完成该任务？");
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
                            DBControl.modifyAcceptor(task.id,null);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

               AcceptedTaskDetailPage.this.finish();
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
        btn_pos.setTextColor(Color.BLUE);
        Button btn_neg=dialog.getButton((DialogInterface.BUTTON_NEGATIVE));
        btn_neg.setTextColor(Color.BLUE);
    }


}
