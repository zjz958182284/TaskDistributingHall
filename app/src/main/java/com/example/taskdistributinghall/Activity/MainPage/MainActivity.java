package com.example.taskdistributinghall.Activity.MainPage;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.taskdistributinghall.DBControl.ChatRecordHelper;
import com.example.taskdistributinghall.DBControl.DBControl;
import com.example.taskdistributinghall.Fragment.ChatRoom.ChatRoomFragment;
import com.example.taskdistributinghall.Fragment.Home.HomeFragment;
import com.example.taskdistributinghall.Fragment.Mission.MissionFragment;
import com.example.taskdistributinghall.Fragment.PersonalCenter.PersonalCenterFragment;
import com.example.taskdistributinghall.Model.Task;
import com.example.taskdistributinghall.Model.User;
import com.example.taskdistributinghall.R;
import com.google.android.material.tabs.TabLayout;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<Fragment> fragmentList=new ArrayList<>();;
    private MainPageAdapter mainPageAdapter;
    private TabLayout.Tab homePage;
    private TabLayout.Tab chatRoom;
    private   List<Task> tasks;
    private   List<Task> publishedTask;
    private   List<Task> acceptedTask;
    private  List<User> chatUsers;
    private TabLayout.Tab mission;
    private TabLayout.Tab personalCenter;
    private long lastPressTime=0; //记录上一次按下返回键的时间

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

     //   Toolbar toolbar=findViewById(R.id.toolbar);
     //   setSupportActionBar(toolbar);


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    //先把数据准备好再注入到fragment
                    SharedPreferences sp=getApplicationContext().getSharedPreferences("my_info", Context.MODE_PRIVATE);
                    String phone=sp.getString("phone","");
                    tasks= DBControl.searchAllTask(HomeFragment.OrderByDateMode);
                    publishedTask=DBControl.searchPublishedTask(phone);
                    acceptedTask=DBControl.searchAcceptedTask(phone);
                    SQLiteDatabase recordDatabase=new ChatRecordHelper(getApplicationContext()).getReadableDatabase();
                   // Cursor cursor=recordDatabase.query("chatTimeStamp",new String[]{"receiverphone"},null,
                   //         ,"receiverphone",)
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            initFragmentList();
                            initViews();
                            MainActivity.this.findViewById(R.id.cover_photo).setVisibility(View.GONE);
                            MainActivity.this.findViewById(R.id.main_content).setVisibility(View.VISIBLE);
                        }
                    });

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }

   public void initViews(){
        viewPager=findViewById(R.id.id_vp);
        mainPageAdapter=new MainPageAdapter(getSupportFragmentManager(),fragmentList);
        viewPager.setAdapter(mainPageAdapter);

       /**
        * 非常重要
        */
       viewPager.setOffscreenPageLimit(4);


        tabLayout=findViewById(R.id.id_tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        homePage=tabLayout.getTabAt(0);
        chatRoom=tabLayout.getTabAt(1);
        mission=tabLayout.getTabAt(2);
        personalCenter=tabLayout.getTabAt(3);
        homePage.setIcon(R.drawable.homepage);
        chatRoom.setIcon(R.drawable.chat);
        mission.setIcon(R.drawable.mission);
        personalCenter.setIcon(R.drawable.personalcenter);
   }

    public void initFragmentList(){

        HomeFragment homeFragment=new HomeFragment(tasks);
        fragmentList.add(homeFragment);
        fragmentList.add(new ChatRoomFragment());
        fragmentList.add(new MissionFragment(publishedTask,acceptedTask));
        fragmentList.add(new PersonalCenterFragment());
    }

    /**
     * 优化app退出
     */
    @Override
    public void onBackPressed(){
        if(lastPressTime==0) {
           lastPressTime = System.currentTimeMillis();
            Toast.makeText(this,"再按一次退出程序",Toast.LENGTH_SHORT).show();
        }
        else if((System.currentTimeMillis()-lastPressTime)>2000){
            Toast.makeText(this,"再按一次退出程序",Toast.LENGTH_SHORT).show();
            lastPressTime=System.currentTimeMillis();
        }
        else System.exit(0);
    }

}
