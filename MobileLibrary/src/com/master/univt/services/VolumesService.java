package com.master.univt.services;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.google.api.services.books.model.Bookshelf;
import com.google.api.services.books.model.Bookshelves;
import com.google.api.services.books.model.Volumes;
import com.master.univt.support.http.Search;
import com.master.univt.support.http.UserLibrary;


/**
 * Represents an asynchronous login task used to get course content.
 * 
 * @author Gabriela Zaharieva
 */
public class VolumesService extends AsyncTask<String, Integer, Volumes>
{
  private static final String LOG_TAG = VolumesService.class.getSimpleName();
  private final CommunicationService<Volumes> communicationService;
  private final Activity activity;
    
  public VolumesService(final CommunicationService<Volumes> communicationService, final Activity activity)
  {
    this.activity = activity;
    this.communicationService = communicationService;
  }

  @Override
  protected Volumes doInBackground(final String... normalizedTitles)
  {

      Bookshelves bookshelves = UserLibrary.searchVolumes(normalizedTitles[0]);
      if(bookshelves != null) {
          for (Bookshelf shelf : bookshelves.getItems()) {
              Log.d(LOG_TAG, "Swhelf:" + shelf.getTitle() + "-" + shelf.getId());
          }
      }
      return Search.searchVolumes(normalizedTitles[0]);
  }

  @Override
  protected void onPostExecute(final Volumes reqestCodeStatus)
  {
    communicationService.onRequestCompleted(reqestCodeStatus);
  }
}
