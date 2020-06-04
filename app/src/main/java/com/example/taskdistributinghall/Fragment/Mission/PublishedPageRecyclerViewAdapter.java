package com.example.taskdistributinghall.Fragment.Mission;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskdistributinghall.PublishedTaskDetailPage;
import com.example.taskdistributinghall.R;

import java.util.List;

public class PublishedPageRecyclerViewAdapter extends RecyclerView.Adapter<PublishedPageRecyclerViewAdapter.PublishedPageViewHolder> {
    private List<String> missionTitle;//标题

    public PublishedPageRecyclerViewAdapter(List<String> listStr){
        missionTitle=listStr;
    }

    public class PublishedPageViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView imgView;
        public TextView textView;

        public  PublishedPageViewHolder(View itemView){
            super(itemView);
            imgView=itemView.findViewById(R.id.published_page_mission_view);
            textView=itemView.findViewById(R.id.published_page_mission_title);
            itemView.setOnClickListener(this); // 處理按下的事件。
        }
        @Override
        public void onClick(View v) {
            Context context= v.getContext();
            Intent intent =new Intent(context, PublishedTaskDetailPage.class);
            context.startActivity(intent);
        }
    }



    @Override
    public PublishedPageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.published_task_view,parent,false);
       PublishedPageViewHolder viewHolder=new PublishedPageViewHolder(v);
       return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PublishedPageViewHolder holder, int position) {
        holder.imgView.setImageResource(R.drawable.test1);
        holder.textView.setText(missionTitle.get(position));
    }

    @Override
    public int getItemCount() {
       return missionTitle.size();
    }
}
