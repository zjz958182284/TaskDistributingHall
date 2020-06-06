package com.example.taskdistributinghall.Fragment.PersonalCenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.taskdistributinghall.DBControl.DBControl;
import com.example.taskdistributinghall.Model.User;
import com.example.taskdistributinghall.R;
import com.shehuan.niv.NiceImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PersonalCenterFragment extends Fragment {


    User user;

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private static PersonalCenterFragment fragment;

    public  PersonalCenterFragment(){
        PersonalCenterFragment.fragment=this;
    }

    public static PersonalCenterFragment getInstance(){
        return PersonalCenterFragment.fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.personal_center_page,null);

        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sp=getActivity().getSharedPreferences("my_info", Context.MODE_PRIVATE);
                String phone=sp.getString("phone","");
                user= DBControl.searchUserByPhone(phone);

            }
        });
        thread.start();

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        TextView name=view.findViewById(R.id.personal_name_text);
        NiceImageView photo=view.findViewById(R.id.profile_photo);
        TextView publish=view.findViewById(R.id.published_task_number);
        TextView accept=view.findViewById(R.id.accepted_task_number);
        name.setText(user.name);
        publish.setText(String.valueOf(user.completedTask));
        accept.setText(String.valueOf(user.acceptedTask));
        photo.setImageBitmap(user.headPortrait);
        init(view);
        return view;
     }

     public void init(View view){
         List<Map<String, Object>> listItem = new ArrayList<Map<String, Object>>(); //存储数据的数组列表
         int[] image_expense = new int[]{R.drawable.about_icon, R.drawable.sign_out_icon}; //存储图片
         int[] image_behind = new int[]{R.drawable.right_arrow, 0}; //存储图片

         String[] titles=new String[]{"修改个人资料","退出"};
         if(listItem!=null) {
             for (int i = 0; i < image_expense.length; i++) {
                 Map<String, Object> map = new HashMap<String, Object>();
                 map.put("iv", image_expense[i]);
                 map.put("tv", titles[i]);
                 map.put("iv_behind", image_behind[i]);
                 listItem.add(map);
             }

             SimpleAdapter adapter = new SimpleAdapter(getActivity()
                     , listItem
                     , R.layout.custom_list_view
                     , new String[]{"iv", "tv", "iv_behind"}
                     , new int[]{R.id.iv, R.id.tv, R.id.iv_behind});
             ListView listView = (ListView) view.findViewById(R.id.option_list_view);
             listView.setAdapter(adapter);
             listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                 @Override
                 public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                     Map<String,Object>map=(Map<String, Object>)parent.getItemAtPosition(position);
                    switch (position){
                        case 0:
                           Intent intent=new Intent(getActivity(), ModifyPersonalInfo.class);
                            startActivity(intent);
                        case 1:

                            break;
                        default:
                    }
                 }
             });
         }
     }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

            new Thread(new Runnable() {
                @Override
                public void run() {

                }
            }).start();

        }
    }


    public void refresh(){

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SharedPreferences sp=getActivity().getSharedPreferences("my_info", Context.MODE_PRIVATE);
                        String phone=sp.getString("phone","");
                        user= DBControl.searchUserByPhone(phone);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TextView name=PersonalCenterFragment.this.getView().findViewById(R.id.personal_name_text);
                                NiceImageView photo=PersonalCenterFragment.this.getView().findViewById(R.id.profile_photo);
                                TextView publish=PersonalCenterFragment.this.getView().findViewById(R.id.published_task_number);
                                TextView accept=PersonalCenterFragment.this.getView().findViewById(R.id.accepted_task_number);
                                name.setText(user.name);
                                publish.setText(String.valueOf(user.completedTask));
                                accept.setText(String.valueOf(user.acceptedTask));
                                photo.setImageBitmap(user.headPortrait);
                            }
                        });

                    }
                }).start();
            }
}

