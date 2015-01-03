package com.master.univt.utils;

import java.util.List;

import com.master.univt.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Courses content list view adapter.
 * 
 * @author Gabriela Zaharieva
 */
public class BooksListViewAdapter extends ApplicationListViewAdapter
{

  private LayoutInflater layoutInflater;

  /**
   * Initialization of the navigation sliding layout on the left with the given context and list
   * items.
   * 
   * @param context the content context
   * @param courses the list of navigation items
   */
  public BooksListViewAdapter(final Context context, final List<Books> courses)
  {
    super(context, courses);
  }

  @Override
  public View getView(final int position, final View convertView, final ViewGroup parent)
  {
    layoutInflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

    Books course = getBooks().get(position);
    View currentItemView;

    
      currentItemView = layoutInflater.inflate(R.layout.listitem_course, parent, false);
    
    TextView titleTextView = (TextView) currentItemView.findViewById(R.id.title);
    titleTextView.setText(course.getTitle());

    return currentItemView;
  }


}
