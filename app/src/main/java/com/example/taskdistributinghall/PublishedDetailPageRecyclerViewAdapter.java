package com.example.taskdistributinghall;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PublishedDetailPageRecyclerViewAdapter extends RecyclerView.Adapter<PublishedDetailPageRecyclerViewAdapter.PublishedDetailPageViewHolder>  {
    private List<String> numberList;//标题

    public PublishedDetailPageRecyclerViewAdapter(List<String> listStr){
        numberList=listStr;
    }

    public class PublishedDetailPageViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView imgView;
        public TextView textView;

        public  PublishedDetailPageViewHolder(View itemView){
            super(itemView);
            imgView=itemView.findViewById(R.id.accept_people_head_image);
            textView=itemView.findViewById(R.id.accept_people_name);
            itemView.setOnClickListener(this); // 處理按下的事件。
        }
        @Override
        public void onClick(View v) {

        }
    }



    @Override
    public PublishedDetailPageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.accept_people_list_view,parent,false);
      PublishedDetailPageViewHolder viewHolder=new PublishedDetailPageViewHolder(v);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull PublishedDetailPageViewHolder holder, int position) {
        holder.imgView.setImageResource(R.drawable.test1);
        holder.textView.setText(numberList.get(position));
    }

    @Override
    public int getItemCount() {
        return numberList.size();
    }
}

