package com.example.taskdistributinghall.Activity.MainPage;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class MainPageAdapter extends FragmentPagerAdapter {
private List<Fragment>fragmentList;
private String title[]=new String[]{"任务大厅","聊天室","任务","个人"};

    public MainPageAdapter(@NonNull FragmentManager fm,List<Fragment>fragmentList) {
        super(fm);
        this.fragmentList=fragmentList;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    public CharSequence getPageTitle(int position){
    return title[position];
    }

}
