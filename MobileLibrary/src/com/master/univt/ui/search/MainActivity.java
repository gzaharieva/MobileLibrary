package com.master.univt.ui.search;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.master.univt.R;
import com.master.univt.support.util.LogUtil;


//@EActivity(R.layout.activity_main)
public class MainActivity extends ActionBarActivity implements SearchView.OnQueryTextListener {
   // @ViewById
    DrawerLayout drawerLayout;

    //@ViewById(R.id.tl_custom)
    Toolbar toolbar;

    SearchSetingFragment searchSetingFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        toolbar = (Toolbar) findViewById(R.id.tl_custom);
        afterViews();
    }

    //@AfterViews
    void afterViews() {
        toolbar.setTitleTextColor(0xffffffff);
        toolbar.setTitle(getString(R.string.action_search));
        setSupportActionBar(toolbar);
        searchSetingFragment = findSearchSetingFragment();
        searchSetingFragment.setup(R.id.drawcontainer, drawerLayout, toolbar);
    }

    SearchView searchView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!searchSetingFragment.isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.menu_search, menu);
            searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
            searchView.setOnQueryTextListener(this);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    private SearchFragment findSearchFragment() {
        return (SearchFragment) getSupportFragmentManager().findFragmentById(R.id.maincontainer);
    }

    private SearchSetingFragment findSearchSetingFragment() {
        return (SearchSetingFragment) getFragmentManager().findFragmentById(R.id.drawcontainer);
    }

    boolean duplicate;
    String query;

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (!duplicate) {
            this.query = query;
            LogUtil.d("query===" + query);
            duplicate = true;
            setTitle(query);
            SearchFragment searchFragment = findSearchFragment();
            hideSoftKeyboard(searchView);
            searchView.clearFocus();
            return searchFragment.onQueryTextSubmit(query);
        } else
            duplicate = false;

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        SearchFragment searchFragment = findSearchFragment();
        return searchFragment.onQueryTextChange(newText);
    }

    public String getQuery() {
        return query == null ? "Search" : query;
    }

    public void hideSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
