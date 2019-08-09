package com.warn.hr.persistence.entity.task;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.warn.hr.persistence.entity.common.AbstractEntity;
import com.warn.hr.persistence.entity.hr.staff.common.Staff;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

public @Data class TaskComment extends AbstractEntity {

    private Long id;

    private Task task;

    private Staff createdBy;

    private String comment;

    private Date createdDate;

}
