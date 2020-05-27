package com.example.taskdistributinghall;

import com.example.taskdistributinghall.Model.Task;
import com.example.taskdistributinghall.Model.User;

import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;

public class DBControlTest {

    @Test
    public void getConnection() {
        Connection connection = DBControl.GetConnection();
        assertNotNull(connection);
    }

    @Test
    public void searchTaskByID() {
        Task task =DBControl.searchTaskByID(1);
        assertNotNull(task);
    }

    @Test
    public void searchTaskState() {
        Task.taskState state=DBControl.searchTaskState(1);
        assertNotNull(state);
    }

    @Test
    public void searchTaskByType() {
        List<Task> tasks=DBControl.searchTaskByType("study");
        assertNotNull(tasks);

    }

    @Test
    public void searchUserByPhone() {
        User user=DBControl.searchUserByPhone("123");
        assertNotNull(user);
    }

    @Test
    public void searchPublishedTask() {
        List<Task> list= DBControl.searchPublishedTask("19945102192");
        assertNotNull(list);
    }

    @Test
    public void searchAcceptedTask() {
        List<Task> list=DBControl.searchAcceptedTask("123");
        assertNotNull(list);
    }

    @Test
    public void searchNumberOfRequests() {
        int num=DBControl.searchNumberOfRequests(4);
        assertEquals(2,num);
    }

    @Test
    public void searchRequestingUser() {
      List<User> list=  DBControl.searchRequestingUser(4);
      assertNotNull(list);
    }

    @Test
    public void addRequestingTask() {
        try {
            DBControl.addRequestingTask("123",4);
            DBControl.addRequestingTask("19945102192",4);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void addUser() {
        try {
            DBControl.addUser("123","789");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void deleteUser() {
        try {
            boolean b=DBControl.deleteUser("123");
            assertTrue(b);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void updateUser() {
        try {
            boolean flag=  DBControl.updateUser("19945102192","zjz","男","计算机学院","大一","信二一舍301");
            assertTrue(flag);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void addTask() {
        try {
            DBControl.addTask("19945102192","test","test","study",5);
            DBControl.addTask("123","test","test","errand",6);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void deleteTask() {
        try {
           boolean b = DBControl.deleteTask(4);
            assertTrue(b);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void updateTaskState() {
        boolean b= false;
        try {
            b = DBControl.updateTaskState(3, Task.taskState.accepted);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        assertTrue(b);

    }

    @Test
    public void modifyAcceptor() {
        try {
            boolean b=DBControl.modifyAcceptor(7,"123");
            assertTrue(b);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void deletePendingTask() {
        try {
            boolean b=DBControl.deletePendingTask(4);
            assertTrue(b);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    @Test
    public void validateAccount() {
        try {
            boolean b= DBControl.validateAccount("123","789");
            assertTrue(b);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}