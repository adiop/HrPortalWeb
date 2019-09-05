package com.warn.hr.persistence.entity.hr.staff;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.warn.hr.persistence.entity.common.AbstractEntity;
import com.warn.hr.persistence.entity.hr.Position;
import com.warn.hr.persistence.entity.hr.StaffResource;
import com.warn.hr.persistence.entity.hr.staff.Manager;
import com.warn.hr.persistence.entity.hr.staff.common.Staff;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "HumanResources.dbo.staff_history")
@EqualsAndHashCode(callSuper = true)
public @Data class StaffHistory extends AbstractEntity {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "STAFF_ID")
    private Long staffId;

    @Column(name = "IMAGE")
    private String image;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "PREFERRED_NAME")
    private String preferredName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "SHIFT")
    private String shift;

    @Column(name = "KEY_CARD_NUMBER")
    private String keyCardNumber;

    @Column(name = "STAFF_NUMBER")
    private String staffNumber;

    @Column(name = "TEAM_LEADER")
    private Boolean teamLeader;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="JOB_TITLE_ID", nullable=false)
    private Position jobTitle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="MANAGER_ID")
    private Manager manager;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "FLAGGED")
    private Boolean flagged;

    @Column(name = "COMMENTS")
    private String comments;

    @Column(name = "CREATED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @ManyToMany(fetch = FetchType.LAZY)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinTable(
            name = "HumanResources.dbo.staff_x_staff_resources_history",
            joinColumns = { @JoinColumn(name = "HISTORY_ID") },
            inverseJoinColumns = { @JoinColumn(name = "STAFF_RESOURCE_ID") }
    )
    private List<StaffResource> staffResources;

    public String getImage() {
        return Optional.ofNullable(image).orElseGet(String::new);
    }

    public List<Long> addedStaffResourcesIds(Staff newStaffData) {

        List<Long> addedStaffResources = newStaffData.getStaffResources().stream().map(sr -> sr.getId()).collect(Collectors.toList());
        addedStaffResources.removeAll(getStaffResources().stream().map(sr -> sr.getId()).collect(Collectors.toList()));

        return addedStaffResources;

    }

    public List<Long> removedStaffResourcesIds(Staff newStaffData) {

        List<Long> removedStaffResources = getStaffResources().stream().map(sr -> sr.getId()).collect(Collectors.toList());
        removedStaffResources.removeAll(newStaffData.getStaffResources().stream().map(sr -> sr.getId()).collect(Collectors.toList()));

        return removedStaffResources;

    }

    public Boolean hasDataFlag(Staff newStaffData) {

        Boolean isFlagged = Boolean.FALSE;

        if(newStaffData.getFlagged()) {
            isFlagged = !Optional.ofNullable(getComments()).orElseGet(String::new).equalsIgnoreCase(Optional.ofNullable(newStaffData.getComments()).orElseGet(String::new));
        }

        return isFlagged;
    }

    public Boolean hasDataChanges(Staff newStaffData) {
        return !(Optional.ofNullable(getImage()).orElseGet(String::new).equalsIgnoreCase(Optional.ofNullable(newStaffData.getImage()).orElseGet(String::new)) &&
                getPreferredName().equalsIgnoreCase(newStaffData.getPreferredName()) &&
                getFirstName().equalsIgnoreCase(newStaffData.getFirstName()) &&
                getLastName().equalsIgnoreCase(newStaffData.getLastName()) &&
                Optional.ofNullable(getEmail()).orElseGet(String::new).equalsIgnoreCase(Optional.ofNullable(newStaffData.getEmail()).orElseGet(String::new)) &&
                Optional.ofNullable(getPhone()).orElseGet(String::new).equalsIgnoreCase(Optional.ofNullable(newStaffData.getPhone()).orElseGet(String::new)) &&
                getJobTitle().getId().equals(newStaffData.getJobTitle().getId()) &&
                Optional.ofNullable(Optional.ofNullable(getManager()).orElseGet(Manager::new).getId()).orElseGet(() -> 0L).equals(Optional.ofNullable(newStaffData.getManager()).orElseGet(Manager::new).getId()));
    }

}

