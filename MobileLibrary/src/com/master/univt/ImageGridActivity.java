/*******************************************************************************
 * Copyright 2011-2013 Sergey Tarasevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.master.univt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.master.univt.utils.Books;
import com.master.univt.utils.BooksGridViewAdapter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;


/**
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
public class ImageGridActivity extends AbsListViewBaseActivity {

	String[] imageUrls;

	DisplayImageOptions options;

	Books fromJson;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_image_grid);

		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		Bundle bundle = getIntent().getExtras();
		imageUrls = bundle.getStringArray(Constants.Extra.IMAGES);

		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_stub)
				.showImageForEmptyUri(R.drawable.ic_empty)
				.showImageOnFail(R.drawable.ic_error).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();

		listView = (GridView) findViewById(R.id.gridview);

		fromJson = new Gson().fromJson(getJOSN(), Books.class);
		BooksGridViewAdapter gridViewAdapter = new BooksGridViewAdapter(this,
				fromJson.getCourses());
		((GridView) listView).setAdapter(gridViewAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				startImagePagerActivity(position);
			}
		});
	}

	@Override
	public void onConfigurationChanged(final Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (newConfig.orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			((GridView) listView).setNumColumns(1);
		} else {
			((GridView) listView).setNumColumns(2);
		}
	}

	private String getJOSN() {
		StringBuilder stringBuilder = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					getAssets().open("books.txt"), "UTF-8"));
			String line;
			while ((line = reader.readLine()) != null) {
				stringBuilder.append(line + "\n");
			}

			reader.close();
		} catch (IOException e) {
		}
		return stringBuilder.toString();
	}

	private void startImagePagerActivity(int position) {
		// Intent intent = new Intent(this, ImagePagerActivity.class);
		// intent.putExtra(Extra.IMAGES, imageUrls);
		// intent.putExtra(Extra.IMAGE_POSITION, position);
		// startActivity(intent);
		//
		Books book = (Books) listView.getItemAtPosition(position);
		Intent intent = new Intent(this, BooksDetailsActivity.class);
		intent.putExtra(Constants.Extra.BOOK, (java.io.Serializable) book);
		startActivity(intent);

	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {

		if (item.getItemId() == android.R.id.home) {
			onBackPressed();
		}

		return super.onOptionsItemSelected(item);
	}

	static class ViewHolder {
		ImageView imageView;
		ProgressBar progressBar;
	}

	public class ImageAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return imageUrls.length;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			View view = convertView;
			if (view == null) {
				view = getLayoutInflater().inflate(R.layout.item_grid_image,
						parent, false);
				holder = new ViewHolder();
				assert view != null;
				holder.imageView = (ImageView) view.findViewById(R.id.image);
				holder.progressBar = (ProgressBar) view
						.findViewById(R.id.progress);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}

			imageLoader.displayImage(imageUrls[position], holder.imageView,
					options, new SimpleImageLoadingListener() {
						@Override
						public void onLoadingStarted(String imageUri, View view) {
							holder.progressBar.setProgress(0);
							holder.progressBar.setVisibility(View.VISIBLE);
						}

						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {
							holder.progressBar.setVisibility(View.GONE);
						}

						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {
							holder.progressBar.setVisibility(View.GONE);
						}
					}, new ImageLoadingProgressListener() {
						@Override
						public void onProgressUpdate(String imageUri,
								View view, int current, int total) {
							holder.progressBar.setProgress(Math.round(100.0f
									* current / total));
						}
					});

			return view;

		}

	}

}