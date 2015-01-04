package com.master.univt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.master.univt.entities.ParameterTag;
import com.master.univt.navigation.NavigationDrawerItem;
import com.master.univt.navigation.NavigationDrawerListAdapter;
import com.master.univt.ui.SplashActivity;
import com.master.univt.ui.search.MainActivity;

import java.util.ArrayList;
import java.util.List;

import static com.master.univt.Constants.IMAGES;


/**
 * The home page. On the left side is the navigation bar.
 *
 * @author Gabriela Zaharieva
 */
public class HomeActivity extends ActionBarActivity
{
    //@ViewById(R.id.tl_custom)
    Toolbar toolbar;
    /**
     * The log tag.
     */
    private static final String LOG_TAG = HomeActivity.class.getSimpleName();
    /** The shared preferences. */

    /**
     * The navigation drawer layout for devices with side small then 600dp and portrait view.
     */
    private DrawerLayout navigationDrawerLayout;
    /**
     * The navigation menu list.
     */
    private ListView navigationDrawerList;

    private ActionBarDrawerToggle navigationDrawerToggle;
    /**
     * The navigation menu adapter on the left side.
     */
    private NavigationDrawerListAdapter navigationMenuAdapter;
    private BookshelvesFragment bookshelvesFragment = new BookshelvesFragment();
    private SearchView searchView;


    /**
     * Used to store the last screen title. For use in
     */
    private CharSequence titleActionBar;
    // GoogleApiClient wraps our service connection to Google Play services and
    // provides access to the users sign in state and Google's APIs.
    private GoogleApiClient mGoogleApiClient;


