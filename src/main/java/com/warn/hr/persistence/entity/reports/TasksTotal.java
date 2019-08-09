package com.warn.hr.persistence.entity.reports;

import lombok.Data;

import java.util.Date;

public @Data class TasksTotal {

    private Date initialDate;

    private Date finalDate;

    private Integer newStaffTasks = 0;

    private Integer updateStaffTasks = 0;

    private Integer termStaffTasks = 0;

}