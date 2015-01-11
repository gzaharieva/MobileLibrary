package com.master.univt.ui.search;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.master.univt.R;
import com.master.univt.support.util.SearchSetting;



/**
 * Created by LQG on BookDetailActivity2014/12/8.
 */
//@EFragment
public class SearchSetingFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private ActionBarDrawerToggle mDrawerToggle;

    public static final String KEYWORDSTYPE = "keywordsType";
    public static final String DOWNLOAD = "download";
    public static final String FILTER = "filter";
    public static final String MAXRESULTS = "maxResults";
    public static final String ORDERBY = "orderBy";
    public static final String PRINTTYPE = "printType";
    public static final String PROJECTION = "projection";

    private View mFragmentContainerView;
    private DrawerLayout mDrawerLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting);
        setRetainInstance(false);
        PreferenceManager.getDefaultSharedPreferences(getActivity()).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setBackgroudColor();
    }

    void setBackgroudColor() {
        getView().setBackgroundColor(0xfff5f5f5);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        PreferenceManager.getDefaultSharedPreferences(getActivity()).unregisterOnSharedPreferenceChangeListener(this);
    }

    public void setup(int fragmentId, DrawerLayout drawerLayout, Toolbar toolbar) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded())
                    return;

                getActionBar().setTitle(getParentActivity().getQuery());
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                if (!isAdded())
                    return;

                getActionBar().setTitle("Setting");
                getActivity().invalidateOptionsMenu();
            }
        };
        drawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
        drawerLayout.setDrawerListener(mDrawerToggle);
    }

    private ActionBar getActionBar() {
        return ((ActionBarActivity) getActivity()).getSupportActionBar();
    }

    private SearchActivity getParentActivity() {
        return (SearchActivity) getActivity();
    }

    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(MAXRESULTS)) {
            String value = SearchSetting.getMaxResultsFromPre();
            getPreferenceScreen().findPreference(MAXRESULTS).setSummary(value);
            SearchSetting.getInstance().setMaxResults(value);
        } else if (key.equals(KEYWORDSTYPE)) {
            String value = SearchSetting.getKeywordsTypeFromPre();
            getPreferenceScreen().findPreference(KEYWORDSTYPE).setSummary(value);
            SearchSetting.getInstance().setKeywordsType(value);
        } else if (key.equals(DOWNLOAD)) {
            String value = SearchSetting.getDownloadFromPre();
            getPreferenceScreen().findPreference(DOWNLOAD).setSummary(value);
            SearchSetting.getInstance().setDownload(value);
        } else if (key.equals(FILTER)) {
            String value = SearchSetting.getFilterFromPre();
            getPreferenceScreen().findPreference(FILTER).setSummary(value);
            SearchSetting.getInstance().setFilter(value);
        } else if (key.equals(PRINTTYPE)) {
            String value = SearchSetting.getPrintTypeFromPre();
            getPreferenceScreen().findPreference(PRINTTYPE).setSummary(value);
            SearchSetting.getInstance().setPrintType(value);
        } else if (key.equals(PROJECTION)) {
            String value = SearchSetting.getProjectionFromPre();
            getPreferenceScreen().findPreference(PROJECTION).setSummary(value);
            SearchSetting.getInstance().setProjection(value);
        } else if (key.equals(ORDERBY)) {
            String value = SearchSetting.getOrderByFromPer();
            getPreferenceScreen().findPreference(ORDERBY).setSummary(value);
            SearchSetting.getInstance().setOrderBy(value);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (mDrawerLayout != null && isDrawerOpen()) {
            menu.clear();
            return;
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
