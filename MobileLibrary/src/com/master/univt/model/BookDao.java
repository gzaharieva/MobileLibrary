package com.master.univt.model;

import java.util.List;
import java.util.ArrayList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.SqlUtils;
import de.greenrobot.dao.internal.DaoConfig;
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table BOOK.
*/
public class BookDao extends AbstractDao<Book, Long> {

    public static final String TABLENAME = "BOOK";

    /**
     * Properties of entity Book.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, long.class, "id", true, "_id");
        public final static Property BookInfoId = new Property(1, Long.class, "book_info_id", true, "BOOK_INFO_ID");
        public final static Property UserInfoId = new Property(2, Long.class, "user_info_id", true, "USER_INFO_ID");
        public final static Property SaleInfoId = new Property(3, Long.class, "sale_info_id", true, "SALE_INFO_ID");
        public final static Property AccessInfoId = new Property(4, Long.class, "access_info_id", true, "ACCESS_INFO_ID");
        public final static Property BookshelfId = new Property(5, Long.class, "bookshelf_id", true, "BOOKSHELF_ID");
    };

    private DaoSession daoSession;

    private Query<Book> bookshelf_BooksQuery;

    public BookDao(DaoConfig config) {
        super(config);
    }
    
    public BookDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'BOOK' (" + //
                "'_id' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ," + // 0: id
                "'BOOK_INFO_ID' INTEGER  ," + // 1: id
                "'USER_INFO_ID' INTEGER  ," + // 2: id
                "'SALE_INFO_ID' INTEGER  ," + // 3: id
                "'ACCESS_INFO_ID' INTEGER  );"); // 4: id
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'BOOK'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Book entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
    }

    @Override
    protected void attachEntity(Book entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Book readEntity(Cursor cursor, int offset) {
        Book entity = new Book( //
            cursor.getLong(offset + 0) // id
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Book entity, int offset) {
        entity.setId(cursor.getLong(offset + 0));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Book entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Book entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
    /** Internal query to resolve the "books" to-many relationship of Bookshelf. */
    public List<Book> _queryBookshelf_Books(long id) {
        synchronized (this) {
            if (bookshelf_BooksQuery == null) {
                QueryBuilder<Book> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.BookInfoId.eq(null));
                bookshelf_BooksQuery = queryBuilder.build();
            }
        }
        Query<Book> query = bookshelf_BooksQuery.forCurrentThread();
        query.setParameter(0, id);
        return query.list();
    }

    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getBookInfoDao().getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T1", daoSession.getUserInfoDao().getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T2", daoSession.getSaleInfoDao().getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T3", daoSession.getAccessInfoDao().getAllColumns());
            builder.append(" FROM BOOK T");
            builder.append(" LEFT JOIN BOOK_INFO T0 ON T.'book_info_id'=T0.'_id'");
            builder.append(" LEFT JOIN USER_INFO T1 ON T.'user_info_id'=T1.'_id'");
            builder.append(" LEFT JOIN SALE_INFO T2 ON T.'sale_info_id'=T2.'_id'");
            builder.append(" LEFT JOIN ACCESS_INFO T3 ON T.'access_info_id'=T3.'_id'");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected Book loadCurrentDeep(Cursor cursor, boolean lock) {
        Book entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        BookInfo bookInfo = loadCurrentOther(daoSession.getBookInfoDao(), cursor, offset);
        entity.setBookInfo(bookInfo);
        offset += daoSession.getBookInfoDao().getAllColumns().length;

        UserInfo userInfo = loadCurrentOther(daoSession.getUserInfoDao(), cursor, offset);
        entity.setUserInfo(userInfo);
        offset += daoSession.getUserInfoDao().getAllColumns().length;

        SaleInfo saleInfo = loadCurrentOther(daoSession.getSaleInfoDao(), cursor, offset);
        entity.setSaleInfo(saleInfo);
        offset += daoSession.getSaleInfoDao().getAllColumns().length;

        AccessInfo accessInfo = loadCurrentOther(daoSession.getAccessInfoDao(), cursor, offset);
        entity.setAccessInfo(accessInfo);

        return entity;    
    }

    public Book loadDeep(Long key) {
        assertSinglePk();
        if (key == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(getSelectDeep());
        builder.append("WHERE ");
        SqlUtils.appendColumnsEqValue(builder, "T", getPkColumns());
        String sql = builder.toString();
        
        String[] keyArray = new String[] { key.toString() };
        Cursor cursor = db.rawQuery(sql, keyArray);
        
        try {
            boolean available = cursor.moveToFirst();
            if (!available) {
                return null;
            } else if (!cursor.isLast()) {
                throw new IllegalStateException("Expected unique result, but count was " + cursor.getCount());
            }
            return loadCurrentDeep(cursor, true);
        } finally {
            cursor.close();
        }
    }
    
    /** Reads all available rows from the given cursor and returns a list of new ImageTO objects. */
    public List<Book> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<Book> list = new ArrayList<Book>(count);
        
        if (cursor.moveToFirst()) {
            if (identityScope != null) {
                identityScope.lock();
                identityScope.reserveRoom(count);
            }
            try {
                do {
                    list.add(loadCurrentDeep(cursor, false));
                } while (cursor.moveToNext());
            } finally {
                if (identityScope != null) {
                    identityScope.unlock();
                }
            }
        }
        return list;
    }
    
    protected List<Book> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<Book> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
