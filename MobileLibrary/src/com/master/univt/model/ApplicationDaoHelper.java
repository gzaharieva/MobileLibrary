package com.master.univt.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

/**
 * DAO helper implements the @class DevOpenHelper
 * 
 * @author Gabriela Zaharieva
 */
public class ApplicationDaoHelper extends DaoMaster.DevOpenHelper
{

  public ApplicationDaoHelper(final Context context, final String name, final CursorFactory factory)
  {
    super(context, name, factory);
  }


  @Override
  public void onUpgrade(final SQLiteDatabase paramSQLiteDatabase, final int oldVersion, final int newVersion)
  {
    super.onUpgrade(paramSQLiteDatabase, oldVersion, newVersion);
  }

}
