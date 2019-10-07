package com.warn.hr.business;

import com.warn.hr.persistence.entity.hr.Notification;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
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
public class NotificationService {


    public  String generateNotificationBody(String template, Map<String, Object> input) {

        String generatedCode = null;

        try {

            Configuration cfg = new Configuration();
            cfg.setClassForTemplateLoading(NotificationService.class, "/notification/template/");
            freemarker.template.Template freemarkerTemplate = cfg.getTemplate(template);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Writer writer = new OutputStreamWriter(out);
            freemarkerTemplate.process(input, writer);

            generatedCode = out.toString("UTF-8");

        } catch (IOException | TemplateException e) {
            throw new RuntimeException(e);
        }

        return generatedCode;

    }



}
