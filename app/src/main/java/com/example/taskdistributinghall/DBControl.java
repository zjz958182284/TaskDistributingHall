package com.example.taskdistributinghall;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;


public class DBControl {

    public static Connection GetConnection(){
        String driver="com.mysql.jdbc.Driver";
        String url="jdbc:mysql://192.168.0.102:3306/taskhalldb";
        try {
            Class.forName(driver);
            Connection conn=DriverManager.getConnection(url,"zjz","Xjz19990507");
            return conn;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //根据ID查询任务
    public static Task SearchTaskByID(String ID) throws SQLException {
        Connection conn=GetConnection();
        try {
            Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery("select * from task where id=" + ID);
            if (rs.next())
                return null;
            else {
                Task task = new Task();
                task.id = rs.getString("id");
                task.date = rs.getString("date");
                task.publisher = rs.getString("publisher");
                task.content = rs.getString("content");
                task.title = rs.getString("title");
                task.status = rs.getString("status");
                task.rewards = rs.getInt("rewards");
                task.acceptor = rs.getString("acceptor");
                task.type = rs.getString("type");
                return task;
            }
        }finally{
            conn.close();
        }
    }

    //根据类型查询任务集合
    public List<Task> SearchTaskByType(String type) throws SQLException {
        Connection conn=GetConnection();
        try {
            Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery("select * from task where type="+type+
                    "order by date desc");
            List<Task> list=new LinkedList<Task>();
            if(rs.next())
                return  null;
            else
                do{
                    Task task=new Task();
                    task.id = rs.getString("id");
                    task.date = rs.getString("date");
                    task.publisher = rs.getString("publisher");
                    task.content = rs.getString("content");
                    task.title = rs.getString("title");
                    task.status = rs.getString("status");
                    task.rewards = rs.getInt("rewards");
                    task.acceptor = rs.getString("acceptor");
                    task.type = rs.getString("type");
                    list.add(task);
                } while (rs.next());
            return list;
        }finally {
            conn.close();
        }
    }

    //根据电话号码查询用户
    public User SearchUserByPhone(String phone) throws SQLException {
        Connection conn=GetConnection();
        try {
            Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery("select * from user where phone=" + phone);
            if (rs.next())
                return null;
            else {
                User user=new User();
                user.phone=rs.getString("phone");
                user.acceptedTask=rs.getInt("acceptedTask");
                user.completedTask=rs.getInt("completedTask");
                user.address=rs.getString("address");
                user.dept=rs.getString("dept");
                user.grade=rs.getString("grade");
                user.sex=rs.getString("sex");
                user.name=rs.getString("name");
                return  user;
            }
        }finally {
            conn.close();
        }
    }

    //查询发布任务的集合
    public List<Task> SearchPublishedTask(String phone) throws SQLException {
        Connection conn=GetConnection();
        try {
            Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery("select * from task where publisher="+phone+
                    "order by date");
            List<Task> list=new LinkedList<Task>();
            if(rs.next())
                return  null;
            else
                do{
                    Task task=new Task();
                    task.id = rs.getString("id");
                    task.date = rs.getString("date");
                    task.publisher = rs.getString("publisher");
                    task.content = rs.getString("content");
                    task.title = rs.getString("title");
                    task.status = rs.getString("status");
                    task.rewards = rs.getInt("rewards");
                    task.acceptor = rs.getString("acceptor");
                    task.type = rs.getString("type");
                    list.add(task);
                } while (rs.next());
            return list;
        }finally {
            conn.close();
        }
    }

    //查询接受任务集合
    public List<Task> SearchAcceptedTask(String phone) throws SQLException {
        Connection conn=GetConnection();
        try {
            Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery("select * from task where acceptor="+phone);
            List<Task> list=new LinkedList<Task>();
            if(rs.next())
                return  null;
            else
                do{
                    Task task=new Task();
                    task.id = rs.getString("id");
                    task.date = rs.getString("date");
                    task.publisher = rs.getString("publisher");
                    task.content = rs.getString("content");
                    task.title = rs.getString("title");
                    task.status = rs.getString("status");
                    task.rewards = rs.getInt("rewards");
                    task.acceptor = rs.getString("acceptor");
                    task.type = rs.getString("type");
                    list.add(task);
                } while (rs.next());
            return list;
        }finally {
            conn.close();
        }
    }

    //根据任务ID查询任务正在请求人数
    public int NumberOfRequests(String ID) throws SQLException {
        Connection conn=GetConnection();
        try {
            Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery("select count(id) from pendingTask where id="+ID);
            if(rs.next())
                return rs.getInt(1);
            return 0;
        }finally {
            conn.close();
        }
    }

    //根据任务ID查询正在请求任务的用户的集合
    public List<User> SearchRequestingUser(String ID) throws SQLException {
        Connection conn=GetConnection();
        try {
            Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery("select User.* from pendingTask,User where " +
                    "pendingTask.id="+ID+" and pendingTask.phone=User.phone");
            List<User> list=new LinkedList<User>();
            if(rs.next())
                return  null;
            else
                do{
                    User user=new User();
                    user.phone=rs.getString("phone");
                    user.acceptedTask=rs.getInt("acceptedTask");
                    user.completedTask=rs.getInt("completedTask");
                    user.address=rs.getString("address");
                    user.dept=rs.getString("dept");
                    user.grade=rs.getString("grade");
                    user.sex=rs.getString("sex");
                    user.name=rs.getString("name");
                    list.add(user);
                } while (rs.next());
            return list;
        }finally {
            conn.close();
        }
    }

    //根据任务ID和接受人添加正在请求的任务项
    public void AddRequestingTask(String phone,String ID) throws SQLException {
        Connection connc =GetConnection();
        try{
            Statement stat=connc.createStatement();
            stat.executeUpdate("insert into pendingTask values("+phone+","+ID+")");
        }finally {
            connc.close();
        }
    }

}
