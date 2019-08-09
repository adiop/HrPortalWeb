package com.warn.hr.persistence.entity.task;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.warn.hr.persistence.entity.common.AbstractEntity;
import com.warn.hr.persistence.entity.hr.staff.common.Staff;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.Date;

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
