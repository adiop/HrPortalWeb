package com.warn.hr.business;

import com.warn.hr.persistence.dao.StaffDAO;
import com.warn.hr.persistence.dao.StaffResourceDAO;
import com.warn.hr.persistence.dao.EmployeeDAO;
import com.warn.hr.persistence.dao.JobTitleDAO;
import com.warn.hr.persistence.entity.hr.*;
import com.warn.hr.persistence.entity.hr.staff.Employee;
import com.warn.hr.persistence.entity.hr.staff.Technician;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static javaslang.API.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class StaffResourceService {

    @NonNull
    private StaffResourceDAO staffResourceDAO;

    @NonNull
    private JobTitleDAO jobTitleDAO;

    @NonNull
    private StaffDAO staffDAO;

    @Transactional
    public Long insert(StaffResource asset) {
        return staffResourceDAO.insert(asset);
    }

    @Transactional
    public Boolean update(StaffResource asset) {
        return staffResourceDAO.update(asset);
    }

    @Transactional
    public Boolean delete(StaffResource asset) {
        return Match(validateExclusion(asset)).of(Case($(Boolean::booleanValue), () -> staffResourceDAO.delete(asset)),
                                                  Case($(), () -> Boolean.FALSE));
    }

    @Transactional(readOnly = true)
    public StaffResource findById(Long id) {
        return staffResourceDAO.findById(id);
    }

    @Transactional(readOnly = true)
    public List<StaffResource> list(StaffResource staffResource) {
        return staffResourceDAO.list(staffResource);
    }

    /*
     * Verifies if the asset has job titles associated, in this case the asset cannot be excluded
     */
    private Boolean validateExclusion(StaffResource staffResource) {

        StaffResource staffResourceLoaded = findById(staffResource.getId());

        JobTitle jobTitle = new JobTitle();
        jobTitle.addStaffResource(staffResourceLoaded);

        Employee employee = new Employee();
        employee.addStaffResource(staffResourceLoaded);

        return jobTitleDAO.list(jobTitle).isEmpty() &&
                staffDAO.list(employee).isEmpty();

    }
    
}

