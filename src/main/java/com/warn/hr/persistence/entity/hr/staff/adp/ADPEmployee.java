package com.warn.hr.persistence.entity.hr.staff.adp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.warn.hr.persistence.entity.hr.Department;
import com.warn.hr.persistence.entity.hr.Position;
import com.warn.hr.persistence.entity.hr.staff.Employee;
import com.warn.hr.persistence.entity.hr.staff.Manager;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.Optional;

@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor(staticName = "newInstance")
public @Data class ADPEmployee {

    private String image;

    private String firstName;

    private String preferredName;

    private String lastName;

    private String employeeNumber;

    private Position jobTitle;

    private Department department;

    private Shift shift;

    private Manager manager;

    private Employee employee;

    public void setEmployeeNumber(String employeeNumber) {

        Optional.ofNullable(employeeNumber).ifPresent((en) -> {

            if(!employeeNumber.trim().isEmpty()) {
                this.employeeNumber = employeeNumber.substring(2);
            }

        });


    }

    public Boolean getHasChanges() {

        Boolean hasChanges = null;

        if(Objects.nonNull(employee)) {
            hasChanges = getHasNameChange() ||
                    getHasDepartmentChange() ||
                    getHasJobTitleChange() ||
                    getHasShiftChange() ||
                    getHasManagerChange();
        }

        return hasChanges;

    }

    public Boolean getHasNameChange() {

        Boolean hasNameChange = null;

        if(Objects.nonNull(employee)) {
            hasNameChange = !employee.getPreferredName().equalsIgnoreCase(getPreferredName()) ||
                    !employee.getLastName().equalsIgnoreCase(getLastName());
        }

        return hasNameChange;

    }

    public Boolean getHasJobTitleChange() {

        Boolean hasJobTitleChange = null;

        if(Objects.nonNull(employee)) {
            hasJobTitleChange = !employee.getJobTitle().getId().equals(getJobTitle().getId());
        }

        return hasJobTitleChange;

    }

    public Boolean getHasDepartmentChange() {

        Boolean hasDepartmentChange = null;

        if(Objects.nonNull(employee)) {
            hasDepartmentChange = !employee.getJobTitle().getDepartment().getId().equals(getDepartment().getId());
        }

        return hasDepartmentChange;

    }

    public Boolean getHasShiftChange() {

        Boolean hasShiftChange = null;

        if(Objects.nonNull(employee)) {
            hasShiftChange = !employee.getShift().name().equals(getShift().name());
        }

        return hasShiftChange;

    }

    public Boolean getHasManagerChange() {

        Boolean hasManagerChanges = null;

        if(Objects.nonNull(employee)) {

            hasManagerChanges = Boolean.FALSE;

            if (Objects.nonNull(getManager().getId()) && Objects.isNull(employee.getManager())) {
                hasManagerChanges = Boolean.TRUE;
            } else if (Objects.isNull(getManager().getId()) && Objects.nonNull(employee.getManager())) {
                hasManagerChanges = Boolean.TRUE;
            } else if (Objects.nonNull(getManager().getId()) && Objects.nonNull(employee.getManager())) {
                hasManagerChanges = !employee.getManager().getId().equals(getManager().getId());
            }

        }

        return hasManagerChanges;
    }

    public enum Shift {

        DAY_4_10,
        DAY_5_8,
        SWING_4_10,
        SWING_5_8,;

        public static Shift valueOfADP(String adpValue) {

            Shift shift = null;

            switch (adpValue) {

                case "1 - Day":
                    shift = DAY_5_8;
                    break;
                case "1 - Day M-TH":
                    shift = DAY_4_10;
                    break;
                case "2 - Swing":
                    shift = SWING_5_8;
                    break;
                case "2 - Swing M-TH":
                    shift = SWING_4_10;
                    break;

            }

            return shift;

        }

    }

}
