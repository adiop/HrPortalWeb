package com.warn.hr.business;

import com.warn.hr.business.rest.TaskReportRestClient;
import com.warn.hr.persistence.entity.reports.*;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReportService {

    @NonNull
    private String taskRestServiceKey;

    @NonNull
    private TaskReportRestClient taskReportRestClient;

    public TasksTotal tasksTotal(String initialDate, String finalDate) {
        return taskReportRestClient.tasksTotal(taskRestServiceKey,
                getParametersMap(initialDate, finalDate));
    }

    public TaskByMonthYear tasksByMonthYear(String initialDate, String finalDate) {
        return taskReportRestClient.tasksByMonthYear(taskRestServiceKey,
                getParametersMap(initialDate, finalDate));
    }

    public TaskByDate tasksByDate(String initialDate, String finalDate) {
        return taskReportRestClient.tasksByDate(taskRestServiceKey,
                getParametersMap(initialDate, finalDate));
    }

    public TaskByDepartment tasksByDepartment(String initialDate, String finalDate) {
        return taskReportRestClient.tasksByDepartment(taskRestServiceKey,
                getParametersMap(initialDate, finalDate));
    }

    public TaskByJobTitle tasksByJobTitle(String initialDate, String finalDate) {
        return taskReportRestClient.tasksByJobTitle(taskRestServiceKey,
                getParametersMap(initialDate, finalDate));
    }

    private Map<String, String> getParametersMap(String initialDate, String finalDate) {

        Map<String, String> parametersMap = new HashMap<>();

        parametersMap.put("initialDate", initialDate);
        parametersMap.put("finalDate", finalDate);

        return parametersMap;

    }

}
