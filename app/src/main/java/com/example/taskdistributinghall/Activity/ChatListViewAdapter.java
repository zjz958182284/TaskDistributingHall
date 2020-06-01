package com.example.taskdistributinghall.Activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.taskdistributinghall.Model.Message;
import com.example.taskdistributinghall.R;

import java.util.LinkedList;

public class ChatListViewAdapter extends BaseAdapter {

    private Bitmap myPortrait;
    private Bitmap hisPortrait;
    private LinkedList<Message> messages;
    private LayoutInflater inflater;

    public ChatListViewAdapter(Context context,LinkedList<Message> list,Bitmap myPortrait
    ,Bitmap hisPortrait) {
        super();
        this.messages=list;
        this.myPortrait=myPortrait;
        this.hisPortrait=hisPortrait;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int position){
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type=getItemViewType(position);
        Message message=messages.get(position);//与该行view对应的数据源
        ViewHolder viewHolder;
        if(convertView==null){
            viewHolder=new ViewHolder();
            if(type==Message.recentChatTime){  //如果消息类型为时间
                convertView=inflater.inflate(R.layout.chat_time_section,null);
                viewHolder.progressBar=convertView.findViewById(R.id.chat_progress);
                viewHolder.textView=convertView.findViewById(R.id.chat_time);
                viewHolder.textView.setText(message.getContent());
            }
            else if(type==Message.Isend){ //如果消息类型为我发送的消息
                convertView=inflater.inflate(R.layout.send_message,null);
                viewHolder.imageView=convertView.findViewById(R.id.imageViewSend);
                viewHolder.imageView.setImageBitmap(myPortrait);
                viewHolder.textView=convertView.findViewById(R.id.textViewSend);
                viewHolder.textView.setText(message.getContent());
            }
            else {  //如果消息类型为他发送的消息
                convertView=inflater.inflate(R.layout.recept_message,null);
                viewHolder.imageView=convertView.findViewById(R.id.imageViewRecept);
                viewHolder.imageView.setImageBitmap(hisPortrait);
                viewHolder.textView=convertView.findViewById(R.id.textViewRecept);
                viewHolder.textView.setText(message.getContent());
            }
            convertView.setTag(viewHolder);
            return convertView;
        }
        else return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return messages.get(position).getType();
    }



    /**
     * 便于复用itemview
     */
    private static class ViewHolder{
        private TextView textView;
        private ImageView imageView;
        private ProgressBar progressBar;
    }
}


