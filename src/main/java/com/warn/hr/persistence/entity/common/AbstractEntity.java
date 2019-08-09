package com.warn.hr.persistence.entity.common;

import com.warn.hr.common.utility.NullUtility;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

public abstract @Data class AbstractEntity implements Serializable, NullUtility {

    @Column(name = "CREATED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Column(name = "UPDATED_DATE", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;

}
