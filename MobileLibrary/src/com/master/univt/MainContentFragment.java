package com.master.univt;

import static com.master.univt.Constants.IMAGES;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.nostra13.universalimageloader.utils.L;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * The user courses fragment.
 * 
 * @author Gabriela Zaharieva
 */
public class MainContentFragment extends Fragment 
{
	private static final String TEST_FILE_NAME = "Universal Image Loader @#&=+-_.,!()~'%20.png";
  public Context context;
  // interface via which we communicate to hosting Activity
  
  private View rootView;

  @Override
  public void onAttach(final Activity activity)
  {
    super.onAttach(activity);
    context = getActivity();
  }

  @Override
  public void onSaveInstanceState(final Bundle outState)
  {
    super.onSaveInstanceState(outState);
  }

  @Override
  public void onCreate(final Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState)
  {
    rootView = inflater.inflate(R.layout.ac_home, container, false);


	File testImageOnSdCard = new File("/mnt/sdcard", TEST_FILE_NAME);
	if (!testImageOnSdCard.exists()) {
		copyTestImageToSdCard(testImageOnSdCard);
	}
   
    setHasOptionsMenu(true);
    return rootView;
  }


	private void copyTestImageToSdCard(final File testImageOnSdCard) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					InputStream is = getActivity().getAssets().open(TEST_FILE_NAME);
					FileOutputStream fos = new FileOutputStream(testImageOnSdCard);
					byte[] buffer = new byte[8192];
					int read;
					try {
						while ((read = is.read(buffer)) != -1) {
							fos.write(buffer, 0, read);
						}
					} finally {
						fos.flush();
						fos.close();
						is.close();
					}
				} catch (IOException e) {
					L.w("Can't copy test image onto SD card");
				}
			}
		}).start();
	}

}
