package com.example.taskdistributinghall.Fragment.Home;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskdistributinghall.Mission_detail_page;
import com.example.taskdistributinghall.R;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    @NonNull
    // 儲存要顯示的資料。
    private List<String>titleList;//标题

    public RecyclerViewAdapter(List<String> listStr) {
        titleList = listStr;
    }

    // ViewHolder 是把項目中所有的 View 物件包起來。
    // 它在 onCreateViewHolder() 中使用。
    public class ViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView imgView;
        public TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgView=itemView.findViewById(R.id.mission_view);
            textView=itemView.findViewById(R.id.mission_title);
            itemView.setOnClickListener(this); // 處理按下的事件。
        }


        @Override
        public void onClick(View view) {
          Context context= view.getContext();
          Intent intent =new Intent(context, Mission_detail_page.class);
          context.startActivity(intent);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.home_page_mission_view,parent,false);
       ViewHolder viewHolder=new ViewHolder(v);
        return viewHolder;
    }


    // RecyclerView會呼叫這個方法，我們必須把項目資料填入ViewHolder物件。
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // 把資料設定給 holder。
        holder.imgView.setImageResource(R.drawable.test1);
        holder.textView.setText(titleList.get(position));
    }



    // RecyclerView會呼叫這個方法，我們要傳回總共有幾個項目。
    @Override
    public int getItemCount() {
        return titleList.size();
    }
}
