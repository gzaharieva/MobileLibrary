package com.master.univt.utils;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.master.univt.entities.Books;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.master.univt.R;

/**
 * Course content list view adapter.
 * 
 * @author Gabriela Zaharieva
 */
public class BooksGridViewAdapter extends BaseAdapter
{

  /** The context. */
  private final Context context;
  /** The navigation drawerItems. */
  private final List<Books> coursesItems;
  private LayoutInflater layoutInflater;
  private int selectedPosition = -1;

  private final DisplayImageOptions options;
  protected ImageLoader imageLoader = ImageLoader.getInstance();

  /**
   * Initialization of the navigation sliding layout on the left with the given context and list
   * items.
   * 
   * @param context the content context
   * @param courses the list of navigation items
   */
  public BooksGridViewAdapter(final Context context, final List<Books> courses)
  {
    this.context = context;
    this.coursesItems = courses;
    options =
      new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.ic_empty)
        .showImageForEmptyUri(R.drawable.ic_empty).showImageOnFail(R.drawable.ic_empty)
        .cacheInMemory(true).cacheOnDisc(true).considerExifParams(true).build();
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
    return coursesItems.size();
  }

  @Override
  public Object getItem(final int position)
  {
    return coursesItems.get(position);
  }

  @Override
  public long getItemId(final int position)
  {
    return position;
  }

  @Override
  public View getView(final int position, final View convertView, final ViewGroup parent)
  {
    final ViewHolder holder;
    View view = convertView;
    if (view == null)
    {
      layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

      view = layoutInflater.inflate(R.layout.gridview_item_bookshelf, parent, false);
      holder = new ViewHolder();
      assert view != null;
      holder.imageView = (ImageView) view.findViewById(R.id.image);
      view.setTag(holder);
    }
    else
    {
      holder = (ViewHolder) view.getTag();
    }
    


    Books course = coursesItems.get(position);
    TextView titleTextView = (TextView) view.findViewById(R.id.title);
    titleTextView.setText(course.getTitle());

    TextView freeLabelView = (TextView) view.findViewById(R.id.label_free);

    RatingBar ratingBar = (RatingBar) view.findViewById(R.id.rating_bar);
	ratingBar.setRating((float) course.getRating());

    if (!imageLoader.isInited())
    {
      imageLoader.init(ImageLoaderConfiguration.createDefault(context));
    }
    imageLoader.displayImage(coursesItems.get(position).getImageURL(), holder.imageView, options, null);

    return view;
  }

  class ViewHolder
  {
    ImageView imageView;
  }
}
