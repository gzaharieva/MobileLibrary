package com.master.univt;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.master.univt.ui.fragments.NavigationDrawerFragment;
import com.master.univt.ui.search.SearchFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_main)
public class HomeActivity extends ActionBarActivity  implements  NavigationDrawerFragment.NavigationDrawerCallbacks {
    @ViewById
    DrawerLayout drawerLayout;

    @ViewById(R.id.tl_custom)
    Toolbar toolbar;

    NavigationDrawerFragment navigationdrawerFragment;

    @AfterViews
    void afterViews() {
        toolbar.setTitleTextColor(0xffffffff);
        toolbar.setTitle(getString(R.string.action_search));
        setSupportActionBar(toolbar);
        navigationdrawerFragment = findSearchSetingFragment();
        navigationdrawerFragment.setUp(R.id.drawcontainer, drawerLayout);
    }

    SearchView searchView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return super.onCreateOptionsMenu(menu);
    }

    private SearchFragment findSearchFragment() {
        return (SearchFragment) getSupportFragmentManager().findFragmentById(R.id.maincontainer);
    }

    private NavigationDrawerFragment findSearchSetingFragment() {
        return (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.drawcontainer);
    }

    boolean duplicate;
    String query;


    public void hideSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

    }
}
