package com.warn.hr.business;

import com.warn.hr.persistence.dao.StaffDAO;
import com.warn.hr.persistence.entity.hr.StaffResource;
import com.warn.hr.persistence.entity.hr.staff.Manager;
import com.warn.hr.persistence.entity.hr.staff.Technician;
import com.warn.hr.persistence.entity.hr.staff.Temporary;
import com.warn.hr.persistence.entity.hr.staff.common.Staff;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StaffService {

    @Autowired
    private StaffResourceService staffResourceService;

    @Autowired
    private StaffDAO staffDAO;

    @Transactional(value = "hibernateTransactionManagerHR", readOnly = true)
    public Staff findById(Long id) {
        return staffDAO.findById(id);
    }

    @Transactional(value = "hibernateTransactionManagerHR", readOnly = true)
    public Staff findByLogin(String login) {
        return staffDAO.findByLogin(login);
    }

    @Transactional(value = "hibernateTransactionManagerHR", readOnly = true)
    public String findImageByStaffId(Long id) {
        return staffDAO.findImageByStaffId(id);
    }

    @Transactional(value = "hibernateTransactionManagerHR", readOnly = true)
    public List<Staff> listAll() {
        return staffDAO.listAll();
    }

    @Transactional(value = "hibernateTransactionManagerHR", propagation = Propagation.REQUIRED)
    public Boolean update(Staff staff) {
        return staffDAO.update(staff);
    }

    @Transactional(value = "hibernateTransactionManagerHR", readOnly = true)
    public Boolean validateExclusion(Long id) {

        Boolean isValidExclusion = Boolean.TRUE;

        //Is the user technician in any type of staff resource?
        Technician technician = new Technician();
        technician.setId(id);

        StaffResource staffResource = new StaffResource();
        staffResource.setTechnician(technician);

        if(!staffResourceService.list(staffResource).isEmpty()) {
            isValidExclusion = Boolean.FALSE;
        }

        //Is the use manager of any employee?
        if(staffDAO.isManager(id)) {
            isValidExclusion = Boolean.FALSE;
        }

        return isValidExclusion;

    }

}
