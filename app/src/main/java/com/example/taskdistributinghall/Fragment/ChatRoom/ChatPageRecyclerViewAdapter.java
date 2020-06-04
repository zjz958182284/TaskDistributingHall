package com.example.taskdistributinghall.Fragment.ChatRoom;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskdistributinghall.R;

import java.util.List;

public class ChatPageRecyclerViewAdapter extends RecyclerView.Adapter<ChatPageRecyclerViewAdapter.ChatPageViewHolder> {
    private List<String> chatTitleList;//标题

    public ChatPageRecyclerViewAdapter(List<String> listStr) {
        chatTitleList = listStr;
    }

    public class ChatPageViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView imgView;
        public TextView textView;

        public ChatPageViewHolder(@NonNull View itemView) {
            super(itemView);
            imgView=itemView.findViewById(R.id.chat_head_image);
            textView=itemView.findViewById(R.id.chat_name);
            itemView.setOnClickListener(this); // 處理按下的事件。
        }

        @Override
        public void onClick(View view) {



        }
    }
    @NonNull
    @Override
    public ChatPageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_list_view,parent,false);
        ChatPageViewHolder viewHolder=new ChatPageViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatPageViewHolder holder, int position) {
        holder.imgView.setImageResource(R.drawable.tomato);
        holder.textView.setText(chatTitleList.get(position));
    }


    @Override
    public int getItemCount() {
        return chatTitleList.size();
    }
}
