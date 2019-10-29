package com.warn.hr.business.rest;

import com.warn.hr.persistence.entity.reports.*;
import com.warn.hr.persistence.entity.task.Task;
import feign.Headers;
import feign.Param;
import feign.QueryMap;
import feign.RequestLine;

import java.util.List;
import java.util.Map;

/**
 * Rest client that communicates with the Task Rest Service.
 * To find the connection data see Application.yaml
 */
public interface TaskReportRestClient extends _RestClient {

    @Headers({"key: {token}"})
    @RequestLine("GET /api/hr/v1/task/reports/tasks-total")
    public TasksTotal tasksTotal(@Param("token") String token,
                                 @QueryMap Map<String, String> parameters);

    @Headers({"key: {token}"})
    @RequestLine("GET /api/hr/v1/task/reports/tasks-by-date")
    public TaskByDate tasksByDate(@Param("token") String token,
                                  @QueryMap Map<String, String> parameters);

    @Headers({"key: {token}"})
    @RequestLine("GET /api/hr/v1/task/reports/tasks-by-month-year")
    public TaskByMonthYear tasksByMonthYear(@Param("token") String token,
                                    @QueryMap Map<String, String> parameters);

    @Headers({"key: {token}"})
    @RequestLine("GET /api/hr/v1/task/reports/tasks-by-department")
    public TaskByDepartment tasksByDepartment(@Param("token") String token,
                                     @QueryMap Map<String, String> parameters);

    @Headers({"key: {token}"})
    @RequestLine("GET /api/hr/v1/task/reports/tasks-by-job-title")
    public TaskByJobTitle tasksByJobTitle(@Param("token") String token,
                                          @QueryMap Map<String, String> parameters);

}
