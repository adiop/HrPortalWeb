package com.warn.hr.business.rest;

import com.warn.hr.common.config.application.common.RestServiceConfig;
import com.warn.hr.persistence.entity.hr.staff.common.Staff;
import com.warn.hr.persistence.entity.task.Task;
import feign.Headers;
import feign.Param;
import feign.QueryMap;
import feign.RequestLine;
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
import java.util.Map;
import java.util.Optional;

/**
 * Rest client that communicates with the Task Rest Service.
 * To find the connection data see Application.yaml
 */
public interface TaskRestClient extends _RestClient {

    @Headers({"key: {token}", "Content-Type: application/json"})
    @RequestLine("POST /api/hr/v1/task/")
    public Long create(@Param("token") String token,
                       Task task);

    @Headers({"key: {token}", "Content-Type: application/json"})
    @RequestLine("GET /api/hr/v1/task/{id}")
    public Task findById(@Param("token") String token,
                         @Param("id") Long id);

    @Headers({"key: {token}", "Content-Type: application/json"})
    @RequestLine("GET /api/hr/v1/task/{id}/{technicianId}")
    public Task findById(@Param("token") String token,
                         @Param("id") Long id,
                         @Param("technicianId") Long technicianId);

    @Headers({"key: {token}", "Content-Type: application/json"})
    @RequestLine("PUT /api/hr/v1/task/comment/{id}/{createdById}")
    public Boolean insertComment(@Param("token") String token,
                                 @Param("id") Long id,
                                 @Param("createdById") Long createdById,
                                 String comment);

    @Headers({"key: {token}", "Content-Type: application/json"})
    @RequestLine("GET /api/hr/v1/task/list/open/")
    public List<Task> listOpenTasks(@Param("token") String token,
                                    @QueryMap Map<String, String> parameters);

    @Headers({"key: {token}", "Content-Type: application/json"})
    @RequestLine("GET /api/hr/v1/task/list/open/{technicianId}")
    public List<Task> listOpenTasks(@Param("token") String token,
                                    @Param("technicianId") Long technicianId,
                                    @QueryMap Map<String, String> parameters);

    @Headers({"key: {token}", "Content-Type: application/json"})
    @RequestLine("GET /api/hr/v1/task/list/closed/")
    public List<Task> listClosedTasks(@Param("token") String token,
                                      @QueryMap Map<String, String> parameters);

    @Headers({"key: {token}", "Content-Type: application/json"})
    @RequestLine("GET /api/hr/v1/task/list/closed/{technicianId}")
    public List<Task> listClosedTasks(@Param("token") String token,
                                      @Param("technicianId") Long technicianId,
                                      @QueryMap Map<String, String> parameters);

    @Headers({"key: {token}", "Content-Type: application/json"})
    @RequestLine("GET /api/hr/v1/task/list/future/")
    public List<Task> listFutureTasks(@Param("token") String token,
                                      @QueryMap Map<String, String> parameters);

    @Headers({"key: {token}", "Content-Type: application/json"})
    @RequestLine("GET /api/hr/v1/task/list/future/{technicianId}")
    public List<Task> listFutureTasks(@Param("token") String token,
                                      @Param("technicianId") Long technicianId,
                                      @QueryMap Map<String, String> parameters);

}
