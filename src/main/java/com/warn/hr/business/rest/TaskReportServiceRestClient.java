package com.warn.hr.business.rest;

import com.warn.hr.common.config.application.common.RestServiceConfig;
import com.warn.hr.persistence.entity.reports.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
public @Data class TaskReportServiceRestClient {

    private final RestTemplate restTemplate;

    private final RestServiceConfig taskRestServiceConfig;

    public TaskReportServiceRestClient(RestServiceConfig taskRestServiceConfig) {
        Assert.notNull(taskRestServiceConfig, "taskRestServiceConfig can't be null");
        this.taskRestServiceConfig = taskRestServiceConfig;
        this.restTemplate = new RestTemplate();
    }

    public TasksTotal tasksTotal(String initialDate, String finalDate) {
        String uri = getUri().path("/reports/tasks-total").queryParam("initialDate", initialDate).queryParam("finalDate", finalDate).toUriString();
        return restTemplate.exchange(uri, HttpMethod.GET, null, TasksTotal.class).getBody();
    }

    public TaskByMonthYear tasksByMonthYear(String initialDate, String finalDate) {
        String uri = getUri().path("/reports/tasks-by-month-year").queryParam("initialDate", initialDate).queryParam("finalDate", finalDate).toUriString();
        return restTemplate.exchange(uri, HttpMethod.GET, null, TaskByMonthYear.class).getBody();
    }

    public TaskByDate tasksByDate(String initialDate, String finalDate) {
        String uri = getUri().path("/reports/tasks-by-date").queryParam("initialDate", initialDate).queryParam("finalDate", finalDate).toUriString();
        return restTemplate.exchange(uri, HttpMethod.GET, null, TaskByDate.class).getBody();
    }

    public TaskByDepartment tasksByDepartment(String initialDate, String finalDate) {
        String uri = getUri().path("/reports/tasks-by-department").queryParam("initialDate", initialDate).queryParam("finalDate", finalDate).toUriString();
        return restTemplate.exchange(uri, HttpMethod.GET, null, TaskByDepartment.class).getBody();
    }

    public TaskByJobTitle tasksByJobTitle(String initialDate, String finalDate) {
        String uri = getUri().path("/reports/tasks-by-job-title").queryParam("initialDate", initialDate).queryParam("finalDate", finalDate).toUriString();
        return restTemplate.exchange(uri, HttpMethod.GET, null, TaskByJobTitle.class).getBody();
    }

    private UriComponentsBuilder getUri() {
        return taskRestServiceConfig.getUri();
    }

    private <T> HttpEntity<T> getHttpEntity(T body) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("key", taskRestServiceConfig.getKey());
        return new HttpEntity<T>(body, headers);
    }

}
