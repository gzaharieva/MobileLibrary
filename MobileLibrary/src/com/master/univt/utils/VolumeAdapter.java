package com.master.univt.utils;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
public class VolumeAdapter extends BaseAdapter {

    /**
     * The context.
     */
    private final Context context;
    /**
     * The navigation drawerItems.
     */
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
     * @param volume  the list of navigation items
     */
    public VolumeAdapter(final Context context, final List<Volume> volume) {
        this.context = context;
        this.volumes = volume;
        options =
                new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.ic_place).showImageForEmptyUri(R.drawable.ic_place)
                        .showImageOnFail(R.drawable.ic_place).cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).build();
    }

    public void setSelectedPosition(final int selectedPosition) {
        this.selectedPosition = selectedPosition;
        notifyDataSetChanged();
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    @Override
    public int getCount() {
        return volumes != null ? volumes.size() : 0;
    }

    @Override
    public Object getItem(final int position) {
        return volumes.get(position);
    }

    @Override
    public long getItemId(final int position) {
        return position;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        final ViewHolder holder;
        View view = convertView;
        if (view == null) {
            layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            view = layoutInflater.inflate(R.layout.gridview_item_bookshelf, parent, false);
            holder = new ViewHolder();
            assert view != null;
            holder.setImageView((ImageView) view.findViewById(R.id.image));
            holder.setVolumeCountryTextView((TextView) view.findViewById(R.id.volume_country));
            holder.setTitle((TextView) view.findViewById(R.id.title));
            holder.setVolumeIsbnTextView((TextView) view.findViewById(R.id.volume_isbn));
            holder.setVolumePublisherTextView((TextView) view.findViewById(R.id.volume_publisher));
            holder.setVolumePublishedDate((TextView) view.findViewById(R.id.volume_published_date));
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Volume volume = volumes.get(position);

        Volume.VolumeInfo volumeInfo = volume.getVolumeInfo();
        holder.getTitle().setText(volumeInfo.getTitle());
        holder.getVolumeCountryTextView().setText(volume.getAccessInfo().getCountry());
        if (volumeInfo.getIndustryIdentifiers() != null) {
            holder.getVolumeIsbnTextView().setText(volumeInfo.getIndustryIdentifiers().get(0).getIdentifier());
        }
        holder.getVolumePublisherTextView().setText(volumeInfo.getPublisher());
        holder.getVolumePublishedDate().setText(volumeInfo.getPublishedDate());
        if (!imageLoader.isInited()) {
            imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        }

        Volume.VolumeInfo.ImageLinks imageLinks = volumeInfo.getImageLinks();
        if (imageLinks != null) {
            imageLoader.displayImage(imageLinks.getThumbnail(), holder.imageView, options, null);
        }

        return view;
    }

    class ViewHolder {
        private ImageView imageView;
        private TextView volumeIsbnTextView;
        private TextView volumePublisherTextView;
        private TextView volumeCountryTextView;
        private TextView title;
        private TextView volumePublishedDate;

        public TextView getVolumePublishedDate() {
            return volumePublishedDate;
        }

        public void setVolumePublishedDate(TextView volumePublishedDate) {
            this.volumePublishedDate = volumePublishedDate;
        }

        public TextView getTitle() {
            return title;
        }

        public void setTitle(TextView title) {
            this.title = title;
        }

        public ImageView getImageView() {
            return imageView;
        }

        public void setImageView(ImageView imageView) {
            this.imageView = imageView;
        }

        public TextView getVolumeCountryTextView() {
            return volumeCountryTextView;
        }

        public void setVolumeCountryTextView(TextView volumeCountryTextView) {
            this.volumeCountryTextView = volumeCountryTextView;
        }

        public TextView getVolumeIsbnTextView() {
            return volumeIsbnTextView;
        }

        public void setVolumeIsbnTextView(TextView volumeIsbnTextView) {
            this.volumeIsbnTextView = volumeIsbnTextView;
        }

        public TextView getVolumePublisherTextView() {
            return volumePublisherTextView;
        }

        public void setVolumePublisherTextView(TextView volumePublisherTextView) {
            this.volumePublisherTextView = volumePublisherTextView;
        }
    }
}
