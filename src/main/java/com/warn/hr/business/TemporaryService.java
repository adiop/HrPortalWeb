package com.warn.hr.business;

import com.warn.hr.business.events.StaffEvent;
import com.warn.hr.persistence.dao.EmployeeDAO;
import com.warn.hr.persistence.dao.TemporaryDAO;
import com.warn.hr.persistence.entity.hr.*;
import com.warn.hr.persistence.entity.hr.staff.Employee;
import com.warn.hr.persistence.entity.hr.staff.Manager;
import com.warn.hr.persistence.entity.hr.staff.Temporary;
import com.warn.hr.persistence.entity.hr.staff.common.Staff;
import com.warn.hr.persistence.entity.hr.staff.Technician;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TemporaryService {

    @NonNull
    private TemporaryDAO temporaryDAO;

    @NonNull
    private StaffService staffService;

    @NonNull
    private ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public Long insert(Staff createdBy, Temporary temporary) {

        // Insert the new temporary
        Long temporaryId = temporaryDAO.insert(loadDefaultData(temporary));

        if(Objects.nonNull(temporaryId)) {
            applicationEventPublisher.publishEvent(StaffEvent.newStaffInstance(this, createdBy, temporary, temporary.getContractStartDate()));
        }

        return temporaryId;
    }


    @Transactional
    public Boolean update(Staff createdBy, Temporary temporary) {

        Boolean isUpdated = temporaryDAO.update(temporary);

        if(isUpdated) {
            applicationEventPublisher.publishEvent(StaffEvent.updateStaffInstance(this, createdBy, temporary));
        }

        return isUpdated;

    }

    @Transactional
    public Boolean hire(Staff hiredBy, Temporary entity, Date hireDate) {
        return temporaryDAO.hire(entity, hireDate);
    }

    @Transactional
    public Boolean termTemporary(Staff createdBy, Temporary temporary, Date date) {

        Boolean isTermed = Boolean.FALSE;

        if(staffService.validateExclusion(temporary.getId())) {

            isTermed = temporaryDAO.term(temporary, date);

            if(isTermed) {
                applicationEventPublisher.publishEvent(StaffEvent.termStaffInstance(this, createdBy, temporary, date));
            }

        }

        return isTermed;

    }

    public Temporary findById(Long id) {
        return temporaryDAO.findById(id);
    }


    public List<Temporary> list(Temporary temporary, Date startHireDate, Date endHireDate) {
        return temporaryDAO.list(temporary, startHireDate, endHireDate);
    }

    public List<Temporary> listAll() {
        return temporaryDAO.listAll();
    }

    private Temporary loadDefaultData(Temporary temporary) {

        // Set the default preferred name
        if(Objects.isNull(temporary.getPreferredName())) {
            temporary.setPreferredName(temporary.getFirstName());
        }

        // Set the default image
        if(Objects.isNull(temporary.getImage())) {
            temporary.setImage(Staff.DEFAULT_IMAGE);
        }

        // Set the default login
        if(Objects.isNull(temporary.getLogin())) {
            temporary.setLogin("u" + temporary.getTemporaryNumber());
        }

        // Names in upper case
        temporary.setFirstName(temporary.getFirstName().toUpperCase());
        temporary.setPreferredName(temporary.getPreferredName().toUpperCase());
        temporary.setLastName(temporary.getLastName().toUpperCase());

        return temporary;

    }

}
