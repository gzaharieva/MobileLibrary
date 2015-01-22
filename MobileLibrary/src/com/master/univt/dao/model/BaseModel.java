package com.master.univt.dao.model;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;

public abstract class BaseModel<Value, Key>
{
  private static ReentrantLock writeLock = new ReentrantLock(true);
  private final AbstractDao<Value, Key> dao;

  protected BaseModel(final AbstractDao<Value, Key> paramAbstractDao)
  {
    this.dao = paramAbstractDao;
  }

  protected Value save(final Property uniqueProperty, final Object value, final Value paramValue)
  {
    if (paramValue == null)
    {
      return null;
    }
    try
    {
      writeLock.lock();
      dao.insertOrReplace(paramValue);

    }
    finally
    {
      writeLock.unlock();
    }
    return paramValue;
  }

  public abstract Value findBy(final Property property, final Object uniqueParam);

  protected List<Value> saveAll(final Property uniqueProperty, final Object value, final List<Value> paramValue)
  {
    if (paramValue == null)
    {
      return null;
    }
    try
    {
      writeLock.lock();

      for (Value val : paramValue)
      {
        save(uniqueProperty, paramValue, val);
      }
    }
    finally
    {
      writeLock.unlock();
    }
    return paramValue;
  }

  public Collection<Value> getAll()
  {
    return this.dao.loadAll();
  }

  public long countAll()
  {
    return this.dao.queryBuilder().count();
  }

  public void delete(final Value paramValue)
  {
    this.dao.delete(paramValue);
  }

  public void deleteAll()
  {
    this.dao.deleteAll();
  }

  public Value load(final Key paramKey)
  {
    if (paramKey == null)
    {
      return null;
    }
    return (Value) this.dao.load(paramKey);
  }

  public Collection<Value> loadAll()
  {
    return this.dao.loadAll();
  }

  //
  // public Collection<Value> loadAll(final Collection<Key> paramCollection)
  // {
  // return this.dao.loadAll(paramCollection);
  // }

  // public Map<Key, Value> loadMap(final Collection<Key> paramCollection)
  // {
  // List localList = this.dao.loadAll(paramCollection);
  // HashMap localHashMap = new HashMap(localList.size());
  // Iterator localIterator = localList.iterator();
  // while (localIterator.hasNext())
  // {
  // SupportsUpdate localSupportsUpdate = (SupportsUpdate) localIterator.next();
  // localHashMap.put(resolveKey(localSupportsUpdate), localSupportsUpdate);
  // }
  // return localHashMap;
  // }

  protected abstract Key resolveKey(Value paramValue);

  protected void runInTx(final Runnable paramRunnable)
  {
    try
    {
      writeLock.lock();
      this.dao.getSession().runInTx(paramRunnable);
      return;
    }
    finally
    {
      writeLock.unlock();
    }
  }
}