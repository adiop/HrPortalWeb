package com.warn.hr.persistence.dao;

import com.warn.hr.business.exception.InvalidFileException;
import com.warn.hr.persistence.entity.hr.Department;
import com.warn.hr.persistence.entity.hr.Position;
import com.warn.hr.persistence.entity.hr.staff.Employee;
import com.warn.hr.persistence.entity.hr.staff.Manager;

import com.warn.hr.persistence.entity.hr.staff.adp.ADPEmployee;
import com.warn.hr.persistence.entity.hr.staff.common.Staff;
import com.warn.hr.persistence.utils.ExcelUtils;
import javaslang.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class ADPEmployeeDAO {

    private static final String CACHE_NAME = "adp_report_cache";

    private static final String CACHE_KEY_ADP_EMPLOYEE_LIST = "adpEmployees";

    private static final String CACHE_KEY_EMPLOYEE_LIST = "employees";

    private static final String CACHE_KEY_MANAGER_LIST = "managers";

    private static final String CACHE_KEY_DEPARTMENT_LIST = "departments";

    private static final String CACHE_KEY_JOB_TITLE_LIST = "jobTitles";

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private CacheManager cacheManager;

    public Boolean insertAdpFile(byte[] fileContent) {
        addEmployeeListToCache(fileContent);
        return Boolean.TRUE;
    }

    @Transactional(readOnly = true)
    public List<ADPEmployee> listInsertedEmployees() {

        initMaps();

        List<ADPEmployee> employeeNumberFileList = getEmployeeListFromCache()
                .parallelStream()
                .filter(e -> e.getIsValid())
                .filter(e -> Objects.isNull(getEmployee(e.getEmployeeNumber())))
                .map(e -> {

                    e.setDepartment(getDepartment(e.getDepartment().getName()));
                    e.setJobTitle(getPosition(e.getJobTitle().getName(), e.getDepartment()));
                    e.setManager(getManager(e.getManager().getFirstName(), e.getManager().getLastName()));

                    return e;

                })
                .collect(Collectors.toList());

        return employeeNumberFileList;

    }

    @Transactional(readOnly = true)
    public List<ADPEmployee> listUpdatedEmployees() {

        initMaps();

        List<ADPEmployee> updatedEmployeeList = getEmployeeListFromCache()
                .parallelStream()
                .filter(e -> e.getIsValid())
                .filter(e -> Objects.nonNull(getEmployee(e.getEmployeeNumber())))
                .map(e -> {

                    Employee employee = getEmployee(e.getEmployeeNumber());

                    e.setImage(employee.getImage());
                    e.setEmployee(employee);
                    e.setDepartment(getDepartment(e.getDepartment().getName()));
                    e.setJobTitle(getPosition(e.getJobTitle().getName(), e.getDepartment()));
                    e.setManager(getManager(e.getManager().getFirstName(), e.getManager().getLastName()));

                    return e;

                })
                .filter(e -> e.getHasChanges())
                .collect(Collectors.toList());

        return updatedEmployeeList;
    }

    @Transactional(readOnly = true)
    public List<ADPEmployee> listRemovedEmployees() {

        Set<String> fileEmployeeNumberSet = getEmployeeListFromCache()
                .parallelStream()
                .filter(e -> e.getIsValid())
                .map(ADPEmployee::getEmployeeNumber)
                .collect(Collectors.toSet());

        List<ADPEmployee> employees = ((Map<String, Employee>) getCache().get(CACHE_KEY_EMPLOYEE_LIST).get())
                .values()
                .parallelStream()
                .filter(e -> !fileEmployeeNumberSet.contains(e.getEmployeeNumber()))
                .map(e -> {

                    ADPEmployee employee = ADPEmployee.newInstance();

                    employee.setImage(e.getImage());
                    employee.setEmployeeNumber(e.getEmployeeNumber());
                    employee.setFirstName(e.getFirstName());
                    employee.setPreferredName(e.getPreferredName());
                    employee.setLastName(e.getLastName());
                    employee.setShift(ADPEmployee.Shift.valueOf(e.getShift().name()));
                    employee.setManager(e.getManager());
                    employee.setDepartment(e.getJobTitle().getDepartment());
                    employee.setJobTitle(e.getJobTitle());

                    return employee;

                })
                .collect(Collectors.toList());

        return employees;

    }

    public List<ADPEmployee> listErrorEmployees() {

        List<ADPEmployee> errorEmployeeList = getEmployeeListFromCache()
                .parallelStream()
                .filter(e -> !e.getIsValid())
                .collect(Collectors.toList());

        return errorEmployeeList;
    }

    private void addEmployeeListToCache(byte[] fileContent) {

        // Clear the whole cache
        getCache().clear();

        // Add the file to the cache
        getCache().put(CACHE_KEY_ADP_EMPLOYEE_LIST, convertFileToEmployeeList(fileContent));

    }

    private List<ADPEmployee> getEmployeeListFromCache() {
        return (List<ADPEmployee>) getCache().get(CACHE_KEY_ADP_EMPLOYEE_LIST).get();
    }

    private List<ADPEmployee> convertFileToEmployeeList(byte[] fileContent) {

        return Try.of(() ->{
            return file2ADPEmployee(fileContent).parallelStream()
                    .filter(e -> Objects.nonNull(e.getEmployeeNumber()) && !e.getEmployeeNumber().isEmpty())
                    .collect(Collectors.toList());
        })
        .onFailure((e) -> log.error("An error has occurred while trying to read the file", e))
        .getOrElse(() -> new ArrayList<>());

    }

    private static List<ADPEmployee> file2ADPEmployee(byte[] file) throws InvalidFileException {

        List<ADPEmployee> adpEmployees = null;

        try {

            adpEmployees = ExcelUtils
                    .readFileStream(file)
                    .skip(1)
                    .map(row -> {

                ADPEmployee adpEmployee = ADPEmployee.newInstance();

                adpEmployee.setEmployeeNumber(row.getCell(0).getStringCellValue());
                adpEmployee.setFirstName(row.getCell(2).getStringCellValue());
                adpEmployee.setPreferredName(row.getCell(3).getStringCellValue());
                adpEmployee.setLastName(row.getCell(1).getStringCellValue());

                try {

                    adpEmployee.setShift(ADPEmployee.Shift.valueOfADP(row.getCell(6).getStringCellValue()));

                    Manager manager = new Manager();
                    manager.setFirstName(row.getCell(10).getStringCellValue());
                    manager.setLastName(row.getCell(11).getStringCellValue());

                    adpEmployee.setManager(manager);

                    Department department = new Department();
                    department.setName(row.getCell(9).getStringCellValue());

                    adpEmployee.setDepartment(department);

                    Position jobTitle = new Position();
                    jobTitle.setName(row.getCell(4).getStringCellValue());

                    adpEmployee.setJobTitle(jobTitle);

                } catch (Exception e) {
                    adpEmployee.setIsValid(Boolean.FALSE);
                }

                return adpEmployee;

            }).collect(Collectors.toList());

        } catch (Exception e) {
            throw new InvalidFileException(e);
        }

        return adpEmployees;

    }

    private Employee getEmployee(String employeeNumber) {
        return ((Map<String, Employee>) getCache().get(CACHE_KEY_EMPLOYEE_LIST).get()).get(employeeNumber);
    }

    private Manager getManager(String firstName, String lastName) {

        Map<String, List<Manager>> managerMap = ((Map<String, List<Manager>>) getCache().get(CACHE_KEY_MANAGER_LIST).get());

        Supplier<Manager> managerSupplier = () -> {

            Manager manager = new Manager();
            manager.setFirstName(firstName);
            manager.setPreferredName(firstName);
            manager.setLastName(lastName);

            return manager;

        };

        return Optional.ofNullable(managerMap.get(lastName.toUpperCase()))
                .orElseGet(() -> new LinkedList<>())
                .stream()
                .filter(m -> m.getFirstName().toUpperCase().startsWith(firstName.toUpperCase()))
                .findFirst()
                .orElseGet(managerSupplier);

    }

    private Department getDepartment(String name) {
        Integer cacheKey = new StringBuilder().append(name).toString().toUpperCase().hashCode();
        return Optional.ofNullable(((Map<Integer, Department>) getCache().get(CACHE_KEY_DEPARTMENT_LIST).get()).get(cacheKey)).orElseGet(() -> {

            Department department = new Department();
            department.setName(name);

            return department;

        });
    }

    private Position getPosition(String name, Department department) {
        Integer cacheKey = new StringBuilder().append(name).append(department.getName()).toString().toUpperCase().hashCode();
        return Optional.ofNullable(((Map<Integer, Position>) getCache().get(CACHE_KEY_JOB_TITLE_LIST).get()).get(cacheKey)).orElseGet(() -> {

            Position position = new Position();
            position.setName(name);

            return position;

        });
    }

    @Transactional(readOnly = true)
    protected void initMaps() {

        // Init employee and manager map
        {

            Map<String, Employee> employeeMap = new HashMap<>();

            CriteriaBuilder criteriaBuilder = sessionFactory.getCurrentSession().getCriteriaBuilder();
            CriteriaQuery<Employee> query = criteriaBuilder.createQuery(Employee.class);
            Root<Employee> root = query.from(Employee.class);
            query.select(root).where(criteriaBuilder.equal(root.get("status"), Employee.Status.ACTIVE));

            List<Employee> employees = sessionFactory.getCurrentSession().createQuery(query).getResultList();

            // Employee map
            employees.forEach(employee -> {

                Hibernate.initialize(employee.getJobTitle());
                Hibernate.initialize(employee.getManager());

                employee.setStaffResources(null);

                String mapKey = employee.getEmployeeNumber();

                employeeMap.put(mapKey, employee);

            });

            getCache().put(CACHE_KEY_EMPLOYEE_LIST, employeeMap);

            // Manager map
            Map<String, List<Manager>> managerMap = employees.stream().map(e -> {

                Manager manager = new Manager();
                manager.setId(e.getId());
                manager.setFirstName(e.getFirstName());
                manager.setPreferredName(e.getPreferredName());
                manager.setLastName(e.getLastName());
                manager.setJobTitle(e.getJobTitle());
                manager.setEmail(e.getEmail());
                manager.setPhone(e.getPhone());

                return manager;

            }).collect(Collectors.groupingBy((m) -> m.getLastName().toUpperCase()));

            getCache().put(CACHE_KEY_MANAGER_LIST, managerMap);

        }

        // Init department map
        {

            Map<Integer, Department> departmentMap = new HashMap<>();

            CriteriaBuilder criteriaBuilder = sessionFactory.getCurrentSession().getCriteriaBuilder();
            CriteriaQuery<Department> query = criteriaBuilder.createQuery(Department.class);
            Root<Department> root = query.from(Department.class);

            List<Department> departments = sessionFactory.getCurrentSession().createQuery(query).getResultList();

            departments.forEach(department -> {

                department.setJobTitles(null);

                Integer mapKey = department.getName().toUpperCase().hashCode();

                departmentMap.put(mapKey, department);

            });

            getCache().put(CACHE_KEY_DEPARTMENT_LIST, departmentMap);

        }

        // Init job title map
        {
            Map<Integer, Position> positionMap = new HashMap<>();

            CriteriaBuilder criteriaBuilder = sessionFactory.getCurrentSession().getCriteriaBuilder();
            CriteriaQuery<Position> query = criteriaBuilder.createQuery(Position.class);
            Root<Position> root = query.from(Position.class);

            List<Position> positions = sessionFactory.getCurrentSession().createQuery(query).getResultList();

            positions.forEach(position -> {

                Integer mapKey = new StringBuilder().append(position.getName()).append(position.getDepartment().getName()).toString().toUpperCase().hashCode();

                positionMap.put(mapKey, position);

            });

            getCache().put(CACHE_KEY_JOB_TITLE_LIST, positionMap);

        }

    }

    private Cache getCache() {
        return cacheManager.getCache(CACHE_NAME);
    }

}
