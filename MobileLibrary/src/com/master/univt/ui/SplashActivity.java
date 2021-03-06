package com.master.univt.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.master.univt.Constants;
import com.master.univt.R;
import com.master.univt.configs.BuildConfig;
import com.master.univt.model.User;
import com.master.univt.model.DBHelper;
import com.master.univt.model.UserModel;
import com.master.univt.services.CommunicationService;
import com.master.univt.services.SharedPreferencedSingleton;
import com.master.univt.support.GlobalApplication;
import com.master.univt.support.http.RefreshTokenService;

import java.io.IOException;

/**
 * Splash screen. The initial screen view of the application. An example full-screen activity that
 * shows and hides the system UI (i.e. status bar and navigation/system bar) with user interaction.
 *
 * @author Gabriela Zaharieva
 */
//@EActivity(R.layout.activity_splash)
public class SplashActivity extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private final String LOG_TAG = SplashActivity.class.getSimpleName();
    private static int REQUEST_EXIT = 0;

    public static final int REQUEST_CODE_TOKEN_AUTH = 6;

    //@ViewById(R.id.tl_custom)
    Toolbar toolbar;


    private static final int STATE_DEFAULT = 0;
    private static final int STATE_SIGN_IN = 1;
    private static final int STATE_IN_PROGRESS = 2;
    private static final int STATE_FAILED = 3;

    private static final int RC_SIGN_IN = 0;

    private static final int DIALOG_PLAY_SERVICES_ERROR = 0;

    private static final String SAVED_PROGRESS = "sign_in_progress";
    // GoogleApiClient wraps our service connection to Google Play services and
    // provides access to the users sign in state and Google's APIs.
    private GoogleApiClient mGoogleApiClient;
    // We use mSignInProgress to track whether user has clicked sign in.
    // mSignInProgress can be one of three values:
    //
    //       STATE_DEFAULT: The default state of the application before the user
    //                      has clicked 'sign in', or after they have clicked
    //                      'sign out'.  In this state we will not attempt to
    //                      resolve sign in errors and so will display our
    //                      Activity in a signed out state.
    //       STATE_SIGN_IN: This state indicates that the user has clicked 'sign
    //                      in', so resolve successive errors preventing sign in
    //                      until the user has successfully authorized an account
    //                      for our app.
    //   STATE_IN_PROGRESS: This state indicates that we have started an intent to
    //                      resolve an error, and so we should not start further
    //                      intents until the current intent completes.
    private int mSignInProgress;
    // Used to store the PendingIntent most recently returned by Google Play
    // services until the user clicks 'sign in'.
    private PendingIntent mSignInIntent;

    // Used to store the error code most recently returned by Google Play services
    // until the user clicks 'sign in'.
    private int mSignInError;

    //    @ViewById(R.id.sign_in_button)
    public SignInButton mSignInButton;
    private TextView mStatus;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            // Here activity is brought to front, not created,
            // so finishing this will get you to the last viewed activity
            finish();
            return;
        }
        SharedPreferencedSingleton.getInstance().Initialize(this);
        mGoogleApiClient = buildGoogleApiClient();
        if (savedInstanceState != null) {
            mSignInProgress = savedInstanceState
                    .getInt(SAVED_PROGRESS, STATE_DEFAULT);
        }

        toolbar = (Toolbar) findViewById(R.id.tl_custom);
        mSignInButton = (SignInButton) findViewById(R.id.sign_in_button);
        mStatus = (TextView) findViewById(R.id.mStatus);

        mSignInButton.setOnClickListener(this);

        mGoogleApiClient.connect();

        Crashlytics.start(this);
        GlobalApplication.getInstance().trackView(getString(R.string.app_name));
    }

    private GoogleApiClient buildGoogleApiClient() {
        // When we build the GoogleApiClient we specify where connected and
        // connection failed callbacks should be returned, which Google APIs our
        // app uses and which OAuth 2.0 scopes our app requests.
        return new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        GlobalApplication.getInstance().trackView(getString(R.string.app_name));
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_PROGRESS, mSignInProgress);
    }

    @Override
    public void onClick(View v) {
        if (!mGoogleApiClient.isConnecting()) {
            // We only process button clicks when GoogleApiClient is not transitioning
            // between connected and not connected.
            switch (v.getId()) {
                case R.id.sign_in_button:
                    //   mStatus.setText(R.string.status_signing_in);
                    resolveSignInError();
                    break;
            }
        }
    }

    /* Starts an appropriate intent or dialog for user interaction to resolve
   * the current error preventing the user from being signed in.  This could
   * be a dialog allowing the user to select an account, an activity allowing
   * the user to consent to the permissions being requested by your app, a
   * setting to enable device networking, etc.
   */
    private void resolveSignInError() {
        if (mSignInIntent != null) {
            // We have an intent which will allow our user to sign in or
            // resolve an error.  For example if the user needs to
            // select an account to sign in with, or if they need to consent
            // to the permissions your app is requesting.

            try {
                // Send the pending intent that we stored on the most recent
                // OnConnectionFailed callback.  This will allow the user to
                // resolve the error currently preventing our connection to
                // Google Play services.
                mSignInProgress = STATE_IN_PROGRESS;
                startIntentSenderForResult(mSignInIntent.getIntentSender(),
                        RC_SIGN_IN, null, 0, 0, 0);
            } catch (IntentSender.SendIntentException e) {
                Log.i(LOG_TAG, "Sign in intent could not be sent: "
                        + e.getLocalizedMessage());
                // The intent was canceled before it was sent.  Attempt to connect to
                // get an updated ConnectionResult.
                mSignInProgress = STATE_SIGN_IN;
                mGoogleApiClient.connect();
            }
        } else {
            // Google Play services wasn't able to provide an intent for some
            // error types, so we show the default Google Play services error
            // dialog which may still start an intent on our behalf if the
            // user can resolve the issue.
         //   showDialog(DIALOG_PLAY_SERVICES_ERROR);
        }
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason.
        // We call connect() to attempt to re-establish the connection or get a
        // ConnectionResult that we can attempt to resolve.
        mGoogleApiClient.connect();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {

        Log.d(LOG_TAG, "requestCde:"+ requestCode );
//        if (requestCode == REQUEST_CODE_TOKEN_AUTH && resultCode == RESULT_OK) {
//            Bundle extra = data.getExtras();
//            String oneTimeToken = extra.getString("authtoken");
//            Log.d(LOG_TAG, oneTimeToken);
//          //  authenticateUser("8798", "Gabriela", oneTimeToken);
//        }
        
        switch (requestCode) {
            case RC_SIGN_IN:
                if (resultCode == RESULT_OK) {
                    // If the error resolution was successful we should continue
                    // processing errors.
                    mSignInProgress = STATE_SIGN_IN;
                } else {
                    Log.d(LOG_TAG, "RC_SIGN_IN > STATE_DEFAULT");
                    // If the error resolution was not successful or the user canceled,
                    // we should stop processing errors.
//                    mSignInProgress = STATE_DEFAULT;
                    mSignInProgress = STATE_FAILED;
                }

                if (!mGoogleApiClient.isConnecting()) {
                    // If Google Play services resolved the issue with a dialog then
                    // onStart is not called so we need to re-attempt connection here.
                    mGoogleApiClient.connect();
                }
                break;
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_PLAY_SERVICES_ERROR:
                Log.d(LOG_TAG, "DIALOG_PLAY_SERVICES_ERROR" );
                if (GooglePlayServicesUtil.isUserRecoverableError(mSignInError)) {
                    return GooglePlayServicesUtil.getErrorDialog(
                            mSignInError,
                            this,
                            RC_SIGN_IN,
                            new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    Log.e(LOG_TAG, "Google Play services resolution cancelled");
                                    mSignInProgress = STATE_DEFAULT;
                                    // mStatus.setText(R.string.status_signed_out);
                                }
                            });
                } else {
                    return null;
//                            new AlertDialog.Builder(this)
//                            .setMessage(R.string.play_services_error)
//                            .setPositiveButton(R.string.close,
//                                    new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            Log.e(TAG, "Google Play services error could not be "
//                                                    + "resolved: " + mSignInError);
//                                            mSignInProgress = STATE_DEFAULT;
//                                            mStatus.setText(R.string.status_signed_out);
//                                        }
//                                    }).create();
                }
            default:
                return super.onCreateDialog(id);
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might
        // be returned in onConnectionFailed.
        Log.i(LOG_TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode()+ "-" + mSignInProgress);

        if (mSignInProgress == STATE_FAILED) {
         //   authenticateUser("117463411595501910870", "Gabriela Zaharieva", "oAuth");
        }
        //  mStatus.setText(result.getErrorCode());

        if (result.getErrorCode() == ConnectionResult.API_UNAVAILABLE) {
            // An API requested for GoogleApiClient is not available. The device's current
            // configuration might not be supported with the requested API or a required component
            // may not be installed, such as the Android Wear application. You may need to use a
            // second GoogleApiClient to manage the application's optional APIs.
        } else if (mSignInProgress != STATE_IN_PROGRESS) {
            // We do not have an intent in progress so we should store the latest
            // error resolution intent for use when the sign in button is clicked.
            mSignInIntent = result.getResolution();
            mSignInError = result.getErrorCode();

            if (mSignInProgress == STATE_SIGN_IN) {
                // STATE_SIGN_IN indicates the user already clicked the sign in button
                // so we should continue processing errors until the user is signed in
                // or they click cancel.
               resolveSignInError();
            }
        }

        // In this sample we consider the user signed out whenever they do not have
        // a connection to Google Play services.
        //onSignedOut();
    }


    /* onConnected is called when our Activity successfully connects to Google
  * Play services.  onConnected indicates that an account was selected on the
  * device, that the selected account has granted any requested permissions to
  * our app and that we were able to establish a service connection to Google
  * Play services.
  */
    @Override
    public void onConnected(Bundle connectionHint) {
        // Reaching onConnected means we consider the user signed in.
        Log.i(LOG_TAG, "onConnected");
        // Indicate that the sign in process is complete.
        mSignInProgress = STATE_DEFAULT;
        mSignInButton.setEnabled(false);
        // Retrieve some profile information to personalize our app for the user.
        final Person currentUser = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);

        SharedPreferencedSingleton sharedPreferencedSingleton = SharedPreferencedSingleton.getInstance();
        if(!sharedPreferencedSingleton.getString(Constants.PREFS_USERNAME, "").isEmpty()) {
            Intent intent;
            intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        }else {

            if (currentUser != null) {
                User user = new User();
                user.setName(currentUser.getDisplayName());
                user.setUsername(Plus.AccountApi.getAccountName(mGoogleApiClient));
                user.setUId(currentUser.getImage().getUrl());
                insertOrReplace(this, user);

                new RequestTokenTask(new CommunicationService<String>() {
                    @Override
                    public void onRequestCompleted(String token) {
                        Log.d(LOG_TAG, "RequestTokenTask");
                        if (token != null) {
                            authenticateUser(currentUser.getId(), Plus.AccountApi.getAccountName(mGoogleApiClient), token);
                        } else {
                            Log.d(LOG_TAG, "Refresh token:onRequestCompleted");
                            new AlertDialog.Builder(SplashActivity.this)
                                    .setTitle(R.string.app_name)
                                    .setMessage(R.string.app_name)
                                    .create().show();
                            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                            mSignInButton.setEnabled(true);
                            mSignInProgress = STATE_FAILED;
                        }
                    }
                }).execute();
            } else {
                new AlertDialog.Builder(SplashActivity.this)
                        .setTitle(R.string.app_name)
                        .setMessage(R.string.no_internet_connection).setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                        .create().show();
                mSignInButton.setEnabled(true);
                mSignInProgress = STATE_FAILED;
                Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                mGoogleApiClient.disconnect();

            }
        }
    }


    private void insertOrReplace(final Context context, final User user)
    {
        UserModel usermodel = new UserModel(DBHelper.getInstance());
        usermodel.saveUser(user);
    }
    
  public class  RequestTokenTask extends AsyncTask<Void, Void, String> {


      private CommunicationService<String> communicationService;
      public RequestTokenTask(CommunicationService<String> communicationService) {
          this.communicationService = communicationService;
      }

      @Override
        protected String doInBackground(Void... params) {
            String token = null;

            Bundle appActivities = new Bundle();
//            appActivities.putString(GoogleAuthUtil.KEY_REQUEST_VISIBLE_ACTIVITIES,
//                    "com.master.univt.ui.SplashActivity");
            // String scopes = "oauth2:server:client_id:933905793255-pa7ifm7m6elch8nuu9ucpijr3br8cjqv.apps.googleusercontent.com:api_scope:"+Scopes.PLUS_ME;//:api_scope:<SCOPE1> <SCOPE2>
            final String scopes = "oauth2:https://www.googleapis.com/auth/books";// + Scopes.PLUS_ME;
            String code = null;
            try {
                Log.d(LOG_TAG, Plus.AccountApi.getAccountName(mGoogleApiClient));
                token = GoogleAuthUtil.getToken(
                        SplashActivity.this,
                        Plus.AccountApi.getAccountName(mGoogleApiClient),
                        scopes);                                            // String scope
//                            appActivities);                                 // Bundle bundle);
            } catch (IOException transientEx) {
                // Network or server error, try later
                Log.e(LOG_TAG, transientEx.toString());
            } catch (UserRecoverableAuthException e) {
                // Recover (with e.getIntent())
                Log.e(LOG_TAG, e.toString());
                Intent recover = e.getIntent();
                startActivityForResult(recover, REQUEST_CODE_TOKEN_AUTH);
            } catch (GoogleAuthException authEx) {
                // The call is not ever expected to succeed
                // assuming you have already verified that
                // Google Play services is installed.
                Log.e(LOG_TAG,"", authEx);
            }

            return token;
        }

        @Override
        protected void onPostExecute(String token) {
            SharedPreferencedSingleton s = SharedPreferencedSingleton.getInstance();
            String refresh_token = s.getString(Constants.PREFS_OAUTH_TOKEN, "");
            Log.i(LOG_TAG, "Access token retrieved:" + token + "\n"+ refresh_token);
            
            
            String params = String.format("refresh_token=%s&client_id=%s&grant_type=refresh_token&redirect_uri=urn:ietf:wg:oauth:2.0:oob", token, BuildConfig.CLIENT_ID);
            new RefreshTokenService(new CommunicationService<String>() {
                @Override
                public void onRequestCompleted(String resultData) {
                    if(resultData != null) {
                        communicationService.onRequestCompleted(resultData);
                    } else{
//                        new AlertDialog.Builder(SplashActivity.this)
//                                .setTitle(R.string.app_name)
//                                .setMessage(R.string.message_not_unauthorized).setNeutralButton("OK", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        })
//                                .create().show();
//                        Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
//                        mGoogleApiClient.disconnect();
                        mSignInButton.setEnabled(true);
                    }
                }
            }, SplashActivity.this).execute(params);
        }
    };
    
    

    private void authenticateUser(String currentUserId, String username,  String oauthCode) {
        SharedPreferencedSingleton sharedPreferencedSingleton = SharedPreferencedSingleton.getInstance();
        sharedPreferencedSingleton.writePreference(Constants.PREFS_USER_ID, currentUserId);
        sharedPreferencedSingleton.writePreference(Constants.PREFS_USERNAME, username);
        
        sharedPreferencedSingleton.writePreference(Constants.PREFS_OAUTH_TOKEN, oauthCode);
        Intent intent;
        intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }


}
