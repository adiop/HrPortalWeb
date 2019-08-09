package com.warn.hr.persistence.dao;

import com.warn.hr.persistence.dao.common.HRAbstractDAO;
import com.warn.hr.persistence.entity.hr.Department;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Repository
public class DepartmentDAO extends HRAbstractDAO<Department> {

    @Override
    @Transactional(value = "hibernateTransactionManagerHR", readOnly = true)
    public Department findById(Long id) {

        CriteriaBuilder criteriaBuilder = getCurrentSession().getCriteriaBuilder();

        CriteriaQuery<Department> query = criteriaBuilder.createQuery(Department.class);
        Root<Department> root = query.from(Department.class);

        query.select(root).where(criteriaBuilder.equal(root.get("id"), id));

        Department Department =  getCurrentSession().createQuery(query).getSingleResult();

        Hibernate.initialize(Department.getJobTitles());

        Department.getJobTitles().parallelStream().forEach(j ->
            j.setDepartment(null)
        );

        return Department;

    }

    @Transactional(value = "hibernateTransactionManagerHR", readOnly = true)
    public List<Department> list(Department department) {

        CriteriaBuilder criteriaBuilder = getCurrentSession().getCriteriaBuilder();

        CriteriaQuery<Department> query = criteriaBuilder.createQuery(Department.class);
        Root<Department> root = query.from(Department.class);

        Optional.ofNullable(department.getName()).ifPresent(name ->
            query.where(criteriaBuilder.like(root.get("name"), "%" + department.getName() + "%"))
        );

        Optional.ofNullable(department.getDepartmentCode()).ifPresent(departmentCode ->
            query.where(criteriaBuilder.equal(root.get("departmentCode"), departmentCode))
        );

        List<Department> departments = getCurrentSession().createQuery(query).getResultList();

        departments.parallelStream().forEach(d ->
            d.setJobTitles(null)
        );

        return departments;

    }

    @Override
    @Transactional(value = "hibernateTransactionManagerHR", readOnly = true)
    public List<Department> listAll() {

        List<Department> departments = super.listAll();

        departments.parallelStream().forEach(d ->
            d.setJobTitles(null)
        );

        return departments;

    }
}
