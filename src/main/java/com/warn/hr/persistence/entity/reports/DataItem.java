package com.warn.hr.persistence.entity.reports;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor(staticName = "of")
public @Data class DataItem {

    private String name;

    private Integer newStaffTasks;

    private Integer updateStaffTasks;

    private Integer termStaffTasks;

}