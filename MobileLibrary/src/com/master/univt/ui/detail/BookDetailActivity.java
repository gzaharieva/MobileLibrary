package com.master.univt.ui.detail;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.api.services.books.model.Volume;
import com.master.univt.R;
import com.master.univt.support.http.Search;
import com.master.univt.support.util.ImageLoaderUtil;


import java.io.IOException;
import java.util.List;

/**
 * Created by LQG on 2014/12/5.
 */
//@EActivity(R.layout.book_detail)
public class BookDetailActivity extends ActionBarActivity {
   // @ViewById(R.id.tl_custom)
    Toolbar toolbar;

    //@ViewById
    ImageView thumbnail;

    //@ViewById(R.id.book_title)
    TextView titleTv;

   // @ViewById(R.id.book_authors)
    TextView authorTv;

    //@ViewById
    TextView language;

   // @ViewById
    TextView publishedDate;

   // @ViewById
    TextView categories;
    //@ViewById
    RatingBar averageRating;
    //@ViewById
    TextView ratingsCount;
    //@ViewById
    TextView description;

    //@AfterViews

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        ratingsCount= (TextView) findViewById(R.id.ratingsCount);
        description = (TextView) findViewById(R.id.description);
        toolbar.setTitleTextColor(0xffffffff);
        setSupportActionBar(toolbar);
        
        afterView();
    }

    void afterView() {
        restoreActionBar();
        loadData();
    }

    private void restoreActionBar() {
        toolbar.setTitleTextColor(0xffffffff);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    private void loadData() {
        String bookInfo = getIntent().getStringExtra("bookInfo");
        try {
            Volume volume = Search.JSON_FACTORY.fromString(bookInfo, Volume.class);
            Volume.VolumeInfo volumeInfo = volume.getVolumeInfo();
            ImageLoaderUtil.getImageLoader().displayImage(volume.getVolumeInfo().getImageLinks().getThumbnail(), thumbnail);
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

            try {
                averageRating.setRating(Float.valueOf(volumeInfo.getAverageRating() + ""));
            } catch (Exception e) {
            }

            ratingsCount.setText("(" + (volumeInfo.getRatingsCount() == null ? 0 : volumeInfo.getRatingsCount()) + ")");

            description.setText(volumeInfo.getDescription());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getPublishedDate(Volume volume) {
        return "Published Date: " + volume.getVolumeInfo().getPublishedDate();
    }

    private String getLanguage(Volume volume) {
        return "Language: " + volume.getVolumeInfo().getLanguage();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static String getAutor(Volume result) {
        StringBuilder sb = new StringBuilder();
        List<String> authors = result.getVolumeInfo().getAuthors();

        if (authors == null)
            return "";

        for (String author : authors) {
            sb.append(", " + author);
        }
        return sb.toString().replaceFirst(", ", "");
    }

    public static String getCategories(Volume result) {
        StringBuilder sb = new StringBuilder();
        List<String> categories = result.getVolumeInfo().getCategories();

        if (categories == null)
            return "";

        for (String categorie : categories) {
            sb.append(", " + categorie);
        }
        String content = sb.toString().replaceFirst(", ", "");
        if (content != null)
            content = "Categories: " + content;
        return content;
    }

    public static String getImageLink(Volume.VolumeInfo volumeInfo) {
        Volume.VolumeInfo.ImageLinks imageLinks = volumeInfo.getImageLinks();
        return imageLinks == null ? null : imageLinks.getThumbnail();
    }

}
