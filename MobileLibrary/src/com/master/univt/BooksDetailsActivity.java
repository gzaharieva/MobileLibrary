package com.master.univt;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.master.univt.utils.Books;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class BooksDetailsActivity extends FragmentActivity {

	private DisplayImageOptions options;
	/** The image loader. */
	private final ImageLoader imageLoader = ImageLoader.getInstance();
	private Books book;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.books_detail);

	    getActionBar().setHomeButtonEnabled(true);
	    getActionBar().setDisplayHomeAsUpEnabled(true);
	    
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_empty)
				.showImageForEmptyUri(R.drawable.ic_empty)
				.showImageOnFail(R.drawable.ic_empty)
				.cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
				.build();

		if (!imageLoader.isInited()) {
			imageLoader.init(ImageLoaderConfiguration.createDefault(this));
		}

		Bundle bundle = getIntent().getExtras();

		book = (Books) bundle.getSerializable(Constants.Extra.BOOK);
		initializeViewComponents();
	}

	private void initializeViewComponents() {
		ImageView imageView = (ImageView) findViewById(R.id.image);
		imageLoader.displayImage(book.getImageURL(), imageView, options, null);

		// TODO Auto-generated method stub
		TextView title = (TextView) findViewById(R.id.title);
		title.setText(book.getTitle());
		
		getActionBar().setTitle(book.getTitle());

		TextView author = (TextView) findViewById(R.id.author);
		//author.setText(book.getAuthor());

		TextView anotation = (TextView) findViewById(R.id.anotation);
		//anotation.setText(book.getAnotation());
		
		RatingBar ratingBar = (RatingBar) findViewById(R.id.rating_bar);
		//ratingBar.setRating((float) book.getRating());
		
		TextView price = (TextView) findViewById(R.id.price);
		//anotation.setText(book.getPrice() + " лв.");
		
		
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

}
