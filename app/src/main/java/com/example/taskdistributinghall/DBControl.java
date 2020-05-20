package com.example.taskdistributinghall;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;


public class DBControl {

    public static Connection GetConnection() {
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://192.168.0.104:3306/taskhalldb";
        try {
            Class.forName(driver);
            return DriverManager.getConnection(url, "zjz", "Xjz19990507");
        } catch (ClassNotFoundException | SQLException e) {
            return null;
        }
    }

    //根据ID查询任务
    public static Task searchTaskByID(int ID) {
        try (Connection conn = GetConnection();
             Statement stat = conn.createStatement()) {
            ResultSet rs = stat.executeQuery("select * from task where id=" + ID);
            if (rs.next()) {
                Task task = new Task();
                task.id = rs.getInt("id");
                task.date = rs.getString("date");
                task.publisher = rs.getString("publisher");
                task.content = rs.getString("content");
                task.title = rs.getString("title");
                task.status = rs.getString("status");
                task.rewards = rs.getInt("rewards");
                String acceptor= rs.getString("acceptor");
                if(acceptor!=null)
                    task.acceptor=acceptor;
                task.type = rs.getString("type");
                return task;
            } else return null;
        } catch (SQLException e) {
            return null;
        }
    }

    //根据任务ID查询任务状态
    public static Task.taskState searchTaskState(int ID){
        try (Connection conn = GetConnection();
             Statement stat = conn.createStatement()) {
            String state;
           ResultSet rs= stat.executeQuery("select status from task where id="+ID);
            if(rs.next())
                state=rs.getString(1);
            else return null;
           if(state.equals( "unaccepted"))
               return Task.taskState.unaccepted;
           else if(state.equals( "accepted"))
               return Task.taskState.accepted;
           else if(state.equals( "cancelled"))
               return Task.taskState.cancelled;
           else if(state.equals( "completed"))
               return Task.taskState.completed;
        } catch (SQLException e) {
            return null;
        }
        return null;
    }

    //根据类型查询所有未被接受的任务的集合
    public  static List<Task> searchTaskByType(String type) {
        try (Connection conn = GetConnection();
             Statement stat = conn.createStatement()) {
            ResultSet rs = stat.executeQuery("select * from task where type=" + "'" + type + "'" +
                    "order by date desc");
            List<Task> list = new LinkedList<Task>();
            if (rs.next()) {
                do {
                    String status=rs.getString("status");
                    if(!status.equals("unaccepted")) continue;
                    Task task = new Task();
                    task.status = status;
                    task.id = rs.getInt("id");
                    task.date = rs.getString("date");
                    task.publisher = rs.getString("publisher");
                    task.content = rs.getString("content");
                    task.title = rs.getString("title");
                    task.rewards = rs.getInt("rewards");
                    task.acceptor = rs.getString("acceptor");
                    task.type = rs.getString("type");
                    list.add(task);
                } while (rs.next());
                return list;
            } else return null;
        } catch (SQLException e) {
            return null;
        }
    }

    //根据电话号码查询用户
    public  static User searchUserByPhone(String phone) {

        try (Connection conn = GetConnection();
             Statement stat = conn.createStatement()) {
            ResultSet rs = stat.executeQuery("select * from user where phone=" + "'" + phone + "'");
            if (rs.next()) {
                User user = new User();
                user.phone = rs.getString("phone");
                user.acceptedTask = rs.getInt("acceptedTask");
                user.completedTask = rs.getInt("completedTask");
                user.address = rs.getString("address");
                user.dept = rs.getString("dept");
                user.grade = rs.getString("grade");
                user.sex = rs.getString("sex");
                user.name = rs.getString("name");
                return user;
            } else return null;
        } catch (SQLException e) {
            return null;
        }
    }

