package com.warn.hr.business.events;

import com.warn.hr.persistence.entity.hr.staff.common.Staff;
import com.warn.hr.persistence.entity.hr.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.context.ApplicationEvent;

import java.util.Date;

@EqualsAndHashCode(callSuper = false)
public @Data class StaffEvent extends ApplicationEvent {

    private Type type;

    private Staff createdBy;

    private Staff targetStaff;

    private Date effectiveDate;

    public static StaffEvent newStaffInstance(Object source, Staff createdBy, Staff targetStaff, Date effectiveDate) {
        return new StaffEvent(source, Type.NEW_STAFF, createdBy, targetStaff, effectiveDate);
    }

    public static StaffEvent updateStaffInstance(Object source, Staff createdBy, Staff targetStaff) {
        return new StaffEvent(source, Type.UPDATE_STAFF, createdBy, targetStaff, new Date());
    }

    public static StaffEvent termStaffInstance(Object source, Staff createdBy, Staff targetStaff, Date effectiveDate) {
        return new StaffEvent(source, Type.TERM_STAFF, createdBy, targetStaff, effectiveDate);
    }

    private StaffEvent(Object source, Type type, Staff createdBy, Staff targetStaff, Date effectiveDate) {
        super(source);
        this.type = type;
        this.createdBy = createdBy;
        this.targetStaff = targetStaff;
        this.effectiveDate = effectiveDate;
    }

    public enum Type {
        NEW_STAFF,
        UPDATE_STAFF,
        TERM_STAFF
    }

}
