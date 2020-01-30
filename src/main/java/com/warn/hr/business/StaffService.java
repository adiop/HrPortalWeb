package com.warn.hr.business;

import com.warn.hr.persistence.dao.StaffDAO;
import com.warn.hr.persistence.entity.hr.StaffResource;
import com.warn.hr.persistence.entity.hr.staff.Manager;
import com.warn.hr.persistence.entity.hr.staff.Technician;
import com.warn.hr.persistence.entity.hr.staff.Temporary;
import com.warn.hr.persistence.entity.hr.staff.common.Staff;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class StaffService {

    @NonNull
    private StaffResourceService staffResourceService;

    @NonNull
    private StaffDAO staffDAO;

    public Staff findById(Long id) {
        return staffDAO.findById(id);
    }

    public Staff findByLogin(String login) {
        return staffDAO.findByLogin(login);
    }

    public Staff findByStaffNumber(String staffNumber) {
        return staffDAO.findByStaffNumber(staffNumber);
    }

    public String findImageByStaffId(Long id) {
        return staffDAO.findImageByStaffId(id);
    }

    public List<Staff> listAll() {
        return staffDAO.listAll();
    }

    @Transactional
    public Boolean update(Staff staff) {
        return staffDAO.update(staff);
    }

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
