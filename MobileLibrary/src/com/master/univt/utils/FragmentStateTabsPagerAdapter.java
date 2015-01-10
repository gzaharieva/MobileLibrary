package com.master.univt.utils;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

/**
 * The <code>PagerAdapter</code> serves the fragments when paging.
 * 
 * @author Gabriela Zaharieva
 */
public class FragmentStateTabsPagerAdapter extends FragmentStatePagerAdapter
{

  // private List<Fragment> fragments;
  private final List<Fragment> fragments;

  /**
   * @param fragmentManager
   * @param fragments
   */
  public FragmentStateTabsPagerAdapter(final FragmentManager fragmentManager, final List<Fragment> fragments)
  {
    super(fragmentManager);
    this.fragments = fragments;
  }

  /*
   * (non-Javadoc)
   * 
   * @see android.support.v4.app.FragmentStatePagerAdapter#getItem(int)
   */
  @Override
  public Fragment getItem(final int position)
  {
    return fragments.get(position);
  }

  /*
   * (non-Javadoc)
   * 
   * @see android.support.v4.view.PagerAdapter#getCount()
   */
  @Override
  public int getCount()
  {
    return this.fragments.size();
  }

  /*
   * (non-Javadoc)
   * 
   * @see android.support.v4.app.FragmentStatePagerAdapter#destroyItem(android.view.ViewGroup, int,
   * java.lang.Object)
   */
  @Override
  public void destroyItem(final ViewGroup viewPager, final int position, final Object object)
  {
	try
	{
    super.destroyItem(viewPager, position, object);
    }
	catch(Exception ex)
	{
		Log.e("FragmentStateTabsPagerAdapter", "Can not destroy item." + ex.getMessage());
	}
  }

  public @Override int getItemPosition(final Object object)
  {
    return POSITION_NONE;
  }

  @Override
  public Object instantiateItem(final ViewGroup viewGroup, final int position)
  {
	  Object instantiateItem = null;
	  try
	  {
		  instantiateItem = super.instantiateItem(viewGroup, position);
	  }
	  catch(IllegalStateException ex)
	  {
		  Log.e("FragmentStateTabsPagerAdapter", "Can not instantiate item." + ex.getMessage());
	  }
    return instantiateItem;
  }

}