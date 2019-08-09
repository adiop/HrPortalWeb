package com.warn.hr.web.rest;

import com.warn.hr.business.DepartmentService;
import com.warn.hr.persistence.dao.DepartmentDAO;
import com.warn.hr.persistence.dao.JobTitleDAO;
import com.warn.hr.persistence.entity.hr.Department;
import com.warn.hr.persistence.entity.hr.JobTitle;
import com.warn.hr.web.rest.common.AbstractController;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static javaslang.API.*;

@Slf4j
@RestController
public class DepartmentController extends AbstractController {

    @Autowired
    private DepartmentService departmentService;

    @PostMapping("/hr/api/v1/department/")
    public @ResponseBody ResponseEntity create(@RequestBody Department entity) {
        return create(() -> String.format("/hr/api/v1/department/%d", departmentService.insert(entity)));
    }


    @PutMapping("/hr/api/v1/department/{id}")
    public @ResponseBody ResponseEntity update(@PathVariable("id") Long id, @RequestBody Department department) {
        department.setId(id);
        return update(() -> departmentService.update(department));
    }

    @DeleteMapping("/hr/api/v1/department/{id}")
    public @ResponseBody ResponseEntity delete(@PathVariable("id") Long id) {

        Department department = new Department();
        department.setId(id);

        return Match(departmentService.delete(department)).of(Case($(l -> l.booleanValue()), delete(() -> Boolean.TRUE)),
                                                              Case($(), unprocessableEntity("The department cannot be removed! There are Employees or Job Titles associated with this department.")));

    }

    @GetMapping("/hr/api/v1/department/{id}")
    public ResponseEntity<Department> findById(@PathVariable("id") Long id) {
        return find(() -> departmentService.findById(id));
    }

    @GetMapping("/hr/api/v1/department/list")
    public ResponseEntity<List<Department>> list(Department department){
        return list(() -> departmentService.list(department));
    }

    @GetMapping("/hr/api/v1/department/list-all")
    public ResponseEntity<List<Department>> listAll() {
        return list(() -> departmentService.list(new Department()));
    }

}
