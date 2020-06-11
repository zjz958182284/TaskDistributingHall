package com.example.taskdistributinghall.Activity.ChatPage;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.taskdistributinghall.Activity.Launch.Login;
import com.example.taskdistributinghall.DBControl.ChatRecordHelper;
import com.example.taskdistributinghall.DBControl.DBControl;
import com.example.taskdistributinghall.Model.Message;
import com.example.taskdistributinghall.Model.User;
import com.example.taskdistributinghall.R;
import com.shehuan.niv.NiceImageView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Chat extends AppCompatActivity {
    private  User me; //我 /test/
    private  User he;//对方用户  /test/
    private  LinkedList<Message> messages=new LinkedList<Message>();
    private ChatListViewAdapter listViewAdapter;
    private  ListView listView;
    private  String chatTime;//指示当前聊天部分的时间；
    private  boolean isSend=false;//编辑框内文本是否发送
    private  boolean isSendOffLine=true;//默认离线发送
    private SQLiteDatabase  recordDatabase; //获得sqlite数据库操作对象
    private boolean flagOnline=true,flagOff=true,flagReceive=true;//控制三个线程结束de标志
    private Thread sendOnLineThread;
    private Thread sendOffLineThread;
    private Thread receiveThread ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_page);

        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                Intent intent = getIntent();
                String hePhone = intent.getStringExtra("phone");
                SharedPreferences sp=getApplicationContext().getSharedPreferences("my_info", Context.MODE_PRIVATE);
                String mPhone=sp.getString("phone","");
                me=DBControl.searchUserByPhone(mPhone);
                he=DBControl.searchUserByPhone(hePhone);
            }
        });

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ((NiceImageView) findViewById(R.id.header_photo)).setImageBitmap(he.headPortrait);
        ((TextView)findViewById(R.id.textView2)).setText(he.name);
        listView= findViewById(R.id.chat_list_view);
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
     EditText editText=findViewById(R.id.chat_editText);
     editText.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             listView.setSelection(messages.size()-1);
         }
     });




     listView.setSelection(messages.size() - 1); //从最后一行显示
     listViewAdapter.notifyDataSetChanged();
     Button send_btn=findViewById(R.id.send_button);
    send_btn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               EditText editText = findViewById(R.id.chat_editText);
               String text=editText.getText().toString();
               if(!text.equals(""))
               isSend=true;
           }
       });



        /**
         * 下拉刷新效果(练习时长两天半）
         */
     listView.setOnScrollListener(new AbsListView.OnScrollListener() {
        boolean isClimbTop=false; //是否还有未加载的数据，聊天记录全部加载完毕则为true
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
        }

       @Override
       public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
           if(firstVisibleItem==0 && !isClimbTop) {//聊天记录全部加载完毕
               View firstView = view.getChildAt(0);
               ProgressBar progressBar = view.findViewById(R.id.chat_progress);
               if (progressBar != null) { //如果起始listview中没有itemview则progressbar==null
                   progressBar.setVisibility(View.VISIBLE);
                   if (firstView.getTop() == 0) {
                       int rows=loadNativeChatRecord();
                       isClimbTop = (rows <= 0);//判断聊天记录全部加载完毕
                       listViewAdapter.notifyDataSetChanged();
                       progressBar.setVisibility(View.GONE);

                       listView.setSelection(rows);  //保持当前位置不变
                   }
               }
           }
       } });

        StartChat();
    }

    public void StartChat(){

        //尚未连接成功时离线发送
        //连接成功在线发送消息
        //在线发送消息
        /*
         * 更新界面
         *  降低cpu占用率

                */
        //对方客户端退出连接,继续侦听下一次连接
        sendOnLineThread = new Thread(new Runnable() {
            @Override
            public void run() {
                  //尚未连接成功时离线发送
                        while (flagOnline) {
                            Socket socket;
                            while (true) {
                                try {
                                    isSendOffLine = true;
                                    //如果对方服务器没有监听8000端口的服务则不断向对方发出socket连接直到连接为止
                                    //new Socket是有一定阻塞性的
                                     socket= new Socket(he.ip, 8000);
                                    isSendOffLine = false; //连接成功时不再离线发送
                                    break;
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    try {
                                        Thread.sleep(1);
                                    } catch (InterruptedException ez) {
                                        ez.printStackTrace();
                                    }
                                }
                            }
                            //在线发送模式
                            while (flagOnline) {

                                //不断循环监听有没有按下发送键
                                if (isSend){
                                    try {
                                        OutputStream os = socket.getOutputStream();
                                        OutputStreamWriter osw = new OutputStreamWriter(os);
                                        EditText editText = findViewById(R.id.chat_editText);
                                        String text = editText.getText().toString();
                                        osw.write(text);
                                        osw.flush();
                                        //发送一个结束标识符告诉服务器我发送完了
                                        socket.shutdownOutput();

                                        isSend = false;

                                        /**
                                         * 更新界面
                                         */
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                messages.add(new Message(text, Message.Isend));
                                                StoreNativeChatRecord(text, Message.Isend);
                                                listViewAdapter.notifyDataSetChanged();
                                                listView.setSelection(messages.size() - 1);
                                                editText.setText("");
                                            }
                                        });
                                        //发送完此次消息进行下一次socket连接
                                        break;
                                    } catch (IOException e) {
                                        //对方客户端退出连接,继续侦听下一次连接，这时启用离线发送模式
                                    //   runOnUiThread(new Runnable() {
                                    //       @Override
                                    //       public void run() {
                                    //           Toast.makeText(Chat.this, "O异常",
                                    //                   Toast.LENGTH_SHORT).show();
                                    //       }
                                    //   });
                                        break;
                                    }finally {
                                        try {
                                            socket.close();
                                        } catch (IOException ex) {
                                            ex.printStackTrace();
                                        }
                                    }
                            }
                                  /*
                            降低cpu占用率
                             */
                                try {
                                    Thread.sleep(1);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }

                            //最后如何退出线程后socket没有关闭则关闭它
                            if(!socket.isClosed()) {
                                try {
                                    socket.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                        }
            }
        });

        receiveThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try( ServerSocket serverSocket = new ServerSocket(8000);) {
                    serverSocket.setReuseAddress(true);
                while (flagReceive) {

                    //循环准备接受下一次连接
                    //当CHAT_ACTIVITY退出时此线程会阻塞在readline仍然占用端口 对方再发送一条消息时
                    //仍然可以接受并且存在本地聊天记录中

                    try(Socket socket = serverSocket.accept();){
                            isSendOffLine = false;
                                InputStreamReader isr = new InputStreamReader(socket.getInputStream());
                                BufferedReader br = new BufferedReader(isr);
                                StringBuilder builder = new StringBuilder();
                                String line;
                                while ((line = br.readLine()) != null)
                                    builder.append(line);
                                String content = builder.toString();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(!content.equals("")) {
                                            messages.add(new Message(content, Message.heSend));
                                            StoreNativeChatRecord(content, Message.heSend);
                                            listViewAdapter.notifyDataSetChanged();
                                            listView.setSelection(messages.size() - 1);
                                        }
                                    }
                                });
                             }catch (IOException ignored){
                    }
                    }
                }catch (IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                               // Toast.makeText(Chat.this,e.getMessage()+"I异常",
                               //        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

        });

        //离线发送消息
         sendOffLineThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (flagOff) {
                    if (isSendOffLine&&isSend) {
                        EditText editText = findViewById(R.id.chat_editText);
                        String text = editText.getText().toString();
                            //离线发送消息
                            StoreRemoteChatRecord(text);
                            StoreNativeChatRecord(text,Message.Isend);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    messages.add(new Message(text, Message.Isend));
                                    listViewAdapter.notifyDataSetChanged();
                                    listView.setSelection(messages.size()-1);
                                    editText.setText("");

                                    //test
                                   // Toast.makeText(Chat.this,"离线消息发送成功",
                                   //         Toast.LENGTH_SHORT).show();
                                }
                            });
                            isSend = false;

                    }
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
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


    public  void initChatTimeSection(){
                ContentValues contentValues=new ContentValues();
                contentValues.put("senderphone",me.phone);
                contentValues.put("receiverphone",he.phone);
                chatTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                //recordDatabase.execSQL("delete from chatTimeStamp");  //test
                recordDatabase.insert("chatTimeStamp", null, contentValues);


    }

    /**
     * 一次最多加载16条聊天记录
     * @return
     */
    public synchronized int loadNativeChatRecord(){
                    int maxRecordItems=16;
                    int i=0;
                    for(;i<maxRecordItems;)   //编程提醒时刻注意循环的地方防止发生死循环
                        //SQLite数据库
                    try (Cursor cursor1 = recordDatabase.query("chatTimeStamp", new String[]{"date"},
                          "date<? and senderphone=? and receiverphone=?", new String[]
                                   {chatTime,me.phone,he.phone}, null,
                           null, "date desc", "")) {
                     if(cursor1.moveToNext()){
                         String preChatTime=chatTime;
                        chatTime=cursor1.getString(0);
                         try (Cursor cursor2 = recordDatabase.query("chatRecordTemp", new String[]{"record", "issend"},
                                 "date>? and date<? and senderphone=? and receiverphone=?", new String[]
                                         {chatTime,preChatTime, me.phone, he.phone}, null, null, "date desc")) {
                             while ((cursor2.moveToNext())) {
                                 ++i;
                                 String record = cursor2.getString(0);
                                 int issend = cursor2.getInt(1);
                                 if (issend == 1)
                                     messages.addFirst(new Message(record, Message.Isend));
                                 else messages.addFirst(new Message(record, Message.heSend));
                             }
                             if(cursor2.getCount()>0)
                                 messages.addFirst(new Message(chatTime.substring(0,16),Message.recentChatTime));
                         }
                     }
                     else break;  //*****遍历完聊天记录时间后不再提取聊天记录
                 }
                    return i; //一共查询了几条记录
    }

    public void loadRemoteChatRecord() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    try (Connection conn = DBControl.GetConnection();
                         Statement stat = conn.createStatement()) {
                        ResultSet rs = stat.executeQuery("select record,date from chatRecordTemp where " +
                                "senderPhone='" + he.phone + "' and receiverPhone='" + me.phone + "'" +
                                " order by date");
                        while (rs.next()) {
                            String record=rs.getString(1);
                            String date= rs.getString(2);
                            StoreNativeChatRecord(record,Message.heSend,date);
                            messages.add(new Message(record, Message.heSend));
                        }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listViewAdapter.notifyDataSetChanged();
                            listView.setSelection(messages.size()-1);
                        }
                    });
                    DBControl.deleteRecords(he.phone,me.phone);//清空离线聊天记录
                } catch (SQLException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Chat.this, "加载离线聊天记录失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    public synchronized void  StoreNativeChatRecord(String record,int type){
        ContentValues values=new ContentValues();
        values.put("senderphone",me.phone);
        values.put("receiverphone",he.phone);
        values.put("record",record);
        if(type==Message.heSend)
            values.put("issend",0);
        else values.put("issend",1);
        recordDatabase.insert("chatRecordTemp",null,values);
    }


    public synchronized void  StoreNativeChatRecord(String record,int type,String date){
        ContentValues values=new ContentValues();
        values.put("senderphone",me.phone);
        values.put("receiverphone",he.phone);

        values.put("date",date);
        values.put("record",record);

        if(type==Message.heSend)
            values.put("issend",0);
        else values.put("issend",1);
        recordDatabase.insert("chatRecordTemp",null,values);
    }


    public void StoreRemoteChatRecord(String record) {
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



    @Override
    protected void onDestroy() {

        super.onDestroy();

        flagOff=flagOnline=flagReceive=false;

    }
}
