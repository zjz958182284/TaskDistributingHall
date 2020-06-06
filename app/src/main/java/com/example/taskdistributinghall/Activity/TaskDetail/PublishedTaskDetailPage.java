package com.example.taskdistributinghall.Activity.TaskDetail;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.taskdistributinghall.Activity.AcceptorList.AcceptorListPage;
import com.example.taskdistributinghall.Activity.ChatPage.Chat;
import com.example.taskdistributinghall.DBControl.DBControl;
import com.example.taskdistributinghall.Fragment.Home.HomeFragment;
import com.example.taskdistributinghall.Fragment.Mission.PublishedPageRecyclerViewAdapter;
import com.example.taskdistributinghall.Fragment.Mission.PublishedTaskPage;
import com.example.taskdistributinghall.Model.Task;
import com.example.taskdistributinghall.Model.User;
import com.example.taskdistributinghall.R;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

public class PublishedTaskDetailPage extends AppCompatActivity {

    Button button;
    Button cancelButton;
    Task task;
    Task.taskState state;
    List<Task> tasks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_published_task_detail_page);

        button = findViewById(R.id.list_btn);
        cancelButton = findViewById(R.id.cancel_btn);


        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);

        Thread thread=
        new Thread(new Runnable() {
            @Override
            public void run() {
                task = DBControl.searchTaskByID(id);
                state = DBControl.searchTaskState(id);
            }
        });

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //布置界面
        if(task!=null) {
            ((TextView) findViewById(R.id.published_task_detail_page_title_text)).setText(task.title);
            ((TextView) findViewById(R.id.published_task_detail_page_date_text)).setText("发布日期:" + task.date.substring(0, 16));
            ((TextView) findViewById(R.id.published_task_detail_page_bounty_text)).setText("酬劳金额:" + task.rewards + "元");
            String type = task.type;
            if (type.equals("errand"))
                type = "跑腿";
            else if (type.equals("study"))
                type = "学习";
            else type = "合作";
            ((TextView) findViewById(R.id.published_task_detail_page_type_text)).setText("任务类型:" + type);
            ((TextView) findViewById(R.id.published_task_detail_page_detail_text)).setText(task.content);
            ((ImageView) findViewById(R.id.published_task_detail_page_image_view)).setImageBitmap(task.taskPhoto);


            //编译器自动纠正
            if (state == Task.taskState.unaccepted) {
                button.setText("查看请求人列表");
                cancelButton.setVisibility(View.VISIBLE);
                cancelButton.setText("删除任务");
            } else if (state == Task.taskState.accepted)
                button.setText("点击进入与对方聊天");
            else if (state == Task.taskState.cancelled) {
                button.setText("重新发布");
                cancelButton.setVisibility(View.VISIBLE);
            } else {
                button.setVisibility(View.GONE);
                cancelButton.setVisibility(View.VISIBLE);
                cancelButton.setText("对方已收到报酬点击将此任务删除");
            }
        }

        button.setOnClickListener(new View.OnClickListener()

    {
        @Override
        public void onClick (View v){
        String btnText = button.getText().toString();
        if (btnText.equals("查看请求人列表")) {
            Intent intent = getIntent();
            int id = intent.getIntExtra("id", 0);
            Intent intent1 = new Intent();
            intent1.putExtra("id", id);
            intent1.setClass(getApplicationContext(), AcceptorListPage.class);
            startActivity(intent1);
        } else if (btnText.equals("点击进入与对方聊天")) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Intent intent = getIntent();
                    int id = intent.getIntExtra("id", 0);
                    User user = DBControl.searchAcceptor(id);
                    Intent intent1 = new Intent();
                    intent1.putExtra("phone", user.phone);
                    intent1.setClass(getApplicationContext(), Chat.class);
                    startActivity(intent1);
                }
            }).start();
        } else if (btnText.equals("重新发布")) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Intent intent = getIntent();
                    int id = intent.getIntExtra("id", 0);
                    try {
                        DBControl.updateTaskState(id, Task.taskState.unaccepted);

                    } catch (SQLException e) {
                      runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(PublishedTaskDetailPage.this, "重新发布任务失败",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }).start();
        }
        finish();
    }
    });

        cancelButton.setOnClickListener(new View.OnClickListener()

    {
        @Override
        public void onClick (View v){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Intent intent = getIntent();
                int id = intent.getIntExtra("id", 0);
                try {
                    if (DBControl.deleteTask(id)) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                              //  PublishedTaskPage page=PublishedTaskPage.getInstance();
                              //  page.refresh();
                                Toast.makeText(PublishedTaskDetailPage.this, "删除任务成功",
                                        Toast.LENGTH_SHORT).show();
                                 button.setVisibility(View.GONE);
                                 cancelButton.setVisibility(View.GONE);
                            }
                        });

                    }
                } catch (SQLException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(PublishedTaskDetailPage.this, "删除任务失败",
                                    Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        }).start();


    }
    });

}}
