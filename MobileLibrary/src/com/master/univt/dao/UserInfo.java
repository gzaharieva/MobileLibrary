package com.master.univt.dao;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table USER_INFO.
 */
public class UserInfo {

    private Long id;
    private java.util.Date updated;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public UserInfo() {
    }

    public UserInfo(Long id) {
        this.id = id;
    }

    public UserInfo(Long id, java.util.Date updated) {
        this.id = id;
        this.updated = updated;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public java.util.Date getUpdated() {
        return updated;
    }

    public void setUpdated(java.util.Date updated) {
        this.updated = updated;
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}
