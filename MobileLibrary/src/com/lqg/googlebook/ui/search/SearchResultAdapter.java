package com.lqg.googlebook.ui.search;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.api.services.books.model.Volume;
import com.lqg.googlebook.R;
import com.lqg.googlebook.support.util.ImageLoaderUtil;
import com.lqg.googlebook.ui.detail.BookDetailActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.List;


/**
 * Created by LQG on 2014/12/4.
 */
public class SearchResultAdapter extends BaseAdapter {
    private List<Volume> searchResultList;
    private static DisplayImageOptions options;

    Context context;

    public SearchResultAdapter(Fragment fragment, List<Volume> searchResultList) {
        context = fragment.getActivity();
        this.searchResultList = searchResultList;
        options = ImageLoaderUtil.createListPicDisplayImageOptions();
    }

    @Override
    public int getCount() {
        return searchResultList.size();
    }

    @Override
    public Volume getItem(int position) {
        return searchResultList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_search, null);
            holder = new Holder();
            holder.thumIv = (ImageView) convertView.findViewById(R.id.book_thm);
            holder.titleTv = (TextView) convertView.findViewById(R.id.book_title);
            holder.authorTv = (TextView) convertView.findViewById(R.id.book_authors);
            holder.descriptionTv = (TextView) convertView.findViewById(R.id.book_description);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        Volume result = getItem(position);
        Volume.VolumeInfo volumeInfo = result.getVolumeInfo();
        if (volumeInfo != null) {
            holder.titleTv.setText(volumeInfo.getTitle());
            holder.authorTv.setText(BookDetailActivity.getAutor(result));
            holder.descriptionTv.setText(volumeInfo.getDescription());
            ImageLoaderUtil.getImageLoader().displayImage(BookDetailActivity.getImageLink(volumeInfo), holder.thumIv, options);
        }
        return convertView;
    }

    public class Holder {
        ImageView thumIv;
        TextView titleTv;
        TextView authorTv;
        TextView descriptionTv;
    }
}
