package com.master.univt.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.master.univt.R;
import com.master.univt.entities.ResponseStatusCode;
import com.master.univt.ui.search.MainActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;


/**
 * Activity which displays a login screen to the user, offering registration as well.
 */
@EActivity(R.layout.activity_login)
public class LoginActivity extends AuthenticationActivity {

    @ViewById(R.id.tl_custom)
    Toolbar toolbar;
    
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_login);


    }

    @AfterViews
    void afterViews() {

        toolbar.setTitleTextColor(0xffffffff);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        initializeViewComponents();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onRequestCompleted(final ResponseStatusCode requestStatusCode) {
        authenticationTask = null;

        getActionBar().setHomeButtonEnabled(true);

        if (requestStatusCode != null) {
            switch (requestStatusCode) {
                case SUCCESFULL_AUTHENTICATION:
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    Log.d("LOG", "GA: Login: " + actionTrackingGA + "-" + labelTrackingGA);
                    setResult(RESULT_OK, null);
                    finish();
                    break;
                case LOGIN_FAILED:
                    showAuthenticationProgress(false);
//          userInterfaceMessagesHandler.setAlert(getString(R.string.title_error_dialog),
//            getString(R.string.login_failed_request));
                    break;
                case AUTHENTICATION_FAILED:
                    showAuthenticationProgress(false);
//          userInterfaceMessagesHandler.setAlert(getString(R.string.title_error_dialog),
//            getString(R.string.login_serer_error));
                default:
                    break;
            }
        } else {
            showAuthenticationProgress(false);
        }
    }

    @Override
    protected String getAuthenticationSource() {
        return "login.json";
    }


}
