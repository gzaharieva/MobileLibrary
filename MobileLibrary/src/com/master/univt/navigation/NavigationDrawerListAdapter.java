package com.master.univt.navigation;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.master.univt.R;

/**
 * Navigation bar adapter for the sliding list view on the left side of the application
 * 
 * @author Gabriela Zaharieva
 */
public class NavigationDrawerListAdapter extends BaseAdapter
{

  private static NavigationDrawerListAdapter instance;
  /** The context. */
  private final Context context;
  /** The navigation drawerItems. */
  private final List<NavigationDrawerItem> navigationDrawerItems;
  private LayoutInflater layoutInflater;
  private int selectedPosition = -1;

  /**
   * Initialization of the navigation sliding layout on the left with the given context and list
   * items.
   * 
   * @param context the content context
   * @param navDrawerItems the list of navigation items
   */
  public NavigationDrawerListAdapter(final Context context, final List<NavigationDrawerItem> navDrawerItems)
  {
    this.context = context;
    this.navigationDrawerItems = navDrawerItems;
  }

  public static NavigationDrawerListAdapter getInstance(final Context context,
    final List<NavigationDrawerItem> navDrawerItems)
  {
    if (instance == null)
    {
      instance = new NavigationDrawerListAdapter(context, navDrawerItems);
    }
    return instance;
  }

  public void setSelectedPosition(final int selectedPosition)
  {
    this.selectedPosition = selectedPosition;
    notifyDataSetChanged();

  }

  public int getSelectedPosition()
  {
    return selectedPosition;
  }

  @Override
  public int getCount()
  {
    return navigationDrawerItems.size();
  }

  @Override
  public Object getItem(final int position)
  {
    return navigationDrawerItems.get(position);
  }

  @Override
  public long getItemId(final int position)
  {
    return position;
  }

  @Override
  public boolean isEnabled(final int position)
  {
    if (position == 0)
    {
      return false;
    }
    else
    {
      return super.isEnabled(position);
    }
  }

  @Override
  public View getView(final int position, final View convertView, final ViewGroup parent)
  {

    layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

    final View itemView;

    if (position == 0)
    {
      itemView = layoutInflater.inflate(R.layout.listitem_drawer_user, parent, false);
    }
    else
    {
      itemView = layoutInflater.inflate(R.layout.listitem_drawer, parent, false);
    }

    View drawerActiveSection = itemView.findViewById(R.id.drawer_active_section);
    if (drawerActiveSection != null)
    {
      if (selectedPosition == position)
      {
        drawerActiveSection.setVisibility(View.VISIBLE);
      }
      else
      {
        drawerActiveSection.setVisibility(View.INVISIBLE);
      }
    }

    ImageView imgIcon = (ImageView) itemView.findViewById(R.id.icon);
    TextView titleTextView = (TextView) itemView.findViewById(R.id.title);

    imgIcon.setImageResource(navigationDrawerItems.get(position).getIcon());
    titleTextView.setText(navigationDrawerItems.get(position).getTitle());

    return itemView;
  }

}
