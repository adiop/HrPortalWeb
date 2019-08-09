package com.warn.hr.business;

import com.warn.hr.persistence.dao.StaffHistoryDAO;
import com.warn.hr.persistence.entity.hr.staff.StaffHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StaffHistoryService {

    @Autowired
    private StaffHistoryDAO staffHistoryDAO;

    @Transactional(value = "hibernateTransactionManagerHR")
    public void createStaffHistory(Long id) {
        staffHistoryDAO.createStaffHistory(id);
    }

    @Transactional(value = "hibernateTransactionManagerHR", readOnly = true)
    public StaffHistory findLastChange(Long id) {
        return staffHistoryDAO.findLastChange(id);
    }

    @Transactional(value = "hibernateTransactionManagerHR", readOnly = true)
    public List<StaffHistory> list(Long id) {
        return staffHistoryDAO.list(id);
    }

}
