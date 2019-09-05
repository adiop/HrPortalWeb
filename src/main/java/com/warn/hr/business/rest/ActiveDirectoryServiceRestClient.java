package com.warn.hr.business.rest;

import com.warn.hr.common.config.application.common.RestServiceConfig;
import com.warn.hr.persistence.entity.hr.UserAccount;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;


/**
 * Rest client that communicates with the Active Directory Rest Service.
 * To find the connection data see Application.yaml
 */
@Slf4j
@Component
public class ActiveDirectoryServiceRestClient {

    private final RestTemplate restTemplate;

    private final RestServiceConfig activeDirectoryRestServiceConfig;

    public ActiveDirectoryServiceRestClient(RestServiceConfig activeDirectoryRestServiceConfig) {
        Assert.notNull(activeDirectoryRestServiceConfig, "activeDirectoryRestServiceConfig can't be null");
        this.activeDirectoryRestServiceConfig = activeDirectoryRestServiceConfig;
        this.restTemplate = new RestTemplate();
    }

    public UserAccount findByLogin(String login) {
        String uri = getUri().path("/").path(login).toUriString();
        return restTemplate.exchange(uri, HttpMethod.GET, getHttpEntity(), UserAccount.class).getBody();
    }

    public Boolean update(UserAccount userAccount) {
        String uri = getUri().toUriString();
        return restTemplate.exchange(uri, HttpMethod.PUT, getHttpEntity(userAccount), Boolean.class).getBody();
    }

    private UriComponentsBuilder getUri() {
        return activeDirectoryRestServiceConfig.getUri();
    }

    private HttpEntity<String> getHttpEntity() {
        return getHttpEntity("");
    }

    private <T> HttpEntity<T> getHttpEntity(T body) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("key", activeDirectoryRestServiceConfig.getKey());
        return new HttpEntity<T>(body, headers);
    }

}
