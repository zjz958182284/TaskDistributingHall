package com.example.taskdistributinghall.Fragment.Mission;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.taskdistributinghall.AcceptedTaskDetailPage;
import com.example.taskdistributinghall.Activity.PublishedTaskDetail.PublishedTaskDetailPage;
import com.example.taskdistributinghall.Fragment.Home.HomeFragment;
import com.example.taskdistributinghall.Fragment.Home.RecyclerViewAdapter;
import com.example.taskdistributinghall.Mission_detail_page;
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
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(missionPageAdapter);
        super.onViewCreated(view, savedInstanceState);
    }

   public void initFragment(){

        fragmentList=new ArrayList<>();
       AcceptedTaskPage acceptedTaskPage=new AcceptedTaskPage(acceptedTask);
       //设置每一行监听事件

      PublishedTaskPage publishedTaskPage =new PublishedTaskPage(publishedTask);
       //设置每一行监听事件

        fragmentList.add(acceptedTaskPage);
        fragmentList.add(publishedTaskPage);
   }

}

