package com.master.univt.model;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table ACCESS_INFO.
 */
public class AccessInfo {

    private Long id;
    private String country;
    private String webReaderLink;
    private boolean pdf;
    private boolean epub;
    private int accessViewStatus;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public AccessInfo() {
    }

    public AccessInfo(Long id) {
        this.id = id;
    }

    public AccessInfo(Long id, String country, String webReaderLink, boolean pdf, boolean epub, int accessViewStatus) {
        this.id = id;
        this.country = country;
        this.webReaderLink = webReaderLink;
        this.pdf = pdf;
        this.epub = epub;
        this.accessViewStatus = accessViewStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getWebReaderLink() {
        return webReaderLink;
    }

    public void setWebReaderLink(String webReaderLink) {
        this.webReaderLink = webReaderLink;
    }

    public boolean getPdf() {
        return pdf;
    }

    public void setPdf(boolean pdf) {
        this.pdf = pdf;
    }

    public boolean getEpub() {
        return epub;
    }

    public void setEpub(boolean epub) {
        this.epub = epub;
    }

    public int getAccessViewStatus() {
        return accessViewStatus;
    }

    public void setAccessViewStatus(int accessViewStatus) {
        this.accessViewStatus = accessViewStatus;
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}
