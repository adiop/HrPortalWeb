package com.warn.hr.persistence.entity.hr.staff;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.warn.hr.common.utility.NullUtility;
import com.warn.hr.persistence.entity.hr.staff.common.Staff;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "HumanResources.dbo.contractors")
@PrimaryKeyJoinColumn(name = "ID")
@EqualsAndHashCode(callSuper = true)
public @Data class Temporary extends Staff implements Cloneable, NullUtility {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "COMPANY_NAME")
    private String companyName;

    @Column(name = "CONTRACT_START_DATE")
    @Temporal(TemporalType.DATE)
    private Date contractStartDate;

    @Column(name = "CONTRACT_END_DATE")
    @Temporal(TemporalType.DATE)
    private Date contractEndDate;

    public String getTemporaryNumber() {
        return getStaffNumber();
    }

    public void setTemporaryNumber(String temporaryNumber) {
        setStaffNumber(temporaryNumber);
    }

}
