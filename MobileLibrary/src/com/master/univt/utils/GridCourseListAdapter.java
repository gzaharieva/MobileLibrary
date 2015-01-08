package com.master.univt.utils;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.api.services.books.model.Bookshelf;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import com.master.univt.R;


/**
 * Course content list view adapter.
 * 
 * @author Gabriela Zaharieva
 */
public class GridCourseListAdapter extends ArrayAdapter<Bookshelf>
{

  /** The context. */
  private final Context context;
  /** The navigation drawerItems. */
  private final List<Bookshelf> bookshelfs;
  private LayoutInflater layoutInflater;
  private int selectedPosition = -1;

  private final DisplayImageOptions options;
  protected ImageLoader imageLoader = ImageLoader.getInstance();

  public GridCourseListAdapter(final Context context, final int resource, final int textViewResourceId,
                               final List<Bookshelf> objects)
  {
    super(context, resource, textViewResourceId, objects);
    // public CourseGridViewAdapter(final Context context, final List<Courses> courses)

    this.context = context;
    this.bookshelfs = objects;
    options =
      new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.ic_empty)
        .showImageForEmptyUri(R.drawable.ic_empty).showImageOnFail(R.drawable.ic_empty).cacheInMemory(true)
        .cacheOnDisk(true).considerExifParams(true).build();

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
    return bookshelfs.size();
  }

  @Override
  public Bookshelf getItem(final int position)
  {
    return bookshelfs.get(position);
  }

  @Override
  public long getItemId(final int position)
  {
    return position;
  }

  @Override
  public View getView(final int position, final View convertView, final ViewGroup parent)
  {
    final AdapterViewHolder holder;
    View view = convertView;
    if (view == null)
    {
      layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

      view = layoutInflater.inflate(R.layout.item_gridview_course, parent, false);
      holder = new AdapterViewHolder();
      assert view != null;
      holder.setImageView((ImageView) view.findViewById(R.id.image));
      holder.setTitleView((TextView) view.findViewById(R.id.title));

      view.setTag(holder);
    }
    else
    {
      holder = (AdapterViewHolder) view.getTag();
    }

    Bookshelf bookshelf = bookshelfs.get(position);

    holder.getTitleView().setText(bookshelf.getTitle());

    if (!imageLoader.isInited())
    {
      imageLoader.init(ImageLoaderConfiguration.createDefault(context));
    }
    imageLoader.displayImage(bookshelfs.get(position).getSelfLink(), holder.getImageView(), options, null);

    return view;
  }

    public class AdapterViewHolder {
        private TextView titleView;
        private ImageView imageView;


        public TextView getTitleView() {
            return titleView;
        }

        public void setTitleView(TextView titleView) {
            this.titleView = titleView;
        }

        public ImageView getImageView() {
            return imageView;
        }

        public void setImageView(ImageView imageView) {
            this.imageView = imageView;
        }
    }

    }
