package com.warn.hr.persistence.dao;

import com.warn.hr.persistence.dao.common.HRAbstractDAO;
import com.warn.hr.persistence.entity.hr.staff.Temporary;
import com.warn.hr.persistence.entity.hr.staff.common.Staff;
import org.hibernate.Hibernate;
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
public class TemporaryDAO extends HRAbstractDAO<Temporary> {

    @Autowired
    private StaffHistoryDAO staffHistoryDAO;

    @Override
    public Temporary findById(Long id) {

        CriteriaBuilder criteriaBuilder = getCurrentSession().getCriteriaBuilder();

        CriteriaQuery<Temporary> query = criteriaBuilder.createQuery(Temporary.class);
        Root<Temporary> root = query.from(Temporary.class);

        query.select(root).where(criteriaBuilder.equal(root.get("id"), id));

        Temporary temporary =  getCurrentSession().createQuery(query).getSingleResult();

        Hibernate.initialize(temporary.getJobTitle());
        Hibernate.initialize(temporary.getManager());
        Hibernate.initialize(temporary.getStaffResources());

        temporary.getJobTitle().getDepartment().setJobTitles(null);

        Optional.ofNullable(temporary.getManager()).ifPresent(m ->
            m.getJobTitle().getDepartment().setJobTitles(null)
        );

        Optional.ofNullable(temporary.getStaffResources()).ifPresent(srs ->
            srs.forEach(sr ->
                sr.setTechnician(null)
            )
        );

        return temporary;

    }

    public Temporary findSimpleById(Long id) {

        CriteriaBuilder criteriaBuilder = getCurrentSession().getCriteriaBuilder();

        CriteriaQuery<Temporary> query = criteriaBuilder.createQuery(Temporary.class);
        Root<Temporary> root = query.from(Temporary.class);

        query.select(root).where(criteriaBuilder.equal(root.get("id"), id));

        Temporary temporary =  getCurrentSession().createQuery(query).getSingleResult();

        temporary.getJobTitle().getDepartment().setJobTitles(null);

        temporary.setImage(null);
        temporary.setManager(null);
        temporary.setStaffResources(null);

        return temporary;

    }

    @Transactional(readOnly = true)
    public List<Temporary> list(Temporary temporary) {
        return list(temporary, null, null);
    }

    @Transactional(readOnly = true)
    public List<Temporary> list(Temporary temporary, Date startHireDate, Date endHireDate) {

        CriteriaBuilder criteriaBuilder = getCurrentSession().getCriteriaBuilder();

        CriteriaQuery<Temporary> query = criteriaBuilder.createQuery(Temporary.class);
        Root<Temporary> root = query.from(Temporary.class);

        List<Predicate> predicates = new LinkedList<>();

        // First name
        Optional.ofNullable(temporary.getFirstName()).ifPresent((firstName) ->
                predicates.add(criteriaBuilder.like(root.get("firstName"), "%" + firstName + "%"))
        );

        // Preferred name
        Optional.ofNullable(temporary.getPreferredName()).ifPresent((preferredName) ->
                predicates.add(criteriaBuilder.like(root.get("preferredName"), "%" + preferredName + "%"))
        );

        // Last name
        Optional.ofNullable(temporary.getLastName()).ifPresent((name) ->
                predicates.add(criteriaBuilder.like(root.get("lastName"), "%" + name + "%"))
        );

        //Manager
        Optional.ofNullable(temporary.getManager()).ifPresent((manager) ->
                predicates.add(criteriaBuilder.equal(root.get("manager").get("id"), manager.getId()))
        );

        // Job title
        Optional.ofNullable(temporary.getJobTitle()).ifPresent((jobTitle) ->
                predicates.add(criteriaBuilder.equal(root.get("jobTitle").get("id"), jobTitle.getId()))
        );

        // Hire date clause
        Optional.ofNullable(startHireDate).ifPresent(startDate ->
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("contractStartDate"), startDate))
        );

        Optional.ofNullable(endHireDate).ifPresent(endDate ->
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("contractStartDate"), endDate))
        );

        // Status
        Optional.ofNullable(temporary.getStatus()).ifPresent((status) ->
                predicates.add(criteriaBuilder.equal(root.get("status"), status))
        );

        // Add all the predicates in the list to the where clause
        Optional.ofNullable(predicates).filter((p) -> p.size() > 0).ifPresent((p) ->
                query.where(criteriaBuilder.and(p.toArray(new Predicate[p.size()])))
        );

        List<Temporary> temporaries = getCurrentSession().createQuery(query).getResultList();

        temporaries.parallelStream().forEach(t -> {
            t.setImage(null);
            t.setManager(null);
            t.setStaffResources(null);
            t.getJobTitle().getDepartment().setJobTitles(null);
        });

        return temporaries;

    }

    @Transactional(readOnly = true)
    public List<Temporary> listExpiringTemporaries() {

        CriteriaBuilder criteriaBuilder = getCurrentSession().getCriteriaBuilder();

        CriteriaQuery<Temporary> query = criteriaBuilder.createQuery(Temporary.class);
        Root<Temporary> root = query.from(Temporary.class);

        List<Predicate> predicates = new LinkedList<>();

        predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("contractEndDate"), new Date(System.currentTimeMillis() + 10 * 60 * 60 * 24 * 1000)));

        // Status
        predicates.add(criteriaBuilder.equal(root.get("status"), Staff.Status.ACTIVE));


        // Add all the predicates in the list to the where clause
        Optional.ofNullable(predicates).filter((p) -> p.size() > 0).ifPresent((p) ->
                query.where(criteriaBuilder.and(p.toArray(new Predicate[p.size()])))
        );

        List<Temporary> temporaries = getCurrentSession().createQuery(query).getResultList();

        temporaries.parallelStream().forEach(t -> {
            t.setImage(null);
            t.setManager(null);
            t.setStaffResources(null);
            t.getJobTitle().getDepartment().setJobTitles(null);
        });

        return temporaries;

    }

    @Override
    @Transactional(value = "hibernateTransactionManagerHR")
    public Long insert(Temporary entity) {
        return super.insert(entity);
    }

    @Override
    @Transactional(value = "hibernateTransactionManagerHR")
    public Boolean update(Temporary entity) {

        staffHistoryDAO.createStaffHistory(entity.getId());

        return super.merge(entity);

    }

    public Boolean term(Temporary entity, Date date) {

        CriteriaBuilder criteriaBuilder = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Temporary> query = criteriaBuilder.createQuery(Temporary.class);
        Root<Temporary> root = query.from(Temporary.class);

        query.select(root).where(criteriaBuilder.equal(root.get("id"), entity.getId()));

        Temporary temporaryToTerminate =  getCurrentSession().createQuery(query).getSingleResult();
        temporaryToTerminate.setContractEndDate(date);

        return update(temporaryToTerminate);
    }

}
