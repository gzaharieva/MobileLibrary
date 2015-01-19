package com.master.univt.support.http;

import java.util.Map;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.master.univt.Constants;
import com.master.univt.entities.ResponseStatusCode;
import com.master.univt.services.CommunicationService;
import com.master.univt.services.RequestResponse;
import com.master.univt.services.SharedPreferencedSingleton;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Represents an asynchronous login task used to authenticate the user.
 * 
 * @author Gabriela Zaharieva
 */
public class AuthenticationService extends AsyncTask<String, Integer, String>
{
  private static final String LOG_TAG = AuthenticationService.class.getSimpleName();
  /** The shared preferences of the application. */
  private final SharedPreferencedSingleton sharedPreferencesInstance;
  /** The parent / calling activity fort the asyn task. */
  private final Context parentContext;
  /** The communication services between the calling activity and service. */
  private final CommunicationService<String> communicationService;

  /**
   * Initialization of the user login service asyn task.
   * 
   * @param communicationService the communication service between the calling activity and the task
   * @param context the calling activity
   */
  public AuthenticationService(final CommunicationService<String> communicationService, final Context context)
  {
    this.communicationService = communicationService;
    this.parentContext = context;
    sharedPreferencesInstance = SharedPreferencedSingleton.getInstance();
  }

  @Override
  protected String doInBackground(final String... params)
  {
    ResponseStatusCode serverResponseStatusCode = null;
      String resultToken = null;
    RequestResponse requestResponse = null;

      try
    {
//        Log.d(LOG_TAG, postParameters:" + postParameters);
        String url = "https://www.googleapis.com/oauth2/v3/token?"+params[0];
        Log.d(LOG_TAG, url);
        requestResponse =
                ServiceProvider.getInstance().executeHttpPost(url , null);
    }
    catch (Exception ex)
    {
      Log.e(LOG_TAG, "Can not execute the API Call.", ex);
      Crashlytics.logException(ex);
    }

    if (requestResponse != null)
    {
        Log.d(LOG_TAG, "RESULT:"+ requestResponse.getResult() + "-"+ requestResponse.getStatusCode());
      switch (requestResponse.getStatusCode())
      {
        case 200:
            JSONObject response = null;
            try {
                response = new JSONObject(requestResponse.getResult());
                if (response.has("access_token")) {
                    resultToken = response.getString("access_token");

                    SharedPreferencedSingleton sharedPreferencedSingleton = SharedPreferencedSingleton.getInstance();
                    sharedPreferencedSingleton.writePreference(Constants.PREFS_OAUTH_TOKEN, resultToken);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d(LOG_TAG, "response:"+ response);


          break;
        case 403:
          Log.e(LOG_TAG, "403 Forbidden. Can not authenticate in fron");
          serverResponseStatusCode = ResponseStatusCode.AUTHENTICATION_FAILED_EMAIL_IN_USE;
          break;
        case 404:
          Log.e(LOG_TAG, "Not found page. Login failed");
          serverResponseStatusCode = ResponseStatusCode.LOGIN_FAILED;
        case 500:
          Log.e(LOG_TAG, "Internal server error");
          serverResponseStatusCode = ResponseStatusCode.AUTHENTICATION_FAILED;
          break;
        default:
          serverResponseStatusCode = ResponseStatusCode.AUTHENTICATION_FAILED;
          break;
      }
    }
    else
    {
      serverResponseStatusCode = ResponseStatusCode.AUTHENTICATION_FAILED;
    }

    return resultToken;
  }

  @Override
  protected void onPostExecute(final String reqestCodeStatus)
  {
    communicationService.onRequestCompleted(reqestCodeStatus);
  }

}
