package com.warn.hr.persistence.entity.reports;

import lombok.Data;

import java.util.Date;
import java.util.List;

public @Data class TaskByDepartment {

    private Date initialDate;

    private Date finalDate;

    private List<DataItem> taskByDepartment;

}