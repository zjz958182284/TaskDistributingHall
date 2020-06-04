package com.example.taskdistributinghall.Fragment.Mission;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.taskdistributinghall.R;

import java.util.ArrayList;
import java.util.List;

public class AcceptedTaskPage extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.accepted_task_page,null);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        // 把項目清單準備好，放在一個List物件裏頭
        List<String> listStr = new ArrayList<>();
        for (int i = 0; i < 50; i++)
            listStr.add(new String("第" + String.valueOf(i+1) + "項"));
        RecyclerView recyclerView=view.findViewById(R.id.accepted_page_recycler_view);

        // 設定RecyclerView使用的LayoutManager，
        // LayoutManager決定項目的排列方式。
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));

        // 建立RecyclerView的Adapter物件，傳入包含項目清單的List物件
        AcceptedPageRecyclerViewAdapter adapter=new AcceptedPageRecyclerViewAdapter(listStr);
        recyclerView.setAdapter(adapter);
        super.onViewCreated(view, savedInstanceState);
    }
}
