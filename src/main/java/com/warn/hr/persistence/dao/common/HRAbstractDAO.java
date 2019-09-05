package com.warn.hr.persistence.dao.common;

import com.warn.hr.persistence.entity.common.AbstractEntity;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.lang.reflect.ParameterizedType;
import java.util.Date;
import java.util.List;

@Slf4j
public abstract class HRAbstractDAO<Entity extends AbstractEntity> {

    @Autowired
    private SessionFactory sessionFactory;

    @Transactional(value = "hibernateTransactionManagerHR")
    public Long insert(Entity entity) {

        try {
            entity.setCreatedDate(new Date());
            entity.setUpdatedDate(new Date());
            Long id = (Long) getCurrentSession().save(entity);
            return id;
        } catch (Exception e){
            log.error("An execption has occurred", e);
            throw new RuntimeException("An execption has occurred", e);
        }

    }

    @Transactional(value = "hibernateTransactionManagerHR")
    public Boolean delete(Entity entity) {

        try {
            getCurrentSession().delete(entity);
            return Boolean.TRUE;
        } catch (Exception e){
            log.error("An execption has occurred", e);
            throw new RuntimeException("An execption has occurred", e);
        }

    }

    @Transactional(value = "hibernateTransactionManagerHR")
    public Boolean update(Entity entity) {

        try {
            entity.setUpdatedDate(new Date());
            getCurrentSession().update(entity);
            return Boolean.TRUE;
        } catch (Exception e){
            log.error("An execption has occurred", e);
            throw new RuntimeException("An execption has occurred", e);
        }

    }

    @Transactional(value = "hibernateTransactionManagerHR")
    public Boolean merge(Entity entity) {

        try {
            entity.setUpdatedDate(new Date());
            getCurrentSession().merge(entity);
            return Boolean.TRUE;
        } catch (Exception e){
            log.error("An execption has occurred", e);
            throw new RuntimeException("An execption has occurred", e);
        }

    }

    @Transactional(readOnly = true)
    public Entity findById(Long id)  {

        CriteriaBuilder criteriaBuilder = getCurrentSession().getCriteriaBuilder();

        CriteriaQuery<Entity> query = criteriaBuilder.createQuery(getGenericClass());
        Root<Entity> root = query.from(getGenericClass());

        query.select(root).where(criteriaBuilder.equal(root.get("id"), id));

        return getCurrentSession().createQuery(query).getSingleResult();

    }

    @Transactional(readOnly = true)
    public List<Entity> listAll() {

        CriteriaBuilder criteriaBuilder = getCurrentSession().getCriteriaBuilder();

        CriteriaQuery<Entity> query = criteriaBuilder.createQuery(getGenericClass());
        Root<Entity> root = query.from(getGenericClass());

        return getCurrentSession().createQuery(query).getResultList();

    }

    @Transactional(readOnly = true)
    public List<Entity> list(Entity entity) {
        return null;
    }

    @Transactional(readOnly = true)
    public List<Entity> list(String query) {
        return null;
    }

    protected Entity getSingleResult(CriteriaQuery<Entity> query) {
        return getCurrentSession().createQuery(query).getSingleResult();
    }

    protected List<Entity> getResultList(CriteriaQuery<Entity> query) {
        return getCurrentSession().createQuery(query).getResultList();
    }

    protected CriteriaQuery<Entity> getCriteriaQuery() {
        return getCriteriaBuilder().createQuery(getGenericClass());
    }

    protected CriteriaBuilder getCriteriaBuilder() {
        return getCurrentSession().getCriteriaBuilder();
    }

    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    protected SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    private Class<Entity> getGenericClass() {
        return (Class<Entity>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }


}
