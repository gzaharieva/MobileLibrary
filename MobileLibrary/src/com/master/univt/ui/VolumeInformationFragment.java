package com.master.univt.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.api.services.books.model.Volume;
import com.master.univt.Constants;
import com.master.univt.R;
import com.master.univt.support.http.Search;

import java.io.IOException;


/**
 * The course information tab fragment. Represents the information about details and reviews of the
 * selected course.
 * 
 * @author Gabriela Zaharieva
 */
public class VolumeInformationFragment extends Fragment
{
    private final String LOG_TAG = VolumeInformationFragment.class.getSimpleName();
  public Context context;
  /** The root view of the fragment. */
  private View rootView;
    private TextView description;

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

  public VolumeInformationFragment()
  {
  }

  @Override
  public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState)
  {
    rootView = inflater.inflate(R.layout.fragment_volume_details, container, false);
    description = (TextView) rootView.findViewById(R.id.description);

    return rootView;
  }

  @Override
  public void onActivityCreated(final Bundle savedInstanceState)
  {
    super.onActivityCreated(savedInstanceState);
      String bookInfo = getArguments().getString(Constants.BOOK_INFO);
      Volume volume = null;
      try {
          volume = Search.JSON_FACTORY.fromString(bookInfo, Volume.class);
      } catch (IOException e) {
          Log.e(LOG_TAG, "", e);
      }

    init(volume);
  }

  private void init(Volume volume)
  {
      Volume.VolumeInfo volumeInfo = volume.getVolumeInfo();
      if(volumeInfo != null) {
          description.setText(volumeInfo.getDescription());
      }
  }

}
