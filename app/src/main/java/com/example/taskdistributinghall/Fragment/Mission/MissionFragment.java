package com.example.taskdistributinghall.Fragment.Mission;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.taskdistributinghall.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class MissionFragment extends Fragment {
   private List<Fragment>fragmentList;
   private MissionPageAdapter missionPageAdapter;
   private ViewPager viewPager;

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
        fragmentList.add(new AcceptedTaskPage());
        fragmentList.add(new PublishedTaskPage());
   }

}

