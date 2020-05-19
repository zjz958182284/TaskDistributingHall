package com.example.taskdistributinghall.Fragment.Mission;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class MissionPageAdapter extends FragmentPagerAdapter{
    private List<Fragment>fragmentList;
    private String titles[]={"已接取任务","已发布任务"};

    public MissionPageAdapter(@NonNull FragmentManager fm,List<Fragment>fragmentList) {
        super(fm);
        this.fragmentList=fragmentList;
    }

    public MissionPageAdapter(FragmentManager childFragmentManager) {
        super(childFragmentManager);
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
        return titles[position];
    }
}