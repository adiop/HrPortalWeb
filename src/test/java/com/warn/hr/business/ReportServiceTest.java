package com.warn.hr.business;

import com.warn.hr.Application;
import com.warn.hr.persistence.entity.reports.TaskByMonthYear;
import com.warn.hr.persistence.entity.reports.TasksTotal;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes= Application.class)
public class ReportServiceTest {

    @Autowired
    private ReportService reportService;

    @Test
    public void tasksTotal() {

        TasksTotal total = reportService.tasksTotal("01-01-2018", "12-01-2019");

        Assert.assertNotNull(total);

        System.out.println(total.getUpdateStaffTasks());
        System.out.println(total.getNewStaffTasks());
        System.out.println(total.getTermStaffTasks());

    }

    @Test
    public void tasksByMonthYear() {

        TaskByMonthYear taskByMonthYear = reportService.tasksByMonthYear("01-01-2018", "12-01-2019");

        Assert.assertNotNull(taskByMonthYear);

        taskByMonthYear.getTaskByMonthYear().forEach(System.out::println);

    }

    @Test
    public void tasksByDate() {
    }

    @Test
    public void tasksByDepartment() {
    }

    @Test
    public void tasksByJobTitle() {
    }
}
