package com.warn.hr.web.rest;

import com.warn.hr.business.EmployeeService;
import com.warn.hr.persistence.entity.hr.staff.Employee;
import com.warn.hr.persistence.entity.hr.staff.common.Staff;
import com.warn.hr.web.rest.common.AbstractController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

import static javaslang.API.*;

@Slf4j
@RestController
public class EmployeeController extends AbstractController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/hr/api/v1/employee")
    public @ResponseBody ResponseEntity create(@RequestBody Employee entity, HttpServletRequest request) {
        return create(() -> String.format("/hr/api/v1/employee/%d", employeeService.insert(getStaff(request), entity)));
    }


    @PutMapping("/hr/api/v1/employee/{id}")
    public @ResponseBody ResponseEntity update(@PathVariable("id") Long id, @RequestBody Employee entity, HttpServletRequest request) {
        entity.setId(id);
        return update(() -> employeeService.update(getStaff(request), entity));
    }

    @PutMapping("/hr/api/v1/employee/term/{id}")
    public @ResponseBody ResponseEntity delete(@PathVariable("id") Long id, @RequestBody Employee entity, HttpServletRequest request) {
        return Match(employeeService.termEmployee(getStaff(request), employeeService.findById(id), entity.getTermDate())).of(Case($(Boolean::booleanValue), delete(() -> Boolean.TRUE)),
                                                                               Case($(), unprocessableEntity("The employee cannot be termed! There are Employees or Staff Resources associated with this employee.")));
    }

    @GetMapping("/hr/api/v1/employee/{id}")
    public @ResponseBody ResponseEntity<Employee> findById(@PathVariable("id") Long id) {
        return find(() -> employeeService.findById(id));
    }

    @GetMapping("/hr/api/v1/employee/list")
    public @ResponseBody ResponseEntity<List<Employee>> list(Employee entity,
                                                             @RequestParam(name = "startHireDate", required = false)
                                                             @DateTimeFormat(pattern="yyyy-MM-dd") Date startHireDate,
                                                             @RequestParam(name = "endHireDate", required = false)
                                                             @DateTimeFormat(pattern="yyyy-MM-dd") Date endHireDate) {

        entity.setStatus(Staff.Status.ACTIVE);

        return list(() -> employeeService.list(entity, startHireDate, endHireDate));
    }

    @GetMapping("/hr/api/v1/employee/list-all")
    public @ResponseBody ResponseEntity<List<Employee>> listAll() {

        Employee entity = new Employee();
        entity.setStatus(Staff.Status.ACTIVE);

        return list(() -> employeeService.list(entity, null, null));
    }

}

