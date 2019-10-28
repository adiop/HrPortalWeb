package com.warn.hr.persistence.entity.hr;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
public @Data class Notification {

    public static final String FROM = "HelpDesk <helpdesk@warn.com>";

    private String from;

    private String to;

    private List<String> cc;

    private Type type;

    private String subject;

    private String body;

    public enum Type {
        TEXT,
        HTML;
    }

}
