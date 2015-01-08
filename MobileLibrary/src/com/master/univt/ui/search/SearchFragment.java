package com.master.univt.ui.search;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.api.services.books.model.Volume;
import com.google.api.services.books.model.Volumes;
import com.master.univt.R;
import com.master.univt.support.http.Search;
import com.master.univt.ui.detail.BookDetailActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by LQG on 2014/12/4.
 */
//@EFragment(R.layout.fragment_main)
public class SearchFragment extends Fragment implements SearchView.OnQueryTextListener {

    //@ViewById(R.id.search_pb)
    ProgressBar progressBar;
    private View searchview;

    private List<Volume> searchResultList = new ArrayList<>();
    private SearchResultAdapter searchResultAdapter;
    private QueryTask queryTask;

    // @ViewById(R.id.listview)
    ListView listView;

    // @AfterViews
    private void init() {
        searchResultAdapter = new SearchResultAdapter(this, searchResultList);
        listView.setAdapter(searchResultAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                onResultBookClick((Volume) searchResultAdapter.getItem(position));
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_main, container, false);

        progressBar = (ProgressBar) rootView.findViewById(R.id.search_pb);
        listView = (ListView) rootView.findViewById(R.id.listview);
        searchview = rootView.findViewById(R.id.view_search);
        init();

        return rootView;
    }

    @Override
    public boolean onQueryTextSubmit(final String query) {
        Log.d("LOG", "" + queryTask);
        if (queryTask != null) {
            queryTask.cancel(true);
        }

        progressBar.setVisibility(View.VISIBLE);
        queryTask = new QueryTask();
        queryTask.execute(query);

        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    //@ItemClick(R.id.listview)
    private void onResultBookClick(Volume item) {
        Intent intent = new Intent(getActivity(), BookDetailActivity.class);
        try {
            intent.putExtra("bookInfo", Search.JSON_FACTORY.toString(item));
            startActivity(intent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class QueryTask extends AsyncTask<String, Void, Volumes> {

        @Override
        protected Volumes doInBackground(String... params) {
            return Search.searchVolumes(params[0]);
        }

        @Override
        protected void onPostExecute(Volumes searchListResponse) {
            progressBar.setVisibility(View.INVISIBLE);
            if (searchListResponse == null) {
                searchview.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);

                return;
            }


            onQuery(searchListResponse);
        }
    }

    private boolean onQuery(Volumes result) {
        if (result == null || result.getItems() == null) {
//            searchResultList.clear();
            searchview.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
            return false;
        }
//            return false;

        searchResultList.clear();
        searchResultList.addAll(result.getItems());
        searchResultAdapter.notifyDataSetChanged();
        searchview.setVisibility(View.GONE);
        listView.setVisibility(View.VISIBLE);
        return true;
    }

}
