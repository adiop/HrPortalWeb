package com.warn.hr.persistence.dao;

import com.warn.hr.persistence.dao.common.HRAbstractDAO;
import com.warn.hr.persistence.entity.hr.StaffResource;
import com.warn.hr.persistence.entity.hr.staff.Employee;
import com.warn.hr.persistence.entity.hr.staff.common.Staff;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Repository
public class EmployeeDAO extends HRAbstractDAO<Employee> {

    @Autowired
    private StaffHistoryDAO staffHistoryDAO;

    @Override
    public Employee findById(Long id) {

        CriteriaBuilder criteriaBuilder = getCurrentSession().getCriteriaBuilder();

        CriteriaQuery<Employee> query = criteriaBuilder.createQuery(Employee.class);
        Root<Employee> root = query.from(Employee.class);

        query.select(root).where(criteriaBuilder.equal(root.get("id"), id));

        Employee employee =  getCurrentSession().createQuery(query).getSingleResult();

        Hibernate.initialize(employee.getJobTitle());
        Hibernate.initialize(employee.getManager());
        Hibernate.initialize(employee.getStaffResources());

        Optional.ofNullable(employee.getJobTitle().getDepartment()).ifPresent(d ->
                d.setJobTitles(null)
        );

        Optional.ofNullable(employee.getManager()).ifPresent(m ->
                m.getJobTitle().getDepartment().setJobTitles(null)
        );

        Optional.ofNullable(employee.getStaffResources()).ifPresent(srs ->
            srs.forEach(sr ->
                sr.setTechnician(null)
            )
        );

        return employee;

    }

    public Employee findSimpleById(Long id) {

        CriteriaBuilder criteriaBuilder = getCurrentSession().getCriteriaBuilder();

        CriteriaQuery<Employee> query = criteriaBuilder.createQuery(Employee.class);
        Root<Employee> root = query.from(Employee.class);

        query.select(root).where(criteriaBuilder.equal(root.get("id"), id));

        Employee employee =  getCurrentSession().createQuery(query).getSingleResult();

        Optional.ofNullable(employee.getJobTitle().getDepartment()).ifPresent(d ->
            d.setJobTitles(null)
        );

        employee.setImage(null);
        employee.setManager(null);
        employee.setStaffResources(null);

        return employee;

    }

    @Transactional(value = "hibernateTransactionManagerHR", readOnly = true)
    public List<Employee> list(Employee employee) {
        return list(employee, null, null);
    }

    @Transactional(value = "hibernateTransactionManagerHR", readOnly = true)
    public List<Employee> list(Employee employee, Date startHireDate, Date endHireDate) {

        CriteriaBuilder criteriaBuilder = getCurrentSession().getCriteriaBuilder();

        CriteriaQuery<Employee> query = criteriaBuilder.createQuery(Employee.class);
        Root<Employee> root = query.from(Employee.class);

        List<Predicate> predicates = new LinkedList<>();

        // First name
        Optional.ofNullable(employee.getFirstName()).ifPresent((firstName) ->
            predicates.add(criteriaBuilder.like(root.get("firstName"), "%" + firstName + "%"))
        );

        // Preferred name
        Optional.ofNullable(employee.getPreferredName()).ifPresent((preferredName) ->
            predicates.add(criteriaBuilder.like(root.get("preferredName"), "%" + preferredName + "%"))
        );

        // Last name
        Optional.ofNullable(employee.getLastName()).ifPresent((name) ->
            predicates.add(criteriaBuilder.like(root.get("lastName"), "%" + name + "%"))
        );

        // Employee number
        Optional.ofNullable(employee.getEmployeeNumber()).ifPresent((employeeNumber) ->
            predicates.add(criteriaBuilder.equal(root.get("employeeNumber"), employeeNumber))
        );

        //Team leaders
        Optional.ofNullable(employee.getTeamLeader()).ifPresent((teamLeader) ->
            predicates.add(criteriaBuilder.equal(root.get("teamLeader"), teamLeader))
        );

        //Manager
        Optional.ofNullable(employee.getManager()).ifPresent((manager) ->
            predicates.add(criteriaBuilder.equal(root.get("manager").get("id"), manager.getId()))
        );

        // Job title
        Optional.ofNullable(employee.getJobTitle()).ifPresent((jobTitle) ->
            predicates.add(criteriaBuilder.equal(root.get("jobTitle").get("id"), jobTitle.getId()))
        );

        // Hire date clause
        Optional.ofNullable(startHireDate).ifPresent(startDate ->
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("hiringDate"), startDate))
        );

        Optional.ofNullable(endHireDate).ifPresent(endDate ->
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("hiringDate"), endDate))
        );

        // Status
        Optional.ofNullable(employee.getStatus()).ifPresent((status) ->
            predicates.add(criteriaBuilder.equal(root.get("status"), status))
        );

        // Add all the predicates in the list to the where clause
        Optional.ofNullable(predicates).filter((p) -> p.size() > 0).ifPresent((p) ->
            query.where(criteriaBuilder.and(p.toArray(new Predicate[p.size()])))
        );

        List<Employee> employees = getCurrentSession().createQuery(query).getResultList();

        employees.parallelStream().forEach(e -> {
            e.setImage(null);
            e.setManager(null);
            e.setStaffResources(null);
            e.getJobTitle().getDepartment().setJobTitles(null);
        });

        return employees;

    }

    @Override
    public Long insert(Employee entity) {
        return super.insert(entity);
    }

    @Override
    public Boolean update(Employee entity) {

        staffHistoryDAO.createStaffHistory(entity.getId());

        return super.merge(entity);
    }

    public Boolean term(Employee entity, Date date) {

        CriteriaBuilder criteriaBuilder = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Employee> query = criteriaBuilder.createQuery(Employee.class);
        Root<Employee> root = query.from(Employee.class);

        query.select(root).where(criteriaBuilder.equal(root.get("id"), entity.getId()));

        Employee employeeToTerminate =  getCurrentSession().createQuery(query).getSingleResult();
        employeeToTerminate.setTermDate(date);

        return update(employeeToTerminate);
    }

}
