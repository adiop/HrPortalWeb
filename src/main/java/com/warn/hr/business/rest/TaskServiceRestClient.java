package com.warn.hr.business.rest;

import com.warn.hr.common.config.application.common.RestServiceConfig;
import com.warn.hr.persistence.entity.hr.staff.common.Staff;
import com.warn.hr.persistence.entity.task.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;

/**
 * Rest client that communicates with the Task Rest Service.
 * To find the connection data see Application.yaml
 */
@Slf4j
@Component
public class TaskServiceRestClient {

    private final RestTemplate restTemplate;

    private final RestServiceConfig taskRestServiceConfig;

    public TaskServiceRestClient(RestServiceConfig taskRestServiceConfig) {
        Assert.notNull(taskRestServiceConfig, "taskRestServiceConfig can't be null");
        this.taskRestServiceConfig = taskRestServiceConfig;
        this.restTemplate = new RestTemplate();
    }

    public Long createTask(Task task) {
        String uri = getUri().toUriString();
        ResponseEntity responseEntity = restTemplate.exchange(uri, HttpMethod.POST, getHttpEntity(task), ResponseEntity.class).getBody();
        return null;
    }

    public Task findById(Long id) {
        String uri = getUri().path("/" + id).toUriString();
        return restTemplate.exchange(uri, HttpMethod.GET, null, Task.class).getBody();
    }

    public Task findById(Long id, Long technicianId) {
        String uri = getUri().path("/" + id).path("/" + technicianId).toUriString();
        return restTemplate.exchange(uri, HttpMethod.GET, null, Task.class).getBody();
    }

    public Boolean insertComment(Long id, Staff createdBy, String comment) {
        String uri = getUri().path("/comment/").path(id.toString()).path("/").path(createdBy.getId().toString()).toUriString();
        return restTemplate.exchange(uri, HttpMethod.PUT, getHttpEntity(comment), new ParameterizedTypeReference<Boolean>(){}).getBody();
    }

    public List<Task> listOpenTasks(Task task) {
        String uri = getUri().path("/list/open/").queryParam("taskStaff.fullName", Optional.ofNullable(task.getTaskStaff()).map(s -> s.getFullName()).orElseGet(() -> "")).toUriString();
        return restTemplate.exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<List<Task>>(){}).getBody();
    }

    public List<Task> listOpenTasks(Task task, Long technicianId) {
        String uri = getUri().path("/list/open/").path("/" + technicianId).queryParam("taskStaff.fullName", Optional.ofNullable(task.getTaskStaff()).map(s -> s.getFullName()).orElseGet(() -> "")).toUriString();
        return restTemplate.exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<List<Task>>(){}).getBody();
    }

    public List<Task> listClosedTasks(Task task) {
        String uri = getUri().path("/list/closed/").queryParam("taskStaff.fullName", Optional.ofNullable(task.getTaskStaff()).map(s -> s.getFullName()).orElseGet(() -> "")).toUriString();
        return restTemplate.exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<List<Task>>(){}).getBody();
    }

    public List<Task> listClosedTasks(Task task, Long technicianId) {
        String uri = getUri().path("/list/closed/").path("/" + technicianId).queryParam("taskStaff.fullName", Optional.ofNullable(task.getTaskStaff()).map(s -> s.getFullName()).orElseGet(() -> "")).toUriString();
        return restTemplate.exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<List<Task>>(){}).getBody();
    }

    public List<Task> listFutureTasks(Task task) {
        String uri = getUri().path("/list/future/").queryParam("taskStaff.fullName", Optional.ofNullable(task.getTaskStaff()).map(s -> s.getFullName()).orElseGet(() -> "")).toUriString();
        return restTemplate.exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<List<Task>>(){}).getBody();
    }

    public List<Task> listFutureTasks(Task task, Long technicianId) {
        String uri = getUri().path("/list/future/").path("/" + technicianId).queryParam("taskStaff.fullName", Optional.ofNullable(task.getTaskStaff()).map(s -> s.getFullName()).orElseGet(() -> "")).toUriString();
        return restTemplate.exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<List<Task>>(){}).getBody();
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
