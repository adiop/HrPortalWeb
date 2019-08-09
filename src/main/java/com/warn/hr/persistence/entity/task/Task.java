package com.warn.hr.persistence.entity.task;

import com.warn.hr.persistence.entity.common.AbstractEntity;
import com.warn.hr.persistence.entity.hr.staff.common.Staff;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.*;

public @Data class Task extends AbstractEntity {

    private Long id;

    private Integer progressPercentage;

    private Staff createdBy;

    private Staff taskStaff;

    private Type type;

    private Status status = Status.CREATED;;

    private Date effectiveDate = new Date();;

    private Date createdDate = new Date();

    private Date updatedDate = new Date();;

    private List<TaskComment> comments;

    private List<TicketTask> ticketTasks;

    public enum Type {
        NEW_STAFF,
        TERM_STAFF,
        UPDATE_STAFF
    }

    public enum Status {
        CREATED,
        OPEN,
        CLOSED;
    }

}
