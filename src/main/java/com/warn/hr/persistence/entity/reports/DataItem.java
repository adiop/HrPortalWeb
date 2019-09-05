package com.warn.hr.persistence.entity.reports;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public @Data class DataItem {

    private String name;

    private Integer newStaffTasks;

    private Integer updateStaffTasks;

    private Integer termStaffTasks;

}
