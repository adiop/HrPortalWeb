package com.warn.hr.web.rest;

import com.warn.hr.business.ReportService;
import com.warn.hr.persistence.entity.reports.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
public class ReportsController {

    @Autowired
    private ReportService reportService;

    @GetMapping({"/hr/api/v1/reports/tasks-total"})
    public TasksTotal tasksTotal(String initialDate, String finalDate) {

        try {
            return reportService.tasksTotal(initialDate, finalDate);
        } catch (Exception e) {
            log.error("An error has occurred on /api/hr/v1/task/reports/tasks-total", e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping({"/hr/api/v1/reports/tasks-by-month-year"})
    public TaskByMonthYear tasksByMonthYear(String initialDate, String finalDate) {

        try {
            return reportService.tasksByMonthYear(initialDate, finalDate);
        } catch (Exception e) {
            log.error("An error has occurred on /api/hr/v1/task/reports/tasks-by-month-year", e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping({"/hr/api/v1/reports/tasks-by-date"})
    public TaskByDate tasksByDate(String initialDate, String finalDate) {

        try {
            return reportService.tasksByDate(initialDate, finalDate);
        } catch (Exception e) {
            log.error("An error has occurred on /api/hr/v1/task/reports/tasks-by-date", e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping({"/hr/api/v1/reports/tasks-by-department"})
    public TaskByDepartment tasksByDepartment(String initialDate, String finalDate) {

        try {
            return reportService.tasksByDepartment(initialDate, finalDate);
        } catch (Exception e) {
            log.error("An error has occurred on /api/hr/v1/task/reports/task-by-department", e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping({"/hr/api/v1/reports/tasks-by-job-title"})
    public TaskByJobTitle tasksByJobTitle(String initialDate, String finalDate) {

        try {
            return reportService.tasksByJobTitle(initialDate, finalDate);
        } catch (Exception e) {
            log.error("An error has occurred on /api/hr/v1/task/reports/task-by-job-title", e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

    }

}