package com.warn.hr.persistence.entity.task;

import com.warn.hr.persistence.entity.common.AbstractEntity;
import com.warn.hr.persistence.entity.hr.staff.common.Staff;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

public @Data class TaskComment {

    private Long id;

    private Task task;

    private Staff createdBy;

    private String comment;

    private Date createdDate;

}
