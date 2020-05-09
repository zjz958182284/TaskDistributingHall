package com.example.taskdistributinghall;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.taskdistributinghall.Fragment.ChatRoom.ChatRoomFragment;
import com.example.taskdistributinghall.Fragment.Home.HomeFragment;
import com.example.taskdistributinghall.Fragment.Mission.MissionFragment;
import com.example.taskdistributinghall.Fragment.PersonalCenter.PersonalCenterFragment;

public class MainActivity extends AppCompatActivity {
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.homepage_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView fragment1=findViewById(R.id.homePageBtn);
        ImageView fragment2=findViewById(R.id.personalCenterBtn);
        ImageView fragment3=findViewById(R.id.chatRoomBtn);
        ImageView fragment4=findViewById(R.id.missionBtn);
        fragment1.setOnClickListener(listen);
        fragment2.setOnClickListener(listen);
        fragment3.setOnClickListener(listen);
        fragment4.setOnClickListener(listen);

    }

    View.OnClickListener listen=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            Fragment f = null;
            switch (v.getId()) {
                case R.id.homePageBtn:
                    f = new HomeFragment();
                    break;
                case R.id.personalCenterBtn:
                    f = new PersonalCenterFragment();
                    break;
                case R.id.chatRoomBtn:
                    f=new ChatRoomFragment();
                    break;
                case R.id.missionBtn:
                    f=new MissionFragment();
                    break;
                default:
                    break;
            }
            ft.replace(R.id.fragment, f);
            ft.commit();
        }
    };
}
