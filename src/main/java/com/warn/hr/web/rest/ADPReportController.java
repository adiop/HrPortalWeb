package com.warn.hr.web.rest;

import com.warn.hr.business.*;
import com.warn.hr.persistence.entity.hr.staff.Employee;
import com.warn.hr.persistence.entity.hr.staff.Manager;
import com.warn.hr.persistence.entity.hr.staff.adp.ADPEmployee;
import com.warn.hr.web.rest.common.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ADPReportController extends AbstractController {

    @Autowired
    private AdpReportService adpReportService;

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/hr/api/v1/adp-report/upload")
    public @ResponseBody ResponseEntity<SimpleBooleanValue> upload(@RequestParam("file") MultipartFile file) {
        return find(() -> SimpleBooleanValue.of(adpReportService.uploadFile(file.getBytes())));
    }

    @GetMapping("/hr/api/v1/adp-report/list")
    public @ResponseBody ResponseEntity<Map<String, List<ADPEmployee>>> list() {
        return find(() -> adpReportService.listADPEmployeeMap());
    }

    @PutMapping("/hr/api/v1/adp-report/employee/update/{id}")
    public @ResponseBody ResponseEntity<Boolean> update(@PathVariable("id") Long id, @RequestBody Employee entity, HttpServletRequest request) {

        Employee databaseEmployee = employeeService.findById(id);

        databaseEmployee.setFirstName(entity.getFirstName().toUpperCase());
        databaseEmployee.setPreferredName(entity.getPreferredName().toUpperCase());
        databaseEmployee.setLastName(entity.getLastName().toUpperCase());
        databaseEmployee.setShift(entity.getShift());
        databaseEmployee.setJobTitle(entity.getJobTitle());

        Manager manager = new Manager();
        manager.setId(entity.getManager().getId());

        databaseEmployee.setManager(manager);

        return update(() -> employeeService.update(getStaff(request), databaseEmployee));

    }

}
