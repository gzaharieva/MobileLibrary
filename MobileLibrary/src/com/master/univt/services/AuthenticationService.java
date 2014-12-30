package com.master.univt.services;

import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.master.univt.entities.ResponseStatusCode;


/**
 * Represents an asynchronous login task used to authenticate the user.
 * 
 * @author Gabriela Zaharieva
 */
public class AuthenticationService extends AsyncTask<Map<String, Object>, Integer, ResponseStatusCode>
{
  private static final String LOG_TAG = AuthenticationService.class.getSimpleName();

  /** The parent / calling activity fort the asyn task. */
  private final Activity parentActivity;
  /** The communication services between the calling activity and service. */
  private final CommunicationService<ResponseStatusCode> communicationService;
  private final String authenticationAPISource;

  /**
   * Initialization of the user login service asyn task.
   * 
   * @param communicationService the communication service between the calling activity and the task
   * @param activity the calling activity
   */
  public AuthenticationService(final CommunicationService<ResponseStatusCode> communicationService, final Activity activity,
    final String authenticationAPISource)
  {
    this.communicationService = communicationService;
    this.parentActivity = activity;
    this.authenticationAPISource = authenticationAPISource;
  }

  @Override
  protected ResponseStatusCode doInBackground(final Map<String, Object>... params)
  {
	  
	  try {
		Thread.sleep(3000);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    Map<String, Object> postParamters = params[0];
    ResponseStatusCode resultCode = null;
    
              resultCode = ResponseStatusCode.SUCCESFULL_AUTHENTICATION;


    return resultCode;
  }

  @Override
  protected void onPostExecute(final ResponseStatusCode reqestCodeStatus)
  {
    communicationService.onRequestCompleted(reqestCodeStatus);
  }


}
