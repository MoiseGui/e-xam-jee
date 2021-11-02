package com.github.adminfaces.starter.util;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.PrePersist;
import org.mongodb.morphia.annotations.Property;
import org.mongodb.morphia.annotations.Version;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * @author Ulrich Cech
 */
public abstract class PersistentEntity implements Serializable {

    private static final long serialVersionUID = -8346526742641366019L;

    @Id
    private ObjectId id;

    @Version
    private Long version;

    @Property
    private Date created;

    @Property
    private Date lastUpdate;


    @PrePersist
    protected void prePersist() {
        if (created == null) {
            created = new Date();
        }
        this.lastUpdate = new Date();
//        validate();
    }

//    protected void validate() {}


    public ObjectId getId() {
        return id;
    }

    public Long getVersion() {
        return version;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Date getCreated() {
        return created;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PersistentEntity)) return false;
        PersistentEntity that = (PersistentEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
