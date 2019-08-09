package com.warn.hr.business;

import com.warn.hr.persistence.entity.reports.*;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class ReportService {

    @Autowired
    private TaskReportServiceTemplate taskReportServiceTemplate;

    public TasksTotal tasksTotal(String initialDate, String finalDate) {
        return taskReportServiceTemplate.tasksTotal(initialDate, finalDate);
    }

    public TaskByMonthYear tasksByMonthYear(String initialDate, String finalDate) {
        return taskReportServiceTemplate.tasksByMonthYear(initialDate, finalDate);
    }

    public TaskByDate tasksByDate(String initialDate, String finalDate) {
        return taskReportServiceTemplate.tasksByDate(initialDate, finalDate);
    }

    public TaskByDepartment tasksByDepartment(String initialDate, String finalDate) {
        return taskReportServiceTemplate.tasksByDepartment(initialDate, finalDate);
    }

    public TaskByJobTitle tasksByJobTitle(String initialDate, String finalDate) {
        return taskReportServiceTemplate.tasksByJobTitle(initialDate, finalDate);
    }

    @Component
    @PropertySource("classpath:task.${spring.profiles.active}.properties")
    public @Data class TaskReportServiceTemplate {

        @Value("${task.api.protocol}")
        private String protocol;

        @Value("${task.api.host}")
        private String host;

        @Value("${task.api.port}")
        private String port;

        @Value("${task.api.prefix}")
        private String urlPrefix;

        private RestTemplate restTemplate;

        public TaskReportServiceTemplate() {
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
            return UriComponentsBuilder.newInstance().scheme(protocol).host(host).port(port).path(urlPrefix);
        }

        private <T> HttpEntity<T> getHttpEntity(T body) {
            HttpHeaders headers = new HttpHeaders();
            headers.set("key", "583eccb2-1f67-11e9-ab14-d663bd873d93");
            return new HttpEntity<T>(body, headers);
        }

    }

}