    public HomeActivity() {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!navigationDrawerLayout.isDrawerOpen(navigationDrawerList)) {
            getMenuInflater().inflate(R.menu.menu_search, menu);
            searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
            searchView.setOnSearchClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchView.onActionViewCollapsed();
                    Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            });
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        SharedPreferences settings = getSharedPreferences(SplashActivity.PREFS_NAME, 0);

        String username = settings.getString(SplashActivity.PREFS_USERNAME, getString(R.string.username));
        List<NavigationDrawerItem> items = new ArrayList<NavigationDrawerItem>();
        items.add(new NavigationDrawerItem(username, R.drawable.ic_user));
        items.add(new NavigationDrawerItem(getString(R.string.title_bookshelfs), R.mipmap.ic_star));
        items.add(new NavigationDrawerItem(getString(R.string.title_favorites), android.R.drawable.ic_media_play));
        items.add(new NavigationDrawerItem(getString(R.string.title_toread), android.R.drawable.ic_dialog_map));

        items.add(new NavigationDrawerItem(getString(R.string.title_reading_now), android.R.drawable.ic_popup_sync));
        items.add(new NavigationDrawerItem(getString(R.string.title_browsing_history), android.R.drawable.ic_popup_reminder));


        navigationDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationDrawerList = (ListView) findViewById(R.id.navigation_drawer);
        navigationMenuAdapter = new NavigationDrawerListAdapter(HomeActivity.this, items);

        navigationDrawerList.setAdapter(navigationMenuAdapter);

        navigationDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        navigationDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        toolbar = (Toolbar) findViewById(R.id.tl_custom);
        toolbar.setTitleTextColor(0xffffffff);
        setSupportActionBar(toolbar);

        navigationDrawerToggle =
                new ActionBarDrawerToggle(this, navigationDrawerLayout, toolbar, R.string.navigation_drawer_open,
                        R.string.navigation_drawer_close) {
                    @Override
                    public void onDrawerClosed(final View view) {
                        super.onDrawerClosed(view);
                        getSupportActionBar().invalidateOptionsMenu();
                        setTitle(titleActionBar);
                    }

                    @Override
                    public void onDrawerOpened(final View drawerView) {
                        getSupportActionBar().setTitle(getString(R.string.app_name));
                        getSupportActionBar().invalidateOptionsMenu();
                        super.onDrawerOpened(drawerView);
                    }
                };

        navigationDrawerLayout.setDrawerListener(navigationDrawerToggle);

        if (savedInstanceState == null) {

            selectItem(1);

        }

        titleActionBar = getString(R.string.title_my_courses);
        mGoogleApiClient = buildGoogleApiClient();
        mGoogleApiClient.connect();
    }

    private GoogleApiClient buildGoogleApiClient() {
        // When we build the GoogleApiClient we specify where connected and
        // connection failed callbacks should be returned, which Google APIs our
        // app uses and which OAuth 2.0 scopes our app requests.
        return new GoogleApiClient.Builder(this)
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
    public boolean onOptionsItemSelected(final MenuItem item) {

        switch (item.getItemId()){
        case android.R.id.home:
            if (!navigationDrawerToggle.isDrawerIndicatorEnabled()) {
                onBackPressed();
            } else {
                if (navigationDrawerLayout.isDrawerOpen(navigationDrawerList)) {
                    navigationDrawerLayout.closeDrawer(navigationDrawerList);
                } else {
                    navigationDrawerLayout.openDrawer(navigationDrawerList);
                }
            }
            break;
            case R.id.action_menu_logout:
//                Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
//                mGoogleApiClient.disconnect();
//                mGoogleApiClient.connect();
                Intent intent = new Intent(this, SplashActivity.class);
                startActivity(intent);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void selectItem(final int position) {
        navigationMenuAdapter.setSelectedPosition(position);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        switch (position) {
            case 1:
                fragmentTransaction.replace(R.id.container, bookshelvesFragment, ParameterTag.FRAGMENT_COURSE)
                        .addToBackStack(null);
                break;
            case 2:
                BooksFragment booksFragment = new BooksFragment();
                fragmentTransaction.replace(R.id.container, booksFragment, ParameterTag.FRAGMENT_COURSE)
                        .addToBackStack(null);
                break;
        }

        fragmentTransaction.commit();
        navigationDrawerList.setItemChecked(position, true);

        if (navigationDrawerLayout != null) {
            navigationDrawerLayout.closeDrawer(navigationDrawerList);
        }

    }

    @Override
    protected void onPostCreate(final Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        navigationDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(final Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        navigationDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void setTitle(final CharSequence title) {
        titleActionBar = title;
        getSupportActionBar().setTitle(titleActionBar);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
            selectItem(position);
        }
    }

    /**
     * @return the navigationDrawerToggle
     */
    public ActionBarDrawerToggle getNavigationDrawerToggle() {
        return navigationDrawerToggle;
    }

    /**
     * @param navigationDrawerToggle the navigationDrawerToggle to set
     */
    public void setNavigationDrawerToggle(final ActionBarDrawerToggle navigationDrawerToggle) {
        this.navigationDrawerToggle = navigationDrawerToggle;
    }


    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (requestCode == ParameterTag.RC_REQUEST_CODE_PAYMENT) {
            Log.d(LOG_TAG, "Home activity onActivityResult redirect to the visible fragment");
            Fragment lastVisibleFragmentByTag = getSupportFragmentManager().findFragmentByTag(ParameterTag.FRAGMENT_COURSE);

            if (lastVisibleFragmentByTag != null) {
                lastVisibleFragmentByTag.onActivityResult(requestCode, resultCode, data);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onImageListClick(View view) {
        Intent intent = new Intent(this, ImageListActivity.class);
        intent.putExtra(Constants.Extra.IMAGES, IMAGES);
        startActivity(intent);
    }

    public void onImageGridClick(View view) {
        Intent intent = new Intent(this, ImageGridActivity.class);
        intent.putExtra(Constants.Extra.IMAGES, IMAGES);
        startActivity(intent);
    }

    public void onImagePagerClick(View view) {
        Intent intent = new Intent(this, ImagePagerActivity.class);
        intent.putExtra(Constants.Extra.IMAGES, IMAGES);
        startActivity(intent);
    }

}
