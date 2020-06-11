package com.example.taskdistributinghall.Fragment.Mission;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.taskdistributinghall.Model.Task;
import com.example.taskdistributinghall.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class MissionFragment extends Fragment {
   private List<Fragment>fragmentList;
   private MissionPageAdapter missionPageAdapter;
   private ViewPager viewPager;

    List<Task> publishedTask;
    List<Task> acceptedTask;

    public MissionFragment(List<Task> publishedTask,List<Task> acceptedTask){
        this.publishedTask=publishedTask;
        this.acceptedTask=acceptedTask;

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.mission_page,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initFragment();
        TabLayout tabLayout=view.findViewById(R.id.tab_layout);
        missionPageAdapter=new MissionPageAdapter(getChildFragmentManager(),fragmentList);
        viewPager=view.findViewById(R.id.view_Pager);

        /**
         * 设置缓存页数 如果不设置 从fragment1切换到fragment3然后再从fragment3切换到fragment1是会重复调用
         * fragment1的onCreate方法容易造成程序崩溃
         */

        viewPager.setOffscreenPageLimit(2);
        viewPager.setCurrentItem(1);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(missionPageAdapter);
        missionPageAdapter.notifyDataSetChanged();
        super.onViewCreated(view, savedInstanceState);
    }

   public void initFragment(){

        fragmentList=new ArrayList<>();
       AcceptedTaskPage acceptedTaskPage=new AcceptedTaskPage(acceptedTask);
      PublishedTaskPage publishedTaskPage =new PublishedTaskPage(publishedTask);
        fragmentList.add(acceptedTaskPage);
        fragmentList.add(publishedTaskPage);
   }

}

