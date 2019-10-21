package com.warn.hr.business;

import com.warn.hr.persistence.dao.DepartmentDAO;
import com.warn.hr.persistence.entity.hr.Department;
import com.warn.hr.persistence.entity.hr.JobTitle;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static javaslang.API.$;
import static javaslang.API.Case;
import static javaslang.API.Match;

@Service
@Slf4j
@RequiredArgsConstructor
public class DepartmentService {

    @NonNull
    private DepartmentDAO departmentDAO;

    @NonNull
    private JobTitleService jobTitleService;

    @Transactional
    public Long insert(Department department) {
        return departmentDAO.insert(department);
    }

    @Transactional
    public Boolean update(Department department) {
        return departmentDAO.update(department);
    }

    @Transactional
    public Boolean delete(Department department) {
        return Match(validateExclusion(department)).of(Case($(l -> l.booleanValue()), () -> departmentDAO.delete(department)),
                                                       Case($(), () -> Boolean.FALSE));
    }

    public Department findById(Long id) {
        return departmentDAO.findById(id);
    }

    public List<Department> list(Department department) {
        return departmentDAO.list(department);
    }

    public List<Department> listAll() {
        return departmentDAO.listAll();
    }

    /*
     * Verifies if the department has job titles associated, in this case the department cannot be excluded
     */
    private Boolean validateExclusion(Department department) {

        JobTitle jobTitle = new JobTitle();
        jobTitle.setDepartment(department);

        return jobTitleService.list(jobTitle).isEmpty();

    }

}
