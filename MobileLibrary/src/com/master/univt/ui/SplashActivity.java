package com.master.univt.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

import com.master.univt.R;
import com.master.univt.support.http.Search;
import com.master.univt.ui.detail.BookDetailActivity;
import com.master.univt.ui.search.MainActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;


/**
 * Splash screen. The initial screen view of the application. An example full-screen activity that
 * shows and hides the system UI (i.e. status bar and navigation/system bar) with user interaction.
 *
 * @author Gabriela Zaharieva
 */
@EActivity(R.layout.activity_splash)
public class SplashActivity extends ActionBarActivity implements AnimationListener {
    private static int REQUEST_EXIT = 0;

    @ViewById(R.id.tl_custom)
    Toolbar toolbar;
    
    @ViewById(R.id.content_view)
    public View contentView;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            // Here activity is brought to front, not created,
            // so finishing this will get you to the last viewed activity
            finish();
            return;
        }
    }

    @AfterViews
    void afterViews() {
        //   getSupportActionBar().hide();
    }

    public void navigateToLogin(final View view) {
        Intent intent = new Intent(this, LoginActivity_.class);
            startActivity(intent);
    }

    public void navigateToRegister(final View view) {
        Intent intent = new Intent(this, RegisterActivity_.class);
        startActivityForResult(intent, REQUEST_EXIT);
    }


    @Override
    public void onAnimationEnd(final Animation animation) {
        contentView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onAnimationRepeat(final Animation animation) {
    }

    @Override
    public void onAnimationStart(final Animation animation) {
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (requestCode == REQUEST_EXIT && resultCode == RESULT_OK) {
            this.finish();
        }
    }

}