    //查询发布任务的集合
    public  static List<Task> searchPublishedTask(String phone) {
        try (Connection conn = GetConnection();
             Statement stat = conn.createStatement()) {
            ResultSet rs = stat.executeQuery("select * from task where publisher=" + "'" + phone + "'"
                    + "order by date");
            List<Task> list = new LinkedList<Task>();
            if (rs.next()) {
                do {
                    Task task = new Task();
                    task.id = rs.getInt("id");
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
            } else return null;
        } catch (SQLException e) {
            return null;
        }
    }

    //查询接受任务集合
    public  static List<Task> searchAcceptedTask(String phone) {
        try (Connection conn = GetConnection();
             Statement stat = conn.createStatement()) {
            ResultSet rs = stat.executeQuery("select * from task where acceptor=" + "'" + phone + "'");
            List<Task> list = new LinkedList<Task>();
            if (rs.next()) {
                do {
                    Task task = new Task();
                    task.id = rs.getInt("id");
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
            } else return null;
        } catch (SQLException e) {
            return null;
        }
    }

    //根据任务ID查询任务正在请求人数
    public static  int searchNumberOfRequests(int ID) {
        try (Connection conn = GetConnection();
             Statement stat = conn.createStatement()) {
            ResultSet rs = stat.executeQuery("select count(id) from pendingTask where id=" + ID);
            if (rs.next())
                return rs.getInt(1);
            return 0;
        } catch (SQLException e) {
            return 0;
        }
    }

    //根据任务ID查询正在请求任务的用户的集合
    public static  List<User> searchRequestingUser(int ID) {
        try (Connection conn = GetConnection();
             Statement stat = conn.createStatement()) {
            ResultSet rs = stat.executeQuery("select User.* from pendingTask,User where " +
                    "pendingTask.id=" + ID + " and pendingTask.phone=User.phone");
            List<User> list = new LinkedList<User>();
            if (rs.next()) {
                do {
                    User user = new User();
                    user.phone = rs.getString("phone");
                    user.acceptedTask = rs.getInt("acceptedTask");
                    user.completedTask = rs.getInt("completedTask");
                    user.address = rs.getString("address");
                    user.dept = rs.getString("dept");
                    user.grade = rs.getString("grade");
                    user.sex = rs.getString("sex");
                    user.name = rs.getString("name");
                    list.add(user);
                } while (rs.next());
                return list;
            } else return null;
        } catch (SQLException e) {
            return null;
        }
    }

    //根据任务ID和接受人添加正在请求的任务项
    public static  void addRequestingTask(String phone, int ID) throws SQLException {
        try (Connection conn = GetConnection();) {
            PreparedStatement stat = conn.prepareStatement("insert into pendingTask values(?,?)");
            stat.setString(1, phone);
            stat.setInt(2, ID);
            stat.executeUpdate();
        }
    }

    /**
     * 根据phone,密码创建用户
     *
     * @param phone
     * @param password
     * @throws SQLException
     */
    public  static void addUser(String phone, String password) throws SQLException {
        try (Connection conn = GetConnection();
             PreparedStatement stat = conn.prepareStatement
                     ("insert into user (phone,password) values(?,?)")) {
            stat.setString(1, phone);
            stat.setString(2, password);
            stat.executeUpdate();
        }
    }

    /**
     * 删除指定用户
     *
     * @param phone
     * @throws SQLException
     */
    public  static boolean deleteUser(String phone) throws SQLException {
        try (Connection conn = GetConnection();
             Statement stat = conn.createStatement()) {
            int rows= stat.executeUpdate("delete from User where phone='" + phone + "'");
           return rows>0;
        }
    }

    /**
     * 根据手机号，昵称，性别，学院，年级，地址更新用户信息
     *
     * @param name
     * @param sex
     * @param dept
     * @param grade
     * @param address
     * @throws SQLException
     */
    public static  boolean updateUser(String phone, String name, String sex, String dept, String grade,
                              String address) throws SQLException {
        try (Connection conn = GetConnection();
             Statement stat = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_UPDATABLE)) {
            ResultSet rs = stat.executeQuery("select * from user where phone=" + "'" + phone + "'");
            if (rs.next()) {
                rs.updateString("name", name);
                rs.updateString("dept", dept);
                rs.updateString("sex", sex);
                rs.updateString("grade", grade);
                rs.updateString("address", address);
                rs.updateRow();
                return true;
            }
            else return false;
        }
    }


    /**
     * 添加任务
     * @param publisher
     * @param content
     * @param title
     * @param type
     * @param rewards
     * @throws SQLException
     */
    public  static void addTask( String publisher, String content,
                        String title, String type, int rewards) throws SQLException {
        try (Connection connc = GetConnection();
             PreparedStatement stat = connc.prepareStatement("insert into task (publisher" +
                     ",content,title,type,rewards)values(?,?,?,?,?)" )) {
            stat.setInt(5, rewards);
            stat.setString(1, publisher);
            stat.setString(2, content);
            stat.setString(3, title);
            stat.setString(4, type);
            stat.executeUpdate();
        }
    }

    /**
     * 删除指定任务
     * @param id
     * @throws SQLException
     */
    public static  boolean deleteTask(int id) throws SQLException {
        try (Connection connc = GetConnection();
             Statement stat = connc.createStatement()) {
            int rows = stat.executeUpdate("delete from Task where id=" + id);
            if (rows > 0)
                return true;
            else return false;
        }
    }

    /**
     * 根据任务ID修改任务状态
     * @param ID
     * @param state
     * @return
     * @throws SQLException
     */
    public  static boolean updateTaskState(int ID, Task.taskState state) throws SQLException {
        String status ;
        switch (state) {
            case unaccepted:
                status = "unaccepted";
                break;
            case accepted:
                status = "accepted";
                break;
            case completed:
                status = "completed";
                break;
            case cancelled:
                status = "cancelled";
                break;
            default:
               status=null;
        }
        try (Connection conn = GetConnection();
             Statement stat = conn.createStatement()) {
            int rows = stat.executeUpdate("update task set status='" + status + "' where id="+ID);
            if (rows > 0)
                return true;
            else return false;
        }
    }

    /**
     * 根据任务ID和接收人phone修改任务的接收者
     */
    public static  boolean modifyAcceptor(int ID,String phone) throws SQLException {
        try (Connection conn=GetConnection();
        Statement stat=conn.createStatement()){
            int rows;
            if(phone!=null)
                rows= stat.executeUpdate("update task set acceptor='"+phone+"'"+"where id="+ID);
            else
                rows= stat.executeUpdate("update task set acceptor=null where id="+ID);
          if(rows>0)
              return true;
          else return false;
        }
    }

    /**
     * 根据任务ID删除正在请求的任务中对应任务的所有项
     */
    public static  boolean deletePendingTask(int ID) throws SQLException {
        try (Connection conn=GetConnection();
             Statement stat=conn.createStatement()){
           int rows= stat.executeUpdate("delete from pendingTask where id="+ID);
           if(rows>0)
               return  true;
           else  return  false;
        }
    }

    /**
     * 验证用户名和密码
     * @param phone
     * @param password
     * @return
     * @throws SQLException
     */
    public static  boolean validateAccount(String phone,String password) throws SQLException {
        try (Connection conn=GetConnection();
             Statement stat=conn.createStatement()){
            ResultSet rs=stat.executeQuery("select password from user where phone='"+phone+"'");
            if(rs.next()) {
                String pw = rs.getString(1);
                return pw.equals(password);
            } else return false;
        }
    }
}
