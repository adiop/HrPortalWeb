package com.warn.hr.business;

import com.warn.hr.persistence.dao.EmployeeDAO;
import com.warn.hr.persistence.dao.JobTitleDAO;
import com.warn.hr.persistence.entity.hr.staff.Employee;
import com.warn.hr.persistence.entity.hr.JobTitle;
import com.warn.hr.persistence.entity.hr.Position;
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
public class JobTitleService {

    @NonNull
    private JobTitleDAO jobTitleDAO;

    @NonNull
    private EmployeeDAO employeeDAO;

    @Transactional
    public Long insert(JobTitle jobTitle) {
        return jobTitleDAO.insert(jobTitle);
    }

    @Transactional
    public Boolean update(JobTitle jobTitle) {
        return jobTitleDAO.update(jobTitle);
    }

    @Transactional
    public Boolean delete(JobTitle jobTitle) {
        return Match(validateExclusion(jobTitle)).of(Case($(Boolean::booleanValue), () -> jobTitleDAO.delete(jobTitle)),
                                                     Case($(), () -> Boolean.FALSE));
    }

    public JobTitle findById(Long id) {
        return jobTitleDAO.findById(id);
    }

    public List<JobTitle> list(JobTitle jobTitle) {
        return jobTitleDAO.list(jobTitle);
    }

    public List<JobTitle> listAll() {
        return jobTitleDAO.listAll();
    }

    /*
     * Verifies if the job title has no employees associated, in this case the job title cannot be excluded
     */
    private Boolean validateExclusion(JobTitle jobTitle) {

        Position position = new Position();
        position.setId(jobTitle.getId());

        Employee employee = new Employee();
        employee.setJobTitle(position);

        return employeeDAO.list(employee).isEmpty();

    }

}
