package com.warn.hr.persistence.entity.task;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "HumanResources.dbo.staff")
@Inheritance(strategy = InheritanceType.JOINED)
public @Data class TaskStaff {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "PREFERRED_NAME")
    private String preferredName;

    @Column(name = "LAST_NAME")
    private String lastName;

}