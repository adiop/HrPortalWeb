package com.warn.hr.business;

import com.warn.hr.business.events.StaffEvent;
import com.warn.hr.persistence.dao.*;
import com.warn.hr.persistence.entity.hr.*;
import com.warn.hr.persistence.entity.hr.staff.Employee;
import com.warn.hr.persistence.entity.hr.staff.Manager;
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
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmployeeService {

    @NonNull
    private StaffService staffService;

    @NonNull
    private EmployeeDAO employeeDAO;

    @NonNull
    private ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public Long insert(Staff createdBy, Employee employee) {

        // Insert the new employee
        Long employeeId = employeeDAO.insert(loadDefaultData(employee));

        if(Objects.nonNull(employeeId)) {
            applicationEventPublisher.publishEvent(StaffEvent.newStaffInstance(this, createdBy, employee, employee.getHiringDate()));
        }

        return employeeId;

    }

    @Transactional
    public Boolean update(Staff createdBy, Employee employee) {

        Boolean isUpdated = employeeDAO.update(employee);

        if (isUpdated) {
            applicationEventPublisher.publishEvent(StaffEvent.updateStaffInstance(this, createdBy, employee));
        }

        return isUpdated;


    }

    @Transactional
    public Boolean termEmployee(Staff createdBy, Employee employee, Date termDate) {

        Boolean isTermed = Boolean.FALSE;

        if(staffService.validateExclusion(employee.getId())) {

            isTermed = employeeDAO.term(employee, termDate);

            if(isTermed) {
                applicationEventPublisher.publishEvent(StaffEvent.termStaffInstance(this, createdBy, employee, termDate));
            }

        }

        return isTermed;


    }

    public Employee findById(Long id) {
        return employeeDAO.findById(id);
    }

    public List<Employee> list(Employee employee, Date startHireDate, Date endHireDate) {
        return employeeDAO.list(employee, startHireDate, endHireDate);
    }

    public List<Employee> listAll() {
        return employeeDAO.listAll();
    }

    private Employee loadDefaultData(Employee employee) {

        // Set the default preferred name
        if(Objects.isNull(employee.getPreferredName())) {
            employee.setPreferredName(employee.getFirstName());
        }

        // Set the default image
        if(Objects.isNull(employee.getImage())) {
            employee.setImage(Staff.DEFAULT_IMAGE);
        }

        // Set the default login
        if(Objects.isNull(employee.getLogin()) || employee.getLogin().isEmpty()) {
            employee.setLogin("u" +employee.getEmployeeNumber());
        }

        // Names in upper case
        employee.setFirstName(employee.getFirstName().toUpperCase());
        employee.setPreferredName(employee.getPreferredName().toUpperCase());
        employee.setLastName(employee.getLastName().toUpperCase());

        // Term date is null
        employee.setTermDate(null);

        return employee;

    }

}
