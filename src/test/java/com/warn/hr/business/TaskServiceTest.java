package com.warn.hr.business;

import com.warn.hr.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes= Application.class)
public class TaskServiceTest {

    @Autowired
    private TaskService taskService;

    @Test
    public void findById() {
    }

    @Test
    public void findById1() {
    }

    @Test
    public void listOpenTasks() {
    }

    @Test
    public void listOpenTasks1() {
    }

    @Test
    public void listClosedTasks() {
    }

    @Test
    public void listClosedTasks1() {
    }

    @Test
    public void listFutureTasks() {
    }

    @Test
    public void listFutureTasks1() {
    }

    @Test
    public void createTaskForNewStaff() {
    }

    @Test
    public void createTaskForUpdateStaff() {
    }

    @Test
    public void createTaskForTermStaff() {
    }

    @Test
    public void insertComment() {
    }

}
