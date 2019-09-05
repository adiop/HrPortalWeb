package com.warn.hr.persistence.dao;

import com.warn.hr.persistence.dao.common.HRAbstractDAO;
import com.warn.hr.persistence.entity.hr.staff.StaffHistory;
import org.hibernate.query.NativeQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class StaffHistoryDAO  extends HRAbstractDAO<StaffHistory> {

    @Transactional(readOnly = true)
    public StaffHistory findLastChange(Long staffId) {
        return list(staffId).stream().findFirst().orElse(null);
    }

    @Transactional(readOnly = true)
    public List<StaffHistory> list(Long staffId) {

        CriteriaBuilder criteriaBuilder = getCurrentSession().getCriteriaBuilder();

        CriteriaQuery<StaffHistory> query = criteriaBuilder.createQuery(StaffHistory.class);
        Root<StaffHistory> root = query.from(StaffHistory.class);

        query.select(root).where(criteriaBuilder.equal(root.get("staffId"), staffId));
        query.orderBy(criteriaBuilder.desc(root.get("createdDate")));

        return getCurrentSession().createQuery(query).list();

    }

    @Transactional(value = "hibernateTransactionManagerHR")
    public void createStaffHistory(Long staffId) {

        NativeQuery queryInsertStaffHistory = getCurrentSession().createNativeQuery(Query.INSERT_STAFF_HISTORY);
        queryInsertStaffHistory.setParameter("staffId", staffId);
            queryInsertStaffHistory.executeUpdate();

        NativeQuery queryInsertStaffXStaffResourceHistory = getCurrentSession().createNativeQuery(Query.INSERT_STAFF_X_STAFF_RESOURCE_HISTORY);
        queryInsertStaffXStaffResourceHistory.setParameter("staffId", staffId);
        queryInsertStaffXStaffResourceHistory.executeUpdate();

    }

    private static class Query {

        private static final String INSERT_STAFF_HISTORY = "INSERT INTO [dbo].[staff_history] (" +
                "[STAFF_ID]," +
                "[IMAGE]," +
                "[FIRST_NAME]," +
                "[PREFERRED_NAME]," +
                "[LAST_NAME]," +
                "[EMAIL]," +
                "[PHONE]," +
                "[SHIFT]," +
                "[KEY_CARD_NUMBER]," +
                "[STAFF_NUMBER]," +
                "[TEAM_LEADER]," +
                "[JOB_TITLE_ID]," +
                "[MANAGER_ID]," +
                "[FLAGGED]," +
                "[COMMENTS]," +
                "[STATUS]" +
                ") " +
                "SELECT S.[ID]," +
                "s.[IMAGE]," +
                "s.[FIRST_NAME]," +
                "s.[PREFERRED_NAME]," +
                "s.[LAST_NAME]," +
                "s.[EMAIL]," +
                "s.[PHONE]," +
                "s.[SHIFT]," +
                "s.[KEY_CARD_NUMBER]," +
                "s.[STAFF_NUMBER]," +
                "s.[TEAM_LEADER]," +
                "s.[JOB_TITLE_ID]," +
                "s.[MANAGER_ID]," +
                "s.[FLAGGED]," +
                "s.[COMMENTS]," +
                "s.[STATUS]" +
                "FROM [dbo].[staff] s where s.id = :staffId";

        private static final String INSERT_STAFF_X_STAFF_RESOURCE_HISTORY = "INSERT INTO [dbo].[staff_x_staff_resources_history] (" +
                "[HISTORY_ID]," +
                "[STAFF_ID]," +
                "[STAFF_RESOURCE_ID]" +
                ") " +
                "SELECT " +
                "(SELECT MAX(ID) FROM [dbo].[staff_history] where STAFF_ID = :staffId)," +
                "SS.[STAFF_ID], " +
                "SS.[STAFF_RESOURCE_ID] " +
                "FROM [dbo].[staff_x_staff_resources] SS where STAFF_ID = :staffId";

    }

}
