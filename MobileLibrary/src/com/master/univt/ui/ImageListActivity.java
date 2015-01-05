///*******************************************************************************
// * Copyright 2011-2013 Sergey Tarasevich
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// * http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// *******************************************************************************/
//package com.master.univt.ui;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.util.Collections;
//import java.util.LinkedList;
//import java.util.List;
//
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.os.Bundle;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.BaseAdapter;
//import android.widget.GridView;
//import android.widget.ImageView;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import com.google.gson.Gson;
//import com.nostra13.universalimageloader.core.DisplayImageOptions;
//import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
//import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
//import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
//import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
//import com.master.univt.R;
//
///**
// * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
// */
//public class ImageListActivity extends AbsListViewBaseActivity {
//
//	DisplayImageOptions options;
//
//	String[] imageUrls;
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.ac_image_list);
//
//		Bundle bundle = getIntent().getExtras();
//		imageUrls = bundle.getStringArray(Extra.IMAGES);
//
//
//	    getActionBar().setHomeButtonEnabled(true);
//	    getActionBar().setDisplayHomeAsUpEnabled(true);
//
//		options = new DisplayImageOptions.Builder()
//			.showImageOnLoading(R.drawable.ic_stub)
//			.showImageForEmptyUri(R.drawable.ic_empty)
//			.showImageOnFail(R.drawable.ic_error)
//			.cacheInMemory(true)
//			.cacheOnDisk(true)
//			.considerExifParams(true)
//			.displayer(new RoundedBitmapDisplayer(20))
//			.build();
//
//		listView = (ListView) findViewById(android.R.id.list);
//		((ListView) listView).setAdapter(new ItemAdapter());
//
//		Books books = new Gson().fromJson(getJOSN(), Books.class);
//		BooksListViewAdapter gridViewAdapter = new BooksListViewAdapter(this,
//				books.getCourses());
//		listView.setAdapter(gridViewAdapter);
//
//		listView.setOnItemClickListener(new OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//				startImagePagerActivity(position);
//			}
//		});
//	}
//
//	private String getJOSN() {
//		StringBuilder stringBuilder = new StringBuilder();
//		try {
//			BufferedReader reader = new BufferedReader(new InputStreamReader(
//					getAssets().open("books.txt"), "UTF-8"));
//			String line;
//			while ((line = reader.readLine()) != null) {
//				stringBuilder.append(line + "\n");
//			}
//
//			reader.close();
//		} catch (IOException e) {
//		}
//		return stringBuilder.toString();
//	}
//
//	@Override
//	public void onBackPressed() {
//		AnimateFirstDisplayListener.displayedImages.clear();
//		super.onBackPressed();
//	}
//
////	private void startImagePagerActivity(int position) {
////		Books book = (Books) listView.getItemAtPosition(position);
////		Intent intent = new Intent(this, BooksDetailsActivity.class);
////		intent.putExtra(Extra.BOOK, book);
////		startActivity(intent);
////	}
//
//	private static class ViewHolder {
//		TextView text;
//		ImageView image;
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(final MenuItem item) {
//
//		if (item.getItemId() == android.R.id.home) {
//			onBackPressed();
//		}
//
//		return super.onOptionsItemSelected(item);
//	}
//
//	class ItemAdapter extends BaseAdapter {
//
//		private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
//
//		@Override
//		public int getCount() {
//			return imageUrls.length;
//		}
//
//		@Override
//		public Object getItem(int position) {
//			return position;
//		}
//
//		@Override
//		public long getItemId(int position) {
//			return position;
//		}
//
//		@Override
//		public View getView(final int position, View convertView, ViewGroup parent) {
//			View view = convertView;
//			final ViewHolder holder;
//			if (convertView == null) {
//				view = getLayoutInflater().inflate(R.layout.item_list_image, parent, false);
//				holder = new ViewHolder();
//				holder.text = (TextView) view.findViewById(R.id.text);
//				holder.image = (ImageView) view.findViewById(R.id.image);
//				view.setTag(holder);
//			} else {
//				holder = (ViewHolder) view.getTag();
//			}
//
//			holder.text.setText("Item " + (position + 1));
//
//			imageLoader.displayImage(imageUrls[position], holder.image, options, animateFirstListener);
//
//			return view;
//		}
//	}
//
//	private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {
//
//		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());
//
//		@Override
//		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//			if (loadedImage != null) {
//				ImageView imageView = (ImageView) view;
//				boolean firstDisplay = !displayedImages.contains(imageUri);
//				if (firstDisplay) {
//					FadeInBitmapDisplayer.animate(imageView, 500);
//					displayedImages.add(imageUri);
//				}
//			}
//		}
//	}
//}