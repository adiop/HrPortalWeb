package com.warn.hr.persistence.entity.task;

import com.warn.hr.persistence.entity.hr.staff.common.Staff;
import lombok.Data;

import java.util.Date;

public @Data class TicketTaskHistory {

    private Long id;

    private Staff createdBy;

    private TicketTask ticketTask;

    private Event event;

    private String details;

    private Date createdDate;

    public enum Event {
        OPENED,
        CLOSED,
        REOPENED,
        REASSIGNED;
    }

}
