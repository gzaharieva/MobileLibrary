package com.master.univt.ui;

import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.master.univt.R;

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
    public static final String PREFS_NAME = "mobile_library";
    public static final String PREFS_USER_ID = "user_id";

    //@ViewById(R.id.tl_custom)
    Toolbar toolbar;

    //@ViewById(R.id.content_view)
    public View contentView;

    private static final int STATE_DEFAULT = 0;
    private static final int STATE_SIGN_IN = 1;
    private static final int STATE_IN_PROGRESS = 2;

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
        mGoogleApiClient = buildGoogleApiClient();
        if (savedInstanceState != null) {
            mSignInProgress = savedInstanceState
                    .getInt(SAVED_PROGRESS, STATE_DEFAULT);
        }

        toolbar = (Toolbar) findViewById(R.id.tl_custom);
        contentView = findViewById(R.id.content_view);
        mSignInButton = (SignInButton) findViewById(R.id.sign_in_button);
        
        mSignInButton.setOnClickListener(this);

        mGoogleApiClient.connect();
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
            showDialog(DIALOG_PLAY_SERVICES_ERROR);
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
        switch (requestCode) {
            case RC_SIGN_IN:
                if (resultCode == RESULT_OK) {
                    // If the error resolution was successful we should continue
                    // processing errors.
                    mSignInProgress = STATE_SIGN_IN;
                } else {
                    // If the error resolution was not successful or the user canceled,
                    // we should stop processing errors.
                    mSignInProgress = STATE_DEFAULT;
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
                + result.getErrorCode());
        authenticateUser("117463411595501910870");
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
        Person currentUser = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);

        Bundle appActivities = new Bundle();
        appActivities.putString(GoogleAuthUtil.KEY_REQUEST_VISIBLE_ACTIVITIES,
                "<APP-ACTIVITY1> <APP-ACTIVITY2>");
        String scopes = "oauth2:server:client_id:<SERVER-CLIENT-ID>:api_scope:<SCOPE1> <SCOPE2>";
        String code = null;
        try {
            code = GoogleAuthUtil.getToken(
                    this,                                              // Context context
                    Plus.AccountApi.getAccountName(mGoogleApiClient),  // String accountName
                    scopes,                                            // String scope
                    appActivities                                      // Bundle bundle
            );
    Log.d(LOG_TAG, "TOKEN:"+ code);
        } catch (IOException transientEx) {
            // network or server error, the call is expected to succeed if you try again later.
            // Don't attempt to call again immediately - the request is likely to
            // fail, you'll hit quotas or back-off.

            return;
        } catch (UserRecoverableAuthException e) {
            // Requesting an authorization code will always throw
            // UserRecoverableAuthException on the first call to GoogleAuthUtil.getToken
            // because the user must consent to offline access to their data.  After
            // consent is granted control is returned to your activity in onActivityResult
            // and the second call to GoogleAuthUtil.getToken will succeed.
          //  startActivityForResult(e.getIntent(), AUTH_CODE_REQUEST_CODE);
            return;
        } catch (GoogleAuthException authEx) {
            // Failure. The call is not expected to ever succeed so it should not be
            // retried.

            return;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
//        mStatus.setText(String.format(
//                getResources().getString(R.string.signed_in_as),
//                currentUser.getDisplayName() + currentUser.getId()));
        authenticateUser(currentUser.getId());
    }

    private void authenticateUser(String currentUser) {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(PREFS_USER_ID, currentUser);
        //authenticateUser(null, currentUser.getId());
        Intent intent;
        intent = new Intent(this, com.master.univt.HomeActivity.class);
        startActivity(intent);
        finish();
    }

    public void navigateToLogin(final View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void navigateToRegister(final View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivityForResult(intent, REQUEST_EXIT);
    }


}
