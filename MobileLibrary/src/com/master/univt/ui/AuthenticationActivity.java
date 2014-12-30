package com.master.univt.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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


/**
 * Activity which displays a authentication screen to the user - login and registration as weel.
 */
@EActivity(R.layout.activity_login)
public abstract class AuthenticationActivity extends ActionBarActivity implements CommunicationService<ResponseStatusCode> {

    public static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
    private static final String LOG_TAG = AuthenticationActivity.class.getSimpleName();

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


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        
    }
    @AfterViews
    void afterViews() {
//        getActionBar().setDisplayHomeAsUpEnabled(true);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
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

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(LOG_TAG, "On activity result");

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
