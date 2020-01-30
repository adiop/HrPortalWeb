package com.warn.hr.persistence.dao;

import com.warn.hr.persistence.dao.common.HRAbstractDAO;
import com.warn.hr.persistence.entity.hr.staff.Manager;
import com.warn.hr.persistence.entity.hr.staff.common.Staff;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.hibernate.query.NativeQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class StaffDAO extends HRAbstractDAO<Staff> {

    @Override
    @Transactional(readOnly = true)
    public Staff findById(Long id) {

        CriteriaBuilder criteriaBuilder = getCurrentSession().getCriteriaBuilder();

        CriteriaQuery<Staff> query = criteriaBuilder.createQuery(Staff.class);
        Root<Staff> root = query.from(Staff.class);

        query.select(root).where(criteriaBuilder.equal(root.get("id"), id));

        Staff staff =  getCurrentSession().createQuery(query).getSingleResult();

        Hibernate.initialize(staff.getJobTitle());
        Hibernate.initialize(staff.getManager());
        Hibernate.initialize(staff.getStaffResources());

        Optional.ofNullable(staff.getJobTitle().getDepartment()).ifPresent(d ->
            d.setJobTitles(null)
        );

        Optional.ofNullable(staff.getManager()).ifPresent(m ->
            Optional.ofNullable(m.getJobTitle()).ifPresent(j ->
                Optional.ofNullable(j.getDepartment()).ifPresent(d ->
                    d.setJobTitles(null)
                )
            )
        );

        Optional.ofNullable(staff.getStaffResources()).ifPresent(srs ->
            srs.forEach(sr ->
                sr.setTechnician(null)
            )
        );

        return staff;

    }

    @Transactional(readOnly = true)
    public Staff findByLogin(String login) {

        Staff staff = null;

        try {

            login = login.replaceAll("WARNNT\\\\", "");

            CriteriaBuilder criteriaBuilder = getCurrentSession().getCriteriaBuilder();

            CriteriaQuery<Staff> query = criteriaBuilder.createQuery(Staff.class);
            Root<Staff> root = query.from(Staff.class);

            query.where(criteriaBuilder.equal(root.get("login"), login));

            staff = getCurrentSession().createQuery(query).getSingleResult();

            Hibernate.initialize(staff.getJobTitle());
            Hibernate.initialize(staff.getManager());

            staff.setImage(null);
            staff.setStaffResources(null);

            Optional.ofNullable(staff.getJobTitle().getDepartment()).ifPresent(d ->
                    d.setJobTitles(null)
            );

            Optional.ofNullable(staff.getManager()).ifPresent(m -> {
                m.setImage(null);
                Optional.ofNullable(m.getJobTitle().getDepartment()).ifPresent(d -> {
                    d.setJobTitles(null);
                });
            });

        } catch (Exception e) {
            log.error("An error has occurred while trying to find the staff by the login " + login, e);
        }

        return staff;

    }

    @Transactional(readOnly = true)
    public Staff findByStaffNumber(String staffNumber) {

        Staff staff = null;

        try {

            CriteriaBuilder criteriaBuilder = getCurrentSession().getCriteriaBuilder();

            CriteriaQuery<Staff> query = criteriaBuilder.createQuery(Staff.class);
            Root<Staff> root = query.from(Staff.class);

            query.where(criteriaBuilder.equal(root.get("staffNumber"), staffNumber),
                        criteriaBuilder.equal(root.get("status"), Staff.Status.ACTIVE));

            staff = getCurrentSession().createQuery(query).list().get(0);

            Hibernate.initialize(staff.getJobTitle());
            Hibernate.initialize(staff.getManager());

            staff.setImage(null);
            staff.setStaffResources(null);

            Optional.ofNullable(staff.getJobTitle().getDepartment()).ifPresent(d ->
                    d.setJobTitles(null)
            );

            Optional.ofNullable(staff.getManager()).ifPresent(m -> {
                m.setImage(null);
                Optional.ofNullable(m.getJobTitle().getDepartment()).ifPresent(d -> {
                    d.setJobTitles(null);
                });
            });

        } catch (Exception e) {
            log.error("An error has occurred while trying to find the staff by the staff number " + staffNumber, e);
        }

        return staff;

    }

    @Transactional(readOnly = true)
    public String findImageByStaffId(Long id) {
        return findById(id).getImage();
    }

    @Transactional(readOnly = true)
    public Boolean isManager(Long id) {

        Manager manager = new Manager();
        manager.setId(id);

        Staff staffQuery = new Staff();
        staffQuery.setManager(manager);

        return !list(staffQuery).isEmpty();

    }

    @Override
    @Transactional(readOnly = true)
    public List<Staff> list(Staff staff) {

        CriteriaBuilder criteriaBuilder = getCurrentSession().getCriteriaBuilder();

        CriteriaQuery<Staff> query = criteriaBuilder.createQuery(Staff.class);
        Root<Staff> root = query.from(Staff.class);

        List<Predicate> predicates = new LinkedList<>();

        // First name
        Optional.ofNullable(staff.getFirstName()).ifPresent((firstName) ->
            predicates.add(criteriaBuilder.like(root.get("firstName"), "%" + firstName + "%"))
        );

        // Preferred name
        Optional.ofNullable(staff.getPreferredName()).ifPresent((preferredName) ->
            predicates.add(criteriaBuilder.like(root.get("preferredName"), "%" + preferredName + "%"))
        );

        // Last name
        Optional.ofNullable(staff.getLastName()).ifPresent((name) ->
            predicates.add(criteriaBuilder.like(root.get("lastName"), "%" + name + "%"))
        );

        //Manager
        Optional.ofNullable(staff.getManager()).ifPresent((manager) ->
            predicates.add(criteriaBuilder.equal(root.get("manager").get("id"), manager.getId()))
        );

        // Job title
        Optional.ofNullable(staff.getJobTitle()).ifPresent((jobTitle) ->
            predicates.add(criteriaBuilder.equal(root.get("jobTitle").get("id"), jobTitle.getId()))
        );

        // Staff Resources
        Optional.ofNullable(staff.getStaffResources()).filter(staffResources -> !staffResources.isEmpty()).ifPresent((staffResourceList) -> {

            CriteriaBuilder.In assetsIn = criteriaBuilder.in(root.join("staffResources").get("id"));
            staffResourceList.forEach(staffResource -> {
                assetsIn.value(staffResource.getId());
            });

            predicates.add(assetsIn);

        });

        // Status
        Optional.ofNullable(staff.getStatus()).ifPresent((status) ->
            predicates.add(criteriaBuilder.equal(root.get("status"), status))
        );

        // Add all the predicates in the list to the where clause
        Optional.ofNullable(predicates).filter((p) -> p.size() > 0).ifPresent((p) ->
            query.where(criteriaBuilder.and(p.toArray(new Predicate[p.size()])))
        );

        List<Staff> staffs = getCurrentSession().createQuery(query).getResultList();

        staffs.parallelStream().forEach(s -> {
            s.setImage(null);
            s.setManager(null);
            s.setStaffResources(null);
            s.getJobTitle().getDepartment().setJobTitles(null);
        });

        return staffs;
        
    }

    @Override
    @Transactional(readOnly = true)
    public List<Staff> listAll() {

        List<Staff> staffList = super.listAll();

        staffList.parallelStream().forEach(s -> {
            s.setImage(null);
            s.setManager(null);
            s.setStaffResources(null);
            s.getJobTitle().getDepartment().setJobTitles(null);
        });

        return staffList;

    }

}
