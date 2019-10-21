package com.warn.hr.business;

import com.warn.hr.persistence.dao.StaffHistoryDAO;
import com.warn.hr.persistence.entity.hr.staff.StaffHistory;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class StaffHistoryService {

    @NonNull
    private StaffHistoryDAO staffHistoryDAO;

    public StaffHistory findLastChange(Long id) {
        return staffHistoryDAO.findLastChange(id);
    }

    public List<StaffHistory> list(Long id) {
        return staffHistoryDAO.list(id);
    }

}
