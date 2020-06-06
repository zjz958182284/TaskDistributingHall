package com.example.taskdistributinghall.Fragment.Home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.taskdistributinghall.Activity.AddMissionPage;
import com.example.taskdistributinghall.DBControl.DBControl;
import com.example.taskdistributinghall.Activity.TaskDetail.Mission_detail_page;
import com.example.taskdistributinghall.Model.Task;
import com.example.taskdistributinghall.R;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class HomeFragment  extends Fragment {

    private static HomeFragment homeFragment=null;
    private List<Task> tasks ;
    private RecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter.ItemClick itemClick;
    private View view;
    public  HomeFragment(List<Task> tasks){
        this.tasks=tasks;
       homeFragment=this;
    }

    public void setItemClick(RecyclerViewAdapter.ItemClick itemClick){this.itemClick=itemClick;}

    public void setTasks(List<Task> tasks){
        this.tasks=tasks;
    }
    public List<Task> getTasks(){
        return tasks;
    }
    public RecyclerViewAdapter getAdapter(){
        return adapter;
    }



    //无奈之举
    //永远获取当前实例对象（不是单例模式）
    public static HomeFragment getInstance() {
     if(homeFragment!=null)
         return homeFragment;
       else return null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.home_page,container,false);
        recyclerView=view.findViewById(R.id.home_page_recycler_view);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
        adapter=new RecyclerViewAdapter(getContext(),tasks);

        adapter.setItemClick(new RecyclerViewAdapter.ItemClick() {
            @Override
            public void onItemClick(Task task) {
                Intent intent=new Intent();
                intent.putExtra("id",task.id);
                intent.putExtra("publisher",task.publisher);
                intent.setClass(getContext(), Mission_detail_page.class);
                startActivity(intent);
            }
        });/////////

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
               startActivity(intent1);
        }
        return super.onOptionsItemSelected(item);
    }


    //每次滑动到这个碎片就 实时 更新任务界面显示
  // @Override
  // public void setUserVisibleHint(boolean isVisibleToUser) {
  //  super.setUserVisibleHint(isVisibleToUser);
  //  if (isVisibleToUser) {
//
  //      new Thread(new Runnable() {
  //          @Override
  //          public void run() {
  //              try {
  //                  //不是第一次加载滑动到这个fragment
  //                  if(adapter!=null) {
  //                  tasks=DBControl.searchAllTask();
  //                      adapter.setTasks(tasks);
  //                      Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
  //                          @Override
  //                          public void run() {
  //                              adapter.notifyDataSetChanged();
  //                          }
  //                      });
  //                  }
  //              } catch (SQLException e) {
  //                  e.printStackTrace();
  //              }
  //          }
  //      }).start();
//
//
  //     }
  // }
//

    //比上下注释过的方法好用太多且不会造成系统崩溃
    @Override
    public void onResume() {
        super.onResume();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    tasks=DBControl.searchAllTask();
                    adapter.setTasks(tasks);
                    Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }).start();

    }



  //    public void refresh(){
  //   new Thread(new Runnable() {
  //       @Override
  //       public void run() {
  //           try {
  //                   tasks=DBControl.searchAllTask();
  //                   adapter.setTasks(tasks);
  //                   Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
  //                       @Override
  //                       public void run() {
  //                           adapter.notifyDataSetChanged();
  //                       }
  //                   });
  //               } catch (SQLException ex) {
  //               ex.printStackTrace();
  //           }
  //       }
  //   }).start();
  //}


    }

