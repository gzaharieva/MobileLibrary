package com.master.univt.services;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.google.api.services.books.model.Bookshelf;
import com.google.api.services.books.model.Bookshelves;
import com.google.api.services.books.model.Volumes;
import com.master.univt.support.http.Search;
import com.master.univt.support.http.UserLibrary;
import com.master.univt.ui.SplashActivity;


/**
 * Represents an asynchronous login task used to get course content.
 * 
 * @author Gabriela Zaharieva
 */
public class VolumesService extends AsyncTask<String, Integer, Volumes>
{
  private final CommunicationService<Volumes> communicationService;
    
  public VolumesService(final CommunicationService<Volumes> communicationService, final Activity activity)
  {
    this.communicationService = communicationService;
  }

  @Override
  protected Volumes doInBackground(final String... shelf)
  {
      return UserLibrary.getBookshelfVolumes(shelf[0]);
  }

  @Override
  protected void onPostExecute(final Volumes reqestCodeStatus)
  {
    communicationService.onRequestCompleted(reqestCodeStatus);
  }
}
