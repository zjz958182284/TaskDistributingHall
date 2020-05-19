package com.example.taskdistributinghall;

import org.junit.Test;

import java.sql.Connection;

import static org.junit.Assert.*;

public class DBControlTest {

    @Test
    public void getConnection() {
        Connection connection = DBControl.GetConnection();
        assertNotNull(connection);
    }

    @Test
    public void searchTaskByID() {
    }

    @Test
    public void searchTaskByType() {
    }

    @Test
    public void searchUserByPhone() {
    }

    @Test
    public void searchPublishedTask() {
    }

    @Test
    public void searchAcceptedTask() {
    }

    @Test
    public void numberOfRequests() {
    }

    @Test
    public void searchRequestingUser() {
    }

    @Test
    public void addRequestingTask() {
    }
}