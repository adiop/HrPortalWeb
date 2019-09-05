package com.warn.hr.business;

import com.warn.hr.persistence.dao.DepartmentDAO;
import com.warn.hr.persistence.entity.hr.Department;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class DepartmentServiceTest {

    @Mock
    private DepartmentDAO departmentDAO;

    @Mock
    private JobTitleService jobTitleService;

    @InjectMocks
    private DepartmentService departmentService;

    @Test
    public void insert() {

        Department department = new Department();
        department.setDepartmentCode("000");
        department.setName("Department name");

        departmentService.insert(department);
        departmentService.insert(department);
        departmentService.insert(department);
        departmentService.insert(department);

        System.out.println(departmentService.listAll());


    }

    @Test
    public void update() {
    }

    @Test
    public void delete() {
    }

    @Test
    public void findById() {
    }

    @Test
    public void list() {
    }

    @Test
    public void listAll() {
    }
}
