package com.example.taskdistributinghall.Model;

public class Task {
    public static  enum taskState {unaccepted,accepted,completed,cancelled};
    public  int id;
    public  String  date;
    public String  publisher;
    public String  content;
    public String title;
    public  String   status;
    public int rewards;
    public String  acceptor;
    public String  type;
}
