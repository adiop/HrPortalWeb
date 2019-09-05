package com.warn.hr.business.rest;

import com.warn.hr.common.config.application.common.RestServiceConfig;
import com.warn.hr.persistence.entity.hr.staff.common.Staff;
import com.warn.hr.persistence.entity.task.Task;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Rest client that communicates with the Ticket Rest Service.
 * To find the connection data see Application.yaml
 */
@Slf4j
@Component
public class TicketServiceRestClient {

    private final RestTemplate restTemplate;

    private final RestServiceConfig ticketRestServiceConfig;

    public TicketServiceRestClient(RestServiceConfig ticketRestServiceConfig) {
        Assert.notNull(ticketRestServiceConfig, "ticketRestServiceConfig can't be null");
        this.ticketRestServiceConfig = ticketRestServiceConfig;
        this.restTemplate = new RestTemplate();
    }

    public Task findById(Long id) {
        String uri = getUri().path("/" + id).toUriString();
        return restTemplate.exchange(uri, HttpMethod.GET, null, Task.class).getBody();
    }

    public Boolean reassign(Long id, Staff reassignedBy, Staff newTechnician, String reason) {
        String uri = getUri().path("/reassign/").path(id.toString()).path("/").path(reassignedBy.getId().toString()).path("/").path(newTechnician.getId().toString()).toUriString();
        return restTemplate.exchange(uri, HttpMethod.PUT, getHttpEntity(reason), new ParameterizedTypeReference<Boolean>(){}).getBody();
    }

    public Boolean close(Long id, Staff closedBy, String resolution) {
        String uri = getUri().path("/close/").path(id.toString()).path("/").path(closedBy.getId().toString()).toUriString();
        return restTemplate.exchange(uri, HttpMethod.PUT, getHttpEntity(resolution), new ParameterizedTypeReference<Boolean>(){}).getBody();
    }

    public Boolean reopen(Long id, Staff reopenedBy, String reason) {
        String uri = getUri().path("/reopen/").path(id.toString()).path("/").path(reopenedBy.getId().toString()).toUriString();
        return restTemplate.exchange(uri, HttpMethod.PUT, getHttpEntity(reason), new ParameterizedTypeReference<Boolean>(){}).getBody();
    }

    private UriComponentsBuilder getUri() {
        return ticketRestServiceConfig.getUri();
    }

    private <T> HttpEntity<T> getHttpEntity(T body) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("key", ticketRestServiceConfig.getKey());
        return new HttpEntity<T>(body, headers);
    }

}
