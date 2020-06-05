package com.example.taskdistributinghall.Fragment.Mission;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskdistributinghall.Model.Task;
import com.example.taskdistributinghall.R;

import java.util.List;

public class PublishedPageRecyclerViewAdapter extends RecyclerView.Adapter<PublishedPageRecyclerViewAdapter.ViewHolder> {
    private List<Task> tasks;
    private LayoutInflater inflater;
    private PublishedPageRecyclerViewAdapter.ItemClick itemClick;
    public PublishedPageRecyclerViewAdapter(Context context,List<Task> tasks){
        this.tasks=tasks;
        inflater=LayoutInflater.from(context);
    }

    public void setTasks(List<Task> tasks){
        this.tasks=tasks;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= inflater.inflate(R.layout.home_page_mission_view,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PublishedPageRecyclerViewAdapter.ViewHolder holder, int position) {
        // 把資料設定給 holder。
        holder.imgView.setImageBitmap(tasks.get(position).taskPhoto);
        holder.timeView.setText((tasks.get(position).date).substring(0,16));
        holder.titleView.setText(tasks.get(position).title);
        holder.detailView.setText(tasks.get(position).content);
        int rewards=tasks.get(position).rewards;
        String reward=String.valueOf(rewards);
        holder.bountyView.setText(reward+"元");
        String type=tasks.get(position).type;
        if(type.equals("errand"))
            type="跑腿";
        else if(type.equals("study"))
            type="学习";
        else type="合作";
        holder.typeView.setText(type);
        View v=holder.itemView;
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemClick!=null)
                    itemClick.onItemClick(tasks.get(position));
            }
        });
    }



    @Override
    public int getItemCount() {
       return tasks.size();
    }



    class ViewHolder extends  RecyclerView.ViewHolder {
        private ImageView imgView;
        private TextView titleView;
        private   TextView detailView;
        private TextView bountyView;
        private TextView timeView;
        private TextView typeView;


        //包含有每一行的View
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgView=itemView.findViewById(R.id.mission_view);
            titleView =itemView.findViewById(R.id.mission_title);
            detailView=itemView.findViewById(R.id.description_text);
            bountyView=itemView.findViewById(R.id.bounty);
            timeView=itemView.findViewById(R.id.date);
            typeView=itemView.findViewById(R.id.mission_type);
        }

    }

    public void setItemClick(PublishedPageRecyclerViewAdapter.ItemClick itemClick) {
        this.itemClick = itemClick;
    }


    public  static  interface  ItemClick{
        /**
         *
         * @param task 当前被点击列表项对应的任务
         */
        void onItemClick(Task task);
    }
}
