package com.master.univt.utils;

import java.util.List;

import android.content.Context;
import android.widget.BaseAdapter;

/**
 * The application abstract list view adapter.
 * 
 * @author Gabriela Zaharieva
 */
public abstract class ApplicationListViewAdapter extends BaseAdapter
{

  /** The context. */
  private final Context context;
  /** The navigation drawerItems. */
  private final List<Books> books;
  private int mSelectedPosition = -1;

  /**
   * Initialization of the navigation sliding layout on the left with the given context and list
   * items.
   * 
   * @param context the content context
   * @param books the list of navigation items
   */
  protected ApplicationListViewAdapter(final Context context, final List<Books> books)
  {
    this.context = context;
    this.books = books;
  }

  public void setSelectedPosition(final int selectedPosition)
  {
    mSelectedPosition = selectedPosition;
    notifyDataSetChanged();

  }

  public int getSelectedPosition()
  {
    return mSelectedPosition;
  }

  @Override
  public int getCount()
  {
    return books.size();
  }

  @Override
  public Object getItem(final int position)
  {
    return books.get(position);
  }

  @Override
  public long getItemId(final int position)
  {
    return position;
  }

  protected List<Books> getBooks()
  {
    return books;
  }

  protected Context getContext()
  {
    return context;
  }
}
