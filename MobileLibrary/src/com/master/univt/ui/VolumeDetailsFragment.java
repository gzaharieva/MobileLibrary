package com.master.univt.ui;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.api.services.books.model.Volume;
import com.master.univt.Constants;
import com.master.univt.R;
import com.master.univt.support.http.Search;

/**
 * The course information tab fragment. Represents the information about details and reviews of the
 * selected course.
 * 
 * @author Gabriela Zaharieva
 */
public class VolumeDetailsFragment extends Fragment
{
  private final String LOG_TAG = VolumeDetailsFragment.class.getSimpleName();
  public Context context;
  /** The root view of the fragment. */
  private View rootView;
  private Button webReaderLinkView;
  private TextView epubAvailableView;
  private TextView pdfAvailableView;
  private TextView pageCountView;
  private TextView isbn10;
  private TextView isbn13;

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

  public VolumeDetailsFragment()
  {
  }

  @Override
  public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState)
  {
    rootView = inflater.inflate(R.layout.fragment_volume_details, container, false);
    webReaderLinkView = (Button) rootView.findViewById(R.id.web_reader_link);


    pdfAvailableView = (TextView) rootView.findViewById(R.id.pdf_available);
    epubAvailableView = (TextView) rootView.findViewById(R.id.epub_available);
    pageCountView = (TextView) rootView.findViewById(R.id.page_counts_view);
    isbn10 = (TextView) rootView.findViewById(R.id.isbn_10);
    isbn13 = (TextView) rootView.findViewById(R.id.isbn_13);

    return rootView;
  }

  @Override
  public void onActivityCreated(final Bundle savedInstanceState)
  {
    super.onActivityCreated(savedInstanceState);
    String bookInfo = getArguments().getString(Constants.BOOK_INFO);
    Volume volume = null;
    try
    {
      volume = Search.JSON_FACTORY.fromString(bookInfo, Volume.class);

      final Volume.AccessInfo accessInfo = volume.getAccessInfo();
      if (accessInfo != null)
      {
        webReaderLinkView.setText(getString(R.string.web_reader));
        pdfAvailableView.setText((accessInfo.getPdf().getIsAvailable()) ? getString(R.string.yes) : getString(R.string.no));
        epubAvailableView
          .setText((accessInfo.getEpub().getIsAvailable()) ? getString(R.string.yes) : getString(R.string.no));
      }
      Volume.VolumeInfo volumeInfo = volume.getVolumeInfo();
      if (volumeInfo != null)
      {
        pageCountView.setText(volumeInfo.getPageCount() != null ? String.valueOf(volumeInfo.getPageCount()) : getString(R.string.not_presented));
        List<Volume.VolumeInfo.IndustryIdentifiers> industryIdentifiers = volumeInfo.getIndustryIdentifiers();
        if (industryIdentifiers != null)
        {
          isbn10.setText(industryIdentifiers.get(0).getIdentifier());
          if (industryIdentifiers.get(1) != null)
          {
            isbn13.setText(industryIdentifiers.get(1).getIdentifier());
          }
          else
          {
            isbn13.setText(getString(R.string.not_presented));
          }
        }
        else
        {
          isbn10.setText(getString(R.string.not_presented));
          isbn13.setText(getString(R.string.not_presented));
        }
      }

        webReaderLinkView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(accessInfo != null) {
                    String url = accessInfo.getWebReaderLink();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            }
        });

    }
    catch (IOException e)
    {
      Log.e(LOG_TAG, "", e);
    }

  }

}
