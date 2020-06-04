package com.example.taskdistributinghall.Fragment.Home;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.taskdistributinghall.Activity.AddMissionPage;
import com.example.taskdistributinghall.DBControl.DBControl;
import com.example.taskdistributinghall.Model.Task;
import com.example.taskdistributinghall.R;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.example.taskdistributinghall.R.layout;

public class HomeFragment  extends Fragment {

 private List<Task> tasks;
 private RecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private View view;
    public  HomeFragment(List<Task> tasks){
        this.tasks=tasks;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.home_page,container,false);
        recyclerView=view.findViewById(R.id.home_page_recycler_view);
        adapter=new RecyclerViewAdapter(getContext(),tasks);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.homepage_menu,menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        androidx.appcompat.widget.Toolbar toolbar= getActivity().findViewById(R.id.home_page_toolbar);
        AppCompatActivity appCompatActivity=(AppCompatActivity)getActivity();
        appCompatActivity.setSupportActionBar(toolbar);

        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_bar:
                Intent intent1=new Intent(getActivity(), AddMissionPage.class);
              // Bundle bundle=
              // startActivity(intent1);
        }
        return super.onOptionsItemSelected(item);
    }



}