package com.warn.hr.business.rest;

import com.warn.hr.common.config.application.common.RestServiceConfig;
import com.warn.hr.persistence.entity.hr.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.concurrent.Executors;


@Slf4j
@Component
public class NotificationServiceRestClient {

    private final RestTemplate restTemplate;

    private final RestServiceConfig notificationRestServiceConfig;

    private static final String TO = "hr@warn.com";

    public NotificationServiceRestClient(RestServiceConfig notificationRestServiceConfig) {
        Assert.notNull(notificationRestServiceConfig, "notificationRestServiceConfig can't be null");
        this.notificationRestServiceConfig = notificationRestServiceConfig;
        this.restTemplate = new RestTemplate();
    }

    public Boolean sendEmail(String subject, String body) {

        Notification notification = Notification.builder()
                .to(TO)
                .subject(subject)
                .body(body)
                .build();

        return sendEmail(notification);

    }

    public Boolean sendEmail(Notification notification) {

        notification.setFrom(Notification.FROM);
        notification.setSubject(notification.getSubject());
        notification.setType(Notification.Type.HTML);

        Executors.newSingleThreadExecutor().submit(() -> {

            try {
                log.info("an email was sent {}", notification.toString());

                String uri = notificationRestServiceConfig.getUri().toUriString();
                restTemplate.exchange(uri, HttpMethod.POST, getHttpEntity(notification), Boolean.class);
            } catch (Exception e) {
                log.error("An error has occurred while trying to open the task", e);
            }

        });

        return Boolean.TRUE;
    }

    private <T> HttpEntity<T> getHttpEntity(T body) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("key", notificationRestServiceConfig.getKey());
        return new HttpEntity<T>(body, headers);
    }

}
