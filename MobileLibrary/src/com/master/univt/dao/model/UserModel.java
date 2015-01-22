package com.master.univt.dao.model;

import com.master.univt.dao.User;
import com.master.univt.dao.UserDao;

import java.util.List;

import de.greenrobot.dao.Property;

public class UserModel extends BaseModel<User, Long>
{

  private final UserDao userDao;

  public UserModel(final DBHelper paramDBHelper)
  {
    super(paramDBHelper.getDaoSession().getUserDao());
    this.userDao = paramDBHelper.getDaoSession().getUserDao();
  }

  @Override
  protected Long resolveKey(final User paramUser)
  {
    return paramUser.getId();
  }

  public User saveUser(final User paramUser)
  {
    return (User) save(UserDao.Properties.UId, paramUser.getUId(), paramUser);
  }

  public User findBy(final Property property, final Object userUId)
  {
    User result = null;
    List<User> users = userDao.queryBuilder().where(property.eq(userUId)).list();
    if (users != null && !users.isEmpty())
    {
      result = users.get(0);
    }
    return result;
  }
}