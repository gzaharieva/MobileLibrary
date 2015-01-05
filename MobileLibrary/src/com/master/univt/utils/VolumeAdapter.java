package com.master.univt.utils;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.api.services.books.model.Volume;
import com.master.univt.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;

/**
 * Created by GABI on 3.1.2015 г..
 */
public class VolumeAdapter  extends BaseAdapter
    {

        /** The context. */
        private final Context context;
        /** The navigation drawerItems. */
        private final List<Volume> volumes;
        private LayoutInflater layoutInflater;
        private int selectedPosition = -1;

        private final DisplayImageOptions options;
        protected ImageLoader imageLoader = ImageLoader.getInstance();

        /**
         * Initialization of the navigation sliding layout on the left with the given context and list
         * items.
         *
         * @param context the content context
         * @param volume the list of navigation items
         */
        public VolumeAdapter(final Context context, final List<Volume> volume)
        {
            this.context = context;
            this.volumes = volume;
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
            return volumes != null ? volumes.size() : 0;
        }

        @Override
        public Object getItem(final int position)
        {
            return volumes.get(position);
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

                view = layoutInflater.inflate(R.layout.gridview_item_course, parent, false);
                holder = new ViewHolder();
                assert view != null;
                holder.imageView = (ImageView) view.findViewById(R.id.image);
                view.setTag(holder);
            }
            else
            {
                holder = (ViewHolder) view.getTag();
            }



            Volume course = volumes.get(position);
            TextView titleTextView = (TextView) view.findViewById(R.id.title);
            titleTextView.setText(course.getVolumeInfo().getTitle());

            TextView freeLabelView = (TextView) view.findViewById(R.id.label_free);

            RatingBar ratingBar = (RatingBar) view.findViewById(R.id.rating_bar);
           // ratingBar.setRating((float) course.getAccessInfo().getCountry());

            if (!imageLoader.isInited())
            {
                imageLoader.init(ImageLoaderConfiguration.createDefault(context));
            }
            imageLoader.displayImage(volumes.get(position).getVolumeInfo().getImageLinks().getThumbnail(), holder.imageView, options, null);

            return view;
        }

        class ViewHolder
        {
            ImageView imageView;
        }
    }
