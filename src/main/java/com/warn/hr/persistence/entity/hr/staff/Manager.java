package com.warn.hr.persistence.entity.hr.staff;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.warn.hr.persistence.entity.common.AbstractEntity;
import com.warn.hr.persistence.entity.hr.Department;
import com.warn.hr.persistence.entity.hr.Position;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "HumanResources.dbo.staff")
@EqualsAndHashCode(callSuper = true)
public @Data class Manager extends AbstractEntity {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "IMAGE")
    private String image;

    @Column(name = "PREFERRED_NAME")
    private String preferredName;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "LOGIN")
    private String login;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PHONE")
    private String phone;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="JOB_TITLE_ID", nullable=false)
    private Position jobTitle;

}
