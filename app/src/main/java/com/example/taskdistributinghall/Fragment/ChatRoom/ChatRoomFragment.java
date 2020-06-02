package com.example.taskdistributinghall.Fragment.ChatRoom;

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
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.taskdistributinghall.Activity.AddMissionPage;
import com.example.taskdistributinghall.Activity.Chat;
import com.example.taskdistributinghall.Activity.SignUp;
import com.example.taskdistributinghall.R;

import java.util.ArrayList;
import java.util.List;

public class ChatRoomFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.chat_list_page,null);
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
        inflater.inflate(R.menu.chat_page_menu,menu);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        androidx.appcompat.widget.Toolbar toolbar1= (androidx.appcompat.widget.Toolbar) getActivity().findViewById(R.id.chat_page_toolbar);
        AppCompatActivity appCompatActivity=(AppCompatActivity)getActivity();
        appCompatActivity.setSupportActionBar(toolbar1);


        // 把項目清單準備好，放在一個List物件裏頭
        List<String> listStr = new ArrayList<>();
        for (int i = 0; i < 50; i++)
            listStr.add(new String("第" + String.valueOf(i+1) + "項"));
        RecyclerView recyclerView1=view.findViewById(R.id.chat_list_recycler_view);

        // 設定RecyclerView使用的LayoutManager，
        // LayoutManager決定項目的排列方式。
        recyclerView1.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));

        // 建立RecyclerView的Adapter物件，傳入包含項目清單的List物件
        ChatPageRecyclerViewAdapter adapter=new ChatPageRecyclerViewAdapter(listStr);
        recyclerView1.setAdapter(adapter);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

          // case R.id.add_bar:
          //     Intent intent1=new Intent(getActivity(), Chat.class); /**  */

            case R.id.chat_page_add_bar:
                Intent intent1=new Intent(getActivity(), AddMissionPage.class);

                startActivity(intent1);
        }
        return super.onOptionsItemSelected(item);
    }


}
