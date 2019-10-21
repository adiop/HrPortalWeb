package com.warn.hr.business;

import com.warn.hr.business.rest.TaskReportServiceRestClient;
import com.warn.hr.persistence.entity.reports.*;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReportService {

    @NonNull
    private TaskReportServiceRestClient taskReportServiceRestClient;

    public TasksTotal tasksTotal(String initialDate, String finalDate) {
        return taskReportServiceRestClient.tasksTotal(initialDate, finalDate);
    }

    public TaskByMonthYear tasksByMonthYear(String initialDate, String finalDate) {
        return taskReportServiceRestClient.tasksByMonthYear(initialDate, finalDate);
    }

    public TaskByDate tasksByDate(String initialDate, String finalDate) {
        return taskReportServiceRestClient.tasksByDate(initialDate, finalDate);
    }

    public TaskByDepartment tasksByDepartment(String initialDate, String finalDate) {
        return taskReportServiceRestClient.tasksByDepartment(initialDate, finalDate);
    }

    public TaskByJobTitle tasksByJobTitle(String initialDate, String finalDate) {
        return taskReportServiceRestClient.tasksByJobTitle(initialDate, finalDate);
    }

}
