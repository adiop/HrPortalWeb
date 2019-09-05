package com.warn.hr.persistence.dao;

import com.warn.hr.persistence.dao.common.HRAbstractDAO;
import com.warn.hr.persistence.entity.hr.StaffResource;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Repository
public class StaffResourceDAO extends HRAbstractDAO<StaffResource> {

    @Override
    @Transactional(readOnly = true)
    public StaffResource findById(Long id) {

        CriteriaBuilder criteriaBuilder = getCurrentSession().getCriteriaBuilder();

        CriteriaQuery<StaffResource> query = criteriaBuilder.createQuery(StaffResource.class);
        Root<StaffResource> root = query.from(StaffResource.class);

        query.select(root).where(criteriaBuilder.equal(root.get("id"), id));

        StaffResource staffResource =  getCurrentSession().createQuery(query).getSingleResult();

        Hibernate.initialize(staffResource.getTechnician());

        Optional.ofNullable(staffResource.getTechnician()).ifPresent(t -> {
            t.getJobTitle().getDepartment().setJobTitles(null);
        });

        return staffResource;

    }

    @Transactional(readOnly = true)
    public List<StaffResource> list(StaffResource staffResource) {

        CriteriaBuilder criteriaBuilder = getCurrentSession().getCriteriaBuilder();

        CriteriaQuery<StaffResource> query = criteriaBuilder.createQuery(StaffResource.class);
        Root<StaffResource> root = query.from(StaffResource.class);

        List<Predicate> predicates = new LinkedList<>();

        Optional.ofNullable(staffResource.getName()).ifPresent(name ->
            predicates.add(criteriaBuilder.like(root.get("name"), "%" + name + "%"))
        );

        Optional.ofNullable(staffResource.getType()).ifPresent(type ->
            predicates.add(criteriaBuilder.equal(root.get("type"), type))
        );

        Optional.ofNullable(staffResource.getTechnician()).ifPresent(technician ->
            predicates.add(criteriaBuilder.equal(root.get("technician").get("id"), technician.getId()))
        );

        Optional.ofNullable(staffResource.getIsEmployeeDefault()).ifPresent(isEmployeeDefault ->
            predicates.add(criteriaBuilder.equal(root.get("isEmployeeDefault"), isEmployeeDefault))
        );

        Optional.ofNullable(staffResource.getIsTeamLeaderDefault()).ifPresent(isTeamLeaderDefault ->
            predicates.add(criteriaBuilder.equal(root.get("isTeamLeaderDefault"), isTeamLeaderDefault))
        );

        // Add all the predicates in the list to the where clause
        Optional.ofNullable(predicates).filter((p) -> p.size() > 0).ifPresent((p) ->
            query.where(criteriaBuilder.and(p.toArray(new Predicate[p.size()])))
        );

        List<StaffResource> staffResources = getCurrentSession().createQuery(query).getResultList();

        staffResources.parallelStream().forEach(s ->
           s.setTechnician(null)
        );

        return staffResources;

    }

    @Override
    public List<StaffResource> listAll() {

        List<StaffResource> staffResources = super.listAll();

        staffResources.parallelStream().forEach(s ->
            s.setTechnician(null)
        );

        return staffResources;

    }
}
