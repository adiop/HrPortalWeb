package com.warn.hr.persistence.entity.task;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.warn.hr.persistence.entity.common.AbstractEntity;
import com.warn.hr.persistence.entity.hr.staff.Technician;
import com.warn.hr.persistence.entity.hr.staff.common.Staff;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
public @Data class TicketTask extends AbstractEntity {

    private Long id;

    private String title;

    private String description;

    private Technician technician;

    private Status status = Status.CREATED;

    private String resolution;

    private List<TicketTaskHistory> history;

    public enum Status {
        CREATED,
        OPEN,
        CLOSED
    }

}
