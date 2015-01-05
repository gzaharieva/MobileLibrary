package com.master.univt.services;

import java.util.Map;
import java.util.TreeMap;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

/**
 * Shared preferences for the application.
 */
public class SharedPreferencedSingleton
{

  private static SharedPreferencedSingleton instance;
  private SharedPreferences appPreferences;

  private SharedPreferencedSingleton()
  {
  }

  public static SharedPreferencedSingleton getInstance()
  {
    if (instance == null)
    {
      instance = new SharedPreferencedSingleton();
    }
    return instance;
  }

  public void Initialize(final Context context)
  {
    appPreferences = PreferenceManager.getDefaultSharedPreferences(context);
  }

  public void writePreference(final String key, final String value)
  {
    Editor e = appPreferences.edit();
    e.putString(key, value);
    e.commit();
  }

  public void writePreference(final String key, final Boolean value)
  {
    Editor e = appPreferences.edit();
    e.putBoolean(key, value);
    e.commit();
  }

  public void writePreference(final String key, final Long value)
  {
    Editor e = appPreferences.edit();
    e.putLong(key, value);
    e.commit();
  }

  public void writePreference(final String key, final int value)
  {
    Editor e = appPreferences.edit();
    e.putInt(key, value);
    e.commit();
  }

  public void remove(final String key)
  {
    Editor e = appPreferences.edit();
    e.remove(key);
    e.commit();
  }

  public String getString(final String key, final String defValue)
  {
    return appPreferences.getString(key, defValue);
  }

  public int getInt(final String key, final int defValue)
  {
    return appPreferences.getInt(key, defValue);
  }

  public Boolean getBoolean(final String key, final Boolean defValue)
  {
    return appPreferences.getBoolean(key, defValue);
  }

  public Long getLong(final String key, final Long defValue)
  {
    return appPreferences.getLong(key, defValue);
  }

}
