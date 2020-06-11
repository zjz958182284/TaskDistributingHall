package com.example.taskdistributinghall.Fragment.Home;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inspector.StaticInspectionCompanionProvider;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.taskdistributinghall.Activity.AddMissionPage;
import com.example.taskdistributinghall.Activity.Launch.SignUp;
import com.example.taskdistributinghall.DBControl.DBControl;
import com.example.taskdistributinghall.Activity.TaskDetail.Mission_detail_page;
import com.example.taskdistributinghall.Model.Task;
import com.example.taskdistributinghall.R;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class HomeFragment  extends Fragment {

    private static HomeFragment homeFragment=null;
    private List<Task> tasks ;
    public final static  int OrderByBountyMode=0;
    public final static  int OrderByDateMode=1;
    private int currentMode=OrderByDateMode;  //当前任务选择排序模式
    private View toolbar;
    private RecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter.ItemClick itemClick; //手动创建的一个循环视图的监听接口
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

        toolbar=view.findViewById(R.id.home_page_toolbar);
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
               startActivity(intent1);break;
            case R.id.search11_bar:
                showDropDownPopUpWindowForSearchTask();break;
            case R.id.sort_bar:
                showDropDownPopUpWindowForSortTask();break;



        }
        return super.onOptionsItemSelected(item);
    }



    private  void showDropDownPopUpWindowForSearchTask(){
        View popView=LayoutInflater.from(getContext()).inflate(R.layout.search_task,null,false);
        PopupWindow popupWindow=new PopupWindow(popView, LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        ((ImageButton)popView.findViewById(R.id.pop_search_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText=popView.findViewById(R.id.search_edit);
                String text=editText.getText().toString();
                if(!text.equals("")){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                tasks=DBControl.searchTasksByFilter(text,currentMode);
                                adapter.setTasks(tasks);
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        adapter.notifyDataSetChanged();
                                        popupWindow.dismiss();
                                    }
                                });
                            } catch (SQLException e) {
                               getActivity().runOnUiThread(new Runnable() {
                                   @Override
                                   public void run() {
                                       Toast.makeText(getActivity(),"搜索失败",Toast.LENGTH_SHORT).show();
                                   }
                               });
                            }
                        }
                    }).start();
                }
            }
        });

        popupWindow.showAsDropDown(toolbar,150,10);
    }

    private void showDropDownPopUpWindowForSortTask(){

        View popupView=LayoutInflater.from(getContext()).inflate(R.layout.sort_task,null,false);
        PopupWindow popupWindow=new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        /**
         * 在外点击能够取消它
         */
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        ((Button)popupView.findViewById(R.id.bounty_sort_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentMode=HomeFragment.OrderByBountyMode;
                Refresh();
                popupWindow.dismiss();
            }
        });
        ((Button)popupView.findViewById(R.id.date_sort_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentMode =HomeFragment.OrderByDateMode;
                Refresh();
                popupWindow.dismiss();
            }
        });


        popupWindow.showAsDropDown(toolbar,230,10);
    }

    //每次滑动到这个碎片就 实时 更新任务界面显示
  @Override
  public void setUserVisibleHint(boolean isVisibleToUser) {
   super.setUserVisibleHint(isVisibleToUser);
   if (isVisibleToUser) {

     new Thread(new Runnable() {
         @Override
         public void run() {
             try {
                 Log.i(TAG, "run:123 ");
                 //不是第一次加载滑动到这个fragment
                 if(adapter!=null) {
                 tasks=DBControl.searchAllTask(currentMode);
                     adapter.setTasks(tasks);
                     Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                         @Override
                          public void run() {
                              adapter.notifyDataSetChanged();
                          }
                      });
                  }
              } catch (SQLException e) {
                  e.printStackTrace();
              }
          }
      }).start();


     }
 }




    //比上下注释过的方法更高效且不会造成系统崩溃
    @Override
    public void onResume() {
        super.onResume();
        Refresh();

    }

    private void Refresh() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    tasks= DBControl.searchAllTask(currentMode);
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

