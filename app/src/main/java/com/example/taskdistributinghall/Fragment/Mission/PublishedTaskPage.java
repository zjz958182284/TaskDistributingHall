package com.example.taskdistributinghall.Fragment.Mission;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.taskdistributinghall.Activity.PublishedTaskDetail.PublishedTaskDetailPage;
import com.example.taskdistributinghall.DBControl.DBControl;
import com.example.taskdistributinghall.Model.Task;
import com.example.taskdistributinghall.R;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PublishedTaskPage extends Fragment {

    private  List<Task> tasks;
    private PublishedPageRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;



    public  PublishedTaskPage(List<Task> tasks){this.tasks=tasks;}


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.published_task_page,null);
         recyclerView=view.findViewById(R.id.published_task_recycler_view);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));

        adapter=new PublishedPageRecyclerViewAdapter(getContext(),tasks);
        adapter.setItemClick(new PublishedPageRecyclerViewAdapter.ItemClick() {

            @Override
            public void onItemClick(Task task) {
                Intent intent=new Intent();
                intent.putExtra("id",task.id);
                intent =new Intent(getContext(), PublishedTaskDetailPage.class);
                startActivity(intent);

            }
        });
        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
    }



    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    //不是第一次加载滑动到这个fragment
                    if(adapter!=null) {
                        SharedPreferences sp=getActivity().getSharedPreferences("my_info", Context.MODE_PRIVATE);
                        String phone=sp.getString("phone","");
                        tasks=DBControl.searchPublishedTask(phone);
                        adapter.setTasks(tasks);
                        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }
                }
            }).start();


        }
    }
}
