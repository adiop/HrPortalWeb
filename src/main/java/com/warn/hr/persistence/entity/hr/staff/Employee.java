package com.warn.hr.persistence.entity.hr.staff;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.warn.hr.common.utility.NullUtility;
import com.warn.hr.persistence.entity.hr.staff.common.Staff;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.*;

/**
 * This class represents a full time employee
 *
 * @author Warn
 * @since 04/12/2018
 */
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "HumanResources.dbo.employees")
@PrimaryKeyJoinColumn(name = "ID")
@EqualsAndHashCode(callSuper = true)
public @Data class Employee extends Staff implements NullUtility {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "HIRING_DATE")
    @Temporal(TemporalType.DATE)
    private Date hiringDate;

    @Column(name = "TERM_DATE")
    @Temporal(TemporalType.DATE)
    private Date termDate;

    public String getEmployeeNumber() {
        return getStaffNumber();
    }

    public void setEmployeeNumber(String employeeNumber) {
        setStaffNumber(employeeNumber);
    }

}
