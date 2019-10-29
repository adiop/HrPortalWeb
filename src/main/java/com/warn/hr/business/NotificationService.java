package com.warn.hr.business;

import com.warn.hr.business.rest.NotificationRestClient;
import com.warn.hr.persistence.entity.hr.Notification;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;
import java.util.concurrent.Executors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private static final String TO = "hr@warn.com";

    @NonNull
    private String notificationRestServiceKey;

    @NonNull
    private NotificationRestClient notificationRestClient;

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
                notificationRestClient.sendEmail(notificationRestServiceKey, notification);
            } catch (Exception e) {
                log.error("An error has occurred while trying to open the task", e);
            }

        });

        return Boolean.TRUE;
    }

}
