package com.example.taskdistributinghall.Activity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.taskdistributinghall.DBControl.ChatRecordHelper;
import com.example.taskdistributinghall.DBControl.DBControl;
import com.example.taskdistributinghall.Model.Message;
import com.example.taskdistributinghall.Model.User;
import com.example.taskdistributinghall.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Chat extends AppCompatActivity {
    private  User me; //我
    private  User he;//对方用户
    private  LinkedList<Message> messages;
    private  ChatListViewAdapter listViewAdapter;
    private  ListView listView;
    private  String chatTime;//指示当前聊天部分的时间；
    private  boolean isLoadMore=false;//下拉时是否加载更多
    private  boolean isSend=false;//编辑框内文本是否发送
    private  boolean isSendOffLine=true;//默认离线发送
    private SQLiteDatabase  recordDatabase; //获得sqlite数据库操作对象
    private Thread sendOnLineThread;
    private Thread sendOffLineThread;
    private Thread receiveThread;
    private boolean flagOnline=true,flagOff=true,flagReceive=true;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_page);
        listView=(ListView)findViewById(R.id.chat_list_view);
        listViewAdapter=new ChatListViewAdapter(this,messages,me.headPortrait
        ,he.headPortrait);
        listView.setAdapter(listViewAdapter);
        recordDatabase=new ChatRecordHelper(this).getReadableDatabase();
        /**
         * 初始化聊天记录
         * */
        initChatTimeSection();
        loadNativeChatRecord();
        loadRemoteChatRecord();
        listView.setSelection(messages.size()-1);
        listViewAdapter.notifyDataSetChanged();
        Button send_btn=findViewById(R.id.send_button);
        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSend=true;
            }
        });

        /**
         * 下拉刷新效果
         */
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            ProgressBar progressBar;
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(isLoadMore && scrollState==SCROLL_STATE_FLING) {
                    progressBar.setVisibility(View.VISIBLE);
                    loadNativeChatRecord();
                    listViewAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                    isLoadMore=false;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(firstVisibleItem==0){
                    View firstView=view.getChildAt(0);
                    progressBar=view.findViewById(R.id.chat_progress);
                    progressBar.setVisibility(View.INVISIBLE);
                    if(firstView.getTop()==0){
                        isLoadMore=true;
                    }
                }

            }
        });

        StartChat();
    }

    public void StartChat(){
        sendOnLineThread=new Thread(new Runnable() {
            @Override
            public void run() {
                while (flagOnline) {
                    isSendOffLine=true;//尚未连接成功时离线发送
                    try( Socket socket = new Socket(he.ip, 8189)) {
                        isSendOffLine=false;//连接成功在线发送消息
                        while (true) {
                            OutputStream os=socket.getOutputStream();
                            OutputStreamWriter osw=new OutputStreamWriter(os);
                            if(isSend){
                                EditText editText=findViewById(R.id.chat_editText);
                                String text=editText.getText().toString();
                                if(!text.equals("")) {   //在线发送消息
                                    osw.write(text);
                                    osw.flush();
                                    messages.add(new Message(text, Message.Isend));
                                    StoreNativeChatRecord(text,Message.Isend);
                                    /**
                                     * 更新界面
                                     */
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            listViewAdapter.notifyDataSetChanged();
                                            editText.setText("");
                                            isSend=false;
                                        }
                                    });
                                }
                            }

                            /*
                            降低cpu占用率
                             */
                            try {
                                Thread.sleep(10);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (IOException ignored) {
                        ;//对方客户端退出连接,继续侦听下一次连接
                    }

                }
            }
        });

        receiveThread=new Thread(new Runnable() {
            @Override
            public void run() {
                while (flagReceive){
                    try {
                        ServerSocket serverSocket=new ServerSocket(8189);
                        try (Socket socket = serverSocket.accept()) {
                            while (true) {
                                InputStreamReader isr = new InputStreamReader(socket.getInputStream());
                                BufferedReader br = new BufferedReader(isr);
                                StringBuilder builder = new StringBuilder();
                                String line;
                                while ((line = br.readLine()) != null)
                                    builder.append(line);
                                String content = builder.toString();
                                messages.add(new Message(content,Message.heSend));
                                StoreNativeChatRecord(content,Message.heSend);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        listViewAdapter.notifyDataSetChanged();
                                    }
                                });
                            }
                        }
                    } catch (IOException ignored) {
                        ;
                    }
                }
            }
        });

        sendOffLineThread=new Thread(new Runnable() {
            @Override
            public void run() {
                while (flagOff) {
                    if (isSendOffLine && isSend) {
                        EditText editText = findViewById(R.id.chat_editText);
                        String text = editText.getText().toString();
                        if (!text.equals("")) {
                            //离线发送消息
                            StoreRemoteChatRecord(text);
                            messages.add(new Message(text, Message.Isend));
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    listViewAdapter.notifyDataSetChanged();
                                    editText.setText("");
                                    isSend=false;
                                }
                            });

                        }
                    }
                }
            }
        });
        sendOffLineThread.setDaemon(true);
        sendOffLineThread.start();
        sendOnLineThread.setDaemon(true);
        sendOnLineThread.start();
        receiveThread.setDaemon(true);
        receiveThread.start();
    }


    public void initChatTimeSection(){
        ContentValues contentValues=new ContentValues();
        contentValues.put("senderphone",me.phone);
        contentValues.put("receiverphone",he.phone);
        chatTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        recordDatabase.insert("chatTimeStamp",null,contentValues);
    }

    /**
     * 一次最多加载16条聊天记录
     * @return
     */
    public void loadNativeChatRecord(){
        int maxRecordItems=16;
        for(int i=0;i<maxRecordItems;)
        try (Cursor cursor1 = recordDatabase.query("chatTimeStamp", new String[]{"date"},
                "date<? and senderphone=? and receiverphone=?", new String[]
                        {chatTime,me.phone,he.phone}, null,
                null, "date desc", "1")) {
            if(cursor1.moveToNext()){
                String preChatTime=chatTime;
                chatTime=cursor1.getString(1);
                try (Cursor cursor2 = recordDatabase.query("chatRecordTemp", new String[]{"record", "issend"},
                        "date>? and date<? and senderphone=? and receiverphone=?", new String[]
                                {chatTime,preChatTime, me.phone, he.phone}, null, null, "date desc")) {
                    while ((cursor2.moveToNext())) {
                        ++i;
                        String record = cursor2.getString(1);
                        int issend = cursor2.getInt(2);
                        if (issend == 1)
                            messages.addFirst(new Message(record, Message.Isend));
                        else messages.addFirst(new Message(record, Message.heSend));
                    }
                }
            }
            messages.addFirst(new Message(chatTime.substring(0,16),Message.recentChatTime));
        }
    }

    public void loadRemoteChatRecord(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<String> records= DBControl.getRetentionRecord(he.phone,me.phone);
                    for(int i=0;i<records.size();++i){
                        messages.add(new Message(records.get(i),Message.heSend));
                    }
                } catch (SQLException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Chat.this,"加载离线聊天记录失败",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    public void StoreNativeChatRecord(String record,int type){
        ContentValues values=new ContentValues();
        values.put("senderhone",me.phone);
        values.put("receiverphone",he.phone);
        values.put("record",record);
        if(type==Message.heSend)
            values.put("issend",0);
        else values.put("issend",1);
        recordDatabase.insert("chatRecordTemp",null,values);
    }

    public void StoreRemoteChatRecord(String record) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DBControl.addTempChatRecord(me.phone, he.phone, record);
                } catch (SQLException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Chat.this, "消息发送失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        flagOff=flagOnline=flagReceive=false;
    }
}
