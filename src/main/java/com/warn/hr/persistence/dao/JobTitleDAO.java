package com.warn.hr.persistence.dao;

import com.warn.hr.persistence.dao.common.HRAbstractDAO;
import com.warn.hr.persistence.entity.hr.JobTitle;
import com.warn.hr.persistence.entity.hr.StaffResource;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class JobTitleDAO extends HRAbstractDAO<JobTitle> {

    @Override
    @Transactional(readOnly = true)
    public JobTitle findById(Long id) {

        CriteriaBuilder criteriaBuilder = getCurrentSession().getCriteriaBuilder();

        CriteriaQuery<JobTitle> query = criteriaBuilder.createQuery(JobTitle.class);
        Root<JobTitle> root = query.from(JobTitle.class);

        query.select(root).where(criteriaBuilder.equal(root.get("id"), id));

        JobTitle jobTitle =  getCurrentSession().createQuery(query).getSingleResult();

        Hibernate.initialize(jobTitle.getDepartment());
        Hibernate.initialize(jobTitle.getStaffResources());

        jobTitle.getDepartment().setJobTitles(null);
        jobTitle.getStaffResources().forEach(sr ->
            sr.setTechnician(null)
        );

        return jobTitle;

    }

    @Transactional(readOnly = true)
    public List<JobTitle> list(JobTitle jobTitle) {

        CriteriaBuilder criteriaBuilder = getCurrentSession().getCriteriaBuilder();

        CriteriaQuery<JobTitle> query = criteriaBuilder.createQuery(JobTitle.class);
        Root<JobTitle> root = query.from(JobTitle.class);

        List<Predicate> predicates = new LinkedList<>();

        Optional.ofNullable(jobTitle.getName()).ifPresent((name) ->
            predicates.add(criteriaBuilder.like(root.get("name"), "%" + name + "%"))
        );

        Optional.ofNullable(jobTitle.getDepartment()).ifPresent((department) ->
            predicates.add(criteriaBuilder.equal(root.get("department").get("id"), department.getId()))
        );

        Optional.ofNullable(jobTitle.getStaffResources()).filter(staffResources -> !staffResources.isEmpty()).ifPresent((staffResourceList) -> {

            CriteriaBuilder.In assetsIn = criteriaBuilder.in(root.join("staffResources").get("id"));

            staffResourceList.forEach(staffResource ->
                assetsIn.value(staffResource.getId())
            );

            predicates.add(assetsIn);

        });

        Optional.ofNullable(predicates).filter((p) -> p.size() > 0).ifPresent((p) ->
            query.where(criteriaBuilder.and(p.toArray(new Predicate[p.size()])))
        );

        List<JobTitle> jobTitles = getCurrentSession().createQuery(query).getResultList();

        jobTitles.parallelStream().forEach(j -> {
            Hibernate.initialize(j.getDepartment());
            j.getDepartment().setJobTitles(null);
            j.setStaffResources(null);
        });

        return jobTitles;

    }

    @Override
    @Transactional(readOnly = true)
    public List<JobTitle> listAll() {

        List<JobTitle> jobTitles = super.listAll();

        jobTitles.parallelStream().forEach(j -> {
            Hibernate.initialize(j.getDepartment());
            j.setStaffResources(null);
        });

        return jobTitles;
    }
}
