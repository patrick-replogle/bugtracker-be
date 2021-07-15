package com.patrickreplogle.bugtracker.models;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
abstract public class Auditable {
    // === fields ===
    @CreatedBy
    protected String createdby;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    protected Date createddate;

    @LastModifiedBy
    protected String lastmodifiedby;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    protected Date lastmodifieddate;

    public Date getCreateddate() {
        return createddate;
    }

    public Date getLastmodifieddate() {
        return lastmodifieddate;
    }
}
