package com.example.taskdistributinghall.Model;


import android.graphics.Bitmap;
import android.media.Image;

import java.io.Serializable;

public class User implements Serializable {
    public String phone;
    public  String name;
    public  String   sex;
    public  String  dept;
    public  String   grade;
    public  String address;
    public  int  completedTask;
    public  int acceptedTask;
  transient   public Bitmap headPortrait;
    public String ip;
}
