package com.warn.hr.web.rest.common;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class SynchronizeClientsAspect {

    private static final String WEBSOCKET_TOPIC = "/topic/notify";

    @Autowired
    private SimpMessagingTemplate template;

    @After("@annotation(org.springframework.web.bind.annotation.PostMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PutMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
    public void notifyClients() {
        template.convertAndSend(WEBSOCKET_TOPIC, Boolean.TRUE);
    }

}
