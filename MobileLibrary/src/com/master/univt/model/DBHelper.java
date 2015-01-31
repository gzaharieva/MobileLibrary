package com.master.univt.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.master.univt.services.SharedPreferencedSingleton;
import com.master.univt.support.GlobalApplication;


/**
 * Database helper. It creates the connection with greenDAO database instance.
 * 
 * @author Gabriela Zaharieva
 */
public class DBHelper
{
  private static final String DATABASE_NAME = "books-db";
  private final DaoSession daoSession;
  private static DBHelper instance;

  public static DBHelper getInstance()
  {
    if (instance == null)
    {
      instance = new DBHelper();
    }
    return instance;
  }

  DBHelper()
  {
    Context context = GlobalApplication.getInstance();
    SQLiteDatabase sqLiteDatabase = new ApplicationDaoHelper(context, DATABASE_NAME, null).getWritableDatabase();
    DaoMaster daoMaster = new DaoMaster(sqLiteDatabase);
    daoSession = daoMaster.newSession();
    if (isDbInconsistent())
    {
      clearDatabase();
    }
  }

  private void clearDatabase()
  {
   // this.daoSession.deleteAllData();
    this.daoSession.clear();
    clearInconsistentFlag();
  }

  private void clearInconsistentFlag()
  {
    SharedPreferencedSingleton.getInstance().writePreference("inconsistent", false);
  }

  private boolean isDbInconsistent()
  {
    boolean result = false;
    try
    {
      result = SharedPreferencedSingleton.getInstance().getBoolean("inconsistent", false);
    }
    catch (Exception ex)
    {
      Log.e("LOG", "DB is inconsistent", ex);
    }
    return result;
  }

  public DaoSession getDaoSession()
  {
    return this.daoSession;
  }
}
