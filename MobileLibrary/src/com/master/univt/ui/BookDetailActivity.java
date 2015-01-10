package com.master.univt.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TabHost;
import android.widget.TextView;

import com.google.api.services.books.model.Bookshelf;
import com.google.api.services.books.model.Bookshelves;
import com.google.api.services.books.model.Volume;
import com.master.univt.Constants;
import com.master.univt.R;
import com.master.univt.support.http.Search;
import com.master.univt.support.http.UserLibrary;
import com.master.univt.support.util.ImageLoaderUtil;
import com.master.univt.utils.FragmentStateTabsPagerAdapter;
import com.master.univt.utils.GridBookshelfListAdapter;
import com.master.univt.utils.PagerUtils;

/**
 * Created by LQG on 2014/12/5.
 */
// @EActivity(R.layout.book_detail)
public class BookDetailActivity extends ActionBarActivity implements TabHost.OnTabChangeListener,
  ViewPager.OnPageChangeListener
{
  private final String LOG_TAG = BookDetailActivity.class.getSimpleName();
  // @ViewById(R.id.tl_custom)
  Toolbar toolbar;

  // @ViewById
  ImageView thumbnail;

  // @ViewById(R.id.book_title)
  TextView titleTv;

  // @ViewById(R.id.book_authors)
  TextView authorTv;

  // @ViewById
  TextView language;

  // @ViewById
  TextView publishedDate;

  // @ViewById
  TextView categories;
  // @ViewById
  RatingBar averageRating;
  // @ViewById
  TextView ratingsCount;
  // @ViewById
  TextView description;
    private Button addVolumeButton;

  private ViewPager tabsViewPager;
  private TabHost tabHostView;
  private FragmentStateTabsPagerAdapter mPagerAdapter;


  // @AfterViews

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.book_detail);

    toolbar = (Toolbar) findViewById(R.id.tl_custom);
    thumbnail = (ImageView) findViewById(R.id.thumbnail);
    titleTv = (TextView) findViewById(R.id.book_title);
    authorTv = (TextView) findViewById(R.id.book_authors);
    language = (TextView) findViewById(R.id.language);
    publishedDate = (TextView) findViewById(R.id.publishedDate);
    categories = (TextView) findViewById(R.id.categories);
    averageRating = (RatingBar) findViewById(R.id.averageRating);
    ratingsCount = (TextView) findViewById(R.id.ratingsCount);
    description = (TextView) findViewById(R.id.description);
    tabHostView = (TabHost) findViewById(android.R.id.tabhost);
      addVolumeButton = (Button) findViewById(R.id.action_add_volume);

    tabsViewPager = (ViewPager) findViewById(R.id.viewpager);

    toolbar.setTitleTextColor(0xffffffff);
    setSupportActionBar(toolbar);

    afterView();
  }

  void afterView()
  {
    restoreActionBar();
    String bookInfo = getIntent().getStringExtra(Constants.BOOK_INFO);
    final String bookshelf = getIntent().getStringExtra(Constants.BOOKSHELF);
    Volume volume = null;
    try
    {
      volume = Search.JSON_FACTORY.fromString(bookInfo, Volume.class);
    }
    catch (IOException e)
    {
      Log.e(LOG_TAG, "", e);
    }
    loadData(volume);
      final   Volume currentVolume = volume;

    String courseDetails = getString(R.string.tab_volume_info);
    String courseContent = getString(R.string.tab_volume_description);

    PagerUtils pagerUtils = new PagerUtils();
    pagerUtils.initialiseTabHost(this, tabHostView, courseContent, courseDetails);
    intialiseViewPager(volume);
    addVolumeButton.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            new QueryTask().execute("0", currentVolume.getId());
        }
    });
  }

  private void intialiseViewPager(final Volume volume)
  {

    List<Fragment> fragments = new Vector<Fragment>();
    VolumeInformationFragment content = new VolumeInformationFragment();
    Bundle detailsBundleArguments = new Bundle();
    try
    {
      detailsBundleArguments.putString(Constants.BOOK_INFO, Search.JSON_FACTORY.toString(volume));
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    content.setArguments(detailsBundleArguments);

    VolumeDetailsFragment details = new VolumeDetailsFragment();
    Bundle detailsBundle = new Bundle();
    try
    {
      detailsBundle.putString(Constants.BOOK_INFO, Search.JSON_FACTORY.toString(volume));
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
      details.setArguments(detailsBundle);

    fragments.add(content);
    fragments.add(details);

    mPagerAdapter = new FragmentStateTabsPagerAdapter(getSupportFragmentManager(), fragments);

    tabsViewPager.setOffscreenPageLimit(2);
    tabsViewPager.setAdapter(mPagerAdapter);
    tabsViewPager.setOnPageChangeListener(this);

    tabHostView.setOnTabChangedListener(this);
    tabHostView.getTabWidget().setEnabled(true);

  }

  private void restoreActionBar()
  {
    toolbar.setTitleTextColor(0xffffffff);
    setSupportActionBar(toolbar);
    ActionBar actionBar = getSupportActionBar();
    actionBar.setDisplayHomeAsUpEnabled(true);
    actionBar.setDisplayShowTitleEnabled(true);
    actionBar.setHomeButtonEnabled(true);
  }

  private void loadData(Volume volume)
  {
    try
    {
      Log.d(LOG_TAG, volume.toPrettyString());
      Volume.VolumeInfo volumeInfo = volume.getVolumeInfo();

      Volume.VolumeInfo.ImageLinks imageLinks = volume.getVolumeInfo().getImageLinks();

      if (imageLinks != null)
      {
        ImageLoaderUtil.getImageLoader().displayImage(imageLinks.getThumbnail(), thumbnail);
      }

      titleTv.setText(volumeInfo.getTitle());

      String author = getAutor(volume);
      if (null == author || author.trim().equals(""))
        authorTv.setVisibility(View.GONE);
      else
        authorTv.setText(author);

      String languageStr = getLanguage(volume);
      if (null == languageStr || languageStr.trim().equals(""))
        language.setVisibility(View.GONE);
      else
        language.setText(languageStr);

      String categoriesStr = getCategories(volume);
      if (null == categoriesStr || categoriesStr.trim().equals(""))
        categories.setVisibility(View.GONE);
      else
        categories.setText(categoriesStr);

      String publishedDateStr = getPublishedDate(volume);
      if (null == publishedDateStr || publishedDateStr.trim().equals(""))
        publishedDate.setVisibility(View.GONE);
      else
        publishedDate.setText(publishedDateStr);

      try
      {
        averageRating.setRating(Float.valueOf(volumeInfo.getAverageRating() + ""));
      }
      catch (Exception e)
      {
      }

      ratingsCount.setText("(" + (volumeInfo.getRatingsCount() == null ? 0 : volumeInfo.getRatingsCount()) + ")");

      //description.setText(volumeInfo.getDescription());
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  private String getPublishedDate(Volume volume)
  {
    return "Published Date: " + volume.getVolumeInfo().getPublishedDate();
  }

  private String getLanguage(Volume volume)
  {
    return "Language: " + volume.getVolumeInfo().getLanguage();
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    switch (item.getItemId())
    {
      case android.R.id.home:
        finish();
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  public static String getAutor(Volume result)
  {
    StringBuilder sb = new StringBuilder();
    List<String> authors = result.getVolumeInfo().getAuthors();

    if (authors == null)
      return "";

    for (String author : authors)
    {
      sb.append(", " + author);
    }
    return sb.toString().replaceFirst(", ", "");
  }

  public static String getCategories(Volume result)
  {
    StringBuilder sb = new StringBuilder();
    List<String> categories = result.getVolumeInfo().getCategories();

    if (categories == null)
      return "";

    for (String categorie : categories)
    {
      sb.append(", " + categorie);
    }
    String content = sb.toString().replaceFirst(", ", "");
    if (content != null)
      content = "Categories: " + content;
    return content;
  }

  public static String getImageLink(Volume.VolumeInfo volumeInfo)
  {
    Volume.VolumeInfo.ImageLinks imageLinks = volumeInfo.getImageLinks();
    return imageLinks == null ? null : imageLinks.getThumbnail();
  }

  @Override
  public void onTabChanged(final String tag)
  {
    int position = this.tabHostView.getCurrentTab();
    this.tabsViewPager.setCurrentItem(position);
  }

  @Override
  public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels)
  {
  }

  @Override
  public void onPageSelected(final int position)
  {
    this.tabHostView.setCurrentTab(position);
  }

  @Override
  public void onPageScrollStateChanged(final int state)
  {
  }

    private class QueryTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            UserLibrary.addVolumes(params[0], params[1]);
            return null;
        }

        @Override
        protected void onPostExecute(Void bookshelves) {

        }
    }

}
