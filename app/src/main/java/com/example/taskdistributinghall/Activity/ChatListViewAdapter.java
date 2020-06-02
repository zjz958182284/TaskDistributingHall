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

import java.lang.reflect.Type;
import java.util.LinkedList;

public class ChatListViewAdapter extends BaseAdapter {

    private Bitmap myPortrait;
    private Bitmap hisPortrait;
    private LinkedList<Message> messages;
    private LayoutInflater inflater;

    public ChatListViewAdapter(Context context,LinkedList<Message> list,Bitmap myPortrait
    ,Bitmap hisPortrait) {
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
        ViewHolderTime viewHolderTime=null;
        ViewHolderSender viewHolderSender=null;
        ViewHolderReceiver viewHolderReceiver=null;

        //第一次加载
        if(convertView==null){
            if(type==Message.recentChatTime){  //如果消息类型为时间
                viewHolderTime=new ViewHolderTime();
                convertView=inflater.inflate(R.layout.chat_time_section,parent,false);
                viewHolderTime.progressBar=convertView.findViewById(R.id.chat_progress);
                viewHolderTime.textView=convertView.findViewById(R.id.chat_time);
                convertView.setTag(viewHolderTime);

            }
            else if(type==Message.Isend){ //如果消息类型为我发送的消息
                viewHolderSender=new ViewHolderSender();
                convertView=inflater.inflate(R.layout.send_message,parent,false);
                 viewHolderSender.imageView=convertView.findViewById(R.id.imageViewSend);
                 viewHolderSender.textView=convertView.findViewById(R.id.textViewSend);
                convertView.setTag( viewHolderSender);
            }
            else {
                //如果消息类型为他发送的消息
                viewHolderReceiver=new ViewHolderReceiver();
                convertView=inflater.inflate(R.layout.recept_message,parent,false);
                viewHolderReceiver.imageView=convertView.findViewById(R.id.imageViewRecept);
                viewHolderReceiver.textView=convertView.findViewById(R.id.textViewRecept);
               convertView.setTag(viewHolderReceiver);
            }
        }

        /**
         * 踩坑第二弹 买参考数目，多看官方文档 ，多理解底层原理
         * 复用convertView,校正convertView
         */

        else {
            Object view=convertView.getTag();
            if(type==Message.recentChatTime && !(view instanceof ViewHolderTime)){
                convertView=inflater.inflate(R.layout.chat_time_section,parent,false);
                viewHolderTime=new ViewHolderTime();
                viewHolderTime.progressBar=convertView.findViewById(R.id.chat_progress);
                viewHolderTime.textView=convertView.findViewById(R.id.chat_time);
                convertView.setTag(viewHolderTime);
            }
            else if(type==Message.Isend && !(view instanceof ViewHolderSender)){
                viewHolderSender=new ViewHolderSender();
                convertView=inflater.inflate(R.layout.send_message,parent,false);
                viewHolderSender.imageView=convertView.findViewById(R.id.imageViewSend);
                viewHolderSender.textView=convertView.findViewById(R.id.textViewSend);
                convertView.setTag( viewHolderSender);
            }
            else if(type==Message.heSend && !(view instanceof ViewHolderReceiver)){
                viewHolderReceiver=new ViewHolderReceiver();
                convertView=inflater.inflate(R.layout.recept_message,parent,false);
                viewHolderReceiver.imageView=convertView.findViewById(R.id.imageViewRecept);
                viewHolderReceiver.textView=convertView.findViewById(R.id.textViewRecept);
                convertView.setTag(viewHolderReceiver);
            }

        }

        //viewHolder是设置数据的
        Object view=convertView.getTag();
        if(type==Message.recentChatTime){
            ViewHolderTime viewHolderTime1=(ViewHolderTime)view;
            viewHolderTime1.textView.setText(message.getContent());
        }
        else if(type==Message.Isend){
            ViewHolderSender viewHolderSender1=(ViewHolderSender)view;
            viewHolderSender1.textView.setText(message.getContent());
            viewHolderSender1.imageView.setImageBitmap(myPortrait);
        }else {
            ViewHolderReceiver viewHolderReceiver1=(ViewHolderReceiver)view;
            viewHolderReceiver1.textView.setText((message.getContent()));
            viewHolderReceiver1.imageView.setImageBitmap(hisPortrait);
        }

        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return messages.get(position).getType();
    }



    /**
     * 便于复用itemview
     */
    private static class ViewHolderTime{
        private ProgressBar progressBar;
        private TextView textView;
    }

    private  static  class  ViewHolderSender{
        private  TextView textView;
        private  ImageView imageView;
    }
    private  static class  ViewHolderReceiver{
        private  TextView textView;
        private  ImageView imageView;
    }
}


