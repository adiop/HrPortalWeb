package com.warn.hr.persistence.entity.hr;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.warn.hr.persistence.entity.common.AbstractEntity;
import com.warn.hr.persistence.entity.hr.staff.Technician;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Repository;

import javax.persistence.*;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "HumanResources.dbo.staff_resources")
@EqualsAndHashCode(callSuper = true)
public @Data class StaffResource extends AbstractEntity {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "EMPLOYEE_DEFAULT_RESOURCE")
    private Boolean isEmployeeDefault;

    @Column(name = "TEAM_LEADER_DEFAULT_RESOURCE")
    private Boolean isTeamLeaderDefault;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "TYPE", nullable = false)
    @Enumerated(EnumType.STRING)
    private Type type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="TECHNICIAN_ID")
    private Technician technician;

    public enum Type {
        APPLICATION_ACCESS,
        ASSET,
        EMAIL_GROUP;
    }


}
