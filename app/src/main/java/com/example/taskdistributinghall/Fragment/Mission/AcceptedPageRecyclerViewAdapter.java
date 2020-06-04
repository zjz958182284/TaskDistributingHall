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

import com.example.taskdistributinghall.AcceptedTaskDetailPage;
import com.example.taskdistributinghall.R;

import java.util.List;

public class AcceptedPageRecyclerViewAdapter extends RecyclerView.Adapter<AcceptedPageRecyclerViewAdapter.AcceptedPageViewHolder>{
    private List<String> acceptedPageMissionTitle;//标题

    public AcceptedPageRecyclerViewAdapter(List<String> listStr){
        acceptedPageMissionTitle=listStr;
    }

    public class AcceptedPageViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView imgView;
        public TextView textView;

        public  AcceptedPageViewHolder(View itemView){
            super(itemView);
            imgView=itemView.findViewById(R.id.accepted_page_mission_view);
            textView=itemView.findViewById(R.id.accepted_page_mission_title);
            itemView.setOnClickListener(this); // 處理按下的事件。
        }
        @Override
        public void onClick(View v) {
            Context context= v.getContext();
            Intent intent =new Intent(context, AcceptedTaskDetailPage.class);
            context.startActivity(intent);
        }
    }



    @Override
    public AcceptedPageRecyclerViewAdapter.AcceptedPageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.accepted_task_view,parent,false);
        AcceptedPageRecyclerViewAdapter.AcceptedPageViewHolder viewHolder=new AcceptedPageRecyclerViewAdapter.AcceptedPageViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AcceptedPageRecyclerViewAdapter.AcceptedPageViewHolder holder, int position) {
        holder.imgView.setImageResource(R.drawable.test1);
        holder.textView.setText(acceptedPageMissionTitle.get(position));
    }

    @Override
    public int getItemCount() {
        return acceptedPageMissionTitle.size();
    }
}
