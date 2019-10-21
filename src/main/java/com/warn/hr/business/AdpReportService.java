package com.warn.hr.business;

import com.warn.hr.persistence.dao.ADPEmployeeDAO;
import com.warn.hr.persistence.entity.hr.staff.adp.ADPEmployee;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdpReportService {

    @NonNull
    private ADPEmployeeDAO adpEmployeeDAO;

    public Boolean uploadFile(byte[] fileContent) {
        return adpEmployeeDAO.insertAdpFile(fileContent);
    }

    public Map<String, List<ADPEmployee>> listADPEmployeeMap() {

        Map<String, List<ADPEmployee>> adpEmployeeMap = new HashMap<>();
        adpEmployeeMap.put(ADPEmployeeMapEnum.INSERTED.name(), listInsertedEmployees());
        adpEmployeeMap.put(ADPEmployeeMapEnum.UPDATED.name(), listUpdatedEmployees());
        adpEmployeeMap.put(ADPEmployeeMapEnum.REMOVED.name(), listRemovedEmployees());
        adpEmployeeMap.put(ADPEmployeeMapEnum.ERROR.name(), listErrorEmployees());

        return adpEmployeeMap;

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

    public List<ADPEmployee> listErrorEmployees() {
        return adpEmployeeDAO.listErrorEmployees();
    }

    private enum ADPEmployeeMapEnum {
        INSERTED,
        UPDATED,
        REMOVED,
        ERROR;
    }

}
