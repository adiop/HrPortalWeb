package com.warn.hr.persistence.entity.hr;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.warn.hr.persistence.entity.common.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "HumanResources.dbo.job_titles")
@EqualsAndHashCode(callSuper = true)
public @Data class JobTitle extends AbstractEntity {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="department_id", nullable=false)
    private Department department;

    @ManyToMany(fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinTable(
            name = "HumanResources.dbo.job_titles_x_staff_resources",
            joinColumns = { @JoinColumn(name = "JOB_TITLE_ID") },
            inverseJoinColumns = { @JoinColumn(name = "STAFF_RESOURCE_ID") }
    )
    private List<StaffResource> staffResources;

    public void addStaffResource(StaffResource staffResource) {
        getStaffResources().add(staffResource);
    }

}
