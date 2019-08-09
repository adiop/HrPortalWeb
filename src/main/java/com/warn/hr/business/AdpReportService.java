package com.warn.hr.business;

import com.warn.hr.persistence.dao.ADPEmployeeDAO;
import com.warn.hr.persistence.entity.hr.staff.adp.ADPEmployee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class AdpReportService {

    private static final String CACHE_NAME = "adp_report_cache";

    @Autowired
    private ADPEmployeeDAO adpEmployeeDAO;

    public Boolean uploadFile(byte[] fileContent) {
        return adpEmployeeDAO.insertAdpFile(fileContent);
    }

    public List<ADPEmployee> listUpdatedEmployees() {
        return adpEmployeeDAO.listUpdatedEmployees();
    }

    public List<ADPEmployee> listInsertedEmployees() {
        return adpEmployeeDAO.listInsertedEmployees();
    }

    public List<ADPEmployee> listRemovedEmployees() {
        return adpEmployeeDAO.listRemovedEmployees();
    }

}
