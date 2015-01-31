package com.master.univt.model;

import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table BOOK.
 */
public class Book {

    private long id;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient BookDao myDao;

    private BookInfo bookInfo;
    private Long bookInfo__resolvedKey;

    private UserInfo userInfo;
    private Long userInfo__resolvedKey;

    private SaleInfo saleInfo;
    private Long saleInfo__resolvedKey;

    private AccessInfo accessInfo;
    private Long accessInfo__resolvedKey;


    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public Book() {
    }

    public Book(long id) {
        this.id = id;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getBookDao() : null;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    /** To-one relationship, resolved on first access. */
    public BookInfo getBookInfo() {
        Long __key = this.id;
        if (bookInfo__resolvedKey == null || !bookInfo__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            BookInfoDao targetDao = daoSession.getBookInfoDao();
            BookInfo bookInfoNew = targetDao.load(__key);
            synchronized (this) {
                bookInfo = bookInfoNew;
            	bookInfo__resolvedKey = __key;
            }
        }
        return bookInfo;
    }

    public void setBookInfo(BookInfo bookInfo) {
        synchronized (this) {
            this.bookInfo = bookInfo;
            id = bookInfo == null ? null : bookInfo.getId();
            bookInfo__resolvedKey = id;
        }
    }

    /** To-one relationship, resolved on first access. */
    public UserInfo getUserInfo() {
        Long __key = this.id;
        if (userInfo__resolvedKey == null || !userInfo__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserInfoDao targetDao = daoSession.getUserInfoDao();
            UserInfo userInfoNew = targetDao.load(__key);
            synchronized (this) {
                userInfo = userInfoNew;
            	userInfo__resolvedKey = __key;
            }
        }
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        synchronized (this) {
            this.userInfo = userInfo;
            id = userInfo == null ? null : userInfo.getId();
            userInfo__resolvedKey = id;
        }
    }

    /** To-one relationship, resolved on first access. */
    public SaleInfo getSaleInfo() {
        Long __key = this.id;
        if (saleInfo__resolvedKey == null || !saleInfo__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            SaleInfoDao targetDao = daoSession.getSaleInfoDao();
            SaleInfo saleInfoNew = targetDao.load(__key);
            synchronized (this) {
                saleInfo = saleInfoNew;
            	saleInfo__resolvedKey = __key;
            }
        }
        return saleInfo;
    }

    public void setSaleInfo(SaleInfo saleInfo) {
        synchronized (this) {
            this.saleInfo = saleInfo;
            id = saleInfo == null ? null : saleInfo.getId();
            saleInfo__resolvedKey = id;
        }
    }

    /** To-one relationship, resolved on first access. */
    public AccessInfo getAccessInfo() {
        Long __key = this.id;
        if (accessInfo__resolvedKey == null || !accessInfo__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            AccessInfoDao targetDao = daoSession.getAccessInfoDao();
            AccessInfo accessInfoNew = targetDao.load(__key);
            synchronized (this) {
                accessInfo = accessInfoNew;
            	accessInfo__resolvedKey = __key;
            }
        }
        return accessInfo;
    }

    public void setAccessInfo(AccessInfo accessInfo) {
        synchronized (this) {
            this.accessInfo = accessInfo;
            id = accessInfo == null ? null : accessInfo.getId();
            accessInfo__resolvedKey = id;
        }
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}