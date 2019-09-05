package com.warn.hr.persistence.entity.task;

import com.warn.hr.persistence.entity.common.AbstractEntity;
import com.warn.hr.persistence.entity.hr.staff.common.Staff;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
public @Data class TicketTaskHistory extends AbstractEntity {

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
