package com.master.univt.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.master.univt.R;
import com.master.univt.entities.ResponseStatusCode;
import com.master.univt.ui.search.MainActivity;


/**
 * Activity which displays a register screen to the user in order to authenticate himself.
 */
//@EActivity(R.layout.activity_register)
public class RegisterActivity extends AuthenticationActivity {

    //@ViewById(R.id.tl_custom)
    Toolbar toolbar;
    

    private static final String LOG_TAG = RegisterActivity.class.getSimpleName();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_register);


    }
    //@AfterViews
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
                    Log.d(LOG_TAG, "GA:" + actionTrackingGA + "->" + labelTrackingGA);
                    setResult(RESULT_OK, null);
                    finish();
                    break;
                case AUTHENTICATION_FAILED:
                    showAuthenticationProgress(false);
//          userInterfaceMessagesHandler.setAlert(getString(R.string.title_error_dialog),
//            getString(R.string.registration_server_error));
                    break;
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
