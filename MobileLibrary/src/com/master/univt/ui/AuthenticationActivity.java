package com.master.univt.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.plus.model.people.PersonBuffer;
import com.master.univt.R;
import com.master.univt.entities.ParameterTag;
import com.master.univt.entities.ResponseStatusCode;
import com.master.univt.services.AuthenticationService;
import com.master.univt.services.CommunicationService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.master.univt.ui.search.MainActivity_;


/**
 * Activity which displays a authentication screen to the user - login and registration as weel.
 */
@EActivity(R.layout.activity_login)
public abstract class AuthenticationActivity extends ActionBarActivity implements CommunicationService<ResponseStatusCode>,
        ConnectionCallbacks, OnConnectionFailedListener, View.OnClickListener {

    public static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
    private static final String LOG_TAG = AuthenticationActivity.class.getSimpleName();
    public static final String PREFS_NAME = "mobile_library";
    public static final String PREFS_USER_ID = "user_id";
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    protected AsyncTask<Map<String, Object>, ?, ?> authenticationTask = null;

    protected String labelTrackingGA;
    protected String actionTrackingGA;

    @ViewById(R.id.email)
    public EditText emailEditTextView;
    @ViewById(R.id.password)
    public EditText passwordEditTextView;
    @ViewById(R.id.authentication_status)
    public View authenticationStatusView;
    @ViewById(R.id.progress)
    public View loadingProgressBar;
    @ViewById(R.id.authentication_form)
    public View authenticationFormView;
    @ViewById(R.id.authentication_button)
    public Button authenticationButton;

    @ViewById(R.id.sign_in_status)
    public TextView mStatus;
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

    @ViewById(R.id.sign_in_button)
    public SignInButton mSignInButton;
    
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGoogleApiClient = buildGoogleApiClient();
        if (savedInstanceState != null) {
            mSignInProgress = savedInstanceState
                    .getInt(SAVED_PROGRESS, STATE_DEFAULT);
        }
    }
    @AfterViews
    void afterViews() {
        Log.d(LOG_TAG, "afterViews");
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
                    mStatus.setText(R.string.status_signing_in);
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
        switch(id) {
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
                                    mStatus.setText(R.string.status_signed_out);
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

        mStatus.setText(String.format(
                getResources().getString(R.string.signed_in_as),
                currentUser.getDisplayName()+ currentUser.getId()));
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(PREFS_USER_ID, currentUser.getId());
        authenticateUser(null, getAuthenticationSource());
    }
    /**
     * returns the authentication source which is used by the authentication service in order to call
     * server API.
     *
     * @return the authentication source.
     */
    protected abstract String getAuthenticationSource();


    /**
     * Initialization of all common view components for the authentication pages - login and
     * registration.
     */
    protected void initializeViewComponents() {

        authenticationButton.setEnabled(false);
        emailEditTextView.addTextChangedListener(editTextViewWatcher);
        passwordEditTextView.addTextChangedListener(editTextViewWatcher);

        emailEditTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                emailEditTextView.setCursorVisible(true);
            }
        });

        passwordEditTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(final TextView textView, final int id, final KeyEvent keyEvent) {
                if (id == R.id.authentication_button || id == EditorInfo.IME_NULL) {
                    attemptAuthenticate(null);
                    return true;
                }
                return false;
            }
        });

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Authentication of the user. Calls the server authentication API source and sends the given POST
     * parameters.
     *
     * @param postParams              the POSt parameters for the API call
     * @param authenticationAPISourse the authentication API source
     */
    protected void authenticateUser(final Map<String, Object> postParams, final String authenticationAPISourse) {
        authenticationTask = new AuthenticationService(this, this, authenticationAPISourse);
        authenticationTask.execute(postParams);
    }

    /**
     * Attempts to sign in or register the account specified by the login form. If there are form
     * errors (invalid email, missing fields, etc.), the errors are presented and no actual login
     * attempt is made.
     */
    public void attemptAuthenticate(final View view) {
        Log.d("LOG", "LOGIN");
//    if (authenticationTask != null)
//    {
//      return;
//    }

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(emailEditTextView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        String email = emailEditTextView.getText().toString();
        String password = passwordEditTextView.getText().toString();

        boolean hasValidationError = false;

        if (email.isEmpty() || password.isEmpty()) {
            hasValidationError = true;
        } else if (!Pattern.matches(EMAIL_REGEX, email)) {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
            alertBuilder.setTitle(getString(R.string.title_error_dialog)).setMessage(getString(R.string.incorrect_mail_address));
            alertBuilder.setNeutralButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialog, final int which) {
                    dialog.dismiss();
                }
            });
            alertBuilder.show();

            emailEditTextView.requestFocus();
            hasValidationError = true;
        }

        if (hasValidationError) {
            return;
        } else {
            showAuthenticationProgress(true);
            HashMap<String, Object> postParams = new HashMap<String, Object>();
            postParams.put(ParameterTag.PARAM_EMAIL, email);
            postParams.put(ParameterTag.PARAM_PASSWORD, password);

            authenticateUser(postParams, getAuthenticationSource());
        }
    }

    @Override
    public void onBackPressed() {
        if (authenticationTask == null) {
            super.onBackPressed();
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    protected void showAuthenticationProgress(final boolean show) {
        authenticationStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
        authenticationFormView.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    protected void showProgress(final boolean isShowingLoadingProgress) {
        loadingProgressBar.setVisibility(isShowingLoadingProgress ? View.VISIBLE : View.GONE);
        authenticationFormView.setVisibility(isShowingLoadingProgress ? View.GONE : View.VISIBLE);
    }

    /**
     * Checks email and password fields for empty values. Enable the authentication button if the
     * fields are not empty.
     */
    private void enableAuthenticationButtonForNotEmptyFields() {
        String email = emailEditTextView.getText().toString();
        String password = passwordEditTextView.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            authenticationButton.setEnabled(false);
        } else {
            authenticationButton.setEnabled(true);
        }
    }

    private final TextWatcher editTextViewWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
            enableAuthenticationButtonForNotEmptyFields();
        }

        @Override
        public void beforeTextChanged(final CharSequence charSequence, final int start, final int count, final int after) {
        }

        @Override
        public void afterTextChanged(final Editable editable) {
        }
    };

}
