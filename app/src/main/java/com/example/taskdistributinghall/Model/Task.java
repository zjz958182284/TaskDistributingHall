package com.example.taskdistributinghall.Model;

import android.graphics.Bitmap;

import java.io.Serializable;

public class Task implements Serializable {
    public static  enum taskState {unaccepted,accepted,completed,cancelled};
    public  int id;
    public  String  date;
    public String  publisher;
    public String  content;
    public String title;
    public  String   status="unaccepted";
    public int rewards;
    public Bitmap taskPhoto;
    public String  acceptor;
    public String  type;
}
